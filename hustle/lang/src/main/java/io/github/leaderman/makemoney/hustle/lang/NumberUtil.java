package io.github.leaderman.makemoney.hustle.lang;

import java.math.BigDecimal;

public class NumberUtil {
  /**
   * 将字符串转换为整数。
   * 
   * @param value 字符串。
   * @return 整数。
   * @throws NumberFormatException
   */
  public static Integer toInteger(String value) throws NumberFormatException {
    return Integer.parseInt(value);
  }

  /**
   * 将字符串转换为整数。
   * 
   * @param value        字符串。
   * @param defaultValue 默认值。
   * @return 整数。
   */
  public static Integer toInteger(String value, Integer defaultValue) {
    try {
      return toInteger(value);
    } catch (NumberFormatException e) {
      return defaultValue;
    }
  }

  /**
   * 将字符串转换为高精度浮点数。
   * 
   * @param value 字符串。
   * @return 高精度浮点数。
   * @throws NumberFormatException
   */
  public static BigDecimal toBigDecimal(String value) throws NumberFormatException {
    return new BigDecimal(value);
  }

  /**
   * 将字符串转换为高精度浮点数。
   * 
   * @param value        字符串。
   * @param defaultValue 默认值。
   * @return 高精度浮点数。
   */
  public static BigDecimal toBigDecimal(String value, BigDecimal defaultValue) {
    try {
      return toBigDecimal(value);
    } catch (NumberFormatException e) {
      return defaultValue;
    }
  }

  /**
   * 判断两个数是否相等。
   * 
   * @param left  左边的数。
   * @param right 右边的数。
   * @return 如果两个高精度浮点数相等，则返回 true，否则返回 false。
   */
  public static boolean equals(BigDecimal left, BigDecimal right) {
    return left.compareTo(right) == 0;
  }

  /**
   * 判断左边数是否大于右边数。
   * 
   * @param left  左边的数。
   * @param right 右边的数。
   * @return 如果左边数大于右边数，则返回 true，否则返回 false。
   */
  public static boolean greaterThan(BigDecimal left, BigDecimal right) {
    return left.compareTo(right) > 0;
  }

  /**
   * 判断左边数是否小于右边数。
   * 
   * @param left  左边的数。
   * @param right 右边的数。
   * @return 如果左边数小于右边数，则返回 true，否则返回 false。
   */
  public static boolean lessThan(BigDecimal left, BigDecimal right) {
    return left.compareTo(right) < 0;
  }

  /**
   * 判断左边数是否大于等于右边数。
   * 
   * @param left  左边的数。
   * @param right 右边的数。
   * @return 如果左边数大于等于右边数，则返回 true，否则返回 false。
   */
  public static boolean greaterThanOrEqualTo(BigDecimal left, BigDecimal right) {
    return left.compareTo(right) >= 0;
  }

  /**
   * 判断左边数是否小于等于右边数。
   * 
   * @param left  左边的数。
   * @param right 右边的数。
   * @return 如果左边数小于等于右边数，则返回 true，否则返回 false。
   */
  public static boolean lessThanOrEqualTo(BigDecimal left, BigDecimal right) {
    return left.compareTo(right) <= 0;
  }
}
