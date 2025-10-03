package io.github.leaderman.makemoney.hustle.eastmoney.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import io.github.leaderman.makemoney.hustle.eastmoney.domain.entity.FundPriceEntity;
import io.github.leaderman.makemoney.hustle.eastmoney.domain.model.FundBucketPriceModel;
import io.github.leaderman.makemoney.hustle.eastmoney.domain.model.FundPriceModel;
import io.github.leaderman.makemoney.hustle.eastmoney.mapper.FundPriceMapper;
import io.github.leaderman.makemoney.hustle.eastmoney.service.FundPriceService;
import io.github.leaderman.makemoney.hustle.lang.DatetimeUtil;
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

  @Override
  public long count(String code, LocalDateTime start, LocalDateTime end) {
    return this.count(new LambdaQueryWrapper<FundPriceEntity>().eq(FundPriceEntity::getCode, code)
        .ge(FundPriceEntity::getCreatedAt, start).le(FundPriceEntity::getCreatedAt, end));
  }

  @Override
  public long count(String code) {
    return this.count(code, DatetimeUtil.startOfDay(), DatetimeUtil.endOfDay());
  }

  @Override
  public List<FundBucketPriceModel> bucket(String code, int size, LocalDateTime start, LocalDateTime end) {
    return this.getBaseMapper().bucket(code, size, start, end).stream().map(FundBucketPriceModel::from)
        .collect(Collectors.toList());
  }

  @Override
  public List<FundBucketPriceModel> bucket(String code, int size) {
    return this.bucket(code, size, DatetimeUtil.startOfDay(), DatetimeUtil.endOfDay());
  }
}
