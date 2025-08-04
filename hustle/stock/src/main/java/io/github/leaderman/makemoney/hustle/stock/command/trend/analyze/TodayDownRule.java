package io.github.leaderman.makemoney.hustle.stock.command.trend.analyze;

import java.math.BigDecimal;
import java.util.List;

import io.github.leaderman.makemoney.hustle.lang.MathUtil;

public class TodayDownRule implements StockTrendRule {
  @Override
  public int getDays() {
    return 2;
  }

  @Override
  public boolean match(List<BigDecimal> prices) {
    return prices.get(0).compareTo(prices.get(1)) >= 0;
  }

  @Override
  public BigDecimal getSlope(List<BigDecimal> prices) {
    return MathUtil.getSlope(prices);
  }

  @Override
  public String getName() {
    return "td";
  }
}
