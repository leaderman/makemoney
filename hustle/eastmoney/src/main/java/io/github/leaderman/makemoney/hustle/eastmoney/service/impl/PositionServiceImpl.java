package io.github.leaderman.makemoney.hustle.eastmoney.service.impl;

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

import com.lark.oapi.service.bitable.v1.model.AppTableRecord;

import io.github.leaderman.makemoney.hustle.config.ConfigClient;
import io.github.leaderman.makemoney.hustle.eastmoney.domain.model.PositionModel;
import io.github.leaderman.makemoney.hustle.eastmoney.domain.model.SecurityModel;
import io.github.leaderman.makemoney.hustle.eastmoney.service.PositionService;
import io.github.leaderman.makemoney.hustle.feishu.BitableClient;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PositionServiceImpl implements PositionService {
  private final ConfigClient configClient;
  private final BitableClient bitableClient;

  private String bitable;
  private String positionTable;
  private String securitiesTable;

  @PostConstruct
  public void init() {
    this.bitable = this.configClient.getString("eastmoney.bitable");
    this.positionTable = this.configClient.getString("eastmoney.bitable.position");
    this.securitiesTable = this.configClient.getString("eastmoney.bitable.securities");
  }

  private void syncPosition(PositionModel model) throws Exception {
    Map<String, AppTableRecord> positionRecords = this.bitableClient.listTableRecords(this.bitable, this.positionTable)
        .stream()
        .collect(Collectors.toMap(record -> (String) record.getFields().get("资金名称"), Function.identity()));

    List<String> recordIds = new ArrayList<>(positionRecords.size());
    List<Map<String, Object>> records = new ArrayList<>(positionRecords.size());

    recordIds.add(positionRecords.get("总资产").getRecordId());
    records.add(Map.of("资金值", model.getTotalAssets()));

    recordIds.add(positionRecords.get("证券市值").getRecordId());
    records.add(Map.of("资金值", model.getSecuritiesMarketValue()));

    recordIds.add(positionRecords.get("可用资金").getRecordId());
    records.add(Map.of("资金值", model.getAvailableFunds()));

    recordIds.add(positionRecords.get("持仓盈亏").getRecordId());
    records.add(Map.of("资金值", model.getPositionProfitLoss()));

    recordIds.add(positionRecords.get("资金余额").getRecordId());
    records.add(Map.of("资金值", model.getCashBalance()));

    recordIds.add(positionRecords.get("可取资金").getRecordId());
    records.add(Map.of("资金值", model.getWithdrawableFunds()));

    recordIds.add(positionRecords.get("当日盈亏").getRecordId());
    records.add(Map.of("资金值", model.getDailyProfitLoss()));

    recordIds.add(positionRecords.get("冻结资金").getRecordId());
    records.add(Map.of("资金值", model.getFrozenFunds()));

    this.bitableClient.batchUpdateRecords(this.bitable, this.positionTable, recordIds, records);
  }

  private Map<String, Object> toRecord(SecurityModel security) {
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

  private void syncSecurities(List<SecurityModel> securities) throws Exception {
    // 创建记录列表。
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
        // 创建记录。
        createRecords.add(toRecord(security));
      } else {
        // 更新记录。
        updateRecordIds.add(rightRecords.get(securityCode).getRecordId());
        updateRecords.add(toRecord(security));
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
      // 创建。
      log.info("创建 {} 条记录", createRecords.size());
      this.bitableClient.batchCreateRecords(this.bitable, this.securitiesTable, createRecords);
    }

    if (CollectionUtils.isNotEmpty(updateRecords)) {
      // 更新。
      log.info("更新 {} 条记录", updateRecords.size());
      this.bitableClient.batchUpdateRecords(this.bitable, this.securitiesTable, updateRecordIds, updateRecords);
    }

    if (CollectionUtils.isNotEmpty(deleteRecordIds)) {
      // 删除。
      log.info("删除 {} 条记录", deleteRecordIds.size());
      this.bitableClient.batchDeleteRecords(this.bitable, this.securitiesTable, deleteRecordIds);
    }
  }

  @Override
  public void sync(PositionModel model) {
    try {
      this.syncPosition(model);
      this.syncSecurities(model.getSecurities());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
