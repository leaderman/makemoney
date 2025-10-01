package io.github.leaderman.makemoney.hustle.lang;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.math3.stat.regression.SimpleRegression;

public class MathUtil {
  /**
   * 获取斜率。
   * 
   * @param datas 数据列表。
   * @return 斜率。
   */
  public static BigDecimal getSlope(List<BigDecimal> datas) {
    SimpleRegression simpleRegression = new SimpleRegression();

    for (int index = 0; index < datas.size(); index++) {
      simpleRegression.addData(index, datas.get(index).doubleValue());
    }

    return new BigDecimal(simpleRegression.getSlope());
  }

  /**
   * 获取指定范围内的随机数。
   * 
   * @param min 最小值，包含。
   * @param max 最大值，包含。
   * @return 随机数。
   */
  public static BigDecimal random(BigDecimal min, BigDecimal max) {
    double value = ThreadLocalRandom.current().nextDouble(min.doubleValue(), Math.nextUp(max.doubleValue()));

    return BigDecimal.valueOf(value);
  }
}
