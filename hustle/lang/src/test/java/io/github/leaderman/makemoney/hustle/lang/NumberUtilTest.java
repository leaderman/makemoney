package io.github.leaderman.makemoney.hustle.lang;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

public class NumberUtilTest {
  @Test
  public void testToInteger() {
    System.out.println(NumberUtil.toInteger("123"));
    System.out.println(NumberUtil.toInteger("abc", 0));
  }

  @Test
  public void testToBigDecimal() {
    System.out.println(NumberUtil.toBigDecimal("123.456"));
    System.out.println(NumberUtil.toBigDecimal("abc", BigDecimal.ZERO));
  }
}
