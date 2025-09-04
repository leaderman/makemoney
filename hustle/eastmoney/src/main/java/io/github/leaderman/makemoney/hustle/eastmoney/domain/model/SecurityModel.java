package io.github.leaderman.makemoney.hustle.eastmoney.domain.model;

import java.math.BigDecimal;
import java.util.Objects;

import io.github.leaderman.makemoney.hustle.domain.model.BaseModel;
import io.github.leaderman.makemoney.hustle.eastmoney.domain.entity.SecurityEntity;
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

    String positionProfitLossRatio = security.getPositionProfitLossRatio();
    if (positionProfitLossRatio.endsWith("%")) {
      positionProfitLossRatio = positionProfitLossRatio.substring(0, positionProfitLossRatio.length() - 1);
    }
    model.setPositionProfitLossRatio(NumberUtil.toBigDecimal(positionProfitLossRatio, BigDecimal.ZERO));

    model.setDailyProfitLoss(NumberUtil.toBigDecimal(security.getDailyProfitLoss(), BigDecimal.ZERO));

    String dailyProfitLossRatio = security.getDailyProfitLossRatio();
    if (dailyProfitLossRatio.endsWith("%")) {
      dailyProfitLossRatio = dailyProfitLossRatio.substring(0, dailyProfitLossRatio.length() - 1);
    }
    model.setDailyProfitLossRatio(NumberUtil.toBigDecimal(dailyProfitLossRatio, BigDecimal.ZERO));

    return model;
  }

  public static SecurityEntity toEntity(SecurityModel model) {
    SecurityEntity entity = new SecurityEntity();

    entity.setSecurityCode(model.getSecurityCode());
    entity.setSecurityName(model.getSecurityName());
    entity.setHoldingQuantity(model.getHoldingQuantity());
    entity.setAvailableQuantity(model.getAvailableQuantity());
    entity.setCostPrice(model.getCostPrice());
    entity.setCurrentPrice(model.getCurrentPrice());
    entity.setMarketValue(model.getMarketValue());
    entity.setPositionProfitLoss(model.getPositionProfitLoss());
    entity.setPositionProfitLossRatio(model.getPositionProfitLossRatio());
    entity.setDailyProfitLoss(model.getDailyProfitLoss());
    entity.setDailyProfitLossRatio(model.getDailyProfitLossRatio());

    return entity;
  }
}