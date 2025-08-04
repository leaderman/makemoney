package io.github.leaderman.makemoney.hustle.lang;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

public class CollectionUtilTest {
  @Test
  public void testIsConsecutiveUp() {
    List<BigDecimal> datas = Arrays.asList(new BigDecimal(1), new BigDecimal(2), new BigDecimal(3));
    System.out.println(CollectionUtil.isConsecutiveUp(datas));
  }

  @Test
  public void testIsConsecutiveDown() {
    List<BigDecimal> datas = Arrays.asList(new BigDecimal(3), new BigDecimal(2), new BigDecimal(1));
    System.out.println(CollectionUtil.isConsecutiveDown(datas));
  }

  @Test
  public void testIsOverallUp() {
    List<BigDecimal> datas = Arrays.asList(new BigDecimal(2), new BigDecimal(1), new BigDecimal(3));
    System.out.println(CollectionUtil.isOverallUp(datas));
  }

  @Test
  public void testIsOverallDown() {
    List<BigDecimal> datas = Arrays.asList(new BigDecimal(3), new BigDecimal(4), new BigDecimal(2));
    System.out.println(CollectionUtil.isOverallDown(datas));
  }
}
