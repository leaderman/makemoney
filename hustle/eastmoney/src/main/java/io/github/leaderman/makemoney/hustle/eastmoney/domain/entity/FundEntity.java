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
}
