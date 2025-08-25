package io.github.leaderman.makemoney.hustle.eastmoney.service;

import io.github.leaderman.makemoney.hustle.eastmoney.domain.model.PositionModel;

public interface PositionService {
  /**
   * 同步资金持仓信息。
   * 
   * @param model 资金持仓。
   */
  void sync(PositionModel model);
}
