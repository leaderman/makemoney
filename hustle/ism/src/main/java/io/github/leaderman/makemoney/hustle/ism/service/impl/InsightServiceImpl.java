package io.github.leaderman.makemoney.hustle.ism.service.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.lark.oapi.service.bitable.v1.model.AppTableRecord;

import io.github.leaderman.makemoney.hustle.config.ConfigClient;
import io.github.leaderman.makemoney.hustle.feishu.BitableClient;
import io.github.leaderman.makemoney.hustle.feishu.ImClient;
import io.github.leaderman.makemoney.hustle.ism.domain.model.WeiboExtractModel;
import io.github.leaderman.makemoney.hustle.ism.domain.model.WeiboModel;
import io.github.leaderman.makemoney.hustle.ism.service.InsightService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class InsightServiceImpl implements InsightService {
  private final ConfigClient configClient;

  private String insightKeyPrefix;
  private int insightKeyExpire;
  private final StringRedisTemplate stringRedisTemplate;

  private int maxRetries;
  private int retryInterval;
  private String bullishChat;
  private String bearishChat;

  // 多维表格。
  private String bitable;
  // 微博提取数据表。
  private String weiboExtractTable;

  private final BitableClient bitableClient;
  private final ImClient imClient;

  @PostConstruct
  public void init() {
    this.insightKeyPrefix = this.configClient.getString("ism.insight.key.prefix");
    this.insightKeyExpire = this.configClient.getInt("ism.insight.key.expire");

    this.maxRetries = this.configClient.getInt("ism.insight.max.retries");
    this.retryInterval = this.configClient.getInt("ism.insight.retry.interval");
    this.bullishChat = this.configClient.getString("ism.insight.chat.bullish");
    this.bearishChat = this.configClient.getString("ism.insight.chat.bearish");
    this.bitable = this.configClient.getString("ism.insight.bitable");
    this.weiboExtractTable = this.configClient.getString("ism.insight.bitable.table.weibo.extract");
  }

  private boolean exists(String href) {
    return this.stringRedisTemplate.hasKey(insightKeyPrefix + href);
  }

  private void add(String href) {
    this.stringRedisTemplate.opsForValue().set(insightKeyPrefix + href, href, insightKeyExpire, TimeUnit.SECONDS);
  }

  private WeiboExtractModel extractWeibo(String html) throws Exception {
    AppTableRecord record = this.bitableClient.createRecord(this.bitable, this.weiboExtractTable, Map.of("源代码", html));

    String recordId = record.getRecordId();
    List<String> names = List.of("链接", "昵称", "时间", "正文内容", "转发内容");

    Map<String, String> output = this.bitableClient.getAiOutput(this.bitable, this.weiboExtractTable, recordId, names,
        this.maxRetries,
        this.retryInterval);

    this.bitableClient.deleteRecord(this.bitable, this.weiboExtractTable, recordId);

    WeiboExtractModel model = new WeiboExtractModel();

    model.setHtml(html);
    model.setHref(output.get("链接"));
    model.setNickname(output.get("昵称"));
    model.setDatetime(output.get("时间"));
    model.setMainContent(output.get("正文内容"));
    model.setRetweetContent(output.get("转发内容"));

    return model;
  }

  @Override
  @Async
  public void insight(WeiboModel model) {
    String href = model.getHref();

    try {
      if (this.exists(href)) {
        log.info("微博 {} 已洞察，跳过", href);
        return;
      }
      this.add(href);

      log.info("微博 {} 开始洞察", href);

      WeiboExtractModel extractModel = extractWeibo(model.getHtml());
      log.info("微博 {} 提取完成", href);

      if (StringUtils.isEmpty(extractModel.getMainContent())) {
        log.info("微博 {} 正文内容为空，跳过洞察", href);
        return;
      }
      log.info("微博 {} 正文内容：{}", href, extractModel.getMainContent());

      log.info("微博 {} 结束洞察", href);
    } catch (Exception e) {
      log.error("微博 {} 洞察错误：{}", href, ExceptionUtils.getStackTrace(e));
    }
  }
}
