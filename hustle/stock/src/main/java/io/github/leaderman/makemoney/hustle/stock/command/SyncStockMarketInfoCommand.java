package io.github.leaderman.makemoney.hustle.stock.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Component;

import io.github.leaderman.makemoney.hustle.coze.CozeClient;
import io.github.leaderman.makemoney.hustle.coze.workflow.stock.MarketInfo;
import io.github.leaderman.makemoney.hustle.coze.workflow.stock.StockData;
import io.github.leaderman.makemoney.hustle.lang.DatetimeUtil;
import io.github.leaderman.makemoney.hustle.stock.domain.entity.StockEntity;
import io.github.leaderman.makemoney.hustle.stock.domain.model.StockMarketInfoModel;
import io.github.leaderman.makemoney.hustle.stock.service.StockHolidayService;
import io.github.leaderman.makemoney.hustle.stock.service.StockMarketInfoService;
import io.github.leaderman.makemoney.hustle.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Component
@RequiredArgsConstructor
@Slf4j
@Command(name = "SyncStockMarketInfoEntity", description = "同步股票市场信息", mixinStandardHelpOptions = true)
public class SyncStockMarketInfoCommand implements Runnable {
  @Option(names = { "-d", "--day" }, description = "日期，格式：yyyy-MM-dd")
  private String day;

  @Option(names = { "-s", "--start" }, description = "开始日期，格式：yyyy-MM-dd")
  private String start;
  @Option(names = { "-e", "--end" }, description = "结束日期，格式：yyyy-MM-dd")
  private String end;

  @Option(names = { "-t", "--threads" }, description = "线程数", defaultValue = "10")
  private int threads;
  private ExecutorService executor;

  @Option(names = { "-o", "--overwrite" }, description = "是否覆盖", defaultValue = "false")
  private boolean overwrite;

  private final CozeClient cozeClient;
  private final StockService stockService;
  private final StockMarketInfoService stockMarketInfoEntityService;
  private final StockHolidayService stockHolidayService;

  /**
   * 同步指定股票和日期。
   * 
   * @param stock 股票。
   * @param date  日期。
   * @return 异步任务。
   */
  private Future<?> syncStock(StockEntity stock, String date, int current, int total) {
    return this.executor.submit(() -> {
      try {
        // 获取股票市场信息（DB）
        StockMarketInfoModel stockMarketInfoModel = stockMarketInfoEntityService.get(stock.getCode(), date);
        if (Objects.nonNull(stockMarketInfoModel) && !this.overwrite) {
          log.info("跳过股票市场信息（{}/{}）：{} {} {}", current, total, stock.getCode(), stock.getName(), date);
          return;
        }

        // 获取股票数据
        StockData stockData = cozeClient.getStockData(stock.getCode(), date);
        if (Objects.isNull(stockData)) {
          log.warn("获取股票市场信息为空（{}/{}）：{} {} {}", current, total, stock.getCode(), stock.getName(), date);
          return;
        }

        // 获取股票市场信息（Coze）
        MarketInfo marketInfo = stockData.getMarketInfo();
        if (Objects.isNull(marketInfo)) {
          log.warn("获取股票市场信息为空（{}/{}）：{} {} {}", current, total, stock.getCode(), stock.getName(), date);
          return;
        }

        // 标识新增或更新
        boolean isNew = false;

        if (Objects.isNull(stockMarketInfoModel)) {
          isNew = true;
          stockMarketInfoModel = new StockMarketInfoModel();
        }

        // 设置字段
        stockMarketInfoModel.setCode(stock.getCode());
        stockMarketInfoModel.setDay(date);
        stockMarketInfoModel.setAmount(marketInfo.getAmountDecimal());
        stockMarketInfoModel.setChange(marketInfo.getChangeDecimal());
        stockMarketInfoModel.setClose(marketInfo.getCloseDecimal());
        stockMarketInfoModel.setCurrencyCode(marketInfo.getCurrencyCode());
        stockMarketInfoModel.setCurrencyName(marketInfo.getCurrencyName());
        stockMarketInfoModel.setHigh(marketInfo.getHighDecimal());
        stockMarketInfoModel.setJumpLink(marketInfo.getJumpLink());
        stockMarketInfoModel.setLow(marketInfo.getLowDecimal());
        stockMarketInfoModel.setOpen(marketInfo.getOpenDecimal());
        stockMarketInfoModel.setPercent(marketInfo.getPercentDecimal());
        stockMarketInfoModel.setPreClose(marketInfo.getPreCloseDecimal());
        stockMarketInfoModel.setPrice(marketInfo.getPriceDecimal());
        stockMarketInfoModel.setVolume(marketInfo.getVolumeDecimal());

        if (isNew) {
          // 新增
          stockMarketInfoEntityService.save(stockMarketInfoModel);
          log.info("新增股票市场信息（{}/{}）：{} {} {} {}", current, total, stock.getCode(), stock.getName(), date,
              stockMarketInfoModel.getPrice());
        } else {
          // 更新
          stockMarketInfoEntityService.updateById(stockMarketInfoModel);
          log.info("更新股票市场信息（{}/{}）：{} {} {} {}", current, total, stock.getCode(), stock.getName(), date,
              stockMarketInfoModel.getPrice());
        }
      } catch (Exception e) {
        log.error("同步股票市场信息错误（{}/{}）：{} {} {} {}", current, total, stock.getCode(), stock.getName(), date,
            ExceptionUtils.getStackTrace(e));
      }
    });
  }

  /**
   * 同步指定日期。
   * 
   * @param stocks 股票列表。
   * @param date   日期。
   */
  private void syncDate(List<StockEntity> stocks, String date) {
    log.info("同步日期 {} 开始", date);

    List<Future<?>> futures = new ArrayList<>();
    for (int index = 0; index < stocks.size(); index++) {
      StockEntity stock = stocks.get(index);
      // 异步任务
      futures.add(syncStock(stock, date, index + 1, stocks.size()));
    }

    // 等待任务完成
    for (Future<?> future : futures) {
      try {
        future.get();
      } catch (Exception e) {
      }
    }

    log.info("同步日期 {} 结束", date);
  }

  /**
   * 根据参数解析日期范围
   */
  private void resolveDateRange() {
    if (StringUtils.isNotEmpty(this.day)) {
      this.start = this.day;
      this.end = this.day;
    } else {
      if (StringUtils.isEmpty(this.start)) {
        this.start = DatetimeUtil.getDate();
      }

      if (StringUtils.isEmpty(this.end)) {
        this.end = DatetimeUtil.getDate();
      }
    }
  }

  @Override
  public void run() {
    resolveDateRange();
    log.info("日期范围：{} - {}", this.start, this.end);

    // 日期列表
    List<String> dates = DatetimeUtil.getDatesBetween(this.start, this.end);
    log.info("日期数量：{}", dates.size());

    // 线程池
    this.executor = Executors.newFixedThreadPool(this.threads);
    log.info("线程数：{}", this.threads);

    try {
      // 股票列表
      List<StockEntity> stocks = stockService.list();
      log.info("股票数量：{}", stocks.size());

      // 同步
      log.info("同步开始");
      for (String date : dates) {
        if (this.stockHolidayService.isCnHoliday(date)) {
          log.info("跳过非交易日期：{}", date);
          continue;
        }

        syncDate(stocks, date);
      }
      log.info("同步结束");
    } catch (Exception e) {
      log.error("同步错误：{}", ExceptionUtils.getStackTrace(e));
    } finally {
      this.executor.shutdown();
    }
  }
}
