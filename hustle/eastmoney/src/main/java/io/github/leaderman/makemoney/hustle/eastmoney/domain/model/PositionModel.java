package io.github.leaderman.makemoney.hustle.eastmoney.domain.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;

import io.github.leaderman.makemoney.hustle.domain.model.BaseModel;
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
  private BigDecimal cashBalance;
  private BigDecimal withdrawableFunds;
  private BigDecimal dailyProfitLoss;
  private BigDecimal frozenFunds;
  private List<SecurityModel> securities;

  public static PositionModel from(SyncPositionRequest request) {
    PositionModel model = new PositionModel();

    model.setTotalAssets(NumberUtil.toBigDecimal(request.getTotalAssets(), BigDecimal.ZERO));
    model.setSecuritiesMarketValue(NumberUtil.toBigDecimal(request.getSecuritiesMarketValue(), BigDecimal.ZERO));
    model.setAvailableFunds(NumberUtil.toBigDecimal(request.getAvailableFunds(), BigDecimal.ZERO));
    model.setPositionProfitLoss(NumberUtil.toBigDecimal(request.getPositionProfitLoss(), BigDecimal.ZERO));
    model.setCashBalance(NumberUtil.toBigDecimal(request.getCashBalance(), BigDecimal.ZERO));
    model.setWithdrawableFunds(NumberUtil.toBigDecimal(request.getWithdrawableFunds(), BigDecimal.ZERO));
    model.setDailyProfitLoss(NumberUtil.toBigDecimal(request.getDailyProfitLoss(), BigDecimal.ZERO));
    model.setFrozenFunds(NumberUtil.toBigDecimal(request.getFrozenFunds(), BigDecimal.ZERO));

    if (CollectionUtils.isNotEmpty(request.getSecurities())) {
      model.setSecurities(request.getSecurities().stream().map(SecurityModel::from).collect(Collectors.toList()));
    }

    return model;
  }

}
