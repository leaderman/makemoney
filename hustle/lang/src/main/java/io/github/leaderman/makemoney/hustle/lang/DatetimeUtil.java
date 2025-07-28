package io.github.leaderman.makemoney.hustle.lang;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DatetimeUtil {
  private static final String DATE_FORMAT = "yyyy-MM-dd";

  private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);

  /**
   * 判断日期是否合法，格式：yyyy-MM-dd。
   * 
   * @param date 日期。
   * @return 日期合法，返回 true，否则返回 false。
   */
  public static boolean isDate(String date) {
    try {
      LocalDate.parse(date, DATE_TIME_FORMATTER);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * 获取两个日期之间的所有日期，包含开始日期和结束日期。
   * 
   * @param start 开始日期，格式：yyyy-MM-dd。
   * @param end   结束日期，格式：yyyy-MM-dd。
   * @return 日期列表，格式：yyyy-MM-dd。
   */
  public static List<String> getDatesBetween(String start, String end) {
    LocalDate startDate = LocalDate.parse(start, DATE_TIME_FORMATTER);
    LocalDate endDate = LocalDate.parse(end, DATE_TIME_FORMATTER);

    List<String> dates = new ArrayList<>();

    while (!startDate.isAfter(endDate)) {
      dates.add(startDate.format(DATE_TIME_FORMATTER));
      startDate = startDate.plusDays(1);
    }

    return dates;
  }

  /**
   * 获取当前日期。
   * 
   * @return 当前日期，格式：yyyy-MM-dd。
   */
  public static String getDate() {
    return LocalDate.now().format(DATE_TIME_FORMATTER);
  }
}
