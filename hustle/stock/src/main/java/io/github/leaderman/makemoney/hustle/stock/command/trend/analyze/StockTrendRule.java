package io.github.leaderman.makemoney.hustle.stock.command.trend.analyze;

import java.math.BigDecimal;
import java.util.List;

public interface StockTrendRule {
  int getDays();

  boolean match(List<BigDecimal> prices);

  BigDecimal getSlope(List<BigDecimal> prices);

  String getName();
}
