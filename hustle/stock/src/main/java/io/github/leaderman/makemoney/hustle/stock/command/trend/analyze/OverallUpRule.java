package io.github.leaderman.makemoney.hustle.stock.command.trend.analyze;

import java.math.BigDecimal;
import java.util.List;

import io.github.leaderman.makemoney.hustle.lang.CollectionUtil;
import io.github.leaderman.makemoney.hustle.lang.MathUtil;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OverallUpRule implements StockTrendRule {
  private final int days;

  @Override
  public int getDays() {
    return this.days + 1;
  }

  @Override
  public boolean match(List<BigDecimal> prices) {
    return CollectionUtil.isOverallUp(prices);
  }

  @Override
  public BigDecimal getSlope(List<BigDecimal> prices) {
    return MathUtil.getSlope(prices);
  }

  @Override
  public String getName() {
    return "o" + this.days + "u";
  }
}
