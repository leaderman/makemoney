package io.github.leaderman.makemoney.hustle.eastmoney.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

import io.github.leaderman.makemoney.hustle.eastmoney.domain.entity.FundEntity;
import io.github.leaderman.makemoney.hustle.eastmoney.domain.model.FundModel;

public interface FundService extends IService<FundEntity> {
  /**
   * 同步基金。
   * 
   * @param models 基金列表。
   */
  void sync(List<FundModel> models);

  /**
   * 获取所有基金。
   * 
   * @return 所有基金。
   */
  List<FundModel> gets();
}
