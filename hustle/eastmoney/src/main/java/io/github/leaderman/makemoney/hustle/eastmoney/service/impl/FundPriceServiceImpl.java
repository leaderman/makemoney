package io.github.leaderman.makemoney.hustle.eastmoney.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import io.github.leaderman.makemoney.hustle.eastmoney.domain.entity.FundPriceEntity;
import io.github.leaderman.makemoney.hustle.eastmoney.domain.model.FundPriceModel;
import io.github.leaderman.makemoney.hustle.eastmoney.mapper.FundPriceMapper;
import io.github.leaderman.makemoney.hustle.eastmoney.service.FundPriceService;

@Service
public class FundPriceServiceImpl extends ServiceImpl<FundPriceMapper, FundPriceEntity> implements FundPriceService {
  @Override
  public void save(List<FundPriceModel> models) {
    this.saveBatch(models.stream().map(FundPriceModel::to).collect(Collectors.toList()));
  }
}
