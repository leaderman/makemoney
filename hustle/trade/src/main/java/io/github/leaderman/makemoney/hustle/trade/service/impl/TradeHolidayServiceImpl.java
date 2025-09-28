package io.github.leaderman.makemoney.hustle.trade.service.impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import io.github.leaderman.makemoney.hustle.lang.DatetimeUtil;
import io.github.leaderman.makemoney.hustle.trade.domain.entity.TradeHolidayEntity;
import io.github.leaderman.makemoney.hustle.trade.domain.model.TradeHolidayModel;
import io.github.leaderman.makemoney.hustle.trade.mapper.TradeHolidayMapper;
import io.github.leaderman.makemoney.hustle.trade.service.TradeHolidayService;

@Service
public class TradeHolidayServiceImpl extends ServiceImpl<TradeHolidayMapper, TradeHolidayEntity>
    implements TradeHolidayService {
  @Override
  public boolean isHoliday(String market, String date) {
    return this.getOne(new LambdaQueryWrapper<TradeHolidayEntity>()
        .eq(TradeHolidayEntity::getMarket, market).eq(TradeHolidayEntity::getHoliday, date)) != null;
  }

  @Override
  public boolean isHoliday() {
    return this.isHoliday(TradeHolidayModel.MARKET_CN, DatetimeUtil.getDate());
  }

  @Override
  public boolean isHoliday(String date) {
    return this.isHoliday(TradeHolidayModel.MARKET_CN, date);
  }

  @Override
  public boolean isHoliday(LocalDateTime datetime) {
    return this.isHoliday(TradeHolidayModel.MARKET_CN, DatetimeUtil.formatDate(datetime));
  }
}
