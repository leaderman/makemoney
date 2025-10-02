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
@TableName("position")
public class PositionEntity extends BaseEntity {
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

  public static boolean equals(PositionEntity left, PositionEntity right) {
    return NumberUtil.equals(left.getTotalAssets(), right.getTotalAssets())
        && NumberUtil.equals(left.getSecuritiesMarketValue(), right.getSecuritiesMarketValue())
        && NumberUtil.equals(left.getAvailableFunds(), right.getAvailableFunds())
        && NumberUtil.equals(left.getPositionProfitLoss(), right.getPositionProfitLoss())
        && NumberUtil.equals(left.getCashBalance(), right.getCashBalance())
        && NumberUtil.equals(left.getWithdrawableFunds(), right.getWithdrawableFunds())
        && NumberUtil.equals(left.getDailyProfitLoss(), right.getDailyProfitLoss())
        && NumberUtil.equals(left.getFrozenFunds(), right.getFrozenFunds());
  }
}
