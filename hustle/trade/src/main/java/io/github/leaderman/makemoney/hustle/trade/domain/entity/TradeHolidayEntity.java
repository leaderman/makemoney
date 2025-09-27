package io.github.leaderman.makemoney.hustle.trade.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import io.github.leaderman.makemoney.hustle.domain.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName("trade_holiday")
public class TradeHolidayEntity extends BaseEntity {
  private String market;
  private String holiday;
}
