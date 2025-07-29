package io.github.leaderman.makemoney.hustle.stock.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import io.github.leaderman.makemoney.hustle.domain.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName("stock_holiday")
public class StockHolidayEntity extends BaseEntity {
  private String market;
  private String holiday;
}
