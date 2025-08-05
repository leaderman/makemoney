package io.github.leaderman.makemoney.hustle.stock.command;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Component;

import io.github.leaderman.makemoney.hustle.coze.CozeClient;
import io.github.leaderman.makemoney.hustle.coze.workflow.stock.Stock;
import io.github.leaderman.makemoney.hustle.stock.domain.model.StockModel;
import io.github.leaderman.makemoney.hustle.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine.Command;

@Component
@RequiredArgsConstructor
@Slf4j
@Command(name = "SyncStock", description = "同步股票", mixinStandardHelpOptions = true)
public class SyncStockCommand implements Runnable {
  private final CozeClient cozeClient;
  private final StockService stockService;

  @Override
  public void run() {
    try {
      log.info("同步股票开始");

      List<Stock> sseStocks = cozeClient.getSseStocks();
      log.info("上交所股票数量: {}", sseStocks.size());

      List<Stock> szseStocks = cozeClient.getSzseStocks();
      log.info("深交所股票数量: {}", szseStocks.size());

      List<Stock> stocks = new ArrayList<>();
      stocks.addAll(sseStocks);
      stocks.addAll(szseStocks);
      log.info("总股票数量: {}", stocks.size());

      for (int index = 0; index < stocks.size(); index++) {
        Stock stock = stocks.get(index);

        StockModel model = stockService.get(stock.getCode());
        if (model == null) {
          model = new StockModel();
          model.setMarket(stock.getMarket());
          model.setCode(stock.getCode());
          model.setName(stock.getName());

          stockService.save(model);
          log.info("新增股票({}/{})：{} {} {}", (index + 1), stocks.size(), stock.getMarket(), stock.getCode(),
              stock.getName());
        } else {
          model.setMarket(stock.getMarket());
          model.setName(stock.getName());

          stockService.updateById(model);
          log.info("更新股票({}/{})：{} {} {}", (index + 1), stocks.size(), stock.getMarket(), stock.getCode(),
              stock.getName());
        }
      }

      log.info("同步股票结束");
    } catch (Exception e) {
      log.error("同步股票错误：{}", ExceptionUtils.getStackTrace(e));
    }
  }
}
