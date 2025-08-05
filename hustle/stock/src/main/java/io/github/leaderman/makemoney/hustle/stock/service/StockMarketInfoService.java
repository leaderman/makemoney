package io.github.leaderman.makemoney.hustle.stock.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import io.github.leaderman.makemoney.hustle.stock.domain.entity.StockMarketInfoEntity;
import io.github.leaderman.makemoney.hustle.stock.domain.model.StockMarketInfoModel;
import io.github.leaderman.makemoney.hustle.stock.mapper.StockMarketInfoEntityMapper;

@Service
public class StockMarketInfoService extends ServiceImpl<StockMarketInfoEntityMapper, StockMarketInfoEntity> {
  /**
   * 保存股票市场信息。
   * 
   * @param model 股票市场信息。
   */
  public void save(StockMarketInfoModel model) {
    this.save(StockMarketInfoModel.to(model));
  }

  /**
   * 根据股票代码，获取指定日期的股票市场信息。
   * 
   * @param code 股票代码。
   * @param day  日期。
   * @return 股票市场信息。
   */
  public StockMarketInfoModel get(String code, String day) {
    StockMarketInfoEntity entity = this.getOne(new LambdaQueryWrapper<StockMarketInfoEntity>()
        .eq(StockMarketInfoEntity::getCode, code).eq(StockMarketInfoEntity::getDay, day));
    if (Objects.isNull(entity)) {
      return null;
    }

    return StockMarketInfoModel.from(entity);
  }

  /**
   * 根据股票代码，获取最近几天的股票市场信息。
   * 
   * @param code 股票代码。
   * @param days 天数。
   * @return 股票市场信息。
   */
  public List<StockMarketInfoModel> getLatest(String code, int days) {
    return this
        .list(new LambdaQueryWrapper<StockMarketInfoEntity>().eq(StockMarketInfoEntity::getCode, code)
            .orderByDesc(StockMarketInfoEntity::getDay).last("limit " + days))
        .stream().map(StockMarketInfoModel::from).collect(Collectors.toList());
  }

  /**
   * 更新股票市场信息。
   * 
   * @param model 股票市场信息。
   */
  public void updateById(StockMarketInfoModel model) {
    this.updateById(StockMarketInfoModel.to(model));
  }
}
