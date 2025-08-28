package io.github.leaderman.makemoney.hustle.eastmoney.service;

import java.util.List;

import io.github.leaderman.makemoney.hustle.eastmoney.domain.model.OrderModel;

public interface OrderService {
  /**
   * 同步委托。
   * 
   * @param orders 委托列表。
   */
  void sync(List<OrderModel> orders);
}
