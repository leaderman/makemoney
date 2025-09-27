package io.github.leaderman.makemoney.hustle.eastmoney.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

import io.github.leaderman.makemoney.hustle.eastmoney.domain.entity.FundPriceEntity;
import io.github.leaderman.makemoney.hustle.eastmoney.domain.model.FundPriceModel;

public interface FundPriceService extends IService<FundPriceEntity> {
  /**
   * 保存基金价格。
   * 
   * @param models 基金价格列表。
   */
  void save(List<FundPriceModel> models);
}
