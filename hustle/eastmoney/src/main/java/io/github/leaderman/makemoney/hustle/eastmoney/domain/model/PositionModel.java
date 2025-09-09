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
  private BigDecimal totalAssets;
  private BigDecimal securitiesMarketValue;
  private BigDecimal availableFunds;
  private BigDecimal positionProfitLoss;
  private BigDecimal positionProfitLossMax;
  private BigDecimal positionProfitLossMin;
  private BigDecimal cashBalance;
  private BigDecimal withdrawableFunds;
  private BigDecimal dailyProfitLoss;
  private BigDecimal dailyProfitLossMax;
  private BigDecimal dailyProfitLossMin;
  private BigDecimal frozenFunds;
  private List<SecurityModel> securities;

  public static PositionModel from(SyncPositionRequest request) {
    PositionModel model = new PositionModel();

    model.setTotalAssets(NumberUtil.toBigDecimal(request.getTotalAssets(), BigDecimal.ZERO));
    model.setSecuritiesMarketValue(NumberUtil.toBigDecimal(request.getSecuritiesMarketValue(), BigDecimal.ZERO));
    model.setAvailableFunds(NumberUtil.toBigDecimal(request.getAvailableFunds(), BigDecimal.ZERO));

    model.setPositionProfitLoss(NumberUtil.toBigDecimal(request.getPositionProfitLoss(), BigDecimal.ZERO));
    model.setPositionProfitLossMax(model.getPositionProfitLoss());
    model.setPositionProfitLossMin(model.getPositionProfitLoss());

    model.setCashBalance(NumberUtil.toBigDecimal(request.getCashBalance(), BigDecimal.ZERO));
    model.setWithdrawableFunds(NumberUtil.toBigDecimal(request.getWithdrawableFunds(), BigDecimal.ZERO));

    model.setDailyProfitLoss(NumberUtil.toBigDecimal(request.getDailyProfitLoss(), BigDecimal.ZERO));
    model.setDailyProfitLossMax(model.getDailyProfitLoss());
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
