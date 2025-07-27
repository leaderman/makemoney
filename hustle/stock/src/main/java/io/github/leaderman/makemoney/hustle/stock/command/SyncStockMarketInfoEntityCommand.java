package io.github.leaderman.makemoney.hustle.stock.command;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Component;

import io.github.leaderman.makemoney.hustle.coze.CozeClient;
import io.github.leaderman.makemoney.hustle.coze.workflow.stock.MarketInfo;
import io.github.leaderman.makemoney.hustle.coze.workflow.stock.StockData;
import io.github.leaderman.makemoney.hustle.stock.domain.entity.StockEntity;
import io.github.leaderman.makemoney.hustle.stock.domain.entity.StockMarketInfoEntity;
import io.github.leaderman.makemoney.hustle.stock.service.StockMarketInfoEntityService;
import io.github.leaderman.makemoney.hustle.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Component
@RequiredArgsConstructor
@Slf4j
@Command(name = "SyncStockMarketInfoEntity", description = "同步股票市场信息")
public class SyncStockMarketInfoEntityCommand implements Runnable {
  @Option(names = { "-d", "--day" }, description = "日期，格式：yyyy-MM-dd")
  private String day;

  private final CozeClient cozeClient;
  private final StockService stockService;
  private final StockMarketInfoEntityService stockMarketInfoEntityService;

  @Override
  public void run() {
    if (StringUtils.isEmpty(day)) {
      day = LocalDate.now().toString();
    }
    log.info("同步日期：{}", day);

    try {
      List<StockEntity> stocks = stockService.list();
      log.info("股票数量：{}", stocks.size());

      log.info("同步股票市场信息开始");

      for (int index = 0; index < stocks.size(); index++) {
        StockEntity stock = stocks.get(index);
        log.info("获取股票市场信息（{}/{}）：{} {} {}", index + 1, stocks.size(), stock.getCode(), stock.getName(), day);

        StockData stockData = cozeClient.getStockData(stock.getCode(), day);
        if (Objects.isNull(stockData)) {
          log.warn("获取股票市场信息为空：{} {} {}", stock.getCode(), stock.getName(), day);
          continue;
        }

        MarketInfo marketInfo = stockData.getMarketInfo();
        if (Objects.isNull(marketInfo)) {
          log.warn("获取股票市场信息为空：{} {} {}", stock.getCode(), stock.getName(), day);
          continue;
        }

        boolean isNew = false;

        StockMarketInfoEntity stockMarketInfoEntity = stockMarketInfoEntityService.get(stock.getCode(), day);
        if (Objects.isNull(stockMarketInfoEntity)) {
          isNew = true;
          stockMarketInfoEntity = new StockMarketInfoEntity();
        }

        stockMarketInfoEntity.setCode(stock.getCode());
        stockMarketInfoEntity.setDay(day);
        stockMarketInfoEntity.setAmount(marketInfo.getAmountDecimal());
        stockMarketInfoEntity.setChange(marketInfo.getChangeDecimal());
        stockMarketInfoEntity.setClose(marketInfo.getCloseDecimal());
        stockMarketInfoEntity.setCurrencyCode(marketInfo.getCurrencyCode());
        stockMarketInfoEntity.setCurrencyName(marketInfo.getCurrencyName());
        stockMarketInfoEntity.setHigh(marketInfo.getHighDecimal());
        stockMarketInfoEntity.setJumpLink(marketInfo.getJumpLink());
        stockMarketInfoEntity.setLow(marketInfo.getLowDecimal());
        stockMarketInfoEntity.setOpen(marketInfo.getOpenDecimal());
        stockMarketInfoEntity.setPercent(marketInfo.getPercentDecimal());
        stockMarketInfoEntity.setPreClose(marketInfo.getPreCloseDecimal());
        stockMarketInfoEntity.setPrice(marketInfo.getPriceDecimal());
        stockMarketInfoEntity.setVolume(marketInfo.getVolumeDecimal());

        if (isNew) {
          stockMarketInfoEntityService.save(stockMarketInfoEntity);
          log.info("新增股票市场信息：{} {} {} {}", stock.getCode(), stock.getName(), day, stockMarketInfoEntity.getPrice());
        } else {
          stockMarketInfoEntityService.updateById(stockMarketInfoEntity);
          log.info("更新股票市场信息：{} {} {} {}", stock.getCode(), stock.getName(), day, stockMarketInfoEntity.getPrice());
        }
      }

      log.info("同步股票市场信息结束");
    } catch (Exception e) {
      log.error("同步股票市场信息错误：{}", ExceptionUtils.getStackTrace(e));
    }
  }
}
