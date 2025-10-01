package io.github.leaderman.makemoney.hustle.eastmoney.domain.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableName;

import io.github.leaderman.makemoney.hustle.domain.entity.BaseEntity;
import io.github.leaderman.makemoney.hustle.lang.NumberUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName("security")
public class SecurityEntity extends BaseEntity {
  private String securityCode;
  private String securityName;
  private Integer holdingQuantity;
  private Integer availableQuantity;
  private BigDecimal costPrice;
  private BigDecimal currentPrice;
  private BigDecimal marketValue;
  private BigDecimal positionProfitLoss;
  private BigDecimal positionProfitLossRatio;
  private BigDecimal positionProfitLossMax;
  private BigDecimal positionProfitLossMin;
  private BigDecimal dailyProfitLoss;
  private BigDecimal dailyProfitLossRatio;
  private BigDecimal dailyProfitLossMax;
  private BigDecimal dailyProfitLossMin;

  public static boolean equals(SecurityEntity left, SecurityEntity right) {
    return left.getSecurityCode().equals(right.getSecurityCode())
        && left.getSecurityName().equals(right.getSecurityName())
        && left.getHoldingQuantity().equals(right.getHoldingQuantity())
        && left.getAvailableQuantity().equals(right.getAvailableQuantity())
        && NumberUtil.equals(left.getCostPrice(), right.getCostPrice())
        && NumberUtil.equals(left.getCurrentPrice(), right.getCurrentPrice())
        && NumberUtil.equals(left.getMarketValue(), right.getMarketValue())
        && NumberUtil.equals(left.getPositionProfitLoss(), right.getPositionProfitLoss())
        && NumberUtil.equals(left.getPositionProfitLossRatio(), right.getPositionProfitLossRatio())
        && NumberUtil.equals(left.getDailyProfitLoss(), right.getDailyProfitLoss())
        && NumberUtil.equals(left.getDailyProfitLossRatio(), right.getDailyProfitLossRatio());
  }
}
