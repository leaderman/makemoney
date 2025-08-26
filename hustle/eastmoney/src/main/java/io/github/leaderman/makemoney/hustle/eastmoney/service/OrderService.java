package io.github.leaderman.makemoney.hustle.eastmoney.service;

import java.util.List;

import io.github.leaderman.makemoney.hustle.eastmoney.domain.model.OrderModel;

public interface OrderService {
  void sync(List<OrderModel> orders);
}
