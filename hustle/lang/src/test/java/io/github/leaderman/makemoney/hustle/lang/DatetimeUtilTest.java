package io.github.leaderman.makemoney.hustle.lang;

import org.junit.jupiter.api.Test;

public class DatetimeUtilTest {
  @Test
  public void testIsDate() {
    System.out.println(DatetimeUtil.isDate("2025-07-28"));
    System.out.println(DatetimeUtil.isDate("2025-07/28"));
  }

  @Test
  public void testGetDatesBetween() {
    System.out.println(DatetimeUtil.getDatesBetween("2025-07-28", "2025-07-30"));
  }

  @Test
  public void testGetDate() {
    System.out.println(DatetimeUtil.getDate());
  }

  @Test
  public void testGetDatetime() {
    System.out.println(DatetimeUtil.getDatetime());
  }
}
