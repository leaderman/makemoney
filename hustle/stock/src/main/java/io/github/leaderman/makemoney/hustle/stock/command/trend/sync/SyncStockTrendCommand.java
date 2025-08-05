package io.github.leaderman.makemoney.hustle.stock.command.trend.sync;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Component;

import io.github.leaderman.makemoney.hustle.config.ConfigClient;
import io.github.leaderman.makemoney.hustle.feishu.BitableClient;
import io.github.leaderman.makemoney.hustle.stock.domain.model.StockModel;
import io.github.leaderman.makemoney.hustle.stock.domain.model.StockTrendModel;
import io.github.leaderman.makemoney.hustle.stock.service.StockService;
import io.github.leaderman.makemoney.hustle.stock.service.StockTrendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine.Command;

@Component
@RequiredArgsConstructor
@Slf4j
@Command(name = "SyncStockTrend", description = "同步股票趋势", mixinStandardHelpOptions = true)
public class SyncStockTrendCommand implements Runnable {
  private final ConfigClient configClient;
  private final StockTrendService stockTrendService;
  private final StockService stockService;
  private final BitableClient bitableClient;

  @Override
  public void run() {
    try {
      log.info("同步股票趋势开始");

      // 多维表格
      String appToken = configClient.getString("stock.trend.bitable");

      List<String> trends = stockTrendService.getTrends();
      log.info("股票趋势名称数量: {}", trends.size());

      for (int index = 0; index < trends.size(); index++) {
        String trend = trends.get(index);
        log.info("股票趋势名称({}/{})：{}", (index + 1), trends.size(), trend);

        // 数据表
        String tableId = configClient.getString("stock.trend.bitable.table." + trend);

        // 股票趋势列表
        List<StockTrendModel> stockTrends = stockTrendService.gets(trend);
        log.info("股票趋势数量: {}", stockTrends.size());

        List<Map<String, Object>> records = new ArrayList<>();
        for (StockTrendModel stockTrend : stockTrends) {
          Map<String, Object> record = new HashMap<>();
          record.put("股票代码", stockTrend.getCode());

          StockModel stock = stockService.get(stockTrend.getCode());
          record.put("股票名称", stock.getName());

          List<BigDecimal> prices = stockTrend.getPrices();
          for (int from = 0; from < prices.size(); from++) {
            record.put("最近 " + (prices.size() - from) + " 个交易日价格", prices.get(from));
          }

          record.put("股票趋势", stockTrend.getSlope());

          records.add(record);
        }

        // 清空数据表
        bitableClient.truncateTable(appToken, tableId);
        log.info("清空数据表完成");

        // 写入记录
        bitableClient.batchCreateRecords(appToken, tableId, records);
        log.info("写入记录完成");
      }

      log.info("同步股票趋势结束");
    } catch (Exception e) {
      log.error("同步股票趋势错误：{}", ExceptionUtils.getStackTrace(e));
    }
  }
}
