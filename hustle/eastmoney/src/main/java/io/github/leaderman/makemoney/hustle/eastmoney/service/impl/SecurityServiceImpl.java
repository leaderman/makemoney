package io.github.leaderman.makemoney.hustle.eastmoney.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

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

  private String positionProfitHighSecurityChat;
  private String positionLossLowSecurityChat;

  private String dailyProfitHighSecurityChat;
  private String dailyLossLowSecurityChat;

  @PostConstruct
  public void init() {
    this.bitable = this.configClient.getString("eastmoney.bitable");
    this.securitiesTable = this.configClient.getString("eastmoney.bitable.securities");

    this.positionProfitHighSecurityChat = this.configClient.getString("feishu.chat.position.profit.high.security");
    this.positionLossLowSecurityChat = this.configClient.getString("feishu.chat.position.loss.low.security");

    this.dailyProfitHighSecurityChat = this.configClient.getString("feishu.chat.daily.profit.high.security");
    this.dailyLossLowSecurityChat = this.configClient.getString("feishu.chat.daily.loss.low.security");
  }

  private void newPositionProfitLossMax(SecurityEntity entity, SecurityEntity existingEntity) {
    /*
     * 持仓盈利新高:
     * 
     * 持仓盈亏新最大值大于 0，
     * 持仓盈亏新最大值大于旧最大值；
     */
    if (NumberUtil.greaterThan(entity.getPositionProfitLossMax(), BigDecimal.ZERO)
        && NumberUtil.greaterThan(entity.getPositionProfitLossMax(), existingEntity.getPositionProfitLossMax())) {
      String title = String.format("持仓盈利新高 - %s", entity.getSecurityName());
      String content = String.format("盈利金额：%s\\n日期时间：%s", entity.getPositionProfitLossMax(),
          DatetimeUtil.getDatetime());

      this.imClient.sendRedMessageByChatIdAsync(positionProfitHighSecurityChat, title, content);
    }
  }

  private void newPositionProfitLossMin(SecurityEntity entity, SecurityEntity existingEntity) {
    /*
     * 持仓亏损新低:
     * 
     * 持仓盈亏新最小值小于 0，
     * 持仓盈亏新最小值小于旧最小值；
     */
    if (NumberUtil.lessThan(entity.getPositionProfitLossMin(), BigDecimal.ZERO)
        && NumberUtil.lessThan(entity.getPositionProfitLossMin(), existingEntity.getPositionProfitLossMin())) {
      String title = String.format("持仓亏损新低 - %s", entity.getSecurityName());
      String content = String.format("亏损金额：%s\\n日期时间：%s", entity.getPositionProfitLossMin(),
          DatetimeUtil.getDatetime());

      this.imClient.sendGreenMessageByChatIdAsync(positionLossLowSecurityChat, title, content);
    }
  }

  private void newDailyProfitLossMax(SecurityEntity entity, SecurityEntity existingEntity) {
    /*
     * 当日盈利新高:
     * 
     * 当日盈亏新最大值大于 0，
     * 当日盈亏新最大值大于旧最大值；
     */
    if (NumberUtil.greaterThan(entity.getDailyProfitLossMax(), BigDecimal.ZERO)
        && NumberUtil.greaterThan(entity.getDailyProfitLossMax(), existingEntity.getDailyProfitLossMax())) {
      String title = String.format("当日盈利新高 - %s", entity.getSecurityName());
      String content = String.format("盈利金额：%s\\n日期时间：%s", entity.getDailyProfitLossMax(),
          DatetimeUtil.getDatetime());

      this.imClient.sendRedMessageByChatIdAsync(dailyProfitHighSecurityChat, title, content);
    }
  }

  private void newDailyProfitLossMin(SecurityEntity entity, SecurityEntity existingEntity) {
    /*
     * 当日亏损新低:
     * 
     * 当日盈亏新最小值小于 0，
     * 当日盈亏新最小值小于旧最小值；
     */
    if (NumberUtil.lessThan(entity.getDailyProfitLossMin(), BigDecimal.ZERO)
        && NumberUtil.lessThan(entity.getDailyProfitLossMin(), existingEntity.getDailyProfitLossMin())) {
      String title = String.format("当日亏损新低 - %s", entity.getSecurityName());
      String content = String.format("亏损金额：%s\\n日期时间：%s", entity.getDailyProfitLossMin(),
          DatetimeUtil.getDatetime());

      this.imClient.sendGreenMessageByChatIdAsync(dailyLossLowSecurityChat, title, content);
    }
  }

  private boolean syncDb(List<SecurityModel> securities) {
    // 新增列表。
    List<SecurityEntity> createEntities = new ArrayList<>();
    // 更新列表。
    List<SecurityEntity> updateEntities = new ArrayList<>();
    // 删除列表。
    List<SecurityEntity> deleteEntities = new ArrayList<>();

    Map<String, SecurityEntity> entities = securities.stream()
        .filter(security -> security.getHoldingQuantity() > 0)
        .map(SecurityModel::toEntity)
        .collect(Collectors.toMap(SecurityEntity::getSecurityCode, Function.identity()));
    Map<String, SecurityEntity> existingEntities = Optional.ofNullable(this.lambdaQuery().list())
        .map(
            records -> records.stream().collect(Collectors.toMap(SecurityEntity::getSecurityCode, Function.identity())))
        .orElse(Collections.emptyMap());

    for (Entry<String, SecurityEntity> entry : entities.entrySet()) {
      String securityCode = entry.getKey();
      SecurityEntity entity = entry.getValue();

      if (!existingEntities.containsKey(securityCode)) {
        // 证券不存在于数据库中，需要新增。
        createEntities.add(entity);

        continue;
      }

      SecurityEntity existingEntity = existingEntities.get(securityCode);
      if (SecurityEntity.equals(entity, existingEntity)) {
        // 证券存在于数据库中，且数据相同，跳过。
        continue;
      }

      // 证券存在于数据库中，但数据不同，需要更新。
      // 注意：设置 ID。
      entity.setId(existingEntity.getId());

      // 修正持仓盈亏最大值和最小值。
      if (NumberUtil.greaterThan(existingEntity.getPositionProfitLossMax(), entity.getPositionProfitLossMax())) {
        // 使用旧值。
        entity.setPositionProfitLossMax(existingEntity.getPositionProfitLossMax());
      }
      if (NumberUtil.lessThan(existingEntity.getPositionProfitLossMin(), entity.getPositionProfitLossMin())) {
        // 使用旧值。
        entity.setPositionProfitLossMin(existingEntity.getPositionProfitLossMin());
      }

      // 修正当日盈亏最大值和最小值。
      if (NumberUtil.greaterThan(existingEntity.getDailyProfitLossMax(), entity.getDailyProfitLossMax())) {
        // 使用旧值。
        entity.setDailyProfitLossMax(existingEntity.getDailyProfitLossMax());
      }
      if (NumberUtil.lessThan(existingEntity.getDailyProfitLossMin(), entity.getDailyProfitLossMin())) {
        // 使用旧值。
        entity.setDailyProfitLossMin(existingEntity.getDailyProfitLossMin());
      }

      // 持仓盈利新高。
      this.newPositionProfitLossMax(entity, existingEntity);
      // 持仓亏损新低。
      this.newPositionProfitLossMin(entity, existingEntity);
      // 当日盈利新高。
      this.newDailyProfitLossMax(entity, existingEntity);
      // 当日亏损新低。
      this.newDailyProfitLossMin(entity, existingEntity);

      updateEntities.add(entity);
    }

    for (Entry<String, SecurityEntity> entry : existingEntities.entrySet()) {
      String securityCode = entry.getKey();

      if (!entities.containsKey(securityCode)) {
        // 数据库中存在不再持仓的证券，需要删除。
        deleteEntities.add(entry.getValue());
      }
    }

    boolean changed = false;

    if (CollectionUtils.isNotEmpty(createEntities)) {
      // 新增。
      this.saveBatch(createEntities);
      changed = true;
    }

    if (CollectionUtils.isNotEmpty(updateEntities)) {
      // 更新。
      this.updateBatchById(updateEntities);
      changed = true;
    }

    if (CollectionUtils.isNotEmpty(deleteEntities)) {
      // 删除。
      this.removeByIds(deleteEntities.stream().map(SecurityEntity::getId).collect(Collectors.toList()));
      changed = true;
    }

    return changed;
  }

  private String getSecurityCode(AppTableRecord record) {
    return (String) record.getFields().get("证券代码");
  }

  private Map<String, Object> toRecord(SecurityModel model) {
    Map<String, Object> record = new HashMap<>();

    record.put("证券代码", model.getSecurityCode());
    record.put("证券名称", model.getSecurityName());
    record.put("持仓数量", model.getHoldingQuantity());
    record.put("可用数量", model.getAvailableQuantity());
    record.put("成本价", model.getCostPrice());
    record.put("当前价", model.getCurrentPrice());
    record.put("最新市值", model.getMarketValue());
    record.put("持仓盈亏", model.getPositionProfitLoss());
    record.put("持仓盈亏比例", model.getPositionProfitLossRatio());
    record.put("当日盈亏", model.getDailyProfitLoss());
    record.put("当日盈亏比例", model.getDailyProfitLossRatio());

    return record;
  }

  private SecurityModel toModel(AppTableRecord record) {
    SecurityModel model = new SecurityModel();

    model.setSecurityCode(this.getSecurityCode(record));
    model.setSecurityName((String) record.getFields().get("证券名称"));
    model.setHoldingQuantity(NumberUtil.toInteger((String) record.getFields().get("持仓数量"), 0));
    model.setAvailableQuantity(NumberUtil.toInteger((String) record.getFields().get("可用数量"), 0));
    model.setCostPrice(NumberUtil.toBigDecimal((String) record.getFields().get("成本价"), BigDecimal.ZERO));
    model.setCurrentPrice(NumberUtil.toBigDecimal((String) record.getFields().get("当前价"), BigDecimal.ZERO));
    model.setMarketValue(NumberUtil.toBigDecimal((String) record.getFields().get("最新市值"), BigDecimal.ZERO));
    model.setPositionProfitLoss(NumberUtil.toBigDecimal((String) record.getFields().get("持仓盈亏"), BigDecimal.ZERO));
    model.setPositionProfitLossRatio(
        NumberUtil.toBigDecimal((String) record.getFields().get("持仓盈亏比例"), BigDecimal.ZERO));
    model.setDailyProfitLoss(NumberUtil.toBigDecimal((String) record.getFields().get("当日盈亏"), BigDecimal.ZERO));
    model.setDailyProfitLossRatio(NumberUtil.toBigDecimal((String) record.getFields().get("当日盈亏比例"), BigDecimal.ZERO));

    return model;
  }

  private void syncBitable(List<SecurityModel> models) throws Exception {
    // 过滤掉不再持仓的证券。
    models = models.stream().filter(model -> model.getHoldingQuantity() > 0).collect(Collectors.toList());

    // 获取多维表格中的记录。
    Map<String, AppTableRecord> existingRecords = Optional
        .ofNullable(this.bitableClient.listTableRecords(this.bitable, this.securitiesTable))
        .map(records -> records.stream().collect(Collectors.toMap(this::getSecurityCode, Function.identity())))
        .orElse(Collections.emptyMap());

    // 新增列表。
    List<Map<String, Object>> createRecords = new ArrayList<>();
    // 更新列表。
    List<String> updateRecordIds = new ArrayList<>();
    List<Map<String, Object>> updateRecords = new ArrayList<>();

    for (SecurityModel model : models) {
      // 证券代码。
      String securityCode = model.getSecurityCode();

      if (!existingRecords.containsKey(securityCode)) {
        // 证券不存在于多维表格中，需要新增。
        createRecords.add(this.toRecord(model));

        continue;
      }

      AppTableRecord existingRecord = existingRecords.get(securityCode);
      if (SecurityModel.equals(model, this.toModel(existingRecord))) {
        // 证券存在于多维表格中，且数据相同，跳过。
        continue;
      }

      // 证券存在于多维表格中，但数据不同，需要更新。
      updateRecordIds.add(existingRecord.getRecordId());
      updateRecords.add(this.toRecord(model));
    }

    // 删除列表。
    List<String> deleteRecordIds = new ArrayList<>();

    // 证券代码集合。
    Set<String> securityCodes = models.stream().map(SecurityModel::getSecurityCode).collect(Collectors.toSet());

    for (Entry<String, AppTableRecord> entry : existingRecords.entrySet()) {
      if (!securityCodes.contains(entry.getKey())) {
        // 不再持仓的证券，需要删除。
        deleteRecordIds.add(entry.getValue().getRecordId());
      }
    }

    if (!createRecords.isEmpty()) {
      // 新增。
      this.bitableClient.batchCreateRecords(this.bitable, this.securitiesTable, createRecords);
    }

    if (!updateRecordIds.isEmpty()) {
      // 更新。
      this.bitableClient.batchUpdateRecords(this.bitable, this.securitiesTable, updateRecordIds, updateRecords);
    }

    if (!deleteRecordIds.isEmpty()) {
      // 删除。
      this.bitableClient.batchDeleteRecords(this.bitable, this.securitiesTable, deleteRecordIds);
    }
  }

  @Override
  public void sync(List<SecurityModel> models) {
    try {
      if (this.syncDb(models)) {
        this.syncBitable(models);
      }
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
