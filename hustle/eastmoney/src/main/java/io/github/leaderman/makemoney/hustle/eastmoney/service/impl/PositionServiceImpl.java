package io.github.leaderman.makemoney.hustle.eastmoney.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.lark.oapi.service.bitable.v1.model.AppTableRecord;

import io.github.leaderman.makemoney.hustle.config.ConfigClient;
import io.github.leaderman.makemoney.hustle.eastmoney.domain.model.PositionModel;
import io.github.leaderman.makemoney.hustle.eastmoney.domain.model.SecurityModel;
import io.github.leaderman.makemoney.hustle.eastmoney.service.PositionService;
import io.github.leaderman.makemoney.hustle.feishu.BitableClient;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
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

  private void syncSecurities(List<SecurityModel> securities) throws Exception {
    Map<String, SecurityModel> leftRecords = securities.stream()
        .collect(Collectors.toMap(SecurityModel::getSecurityCode, Function.identity()));

    Map<String, AppTableRecord> rightRecords = this.bitableClient.listTableRecords(this.bitable, this.securitiesTable)
        .stream()
        .collect(Collectors.toMap(record -> (String) record.getFields().get("证券代码"), Function.identity()));
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
