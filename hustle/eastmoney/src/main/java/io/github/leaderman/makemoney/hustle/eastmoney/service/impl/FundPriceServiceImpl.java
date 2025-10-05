package io.github.leaderman.makemoney.hustle.eastmoney.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lark.oapi.service.bitable.v1.model.AppTableRecord;

import io.github.leaderman.makemoney.hustle.config.ConfigClient;
import io.github.leaderman.makemoney.hustle.eastmoney.domain.entity.FundPriceEntity;
import io.github.leaderman.makemoney.hustle.eastmoney.domain.model.FundBucketPriceModel;
import io.github.leaderman.makemoney.hustle.eastmoney.domain.model.FundModel;
import io.github.leaderman.makemoney.hustle.eastmoney.domain.model.FundPriceInterveneModel;
import io.github.leaderman.makemoney.hustle.eastmoney.domain.model.FundPriceModel;
import io.github.leaderman.makemoney.hustle.eastmoney.mapper.FundPriceMapper;
import io.github.leaderman.makemoney.hustle.eastmoney.service.FundPriceService;
import io.github.leaderman.makemoney.hustle.eastmoney.service.FundService;
import io.github.leaderman.makemoney.hustle.eastmoney.service.SecurityService;
import io.github.leaderman.makemoney.hustle.feishu.BitableClient;
import io.github.leaderman.makemoney.hustle.feishu.ImClient;
import io.github.leaderman.makemoney.hustle.lang.DatetimeUtil;
import io.github.leaderman.makemoney.hustle.trade.service.TradeService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FundPriceServiceImpl extends ServiceImpl<FundPriceMapper, FundPriceEntity> implements FundPriceService {
  private final static int BUCKET_SIZE = 60;

  private final ConfigClient configClient;
  private final BitableClient bitableClient;
  private final ImClient imClient;

  private final ObjectMapper objectMapper;

  private final TradeService tradeService;
  private final FundService fundService;
  private final SecurityService securityService;

  // 多维表格。
  private String bitable;
  // 数据表。
  private String table;
  // 最大重试次数。
  private int maxRetries;
  // 重试间隔时间。
  private int retryInterval;

  // 价格介入群组。
  private String priceInterveneChat;

  @PostConstruct
  public void init() {
    this.bitable = this.configClient.getString("eastmoney.price.trend.bitable");
    this.table = this.configClient.getString("eastmoney.price.trend.bitable.table");
    this.maxRetries = this.configClient.getInt("eastmoney.price.trend.max.retries");
    this.retryInterval = this.configClient.getInt("eastmoney.price.trend.retry.interval");

    this.priceInterveneChat = this.configClient.getString("feishu.chat.price.intervene");
  }

  @Override
  @Transactional
  public void save(List<FundPriceModel> models) {
    if (!this.tradeService.isTradingTime()) {
      return;
    }

    this.saveBatch(models.stream().map(FundPriceModel::to).collect(Collectors.toList()));
    log.info("保存 {} 条实体", models.size());
  }

  @Override
  public long count(String code, LocalDateTime start, LocalDateTime end) {
    return this.count(new LambdaQueryWrapper<FundPriceEntity>().eq(FundPriceEntity::getCode, code)
        .ge(FundPriceEntity::getCreatedAt, start).le(FundPriceEntity::getCreatedAt, end));
  }

  @Override
  public long count(String code) {
    return this.count(code, DatetimeUtil.startOfDay(), DatetimeUtil.endOfDay());
  }

  @Override
  public List<FundBucketPriceModel> bucket(String code, int size, LocalDateTime start, LocalDateTime end) {
    return this.getBaseMapper().bucket(code, size, start, end).stream().map(FundBucketPriceModel::from)
        .collect(Collectors.toList());
  }

  @Override
  public List<FundBucketPriceModel> bucket(String code, int size) {
    return this.bucket(code, size, DatetimeUtil.startOfDay(), DatetimeUtil.endOfDay());
  }

  @Override
  public FundPriceInterveneModel shouldIntervene(String code, int size, LocalDateTime start, LocalDateTime end) {
    try {
      List<FundBucketPriceModel> bucketPrices = this.bucket(code, size, start, end);
      if (CollectionUtils.isEmpty(bucketPrices)) {
        return null;
      }

      // 价格（多行）。
      String prices = bucketPrices.stream().map(FundBucketPriceModel::getPrice).map(BigDecimal::toString)
          .collect(Collectors.joining("\n"));

      // 创建记录。
      AppTableRecord record = this.bitableClient.createRecord(this.bitable, this.table,
          Map.of("代码", code, "价格", prices));

      // 获取结果。
      String result = this.bitableClient.getAiOutput(this.bitable, this.table, record.getRecordId(), "趋势",
          this.maxRetries,
          this.retryInterval);

      JsonNode trendNode = this.objectMapper.readTree(result);
      // 行动。
      String action = trendNode.get("action").textValue();
      // 依据。
      String reason = trendNode.get("reason").textValue();
      log.info("代码：{}，分桶大小：{}，开始时间：{}，结束时间：{}，行动：{}，依据：{}", code, size, start, end, action, reason);

      // 删除记录。
      try {
        this.bitableClient.deleteRecord(this.bitable, this.table, record.getRecordId());
      } catch (Exception e) {
        log.error("删除多维表格记录错误：{}", ExceptionUtils.getStackTrace(e));
      }

      return new FundPriceInterveneModel(code, action.equals("介入"), reason);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public FundPriceInterveneModel shouldIntervene(String code) {
    return this.shouldIntervene(code, BUCKET_SIZE, DatetimeUtil.startOfDay(), DatetimeUtil.endOfDay());
  }

  @Override
  public void intervene(int size, LocalDateTime start, LocalDateTime end) {
    List<FundModel> funds = this.fundService.gets();

    try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
      for (FundModel fund : funds) {
        executor.submit(() -> {
          try {
            FundPriceInterveneModel intervene = this.shouldIntervene(fund.getCode(), size, start, end);
            if (Objects.isNull(intervene)) {
              return;
            }

            log.info("代码：{}，介入：{}，依据：{}", intervene.getCode(), intervene.isIntervene(), intervene.getReason());

            if (!intervene.isIntervene()) {
              return;
            }

            String title = String.format("价格介入 - %s", fund.getName());
            if (this.securityService.hasPosition(fund.getCode())) {
              title += "（持仓）";
            }

            String content = String.format("介入依据：%s\\n日期时间：%s", intervene.getReason(), DatetimeUtil.getDatetime());

            this.imClient.sendYellowMessageByChatId(priceInterveneChat, title, content);
          } catch (Exception e) {
            log.error("基金价格判断是否需要人工介入错误：{}", ExceptionUtils.getStackTrace(e));
          }
        });
      }
    }
  }

  @Override
  public void intervene() {
    if (!this.tradeService.isTradingTime()) {
      return;
    }

    this.intervene(BUCKET_SIZE, DatetimeUtil.startOfDay(), DatetimeUtil.endOfDay());
  }
}
