package io.github.leaderman.makemoney.hustle.eastmoney.domain.model;

import java.math.BigDecimal;
import java.util.Objects;

import io.github.leaderman.makemoney.hustle.domain.model.BaseModel;
import io.github.leaderman.makemoney.hustle.eastmoney.domain.entity.SecurityEntity;
import io.github.leaderman.makemoney.hustle.eastmoney.domain.request.SyncPositionRequest;
import io.github.leaderman.makemoney.hustle.lang.NumberUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SecurityModel extends BaseModel {
  // 证券代码。
  private String securityCode;
  // 证券名称。
  private String securityName;
  // 持仓数量。
  private Integer holdingQuantity;
  // 可用数量。
  private Integer availableQuantity;
  // 成本价。
  private BigDecimal costPrice;
  // 当前价。
  private BigDecimal currentPrice;
  // 最新市值。
  private BigDecimal marketValue;
  // 持仓盈亏。
  private BigDecimal positionProfitLoss;
  // 持仓盈亏比例。
  private BigDecimal positionProfitLossRatio;
  // 持仓盈亏最大值。
  private BigDecimal positionProfitLossMax;
  // 持仓盈亏最小值。
  private BigDecimal positionProfitLossMin;
  // 当日盈亏。
  private BigDecimal dailyProfitLoss;
  // 当日盈亏比例。
  private BigDecimal dailyProfitLossRatio;
  // 当日盈亏最大值。
  private BigDecimal dailyProfitLossMax;
  // 当日盈亏最小值。
  private BigDecimal dailyProfitLossMin;

  public static SecurityModel from(SyncPositionRequest.Security security) {
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

    model.setPositionProfitLossMax(model.getPositionProfitLoss());
    model.setPositionProfitLossMin(model.getPositionProfitLoss());

    model.setDailyProfitLoss(NumberUtil.toBigDecimal(security.getDailyProfitLoss(), BigDecimal.ZERO));

    String dailyProfitLossRatio = security.getDailyProfitLossRatio();
    if (dailyProfitLossRatio.endsWith("%")) {
      dailyProfitLossRatio = dailyProfitLossRatio.substring(0, dailyProfitLossRatio.length() - 1);
    }
    model.setDailyProfitLossRatio(NumberUtil.toBigDecimal(dailyProfitLossRatio, BigDecimal.ZERO));

    model.setDailyProfitLossMax(model.getDailyProfitLoss());
    model.setDailyProfitLossMin(model.getDailyProfitLoss());

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
    entity.setPositionProfitLossMax(model.getPositionProfitLossMax());
    entity.setPositionProfitLossMin(model.getPositionProfitLossMin());
    entity.setDailyProfitLoss(model.getDailyProfitLoss());
    entity.setDailyProfitLossRatio(model.getDailyProfitLossRatio());
    entity.setDailyProfitLossMax(model.getDailyProfitLossMax());
    entity.setDailyProfitLossMin(model.getDailyProfitLossMin());

    return entity;
  }

  public static SecurityModel from(SecurityEntity entity) {
    SecurityModel model = new SecurityModel();

    model.setSecurityCode(entity.getSecurityCode());
    model.setSecurityName(entity.getSecurityName());
    model.setHoldingQuantity(entity.getHoldingQuantity());
    model.setAvailableQuantity(entity.getAvailableQuantity());
    model.setCostPrice(entity.getCostPrice());
    model.setCurrentPrice(entity.getCurrentPrice());
    model.setMarketValue(entity.getMarketValue());
    model.setPositionProfitLoss(entity.getPositionProfitLoss());
    model.setPositionProfitLossRatio(entity.getPositionProfitLossRatio());
    model.setPositionProfitLossMax(entity.getPositionProfitLossMax());
    model.setPositionProfitLossMin(entity.getPositionProfitLossMin());
    model.setDailyProfitLoss(entity.getDailyProfitLoss());
    model.setDailyProfitLossRatio(entity.getDailyProfitLossRatio());
    model.setDailyProfitLossMax(entity.getDailyProfitLossMax());
    model.setDailyProfitLossMin(entity.getDailyProfitLossMin());

    return model;
  }
}