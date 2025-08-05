package io.github.leaderman.makemoney.hustle.stock.service;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import io.github.leaderman.makemoney.hustle.stock.domain.model.StockTrendModel;

@SpringBootTest
public class StockTrendServiceTest {
  @Autowired
  private StockTrendService stockTrendService;

  @Test
  public void testGetTrends() {
    List<String> trends = stockTrendService.getTrends();
    trends.forEach(System.out::println);
  }

  @Test
  public void testList() {
    List<StockTrendModel> trends = stockTrendService.gets("tu");
    trends.forEach(System.out::println);
  }
}
