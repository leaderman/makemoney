package io.github.leaderman.makemoney.hustle.trade;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import io.github.leaderman.makemoney.hustle.trade.service.TradeHolidayService;

@SpringBootTest
public class TradeHolidayServiceTest {
  @Autowired
  private TradeHolidayService tradeHolidayService;

  @Test
  public void testIsHoliday() {
    System.out.println(tradeHolidayService.isHoliday());
    System.out.println(tradeHolidayService.isHoliday("2025-09-28"));
    System.out.println(tradeHolidayService.isHoliday(LocalDateTime.now()));
  }
}
