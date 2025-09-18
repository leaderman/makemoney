package io.github.leaderman.makemoney.hustle.eastmoney.domain.model;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import io.github.leaderman.makemoney.hustle.domain.model.BaseModel;
import io.github.leaderman.makemoney.hustle.eastmoney.domain.entity.PositionEntity;
import io.github.leaderman.makemoney.hustle.eastmoney.domain.entity.SecurityEntity;
import io.github.leaderman.makemoney.hustle.eastmoney.domain.request.SyncPositionRequest;
import io.github.leaderman.makemoney.hustle.lang.NumberUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PositionModel extends BaseModel {
  // 总资产。
  private BigDecimal totalAssets;
  // 证券市值。
  private BigDecimal securitiesMarketValue;
  // 可用资金。
  private BigDecimal availableFunds;
  // 持仓盈亏。
  private BigDecimal positionProfitLoss;
  // 持仓盈亏最大值。
  private BigDecimal positionProfitLossMax;
  // 持仓盈亏最小值。
  private BigDecimal positionProfitLossMin;
  // 资金余额。
  private BigDecimal cashBalance;
  // 可取资金。
  private BigDecimal withdrawableFunds;
  // 当日盈亏。
  private BigDecimal dailyProfitLoss;
  // 当日盈亏最大值。
  private BigDecimal dailyProfitLossMax;
  // 当日盈亏最小值。
  private BigDecimal dailyProfitLossMin;
  // 冻结资金。
  private BigDecimal frozenFunds;
  // 证券列表。
  private List<SecurityModel> securities;

  public static PositionModel from(SyncPositionRequest request) {
    PositionModel model = new PositionModel();

    model.setTotalAssets(NumberUtil.toBigDecimal(request.getTotalAssets(), BigDecimal.ZERO));
    model.setSecuritiesMarketValue(NumberUtil.toBigDecimal(request.getSecuritiesMarketValue(), BigDecimal.ZERO));
    model.setAvailableFunds(NumberUtil.toBigDecimal(request.getAvailableFunds(), BigDecimal.ZERO));

    // 持仓盈亏。
    model.setPositionProfitLoss(NumberUtil.toBigDecimal(request.getPositionProfitLoss(), BigDecimal.ZERO));
    // 持仓盈亏最大值默认为持仓盈亏。
    model.setPositionProfitLossMax(model.getPositionProfitLoss());
    // 持仓盈亏最小值默认为持仓盈亏。
    model.setPositionProfitLossMin(model.getPositionProfitLoss());

    model.setCashBalance(NumberUtil.toBigDecimal(request.getCashBalance(), BigDecimal.ZERO));
    model.setWithdrawableFunds(NumberUtil.toBigDecimal(request.getWithdrawableFunds(), BigDecimal.ZERO));

    // 当日盈亏。
    model.setDailyProfitLoss(NumberUtil.toBigDecimal(request.getDailyProfitLoss(), BigDecimal.ZERO));
    // 当日盈亏最大值默认为当日盈亏。
    model.setDailyProfitLossMax(model.getDailyProfitLoss());
    // 当日盈亏最小值默认为当日盈亏。
    model.setDailyProfitLossMin(model.getDailyProfitLoss());

    model.setFrozenFunds(NumberUtil.toBigDecimal(request.getFrozenFunds(), BigDecimal.ZERO));

    if (Objects.nonNull(request.getSecurities())) {
      model.setSecurities(request.getSecurities().stream().map(SecurityModel::from).collect(Collectors.toList()));
    } else {
      model.setSecurities(Collections.emptyList());
    }

    return model;
  }

  public static PositionEntity toEntity(PositionModel model) {
    PositionEntity entity = new PositionEntity();

    entity.setTotalAssets(model.getTotalAssets());
    entity.setSecuritiesMarketValue(model.getSecuritiesMarketValue());
    entity.setAvailableFunds(model.getAvailableFunds());

    entity.setPositionProfitLoss(model.getPositionProfitLoss());
    entity.setPositionProfitLossMax(model.getPositionProfitLossMax());
    entity.setPositionProfitLossMin(model.getPositionProfitLossMin());

    entity.setCashBalance(model.getCashBalance());
    entity.setWithdrawableFunds(model.getWithdrawableFunds());

    entity.setDailyProfitLoss(model.getDailyProfitLoss());
    entity.setDailyProfitLossMax(model.getDailyProfitLossMax());
    entity.setDailyProfitLossMin(model.getDailyProfitLossMin());

    entity.setFrozenFunds(model.getFrozenFunds());

    return entity;
  }

  public static List<SecurityEntity> toEntities(PositionModel model) {
    return model.getSecurities().stream().map(SecurityModel::toEntity).collect(Collectors.toList());
  }
}
