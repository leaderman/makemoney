package io.github.leaderman.makemoney.hustle.stock.service;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import io.github.leaderman.makemoney.hustle.stock.domain.entity.StockMarketInfoEntity;

@SpringBootTest
public class StockMarketInfoServiceTest {
  @Autowired
  private StockMarketInfoService stockMarketInfoEntityService;

  @Test
  public void testSave() {
    StockMarketInfoEntity stockMarketInfoEntity = new StockMarketInfoEntity();
    stockMarketInfoEntity.setCode("sz000001");
    stockMarketInfoEntity.setDay("2025-07-25");
    stockMarketInfoEntity.setAmount(BigDecimal.valueOf(1372000000));
    stockMarketInfoEntity.setChange(BigDecimal.valueOf(0.00));
    stockMarketInfoEntity.setClose(BigDecimal.valueOf(12.35));
    stockMarketInfoEntity.setCurrencyCode("CNY");
    stockMarketInfoEntity.setCurrencyName("人民币");
    stockMarketInfoEntity.setHigh(BigDecimal.valueOf(12.46));
    stockMarketInfoEntity.setJumpLink("http://quotes.sina.cn/hs/company/quotes/view/sz000001");
    stockMarketInfoEntity.setLow(BigDecimal.valueOf(12.32));
    stockMarketInfoEntity.setOpen(BigDecimal.valueOf(12.33));
    stockMarketInfoEntity.setPercent(BigDecimal.valueOf(0.000));
    stockMarketInfoEntity.setPreClose(BigDecimal.valueOf(12.35));
    stockMarketInfoEntity.setPrice(BigDecimal.valueOf(12.35));
    stockMarketInfoEntity.setVolume(BigDecimal.valueOf(1110000));

    stockMarketInfoEntityService.save(stockMarketInfoEntity);
  }

  @Test
  public void testGet() {
    StockMarketInfoEntity stockMarketInfoEntity = stockMarketInfoEntityService.get("sz000001", "2025-07-25");
    System.out.println(stockMarketInfoEntity);
  }

  @Test
  public void testGetLatest() {
    List<StockMarketInfoEntity> stockMarketInfoEntities = stockMarketInfoEntityService.getLatest("sz000001", 10);
    stockMarketInfoEntities.forEach(System.out::println);
  }
}
