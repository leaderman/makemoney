package io.github.leaderman.makemoney.hustle.lang;

import java.time.LocalDateTime;

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

  @Test
  public void testFormatDate() {
    System.out.println(DatetimeUtil.formatDate(LocalDateTime.now()));
  }

  @Test
  public void testFormatDatetime() {
    System.out.println(DatetimeUtil.formatDatetime(LocalDateTime.now()));
  }

  @Test
  public void testParseDate() {
    System.out.println(DatetimeUtil.parseDate("2025-07-28"));
  }

  @Test
  public void testParseDatetime() {
    System.out.println(DatetimeUtil.formatDatetime(DatetimeUtil.parseDatetime("2025-07-28 10:11:12")));
  }

  @Test
  public void testStartOfDay() {
    System.out.println(DatetimeUtil.formatDatetime(DatetimeUtil.startOfDay()));
  }

  @Test
  public void testEndOfDay() {
    System.out.println(DatetimeUtil.formatDatetime(DatetimeUtil.endOfDay()));
  }
}
