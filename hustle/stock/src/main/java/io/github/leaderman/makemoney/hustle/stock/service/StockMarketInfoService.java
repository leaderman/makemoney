package io.github.leaderman.makemoney.hustle.stock.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import io.github.leaderman.makemoney.hustle.stock.domain.entity.StockMarketInfoEntity;
import io.github.leaderman.makemoney.hustle.stock.mapper.StockMarketInfoEntityMapper;

@Service
public class StockMarketInfoService extends ServiceImpl<StockMarketInfoEntityMapper, StockMarketInfoEntity> {
  /**
   * 根据股票代码，获取指定日期的股票市场信息。
   * 
   * @param code 股票代码。
   * @param day  日期。
   * @return 股票市场信息。
   */
  public StockMarketInfoEntity get(String code, String day) {
    return this.baseMapper.selectOne(new LambdaQueryWrapper<StockMarketInfoEntity>()
        .eq(StockMarketInfoEntity::getCode, code)
        .eq(StockMarketInfoEntity::getDay, day));
  }

  /**
   * 根据股票代码，获取最近几天的股票市场信息。
   * 
   * @param code 股票代码。
   * @param days 天数。
   * @return 股票市场信息。
   */
  public List<StockMarketInfoEntity> getLatest(String code, int days) {
    return this.baseMapper.selectList(new LambdaQueryWrapper<StockMarketInfoEntity>()
        .eq(StockMarketInfoEntity::getCode, code)
        .orderByDesc(StockMarketInfoEntity::getDay)
        .last("limit " + days))
        .reversed();
  }
}
