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
@TableName("position")
public class PositionEntity extends BaseEntity {
  private BigDecimal totalAssets;
  private BigDecimal securitiesMarketValue;
  private BigDecimal availableFunds;
  private BigDecimal positionProfitLoss;
  private BigDecimal cashBalance;
  private BigDecimal withdrawableFunds;
  private BigDecimal dailyProfitLoss;
  private BigDecimal frozenFunds;
}
