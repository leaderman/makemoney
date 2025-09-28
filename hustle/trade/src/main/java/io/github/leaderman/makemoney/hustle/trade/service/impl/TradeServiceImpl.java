package io.github.leaderman.makemoney.hustle.trade.service.impl;

import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.stereotype.Service;

import io.github.leaderman.makemoney.hustle.trade.service.TradeHolidayService;
import io.github.leaderman.makemoney.hustle.trade.service.TradeService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TradeServiceImpl implements TradeService {
  private static final LocalTime MARKET_CN_MORNING_START = LocalTime.of(9, 30, 00);
  private static final LocalTime MARKET_CN_MORNING_END = LocalTime.of(11, 30, 00);
  private static final LocalTime MARKET_CN_AFTERNOON_START = LocalTime.of(13, 0, 00);
  private static final LocalTime MARKET_CN_AFTERNOON_END = LocalTime.of(15, 0, 00);

  private final TradeHolidayService tradeHolidayService;

  @Override
  public boolean isTradingTime() {
    return this.isTradingTime(LocalDateTime.now());
  }

  @Override
  public boolean isTradingTime(LocalDateTime datetime) {
    if (this.tradeHolidayService.isHoliday(datetime)) {
      return false;
    }

    LocalTime time = datetime.toLocalTime();
    return time.equals(MARKET_CN_MORNING_START) || time.isAfter(MARKET_CN_MORNING_START)
        && time.isBefore(MARKET_CN_MORNING_END)
        || time.equals(MARKET_CN_AFTERNOON_START) || time.isAfter(MARKET_CN_AFTERNOON_START)
            && time.isBefore(MARKET_CN_AFTERNOON_END);
  }
}
