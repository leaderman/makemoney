package io.github.leaderman.makemoney.hustle.stock.domain.entity;

import java.math.BigDecimal;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;

import io.github.leaderman.makemoney.hustle.domain.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName(value = "stock_trend", autoResultMap = true)
public class StockTrendEntity extends BaseEntity {
  private String code;
  private String trend;
  private BigDecimal slope;
  @TableField(typeHandler = JacksonTypeHandler.class)
  private List<BigDecimal> prices;
}
