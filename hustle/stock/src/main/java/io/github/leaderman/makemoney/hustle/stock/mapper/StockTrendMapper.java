package io.github.leaderman.makemoney.hustle.stock.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import io.github.leaderman.makemoney.hustle.stock.domain.entity.StockTrendEntity;

@Mapper
public interface StockTrendMapper extends BaseMapper<StockTrendEntity> {
}
