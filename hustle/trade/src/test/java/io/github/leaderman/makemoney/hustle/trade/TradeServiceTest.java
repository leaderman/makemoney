package io.github.leaderman.makemoney.hustle.trade;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import io.github.leaderman.makemoney.hustle.trade.service.TradeService;

@SpringBootTest
public class TradeServiceTest {
  @Autowired
  private TradeService tradeService;

  @Test
  public void testIsTradingTime() {
    System.out.println(tradeService.isTradingTime());
    System.out.println(tradeService.isTradingTime(LocalDateTime.now()));
    System.out.println(tradeService.isTradingTime(LocalDateTime.of(2025, 9, 26, 9, 30, 00)));
    System.out.println(tradeService.isTradingTime(LocalDateTime.of(2025, 9, 26, 11, 30, 00)));
    System.out.println(tradeService.isTradingTime(LocalDateTime.of(2025, 9, 26, 13, 00, 00)));
    System.out.println(tradeService.isTradingTime(LocalDateTime.of(2025, 9, 26, 15, 00, 00)));
  }
}
