package io.github.leaderman.makemoney.hustle.eastmoney.service;

import java.util.List;

import io.github.leaderman.makemoney.hustle.eastmoney.domain.model.SecurityModel;

public interface SecurityService {
  /**
   * 同步证券。
   * 
   * @param models 证券列表。
   */
  void sync(List<SecurityModel> models);

  /**
   * 判断是否持仓。
   * 
   * @param securityCode 证券代码。
   * @return 是否持仓。
   */
  boolean hasPosition(String securityCode);

  /**
   * 获取证券。
   * 
   * @param securityCode 证券代码。
   * @return 证券。
   */
  SecurityModel get(String securityCode);
}
