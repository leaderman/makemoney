package io.github.leaderman.makemoney.hustle.stock.service;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import io.github.leaderman.makemoney.hustle.stock.domain.entity.StockMarketInfoEntity;
import io.github.leaderman.makemoney.hustle.stock.mapper.StockMarketInfoEntityMapper;

@Service
public class StockMarketInfoEntityService extends ServiceImpl<StockMarketInfoEntityMapper, StockMarketInfoEntity> {
  public StockMarketInfoEntity get(String code, String day) {
    return this.baseMapper.selectOne(new LambdaQueryWrapper<StockMarketInfoEntity>()
        .eq(StockMarketInfoEntity::getCode, code)
        .eq(StockMarketInfoEntity::getDay, day));
  }
}
