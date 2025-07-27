package io.github.leaderman.makemoney.hustle.stock.domain.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import io.github.leaderman.makemoney.hustle.domain.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName("stock_market_info")
public class StockMarketInfoEntity extends BaseEntity {
  private String code;
  private String day;
  private BigDecimal amount;
  @TableField("`change`")
  private BigDecimal change;
  private BigDecimal close;
  private String currencyCode;
  private String currencyName;
  private BigDecimal high;
  private String jumpLink;
  private BigDecimal low;
  private BigDecimal open;
  private BigDecimal percent;
  private BigDecimal preClose;
  private BigDecimal price;
  private BigDecimal volume;
}
