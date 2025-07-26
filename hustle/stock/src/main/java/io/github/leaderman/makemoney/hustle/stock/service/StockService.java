package io.github.leaderman.makemoney.hustle.stock.service;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import io.github.leaderman.makemoney.hustle.stock.domain.entity.StockEntity;
import io.github.leaderman.makemoney.hustle.stock.mapper.StockMapper;

@Service
public class StockService extends ServiceImpl<StockMapper, StockEntity> {
  public StockEntity get(String code) {
    return this.getOne(new QueryWrapper<StockEntity>().eq("code", code));
  }
}
