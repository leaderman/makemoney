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
}
