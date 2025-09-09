package io.github.leaderman.makemoney.hustle.eastmoney.domain.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableName;

import io.github.leaderman.makemoney.hustle.domain.entity.BaseEntity;
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
}
