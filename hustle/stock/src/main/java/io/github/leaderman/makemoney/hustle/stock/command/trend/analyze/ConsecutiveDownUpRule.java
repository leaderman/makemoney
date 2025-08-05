package io.github.leaderman.makemoney.hustle.stock.command.trend.analyze;

import java.math.BigDecimal;
import java.util.List;

import io.github.leaderman.makemoney.hustle.lang.CollectionUtil;
import io.github.leaderman.makemoney.hustle.lang.MathUtil;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ConsecutiveDownUpRule implements StockTrendRule {
  private final int downDays;
  private final int upDays;

  @Override
  public int getDays() {
    return this.downDays + this.upDays + 1;
  }

  @Override
  public boolean match(List<BigDecimal> prices) {
    return CollectionUtil.isConsecutiveDown(prices.subList(0, this.downDays + 1))
        && CollectionUtil.isConsecutiveUp(prices.subList(this.downDays, prices.size()));
  }

  @Override
  public BigDecimal getSlope(List<BigDecimal> prices) {
    return MathUtil.getSlope(prices.subList(this.downDays, prices.size()));
  }

  @Override
  public String getName() {
    return "c" + this.downDays + "d" + this.upDays + "u";
  }
}
