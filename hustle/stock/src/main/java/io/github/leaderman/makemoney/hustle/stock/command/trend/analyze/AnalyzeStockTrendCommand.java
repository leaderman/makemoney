package io.github.leaderman.makemoney.hustle.stock.command.trend.analyze;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Component;

import io.github.leaderman.makemoney.hustle.stock.domain.entity.StockEntity;
import io.github.leaderman.makemoney.hustle.stock.domain.entity.StockMarketInfoEntity;
import io.github.leaderman.makemoney.hustle.stock.domain.entity.StockTrendEntity;
import io.github.leaderman.makemoney.hustle.stock.service.StockMarketInfoService;
import io.github.leaderman.makemoney.hustle.stock.service.StockService;
import io.github.leaderman.makemoney.hustle.stock.service.StockTrendService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine.Command;

@Component
@RequiredArgsConstructor
@Slf4j
@Command(name = "AnalyzeStockTrend", description = "分析股票趋势")
public class AnalyzeStockTrendCommand implements Runnable {
  private final StockService stockService;
  private final StockMarketInfoService stockMarketInfoService;
  private final StockTrendService stockTrendService;

  private List<StockTrendRule> rules;

  @PostConstruct
  public void init() {
    this.rules = new ArrayList<>();

    // 今天上涨
    this.rules.add(new TodayUpRule());
    // 今天下跌
    this.rules.add(new TodayDownRule());

    // 连续上涨 N 天
    this.rules.add(new ConsecutiveUpRule(2));
    this.rules.add(new ConsecutiveUpRule(3));
    this.rules.add(new ConsecutiveUpRule(4));
    this.rules.add(new ConsecutiveUpRule(5));
    this.rules.add(new ConsecutiveUpRule(6));
    this.rules.add(new ConsecutiveUpRule(7));
    this.rules.add(new ConsecutiveUpRule(8));
    this.rules.add(new ConsecutiveUpRule(9));
    this.rules.add(new ConsecutiveUpRule(10));

    // 连续下跌 N 天
    this.rules.add(new ConsecutiveDownRule(2));
    this.rules.add(new ConsecutiveDownRule(3));
    this.rules.add(new ConsecutiveDownRule(4));
    this.rules.add(new ConsecutiveDownRule(5));
    this.rules.add(new ConsecutiveDownRule(6));
    this.rules.add(new ConsecutiveDownRule(7));
    this.rules.add(new ConsecutiveDownRule(8));
    this.rules.add(new ConsecutiveDownRule(9));
    this.rules.add(new ConsecutiveDownRule(10));

    // 整体上涨 N 天
    this.rules.add(new OverallUpRule(2));
    this.rules.add(new OverallUpRule(3));
    this.rules.add(new OverallUpRule(4));
    this.rules.add(new OverallUpRule(5));
    this.rules.add(new OverallUpRule(6));
    this.rules.add(new OverallUpRule(7));
    this.rules.add(new OverallUpRule(8));
    this.rules.add(new OverallUpRule(9));
    this.rules.add(new OverallUpRule(10));

    // 整体下跌 N 天
    this.rules.add(new OverallDownRule(2));
    this.rules.add(new OverallDownRule(3));
    this.rules.add(new OverallDownRule(4));
    this.rules.add(new OverallDownRule(5));
    this.rules.add(new OverallDownRule(6));
    this.rules.add(new OverallDownRule(7));
    this.rules.add(new OverallDownRule(8));
    this.rules.add(new OverallDownRule(9));
    this.rules.add(new OverallDownRule(10));

    // 连续上涨 M 天，连续下跌 N 天
    this.rules.add(new ConsecutiveUpDownRule(1, 1));
    this.rules.add(new ConsecutiveUpDownRule(1, 2));
    this.rules.add(new ConsecutiveUpDownRule(1, 3));
    this.rules.add(new ConsecutiveUpDownRule(2, 1));
    this.rules.add(new ConsecutiveUpDownRule(2, 2));
    this.rules.add(new ConsecutiveUpDownRule(2, 3));
    this.rules.add(new ConsecutiveUpDownRule(3, 1));
    this.rules.add(new ConsecutiveUpDownRule(3, 2));
    this.rules.add(new ConsecutiveUpDownRule(3, 3));
    this.rules.add(new ConsecutiveUpDownRule(4, 1));
    this.rules.add(new ConsecutiveUpDownRule(4, 2));
    this.rules.add(new ConsecutiveUpDownRule(4, 3));
    this.rules.add(new ConsecutiveUpDownRule(5, 1));
    this.rules.add(new ConsecutiveUpDownRule(5, 2));
    this.rules.add(new ConsecutiveUpDownRule(5, 3));
    this.rules.add(new ConsecutiveUpDownRule(6, 1));
    this.rules.add(new ConsecutiveUpDownRule(6, 2));
    this.rules.add(new ConsecutiveUpDownRule(6, 3));
    this.rules.add(new ConsecutiveUpDownRule(7, 1));
    this.rules.add(new ConsecutiveUpDownRule(7, 2));
    this.rules.add(new ConsecutiveUpDownRule(7, 3));
    this.rules.add(new ConsecutiveUpDownRule(8, 1));
    this.rules.add(new ConsecutiveUpDownRule(8, 2));
    this.rules.add(new ConsecutiveUpDownRule(8, 3));
    this.rules.add(new ConsecutiveUpDownRule(9, 1));
    this.rules.add(new ConsecutiveUpDownRule(9, 2));
    this.rules.add(new ConsecutiveUpDownRule(9, 3));
    this.rules.add(new ConsecutiveUpDownRule(10, 1));
    this.rules.add(new ConsecutiveUpDownRule(10, 2));
    this.rules.add(new ConsecutiveUpDownRule(10, 3));

    // 连续下跌 M 天，连续上涨 N 天
    this.rules.add(new ConsecutiveDownUpRule(1, 1));
    this.rules.add(new ConsecutiveDownUpRule(1, 2));
    this.rules.add(new ConsecutiveDownUpRule(1, 3));
    this.rules.add(new ConsecutiveDownUpRule(2, 1));
    this.rules.add(new ConsecutiveDownUpRule(2, 2));
    this.rules.add(new ConsecutiveDownUpRule(2, 3));
    this.rules.add(new ConsecutiveDownUpRule(3, 1));
    this.rules.add(new ConsecutiveDownUpRule(3, 2));
    this.rules.add(new ConsecutiveDownUpRule(3, 3));
    this.rules.add(new ConsecutiveDownUpRule(4, 1));
    this.rules.add(new ConsecutiveDownUpRule(4, 2));
    this.rules.add(new ConsecutiveDownUpRule(4, 3));
    this.rules.add(new ConsecutiveDownUpRule(5, 1));
    this.rules.add(new ConsecutiveDownUpRule(5, 2));
    this.rules.add(new ConsecutiveDownUpRule(5, 3));
    this.rules.add(new ConsecutiveDownUpRule(6, 1));
    this.rules.add(new ConsecutiveDownUpRule(6, 2));
    this.rules.add(new ConsecutiveDownUpRule(6, 3));
    this.rules.add(new ConsecutiveDownUpRule(7, 1));
    this.rules.add(new ConsecutiveDownUpRule(7, 2));
    this.rules.add(new ConsecutiveDownUpRule(7, 3));
    this.rules.add(new ConsecutiveDownUpRule(8, 1));
    this.rules.add(new ConsecutiveDownUpRule(8, 2));
    this.rules.add(new ConsecutiveDownUpRule(8, 3));
    this.rules.add(new ConsecutiveDownUpRule(9, 1));
    this.rules.add(new ConsecutiveDownUpRule(9, 2));
    this.rules.add(new ConsecutiveDownUpRule(9, 3));
    this.rules.add(new ConsecutiveDownUpRule(10, 1));
    this.rules.add(new ConsecutiveDownUpRule(10, 2));
    this.rules.add(new ConsecutiveDownUpRule(10, 3));

    // 整体上涨 M 天，整体下跌 N 天
    this.rules.add(new OverallUpDownRule(1, 1));
    this.rules.add(new OverallUpDownRule(1, 2));
    this.rules.add(new OverallUpDownRule(1, 3));
    this.rules.add(new OverallUpDownRule(2, 1));
    this.rules.add(new OverallUpDownRule(2, 2));
    this.rules.add(new OverallUpDownRule(2, 3));
    this.rules.add(new OverallUpDownRule(3, 1));
    this.rules.add(new OverallUpDownRule(3, 2));
    this.rules.add(new OverallUpDownRule(3, 3));
    this.rules.add(new OverallUpDownRule(4, 1));
    this.rules.add(new OverallUpDownRule(4, 2));
    this.rules.add(new OverallUpDownRule(4, 3));
    this.rules.add(new OverallUpDownRule(5, 1));
    this.rules.add(new OverallUpDownRule(5, 2));
    this.rules.add(new OverallUpDownRule(5, 3));
    this.rules.add(new OverallUpDownRule(6, 1));
    this.rules.add(new OverallUpDownRule(6, 2));
    this.rules.add(new OverallUpDownRule(6, 3));
    this.rules.add(new OverallUpDownRule(7, 1));
    this.rules.add(new OverallUpDownRule(7, 2));
    this.rules.add(new OverallUpDownRule(7, 3));
    this.rules.add(new OverallUpDownRule(8, 1));
    this.rules.add(new OverallUpDownRule(8, 2));
    this.rules.add(new OverallUpDownRule(8, 3));
    this.rules.add(new OverallUpDownRule(9, 1));
    this.rules.add(new OverallUpDownRule(9, 2));
    this.rules.add(new OverallUpDownRule(9, 3));
    this.rules.add(new OverallUpDownRule(10, 1));
    this.rules.add(new OverallUpDownRule(10, 2));
    this.rules.add(new OverallUpDownRule(10, 3));

    // 整体下跌 M 天，整体上涨 N 天
    this.rules.add(new OverallDownUpRule(1, 1));
    this.rules.add(new OverallDownUpRule(1, 2));
    this.rules.add(new OverallDownUpRule(1, 3));
    this.rules.add(new OverallDownUpRule(2, 1));
    this.rules.add(new OverallDownUpRule(2, 2));
    this.rules.add(new OverallDownUpRule(2, 3));
    this.rules.add(new OverallDownUpRule(3, 1));
    this.rules.add(new OverallDownUpRule(3, 2));
    this.rules.add(new OverallDownUpRule(3, 3));
    this.rules.add(new OverallDownUpRule(4, 1));
    this.rules.add(new OverallDownUpRule(4, 2));
    this.rules.add(new OverallDownUpRule(4, 3));
    this.rules.add(new OverallDownUpRule(5, 1));
    this.rules.add(new OverallDownUpRule(5, 2));
    this.rules.add(new OverallDownUpRule(5, 3));
    this.rules.add(new OverallDownUpRule(6, 1));
    this.rules.add(new OverallDownUpRule(6, 2));
    this.rules.add(new OverallDownUpRule(6, 3));
    this.rules.add(new OverallDownUpRule(7, 1));
    this.rules.add(new OverallDownUpRule(7, 2));
    this.rules.add(new OverallDownUpRule(7, 3));
    this.rules.add(new OverallDownUpRule(8, 1));
    this.rules.add(new OverallDownUpRule(8, 2));
    this.rules.add(new OverallDownUpRule(8, 3));
    this.rules.add(new OverallDownUpRule(9, 1));
    this.rules.add(new OverallDownUpRule(9, 2));
    this.rules.add(new OverallDownUpRule(9, 3));
    this.rules.add(new OverallDownUpRule(10, 1));
    this.rules.add(new OverallDownUpRule(10, 2));
    this.rules.add(new OverallDownUpRule(10, 3));
  }

  @Override
  public void run() {
    try {
      log.info("分析股票趋势开始");

      List<StockEntity> stocks = this.stockService.list();
      log.info("股票数量：{}", stocks.size());

      for (int index = 0; index < stocks.size(); index++) {
        StockEntity stock = stocks.get(index);
        log.info("股票({}/{})：{} {}", (index + 1), stocks.size(), stock.getCode(), stock.getName());

        List<StockTrendEntity> trends = new ArrayList<>();

        for (StockTrendRule rule : this.rules) {
          log.info("规则：{}", rule.getName());

          log.info("获取最近 {} 天的行情", rule.getDays());
          List<StockMarketInfoEntity> stockMarketInfos = this.stockMarketInfoService.getLatest(stock.getCode(),
              rule.getDays());
          if (stockMarketInfos.size() != rule.getDays()) {
            log.info("行情数量 {} 不匹配，跳过", rule.getDays());
            continue;
          }

          List<BigDecimal> prices = stockMarketInfos.stream().map(StockMarketInfoEntity::getClose).toList();
          log.info("价格：{}", prices);

          boolean match = rule.match(prices);
          if (!match) {
            log.info("价格规则不匹配，跳过");
            continue;
          }

          BigDecimal slope = rule.getSlope(prices);
          log.info("斜率：{}", slope.setScale(4, RoundingMode.HALF_UP));

          StockTrendEntity trend = new StockTrendEntity();
          trend.setCode(stock.getCode());
          trend.setTrend(rule.getName());
          trend.setSlope(slope);
          trend.setPrices(prices);
          trends.add(trend);
        }

        this.stockTrendService.remove(stock.getCode());
        log.info("删除旧股票趋势");

        this.stockTrendService.saveBatch(trends);
        log.info("保存新股票趋势：{}", trends.size());
      }

      log.info("分析股票趋势结束");
    } catch (Exception e) {
      log.error("分析股票趋势错误：{}", ExceptionUtils.getStackTrace(e));
    }
  }
}
