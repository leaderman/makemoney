package io.github.leaderman.makemoney.hustle.eastmoney.web.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.leaderman.makemoney.hustle.eastmoney.domain.model.OrderModel;
import io.github.leaderman.makemoney.hustle.eastmoney.domain.request.SyncOrdersRequest;
import io.github.leaderman.makemoney.hustle.eastmoney.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/eastmoney/order")
@Slf4j
public class OrderController {
  private final OrderService orderService;

  @PostMapping("/sync")
  public void sync(@RequestBody SyncOrdersRequest request) {
    try {
      List<SyncOrdersRequest.Order> orders = request.getOrders();
      if (CollectionUtils.isEmpty(orders)) {
        return;
      }

      orderService.sync(orders.stream().map(OrderModel::from).collect(Collectors.toList()));
    } catch (Exception e) {
      log.error("同步今日委托错误：{}", ExceptionUtils.getStackTrace(e));
    }
  }
}
