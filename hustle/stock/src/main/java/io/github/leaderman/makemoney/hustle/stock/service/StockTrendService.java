package io.github.leaderman.makemoney.hustle.stock.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import io.github.leaderman.makemoney.hustle.stock.domain.entity.StockTrendEntity;
import io.github.leaderman.makemoney.hustle.stock.domain.model.StockTrendModel;
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

  /**
   * 获取所有趋势。
   * 
   * @return 所有趋势。
   */
  public List<String> getTrends() {
    return this.listObjs(new QueryWrapper<StockTrendEntity>().select("DISTINCT trend"));
  }

  /**
   * 根据趋势获取股票趋势列表。
   * 
   * @param trend 趋势。
   * @return 股票趋势列表。
   */
  public List<StockTrendModel> gets(String trend) {
    return this.list(new LambdaQueryWrapper<StockTrendEntity>().eq(StockTrendEntity::getTrend, trend)).stream()
        .map(StockTrendModel::from).collect(Collectors.toList());
  }
}
