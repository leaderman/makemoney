package io.github.leaderman.makemoney.hustle.stock.service;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import io.github.leaderman.makemoney.hustle.stock.domain.entity.StockTrendEntity;
import io.github.leaderman.makemoney.hustle.stock.mapper.StockTrendMapper;

@Service
public class StockTrendService extends ServiceImpl<StockTrendMapper, StockTrendEntity> {
  /**
   * 根据股票代码删除股票趋势。
   * 
   * @param code 股票代码。
   */
  public void remove(String code) {
    this.remove(new LambdaQueryWrapper<StockTrendEntity>().eq(StockTrendEntity::getCode, code));
  }
}
