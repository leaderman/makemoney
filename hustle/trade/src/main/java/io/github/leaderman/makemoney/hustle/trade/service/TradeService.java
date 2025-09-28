package io.github.leaderman.makemoney.hustle.trade.service;

import java.time.LocalDateTime;

public interface TradeService {
  /**
   * 判断是否为 A 股交易时间。
   * 
   * @return 是否为 A 股交易时间。
   */
  boolean isTradingTime();

  /**
   * 判断指定日期时间是否为 A 股交易时间。
   * 
   * @param datetime 日期时间。
   * @return 是否为 A 股交易时间。
   */
  boolean isTradingTime(LocalDateTime datetime);
}
