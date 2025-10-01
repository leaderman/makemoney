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
@TableName("fund")
public class FundEntity extends BaseEntity {
  private String code;
  private String name;
  private BigDecimal openPrice;
  private BigDecimal latestPrice;
  private BigDecimal highPrice;
  private BigDecimal lowPrice;
  private BigDecimal changePercent;
  private BigDecimal changeAmount;
  private BigDecimal prevClose;

  public static boolean equals(FundEntity left, FundEntity right) {
    return left.getCode().equals(right.getCode())
        && left.getName().equals(right.getName())
        && NumberUtil.equals(left.getOpenPrice(), right.getOpenPrice())
        && NumberUtil.equals(left.getLatestPrice(), right.getLatestPrice())
        && NumberUtil.equals(left.getHighPrice(), right.getHighPrice())
        && NumberUtil.equals(left.getLowPrice(), right.getLowPrice())
        && NumberUtil.equals(left.getChangePercent(), right.getChangePercent())
        && NumberUtil.equals(left.getChangeAmount(), right.getChangeAmount())
        && NumberUtil.equals(left.getPrevClose(), right.getPrevClose());
  }
}
