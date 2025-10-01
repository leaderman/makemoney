package io.github.leaderman.makemoney.hustle.eastmoney.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import io.github.leaderman.makemoney.hustle.eastmoney.domain.entity.FundPriceEntity;
import io.github.leaderman.makemoney.hustle.eastmoney.domain.model.FundPriceModel;
import io.github.leaderman.makemoney.hustle.eastmoney.mapper.FundPriceMapper;
import io.github.leaderman.makemoney.hustle.eastmoney.service.FundPriceService;
import io.github.leaderman.makemoney.hustle.trade.service.TradeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FundPriceServiceImpl extends ServiceImpl<FundPriceMapper, FundPriceEntity> implements FundPriceService {
  private final TradeService tradeService;

  @Override
  @Transactional
  public void save(List<FundPriceModel> models) {
    if (!this.tradeService.isTradingTime()) {
      return;
    }

    this.saveBatch(models.stream().map(FundPriceModel::to).collect(Collectors.toList()));
    log.info("保存 {} 条实体", models.size());
  }
}
