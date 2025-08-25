package io.github.leaderman.makemoney.hustle.eastmoney.domain.model;

import java.math.BigDecimal;

import io.github.leaderman.makemoney.hustle.domain.model.BaseModel;
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
}