package io.github.leaderman.makemoney.hustle.lang;

import java.math.BigDecimal;
import java.util.List;

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
}
