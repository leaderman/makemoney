package io.github.leaderman.makemoney.hustle.trade.service;

import com.baomidou.mybatisplus.extension.service.IService;

import io.github.leaderman.makemoney.hustle.trade.domain.entity.TradeHolidayEntity;

public interface TradeHolidayService extends IService<TradeHolidayEntity> {
  /**
   * 判断是否为休市日。
   * 
   * @param market 市场。
   * @param date   日期。
   * @return 是否为休市日。
   */
  boolean isHoliday(String market, String date);

  /**
   * 判断当前日期是否为 A 股休市日。
   * 
   * @return 是否为 A 股休市日。
   */
  boolean isHoliday();
}
