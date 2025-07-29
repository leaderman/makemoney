package io.github.leaderman.makemoney.hustle.stock.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import io.github.leaderman.makemoney.hustle.stock.domain.entity.StockHolidayEntity;

@Mapper
public interface StockHolidayMapper extends BaseMapper<StockHolidayEntity> {
}
