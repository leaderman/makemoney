package io.github.leaderman.makemoney.hustle.stock.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import io.github.leaderman.makemoney.hustle.stock.domain.entity.StockEntity;

@Mapper
public interface StockMapper extends BaseMapper<StockEntity> {
}
