package io.github.leaderman.makemoney.hustle.trade.domain.model;

import io.github.leaderman.makemoney.hustle.domain.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TradeHolidayModel extends BaseModel {
  // A 股。
  public static final String MARKET_CN = "cn";

  // 市场。
  private String market;
  // 休市日。
  private String holiday;
}