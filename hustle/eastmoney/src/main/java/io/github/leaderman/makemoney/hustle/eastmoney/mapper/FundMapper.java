package io.github.leaderman.makemoney.hustle.eastmoney.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import io.github.leaderman.makemoney.hustle.eastmoney.domain.entity.FundEntity;

@Mapper
public interface FundMapper extends BaseMapper<FundEntity> {
}
