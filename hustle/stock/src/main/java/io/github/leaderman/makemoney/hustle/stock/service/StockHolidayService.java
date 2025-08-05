package io.github.leaderman.makemoney.hustle.stock.service;

import java.util.Objects;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import io.github.leaderman.makemoney.hustle.stock.domain.entity.StockHolidayEntity;
import io.github.leaderman.makemoney.hustle.stock.domain.model.StockHolidayModel;
import io.github.leaderman.makemoney.hustle.stock.mapper.StockHolidayMapper;

@Service
public class StockHolidayService extends ServiceImpl<StockHolidayMapper, StockHolidayEntity> {
  private static final String MARKET_CN = "cn";

  /**
   * 获取节假日信息。
   * 
   * @param market 市场。
   * @param date   日期。
   * @return 节假日信息。
   */
  public StockHolidayModel get(String market, String date) {
    StockHolidayEntity entity = this.getOne(new LambdaQueryWrapper<StockHolidayEntity>()
        .eq(StockHolidayEntity::getMarket, market).eq(StockHolidayEntity::getHoliday, date));
    if (Objects.isNull(entity)) {
      return null;
    }

    return StockHolidayModel.from(entity);
  }

  /**
   * 判断是否为节假日。
   * 
   * @param market 市场。
   * @param date   日期。
   * @return 如果为节假日，返回 true；否则返回 false。
   */
  public boolean isHoliday(String market, String date) {
    return Objects.nonNull(this.get(market, date));
  }

  /**
   * 判断是否为 A 股节假日。
   * 
   * @param date 日期。
   * @return 如果为节假日，返回 true；否则返回 false。
   */
  public boolean isCnHoliday(String date) {
    return this.isHoliday(MARKET_CN, date);
  }
}
