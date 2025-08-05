package io.github.leaderman.makemoney.hustle.stock.command.trend.analyze;

import java.math.BigDecimal;
import java.util.List;

import io.github.leaderman.makemoney.hustle.lang.CollectionUtil;
import io.github.leaderman.makemoney.hustle.lang.MathUtil;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ConsecutiveUpDownRule implements StockTrendRule {
  private final int upDays;
  private final int downDays;

  @Override
  public int getDays() {
    return this.upDays + this.downDays + 1;
  }

  @Override
  public boolean match(List<BigDecimal> prices) {
    return CollectionUtil.isConsecutiveUp(prices.subList(0, this.upDays + 1))
        && CollectionUtil.isConsecutiveDown(prices.subList(this.upDays, prices.size()));
  }

  @Override
  public BigDecimal getSlope(List<BigDecimal> prices) {
    return MathUtil.getSlope(prices.subList(this.upDays, prices.size()));
  }

  @Override
  public String getName() {
    return "c" + this.upDays + "u" + this.downDays + "d";
  }
}
