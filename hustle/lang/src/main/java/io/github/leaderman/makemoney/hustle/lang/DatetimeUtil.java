package io.github.leaderman.makemoney.hustle.lang;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DatetimeUtil {
  private static final String DATE_FORMAT = "yyyy-MM-dd";
  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);

  private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
  private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern(DATETIME_FORMAT);

  /**
   * 判断日期是否合法，格式：yyyy-MM-dd。
   * 
   * @param date 日期。
   * @return 日期合法，返回 true，否则返回 false。
   */
  public static boolean isDate(String date) {
    try {
      LocalDate.parse(date, DATE_FORMATTER);
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
    LocalDate startDate = LocalDate.parse(start, DATE_FORMATTER);
    LocalDate endDate = LocalDate.parse(end, DATE_FORMATTER);

    List<String> dates = new ArrayList<>();

    while (!startDate.isAfter(endDate)) {
      dates.add(startDate.format(DATE_FORMATTER));
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
    return LocalDate.now().format(DATE_FORMATTER);
  }

  /**
   * 获取当前日期时间。
   * 
   * @return 当前日期时间，格式：yyyy-MM-dd HH:mm:ss。
   */
  public static String getDatetime() {
    return LocalDateTime.now().format(DATETIME_FORMATTER);
  }

  /**
   * 判断两个日期时间是否是同一天。
   * 
   * @param left  左边的日期时间。
   * @param right 右边的日期时间。
   * @return 如果两个日期时间是同一天，则返回 true，否则返回 false。
   */
  public static boolean isSameDay(LocalDateTime left, LocalDateTime right) {
    return left.toLocalDate().equals(right.toLocalDate());
  }

  /**
   * 格式化日期。
   * 
   * @param datetime 日期时间。
   * @return 日期。
   */
  public static String formatDate(LocalDateTime datetime) {
    return datetime.format(DATE_FORMATTER);
  }

  /**
   * 格式化日期时间。
   * 
   * @param datetime 日期时间。
   * @return 日期时间。
   */
  public static String formatDatetime(LocalDateTime datetime) {
    return datetime.format(DATETIME_FORMATTER);
  }

  /**
   * 解析日期。
   * 
   * @param date 日期，格式：yyyy-MM-dd。
   * @return 日期时间。
   */
  public static LocalDateTime parseDate(String date) {
    return LocalDate.parse(date, DATE_FORMATTER).atStartOfDay();
  }

  /**
   * 解析日期时间。
   * 
   * @param datetime 日期时间，格式：yyyy-MM-dd HH:mm:ss。
   * @return 日期时间。
   */
  public static LocalDateTime parseDatetime(String datetime) {
    return LocalDateTime.parse(datetime, DATETIME_FORMATTER);
  }

  /**
   * 获取当天的开始时间。
   * 
   * @return 当天的开始时间。
   */
  public static LocalDateTime startOfDay() {
    return LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
  }

  /**
   * 获取当天的结束时间。
   * 
   * @return 当天的结束时间。
   */
  public static LocalDateTime endOfDay() {
    return LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999999999);
  }
}
