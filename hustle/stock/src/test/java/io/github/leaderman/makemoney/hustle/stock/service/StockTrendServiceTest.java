package io.github.leaderman.makemoney.hustle.stock.service;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class StockTrendServiceTest {
  @Autowired
  private StockTrendService stockTrendService;

  @Test
  public void testGetTrends() {
    List<String> trends = stockTrendService.getTrends();
    trends.forEach(System.out::println);
  }
}
