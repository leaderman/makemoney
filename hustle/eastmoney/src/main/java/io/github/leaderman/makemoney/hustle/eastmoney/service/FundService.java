package io.github.leaderman.makemoney.hustle.eastmoney.service;

import java.util.List;

import io.github.leaderman.makemoney.hustle.eastmoney.domain.model.FundModel;

public interface FundService {
  /**
   * 同步基金。
   * 
   * @param models 基金列表。
   */
  void sync(List<FundModel> models);
}
