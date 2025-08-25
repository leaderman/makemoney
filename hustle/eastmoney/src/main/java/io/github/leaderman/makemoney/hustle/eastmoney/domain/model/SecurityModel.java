package io.github.leaderman.makemoney.hustle.eastmoney.domain.model;

import java.math.BigDecimal;
import java.util.Objects;

import io.github.leaderman.makemoney.hustle.domain.model.BaseModel;
import io.github.leaderman.makemoney.hustle.eastmoney.domain.request.SyncPositionRequest.Security;
import io.github.leaderman.makemoney.hustle.lang.NumberUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SecurityModel extends BaseModel {
  private String securityCode;
  private String securityName;
  private Integer holdingQuantity;
  private Integer availableQuantity;
  private BigDecimal costPrice;
  private BigDecimal currentPrice;
  private BigDecimal marketValue;
  private BigDecimal positionProfitLoss;
  private BigDecimal positionProfitLossRatio;
  private BigDecimal dailyProfitLoss;
  private BigDecimal dailyProfitLossRatio;

  public static SecurityModel from(Security security) {
    SecurityModel model = new SecurityModel();

    model.setSecurityCode(Objects.nonNull(security.getSecurityCode()) ? security.getSecurityCode() : "");
    model.setSecurityName(Objects.nonNull(security.getSecurityName()) ? security.getSecurityName() : "");
    model.setHoldingQuantity(NumberUtil.toInteger(security.getHoldingQuantity(), 0));
    model.setAvailableQuantity(NumberUtil.toInteger(security.getAvailableQuantity(), 0));
    model.setCostPrice(NumberUtil.toBigDecimal(security.getCostPrice(), BigDecimal.ZERO));
    model.setCurrentPrice(NumberUtil.toBigDecimal(security.getCurrentPrice(), BigDecimal.ZERO));
    model.setMarketValue(NumberUtil.toBigDecimal(security.getMarketValue(), BigDecimal.ZERO));
    model.setPositionProfitLoss(NumberUtil.toBigDecimal(security.getPositionProfitLoss(), BigDecimal.ZERO));
    model.setPositionProfitLossRatio(NumberUtil.toBigDecimal(security.getPositionProfitLossRatio(), BigDecimal.ZERO));
    model.setDailyProfitLoss(NumberUtil.toBigDecimal(security.getDailyProfitLoss(), BigDecimal.ZERO));
    model.setDailyProfitLossRatio(NumberUtil.toBigDecimal(security.getDailyProfitLossRatio(), BigDecimal.ZERO));

    return model;
  }
}