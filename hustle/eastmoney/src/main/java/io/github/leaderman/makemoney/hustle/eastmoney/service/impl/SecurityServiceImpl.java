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

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lark.oapi.service.bitable.v1.model.AppTableRecord;

import io.github.leaderman.makemoney.hustle.config.ConfigClient;
import io.github.leaderman.makemoney.hustle.eastmoney.domain.entity.SecurityEntity;
import io.github.leaderman.makemoney.hustle.eastmoney.domain.model.SecurityModel;
import io.github.leaderman.makemoney.hustle.eastmoney.mapper.SecurityMapper;
import io.github.leaderman.makemoney.hustle.eastmoney.service.SecurityService;
import io.github.leaderman.makemoney.hustle.feishu.BitableClient;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SecurityServiceImpl extends ServiceImpl<SecurityMapper, SecurityEntity> implements SecurityService {
  private final ConfigClient configClient;
  private final BitableClient bitableClient;

  private String bitable;
  private String securitiesTable;

  @PostConstruct
  public void init() {
    this.bitable = this.configClient.getString("eastmoney.bitable");
    this.securitiesTable = this.configClient.getString("eastmoney.bitable.securities");
  }

  private void syncDb(List<SecurityModel> securities) {
    // 创建实体列表。
    List<SecurityEntity> creatEntities = new ArrayList<>();

    // 更新实体列表。
    List<SecurityEntity> updateEntities = new ArrayList<>();

    // 删除实体列表。
    List<Long> deleteIds = new ArrayList<>();

    Map<String, SecurityEntity> leftEntities = securities.stream().map(SecurityModel::toEntity)
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
        // 创建实体。
        creatEntities.add(security);
      } else {
        // 更新实体。
        security.setId(rightEntities.get(securityCode).getId());
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
      // 创建。
      log.info("创建 {} 条实体", creatEntities.size());
      this.saveBatch(creatEntities);
    }

    if (CollectionUtils.isNotEmpty(updateEntities)) {
      // 更新。
      log.info("更新 {} 条实体", updateEntities.size());
      this.updateBatchById(updateEntities);
    }

    if (CollectionUtils.isNotEmpty(deleteIds)) {
      // 删除。
      log.info("删除 {} 条实体", deleteIds.size());
      this.removeByIds(deleteIds);
    }
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
        createRecords.add(toBitableRecord(security));
      } else {
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
  public void sync(List<SecurityModel> models) {
    try {
      this.syncDb(models);
      this.syncBitable(models);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
