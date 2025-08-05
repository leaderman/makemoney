package io.github.leaderman.makemoney.hustle.stock.service;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import io.github.leaderman.makemoney.hustle.stock.domain.model.StockMarketInfoModel;

@SpringBootTest
public class StockMarketInfoServiceTest {
  @Autowired
  private StockMarketInfoService stockMarketInfoEntityService;

  @Test
  public void testSave() {
    StockMarketInfoModel stockMarketInfoModel = new StockMarketInfoModel();

    stockMarketInfoModel.setCode("sz000001");
    stockMarketInfoModel.setDay("2025-07-25");
    stockMarketInfoModel.setAmount(BigDecimal.valueOf(1372000000));
    stockMarketInfoModel.setChange(BigDecimal.valueOf(0.00));
    stockMarketInfoModel.setClose(BigDecimal.valueOf(12.35));
    stockMarketInfoModel.setCurrencyCode("CNY");
    stockMarketInfoModel.setCurrencyName("人民币");
    stockMarketInfoModel.setHigh(BigDecimal.valueOf(12.46));
    stockMarketInfoModel.setJumpLink("http://quotes.sina.cn/hs/company/quotes/view/sz000001");
    stockMarketInfoModel.setLow(BigDecimal.valueOf(12.32));
    stockMarketInfoModel.setOpen(BigDecimal.valueOf(12.33));
    stockMarketInfoModel.setPercent(BigDecimal.valueOf(0.000));
    stockMarketInfoModel.setPreClose(BigDecimal.valueOf(12.35));
    stockMarketInfoModel.setPrice(BigDecimal.valueOf(12.35));
    stockMarketInfoModel.setVolume(BigDecimal.valueOf(1110000));

    stockMarketInfoEntityService.save(stockMarketInfoModel);
  }

  @Test
  public void testGet() {
    StockMarketInfoModel stockMarketInfoModel = stockMarketInfoEntityService.get("sz000001", "2025-07-25");
    System.out.println(stockMarketInfoModel);
  }

  @Test
  public void testGetLatest() {
    List<StockMarketInfoModel> stockMarketInfoModels = stockMarketInfoEntityService.getLatest("sz000001", 10);
    stockMarketInfoModels.forEach(System.out::println);
  }
}
