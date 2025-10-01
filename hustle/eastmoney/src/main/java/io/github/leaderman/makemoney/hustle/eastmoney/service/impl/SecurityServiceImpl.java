package io.github.leaderman.makemoney.hustle.eastmoney.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lark.oapi.service.bitable.v1.model.AppTableRecord;

import io.github.leaderman.makemoney.hustle.config.ConfigClient;
import io.github.leaderman.makemoney.hustle.eastmoney.domain.entity.SecurityEntity;
import io.github.leaderman.makemoney.hustle.eastmoney.domain.model.SecurityModel;
import io.github.leaderman.makemoney.hustle.eastmoney.mapper.SecurityMapper;
import io.github.leaderman.makemoney.hustle.eastmoney.service.SecurityService;
import io.github.leaderman.makemoney.hustle.feishu.BitableClient;
import io.github.leaderman.makemoney.hustle.feishu.ImClient;
import io.github.leaderman.makemoney.hustle.lang.DatetimeUtil;
import io.github.leaderman.makemoney.hustle.lang.NumberUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SecurityServiceImpl extends ServiceImpl<SecurityMapper, SecurityEntity> implements SecurityService {
  private final ConfigClient configClient;
  private final BitableClient bitableClient;
  private final ImClient imClient;

  private String bitable;
  private String securitiesTable;

  private String positionProfitSecurityChat;
  private String positionLossSecurityChat;

  private String positionProfitHighSecurityChat;
  private String positionLossLowSecurityChat;

  private String dailyProfitSecurityChat;
  private String dailyLossSecurityChat;

  private String dailyProfitHighSecurityChat;
  private String dailyLossLowSecurityChat;

  @PostConstruct
  public void init() {
    this.bitable = this.configClient.getString("eastmoney.bitable");
    this.securitiesTable = this.configClient.getString("eastmoney.bitable.securities");

    this.positionProfitSecurityChat = this.configClient.getString("feishu.chat.position.profit.security");
    this.positionLossSecurityChat = this.configClient.getString("feishu.chat.position.loss.security");

    this.positionProfitHighSecurityChat = this.configClient.getString("feishu.chat.position.profit.high.security");
    this.positionLossLowSecurityChat = this.configClient.getString("feishu.chat.position.loss.low.security");

    this.dailyProfitSecurityChat = this.configClient.getString("feishu.chat.daily.profit.security");
    this.dailyLossSecurityChat = this.configClient.getString("feishu.chat.daily.loss.security");

    this.dailyProfitHighSecurityChat = this.configClient.getString("feishu.chat.daily.profit.high.security");
    this.dailyLossLowSecurityChat = this.configClient.getString("feishu.chat.daily.loss.low.security");
  }

  private void syncDb(List<SecurityModel> securities) {
    // 新增实体列表。
    List<SecurityEntity> creatEntities = new ArrayList<>();

    // 更新实体列表。
    List<SecurityEntity> updateEntities = new ArrayList<>();

    // 删除实体列表。
    List<Long> deleteIds = new ArrayList<>();

    Map<String, SecurityEntity> leftEntities = securities.stream()
        .filter(security -> security.getHoldingQuantity() > 0)
        .map(SecurityModel::toEntity)
        .collect(Collectors.toMap(SecurityEntity::getSecurityCode, Function.identity()));
    Map<String, SecurityEntity> rightEntities = Optional
        .ofNullable(this.lambdaQuery().list())
        .map(entities -> entities.stream()
            .collect(Collectors.toMap(SecurityEntity::getSecurityCode, Function.identity())))
        .orElse(Collections.emptyMap());

    for (Entry<String, SecurityEntity> entry : leftEntities.entrySet()) {
      String securityCode = entry.getKey();
      SecurityEntity security = entry.getValue();

      if (!rightEntities.containsKey(securityCode)) {
        // 证券不存在于数据库中，需要新增。
        creatEntities.add(security);
      } else {
        SecurityEntity existingSecurity = rightEntities.get(securityCode);
        if (SecurityEntity.equals(security, existingSecurity)) {
          // 证券已存在于数据库中，且数据相同，跳过。
          continue;
        }

        // 持仓盈利。
        if (NumberUtil.lessThanOrEqualTo(existingSecurity.getPositionProfitLoss(), BigDecimal.ZERO)
            && NumberUtil.greaterThan(security.getPositionProfitLoss(), BigDecimal.ZERO)) {
          String title = String.format("【持仓盈利】%s", security.getSecurityName());
          String content = String.format("盈利金额：%s\\n日期时间：%s", security.getPositionProfitLoss(),
              DatetimeUtil.getDatetime());

          this.imClient.sendRedMessageByChatIdAsync(positionProfitSecurityChat, title, content);
        }

        // 持仓亏损。
        if (NumberUtil.greaterThanOrEqualTo(existingSecurity.getPositionProfitLoss(), BigDecimal.ZERO)
            && NumberUtil.lessThan(security.getPositionProfitLoss(), BigDecimal.ZERO)) {
          String title = String.format("【持仓亏损】%s", security.getSecurityName());
          String content = String.format("亏损金额：%s\\n日期时间：%s", security.getPositionProfitLoss(),
              DatetimeUtil.getDatetime());

          this.imClient.sendGreenMessageByChatIdAsync(positionLossSecurityChat, title, content);
        }

        // 持仓盈利新高。
        if (NumberUtil.greaterThan(security.getPositionProfitLoss(), BigDecimal.ZERO)
            && NumberUtil.greaterThan(security.getPositionProfitLoss(), existingSecurity.getPositionProfitLossMax())) {
          String title = String.format("【持仓盈利新高】%s", security.getSecurityName());
          String content = String.format("盈利金额：%s\\n日期时间：%s", security.getPositionProfitLoss(),
              DatetimeUtil.getDatetime());

          this.imClient.sendRedMessageByChatIdAsync(positionProfitHighSecurityChat, title, content);
        }

        // 持仓亏损新低。
        if (NumberUtil.lessThan(security.getPositionProfitLoss(), BigDecimal.ZERO)
            && NumberUtil.lessThan(security.getPositionProfitLoss(), existingSecurity.getPositionProfitLossMin())) {
          String title = String.format("【持仓亏损新低】%s", security.getSecurityName());
          String content = String.format("亏损金额：%s\\n日期时间：%s", security.getPositionProfitLoss(),
              DatetimeUtil.getDatetime());

          this.imClient.sendGreenMessageByChatIdAsync(positionLossLowSecurityChat, title, content);
        }

        // 当日盈利。
        if (NumberUtil.lessThanOrEqualTo(existingSecurity.getDailyProfitLoss(), BigDecimal.ZERO)
            && NumberUtil.greaterThan(security.getDailyProfitLoss(), BigDecimal.ZERO)) {
          String title = String.format("【当日盈利】%s", security.getSecurityName());
          String content = String.format("盈利金额：%s\\n日期时间：%s", security.getDailyProfitLoss(),
              DatetimeUtil.getDatetime());

          this.imClient.sendRedMessageByChatIdAsync(dailyProfitSecurityChat, title, content);
        }

        // 当日亏损。
        if (NumberUtil.greaterThanOrEqualTo(existingSecurity.getDailyProfitLoss(), BigDecimal.ZERO)
            && NumberUtil.lessThan(security.getDailyProfitLoss(), BigDecimal.ZERO)) {
          String title = String.format("【当日亏损】%s", security.getSecurityName());
          String content = String.format("亏损金额：%s\\n日期时间：%s", security.getDailyProfitLoss(),
              DatetimeUtil.getDatetime());

          this.imClient.sendGreenMessageByChatIdAsync(dailyLossSecurityChat, title, content);
        }

        // 当日盈利新高。
        if (NumberUtil.greaterThan(security.getDailyProfitLoss(), BigDecimal.ZERO)
            && NumberUtil.greaterThan(security.getDailyProfitLoss(), existingSecurity.getDailyProfitLossMax())) {
          String title = String.format("【当日盈利新高】%s", security.getSecurityName());
          String content = String.format("盈利金额：%s\\n日期时间：%s", security.getDailyProfitLoss(),
              DatetimeUtil.getDatetime());

          this.imClient.sendRedMessageByChatIdAsync(dailyProfitHighSecurityChat, title, content);
        }

        // 当日亏损新低。
        if (NumberUtil.lessThan(security.getDailyProfitLoss(), BigDecimal.ZERO)
            && NumberUtil.lessThan(security.getDailyProfitLoss(), existingSecurity.getDailyProfitLossMin())) {
          String title = String.format("【当日亏损新低】%s", security.getSecurityName());
          String content = String.format("亏损金额：%s\\n日期时间：%s", security.getDailyProfitLoss(),
              DatetimeUtil.getDatetime());

          this.imClient.sendGreenMessageByChatIdAsync(dailyLossLowSecurityChat, title, content);
        }

        // 修正持仓盈亏最大值和最小值。
        if (NumberUtil.greaterThan(existingSecurity.getPositionProfitLossMax(), security.getPositionProfitLoss())) {
          security.setPositionProfitLossMax(existingSecurity.getPositionProfitLossMax());
        }
        if (NumberUtil.lessThan(existingSecurity.getPositionProfitLossMin(), security.getPositionProfitLoss())) {
          security.setPositionProfitLossMin(existingSecurity.getPositionProfitLossMin());
        }

        // 修正当日盈亏最大值和最小值。
        if (NumberUtil.greaterThan(existingSecurity.getDailyProfitLossMax(), security.getDailyProfitLoss())) {
          security.setDailyProfitLossMax(existingSecurity.getDailyProfitLossMax());
        }
        if (NumberUtil.lessThan(existingSecurity.getDailyProfitLossMin(), security.getDailyProfitLoss())) {
          security.setDailyProfitLossMin(existingSecurity.getDailyProfitLossMin());
        }

        // 更新实体。
        security.setId(existingSecurity.getId());
        updateEntities.add(security);
      }
    }

    for (Entry<String, SecurityEntity> entry : rightEntities.entrySet()) {
      String securityCode = entry.getKey();
      if (!leftEntities.containsKey(securityCode)) {
        // 删除实体。
        deleteIds.add(entry.getValue().getId());
      }
    }

    if (CollectionUtils.isNotEmpty(creatEntities)) {
      // 新增。
      this.saveBatch(creatEntities);
    }

    if (CollectionUtils.isNotEmpty(updateEntities)) {
      // 更新。
      this.updateBatchById(updateEntities);
    }

    if (CollectionUtils.isNotEmpty(deleteIds)) {
      // 删除。
      this.removeByIds(deleteIds);
    }
  }

  private SecurityModel toSecurityModel(AppTableRecord record) {
    SecurityModel security = new SecurityModel();

    security.setSecurityCode((String) record.getFields().get("证券代码"));
    security.setSecurityName((String) record.getFields().get("证券名称"));
    security.setHoldingQuantity(NumberUtil.toInteger((String) record.getFields().get("持仓数量"), 0));
    security.setAvailableQuantity(NumberUtil.toInteger((String) record.getFields().get("可用数量"), 0));
    security.setCostPrice(NumberUtil.toBigDecimal((String) record.getFields().get("成本价"), BigDecimal.ZERO));
    security.setCurrentPrice(NumberUtil.toBigDecimal((String) record.getFields().get("当前价"), BigDecimal.ZERO));
    security.setMarketValue(NumberUtil.toBigDecimal((String) record.getFields().get("最新市值"), BigDecimal.ZERO));
    security.setPositionProfitLoss(NumberUtil.toBigDecimal((String) record.getFields().get("持仓盈亏"), BigDecimal.ZERO));
    security.setPositionProfitLossRatio(
        NumberUtil.toBigDecimal((String) record.getFields().get("持仓盈亏比例"), BigDecimal.ZERO));
    security.setDailyProfitLoss(NumberUtil.toBigDecimal((String) record.getFields().get("当日盈亏"), BigDecimal.ZERO));
    security
        .setDailyProfitLossRatio(NumberUtil.toBigDecimal((String) record.getFields().get("当日盈亏比例"), BigDecimal.ZERO));

    return security;
  }

  private Map<String, Object> toBitableRecord(SecurityModel security) {
    Map<String, Object> record = new HashMap<>();

    record.put("证券代码", security.getSecurityCode());
    record.put("证券名称", security.getSecurityName());
    record.put("持仓数量", security.getHoldingQuantity());
    record.put("可用数量", security.getAvailableQuantity());
    record.put("成本价", security.getCostPrice());
    record.put("当前价", security.getCurrentPrice());
    record.put("最新市值", security.getMarketValue());
    record.put("持仓盈亏", security.getPositionProfitLoss());
    record.put("持仓盈亏比例", security.getPositionProfitLossRatio());
    record.put("当日盈亏", security.getDailyProfitLoss());
    record.put("当日盈亏比例", security.getDailyProfitLossRatio());

    return record;
  }

  private void syncBitable(List<SecurityModel> securities) throws Exception {
    // 新增记录列表。
    List<Map<String, Object>> createRecords = new ArrayList<>();

    // 更新记录列表。
    List<String> updateRecordIds = new ArrayList<>();
    List<Map<String, Object>> updateRecords = new ArrayList<>();

    // 删除记录列表。
    List<String> deleteRecordIds = new ArrayList<>();

    Map<String, SecurityModel> leftRecords = securities.stream()
        .filter(security -> security.getHoldingQuantity() > 0)
        .collect(Collectors.toMap(SecurityModel::getSecurityCode, Function.identity()));
    Map<String, AppTableRecord> rightRecords = Optional
        .ofNullable(this.bitableClient.listTableRecords(this.bitable, this.securitiesTable))
        .map(records -> records.stream()
            .collect(Collectors.toMap(record -> (String) record.getFields().get("证券代码"), Function.identity())))
        .orElse(Collections.emptyMap());

    for (Entry<String, SecurityModel> entry : leftRecords.entrySet()) {
      String securityCode = entry.getKey();
      SecurityModel security = entry.getValue();

      if (!rightRecords.containsKey(securityCode)) {
        // 证券不存在于多维表格中，需要新增。
        createRecords.add(toBitableRecord(security));
      } else {
        AppTableRecord existingRecord = rightRecords.get(securityCode);
        if (SecurityModel.equals(security, toSecurityModel(existingRecord))) {
          // 证券已存在于多维表格中，且数据相同，跳过。
          continue;
        }

        // 更新记录。
        updateRecordIds.add(rightRecords.get(securityCode).getRecordId());
        updateRecords.add(toBitableRecord(security));
      }
    }

    for (Entry<String, AppTableRecord> entry : rightRecords.entrySet()) {
      String securityCode = entry.getKey();
      if (!leftRecords.containsKey(securityCode)) {
        // 删除记录。
        deleteRecordIds.add(entry.getValue().getRecordId());
      }
    }

    if (CollectionUtils.isNotEmpty(createRecords)) {
      // 新增。
      this.bitableClient.batchCreateRecords(this.bitable, this.securitiesTable, createRecords);
    }

    if (CollectionUtils.isNotEmpty(updateRecords)) {
      // 更新。
      this.bitableClient.batchUpdateRecords(this.bitable, this.securitiesTable, updateRecordIds, updateRecords);
    }

    if (CollectionUtils.isNotEmpty(deleteRecordIds)) {
      // 删除。
      this.bitableClient.batchDeleteRecords(this.bitable, this.securitiesTable, deleteRecordIds);
    }
  }

  @Override
  public void sync(List<SecurityModel> models) {
    try {
      StopWatch sw = new StopWatch("sync");

      sw.start("syncDb");
      this.syncDb(models);
      sw.stop();

      sw.start("syncBitable");
      this.syncBitable(models);
      sw.stop();

      log.info(sw.prettyPrint());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public boolean hasPosition(String securityCode) {
    return this.lambdaQuery().eq(SecurityEntity::getSecurityCode, securityCode).exists();
  }

  @Override
  public SecurityModel get(String securityCode) {
    return this.lambdaQuery().eq(SecurityEntity::getSecurityCode, securityCode).oneOpt()
        .map(SecurityModel::from)
        .orElse(null);
  }
}
