package io.github.leaderman.makemoney.hustle.lang;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

public class MathUtilTest {
  @Test
  public void testGetSlope() {
    List<BigDecimal> datas = Arrays.asList(new BigDecimal(44.4200), new BigDecimal(44.4800), new BigDecimal(44.4200));
    BigDecimal slope = MathUtil.getSlope(datas);
    System.out.println(slope.setScale(4, RoundingMode.HALF_UP));
  }

  @Test
  public void testRandom() {
    for (int index = 0; index < 1200; index++) {
      System.out.println(
          MathUtil.random(BigDecimal.valueOf(0.594), BigDecimal.valueOf(0.604)).setScale(3, RoundingMode.HALF_UP));
    }
  }
}
