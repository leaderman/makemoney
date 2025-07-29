package io.github.leaderman.makemoney.hustle.stock.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class StockHolidayServiceTest {
  @Autowired
  private StockHolidayService stockHolidayService;

  @Test
  public void testIsCnHoliday() {
    System.out.println(stockHolidayService.isCnHoliday("2025-07-27"));
    System.out.println(stockHolidayService.isCnHoliday("2025-07-29"));
  }
}
