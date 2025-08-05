package io.github.leaderman.makemoney.hustle.stock.service;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import io.github.leaderman.makemoney.hustle.stock.domain.model.StockModel;

@SpringBootTest
public class StockServiceTest {
  @Autowired
  private StockService stockService;

  @Test
  public void testSave() {
    StockModel stock = new StockModel();
    stock.setMarket("cn");
    stock.setCode("sz002594");
    stock.setName("比亚迪");

    stockService.save(stock);
    System.out.println(stock.getId());
  }

  @Test
  public void testDelete() {
    StockModel stock = stockService.get("sz002594");
    stockService.removeById(stock.getId());
  }

  @Test
  public void testUpdate() {
    StockModel stock = stockService.get("sz002594");
    stock.setName("比亚迪");

    stockService.updateById(stock);
  }

  @Test
  public void testGets() {
    List<StockModel> stocks = stockService.gets();
    System.out.println(stocks.size());
    stocks.forEach(System.out::println);
  }
}
