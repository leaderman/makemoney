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
@TableName("fund_price")
public class FundPriceEntity extends BaseEntity {
  private String code;
  private BigDecimal price;
}
