package io.github.leaderman.makemoney.hustle.lang;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.IntStream;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CollectionUtil {
  /**
   * 判断数值列表是否连续上升。
   * 
   * @param datas 数值列表。
   * @return 如果数值列表连续上升，则返回 true，否则返回 false。
   */
  public static boolean isConsecutiveUp(List<BigDecimal> datas) {
    return IntStream.range(0, datas.size() - 1).allMatch(i -> datas.get(i).compareTo(datas.get(i + 1)) < 0);
  }

  /**
   * 判断数值列表是否连续下降。
   * 
   * @param datas 数值列表。
   * @return 如果数值列表连续下降，则返回 true，否则返回 false。
   */
  public static boolean isConsecutiveDown(List<BigDecimal> datas) {
    return IntStream.range(0, datas.size() - 1).allMatch(i -> datas.get(i).compareTo(datas.get(i + 1)) > 0);
  }

  /**
   * 判断数值列表是否整体上升。
   * 
   * @param datas 数值列表。
   * @return 如果数值列表整体上升，则返回 true，否则返回 false。
   */
  public static boolean isOverallUp(List<BigDecimal> datas) {
    return MathUtil.getSlope(datas).compareTo(BigDecimal.ZERO) > 0;
  }

  /**
   * 判断数值列表是否整体下降。
   * 
   * @param datas 数值列表。
   * @return 如果数值列表整体下降，则返回 true，否则返回 false。
   */
  public static boolean isOverallDown(List<BigDecimal> datas) {
    return MathUtil.getSlope(datas).compareTo(BigDecimal.ZERO) < 0;
  }
}
