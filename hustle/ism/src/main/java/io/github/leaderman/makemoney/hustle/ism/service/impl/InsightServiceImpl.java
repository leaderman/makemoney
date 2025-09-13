package io.github.leaderman.makemoney.hustle.ism.service.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.lark.oapi.service.bitable.v1.model.AppTableRecord;

import io.github.leaderman.makemoney.hustle.config.ConfigClient;
import io.github.leaderman.makemoney.hustle.feishu.BitableClient;
import io.github.leaderman.makemoney.hustle.feishu.ImClient;
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
  private String bitable;
  private String weiboTable;

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
    this.weiboTable = this.configClient.getString("ism.insight.bitable.table.weibo");
  }

  private boolean exists(String href) {
    return this.stringRedisTemplate.hasKey(insightKeyPrefix + href);
  }

  private void add(String href) {
    this.stringRedisTemplate.opsForValue().set(insightKeyPrefix + href, href, insightKeyExpire, TimeUnit.SECONDS);
  }

  @Override
  @Async
  public void insight(WeiboModel model) {
    try {
      String href = model.getHref();
      if (this.exists(href)) {
        log.info("微博 {} 已洞察，跳过", href);
        return;
      }
      this.add(href);

      AppTableRecord record = bitableClient.createRecord(bitable, weiboTable, Map.of("源代码", model.getHtml()));
      String recordId = record.getRecordId();
      log.info("微博 {} 已创建记录 {}", href, recordId);

      List<String> names = List.of("链接", "昵称", "日期时间", "正文", "相关性", "信号", "版块", "解读");

      Map<String, String> output = bitableClient.getAiOutput(bitable, weiboTable, recordId, names, this.maxRetries,
          this.retryInterval);
      log.info("微博 {} 洞察完成，洞察结果：{}", href, output);

      String correlation = output.get("相关性");
      if (!correlation.equals("有")) {
        return;
      }

      String sector = output.get("版块");
      String analysis = output.get("解读");
      String text = output.get("正文");
      String nickname = output.get("博主");
      String datetime = output.get("日期时间");

      String title = String.format("【%s】%s", "微博", sector);
      String content = String.format("解读：%s\\n正文：%s\\n链接：%s\\n博主：%s\\n日期时间：%s", analysis, text, href, nickname,
          datetime);

      String signal = output.get("信号");
      if (signal.equals("好")) {
        this.imClient.sendRedMessageByChatId(bullishChat, title, content);
      } else {
        this.imClient.sendGreenMessageByChatId(bearishChat, title, content);
      }
    } catch (Exception e) {
      log.error("微博洞察错误：{}", ExceptionUtils.getStackTrace(e));
    }
  }
}
