package io.github.leaderman.makemoney.hustle.stock.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import io.github.leaderman.makemoney.hustle.stock.domain.entity.StockEntity;
import io.github.leaderman.makemoney.hustle.stock.domain.model.StockModel;
import io.github.leaderman.makemoney.hustle.stock.mapper.StockMapper;

@Service
public class StockService extends ServiceImpl<StockMapper, StockEntity> {
  public void save(StockModel model) {
    this.save(StockModel.to(model));
  }

  public StockModel get(String code) {
    StockEntity entity = this.getOne(new QueryWrapper<StockEntity>().eq("code", code));
    if (Objects.isNull(entity)) {
      return null;
    }

    return StockModel.from(entity);
  }

  public List<StockModel> gets() {
    return this.list().stream().map(StockModel::from).collect(Collectors.toList());
  }

  public void updateById(StockModel model) {
    this.updateById(StockModel.to(model));
  }
}
