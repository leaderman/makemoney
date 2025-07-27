package io.github.leaderman.makemoney.hustle.stock.service;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import io.github.leaderman.makemoney.hustle.stock.domain.entity.StockMarketInfoEntity;
import io.github.leaderman.makemoney.hustle.stock.mapper.StockMarketInfoEntityMapper;

@Service
public class StockMarketInfoEntityService extends ServiceImpl<StockMarketInfoEntityMapper, StockMarketInfoEntity> {
}
