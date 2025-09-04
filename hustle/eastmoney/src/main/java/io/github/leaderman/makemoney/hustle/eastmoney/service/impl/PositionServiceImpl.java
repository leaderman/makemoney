package io.github.leaderman.makemoney.hustle.eastmoney.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lark.oapi.service.bitable.v1.model.AppTableRecord;

import io.github.leaderman.makemoney.hustle.config.ConfigClient;
import io.github.leaderman.makemoney.hustle.eastmoney.domain.entity.PositionEntity;
import io.github.leaderman.makemoney.hustle.eastmoney.domain.model.PositionModel;
import io.github.leaderman.makemoney.hustle.eastmoney.mapper.PositionMapper;
import io.github.leaderman.makemoney.hustle.eastmoney.service.PositionService;
import io.github.leaderman.makemoney.hustle.eastmoney.service.SecurityService;
import io.github.leaderman.makemoney.hustle.feishu.BitableClient;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PositionServiceImpl extends ServiceImpl<PositionMapper, PositionEntity> implements PositionService {
  private final ConfigClient configClient;
  private final BitableClient bitableClient;

  private final SecurityService securityService;

  private String bitable;
  private String positionTable;

  @PostConstruct
  public void init() {
    this.bitable = this.configClient.getString("eastmoney.bitable");
    this.positionTable = this.configClient.getString("eastmoney.bitable.position");
  }

  private void syncDb(PositionModel model) {
    PositionEntity entity = this.getOne(null);
    if (Objects.isNull(entity)) {
      entity = PositionModel.toEntity(model);
    } else {
      entity.setTotalAssets(model.getTotalAssets());
      entity.setSecuritiesMarketValue(model.getSecuritiesMarketValue());
      entity.setAvailableFunds(model.getAvailableFunds());
      entity.setPositionProfitLoss(model.getPositionProfitLoss());
      entity.setCashBalance(model.getCashBalance());
      entity.setWithdrawableFunds(model.getWithdrawableFunds());
      entity.setDailyProfitLoss(model.getDailyProfitLoss());
      entity.setFrozenFunds(model.getFrozenFunds());
    }

    this.saveOrUpdate(entity);
  }

  private void syncBitable(PositionModel model) throws Exception {
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

  @Override
  @Transactional
  public void sync(PositionModel model) {
    try {
      this.syncDb(model);
      this.syncBitable(model);

      this.securityService.sync(model.getSecurities());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
