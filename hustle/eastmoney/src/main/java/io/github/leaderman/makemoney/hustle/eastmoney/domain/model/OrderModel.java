package io.github.leaderman.makemoney.hustle.eastmoney.domain.model;

import java.math.BigDecimal;

import io.github.leaderman.makemoney.hustle.domain.model.BaseModel;
import io.github.leaderman.makemoney.hustle.eastmoney.domain.request.SyncOrdersRequest;
import io.github.leaderman.makemoney.hustle.lang.NumberUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString
@Slf4j
public class OrderModel extends BaseModel {
  // 委托时间。
  private String orderTime;
  // 证券代码。
  private String securityCode;
  // 证券名称。
  private String securityName;
  // 委托方向。
  private String orderSide;
  // 委托数量。
  private Integer orderQuantity;
  // 委托状态。
  private String orderStatus;
  // 委托价格。
  private BigDecimal orderPrice;
  // 成交数量。
  private Integer filledQuantity;
  // 成交金额。
  private BigDecimal filledAmount;
  // 成交价格。
  private BigDecimal avgFilledPrice;
  // 委托编号。
  private String orderId;
  // 币种。
  private String currency;

  public static OrderModel from(SyncOrdersRequest.Order order) {
    OrderModel orderModel = new OrderModel();

    orderModel.setOrderTime(order.getOrderTime());
    orderModel.setSecurityCode(order.getSecurityCode());
    orderModel.setSecurityName(order.getSecurityName());
    orderModel.setOrderSide(order.getOrderSide());
    orderModel.setOrderQuantity(NumberUtil.toInteger(order.getOrderQuantity(), 0));
    orderModel.setOrderStatus(order.getOrderStatus());
    orderModel.setOrderPrice(NumberUtil.toBigDecimal(order.getOrderPrice(), BigDecimal.ZERO));
    orderModel.setFilledQuantity(NumberUtil.toInteger(order.getFilledQuantity(), 0));
    orderModel.setFilledAmount(NumberUtil.toBigDecimal(order.getFilledAmount(), BigDecimal.ZERO));
    orderModel.setAvgFilledPrice(NumberUtil.toBigDecimal(order.getAvgFilledPrice(), BigDecimal.ZERO));
    orderModel.setOrderId(order.getOrderId());
    orderModel.setCurrency(order.getCurrency());

    return orderModel;
  }

  public static boolean equals(OrderModel left, OrderModel right) {
    log.info("filledAmount: {}, {}, {}", left.getFilledAmount(), right.getFilledAmount(),
        left.getFilledAmount().equals(right.getFilledAmount()));

    return left.getOrderTime().equals(right.getOrderTime())
        && left.getSecurityCode().equals(right.getSecurityCode())
        && left.getSecurityName().equals(right.getSecurityName())
        && left.getOrderSide().equals(right.getOrderSide())
        && left.getOrderQuantity().equals(right.getOrderQuantity())
        && left.getOrderStatus().equals(right.getOrderStatus())
        && left.getOrderPrice().equals(right.getOrderPrice())
        && left.getFilledQuantity().equals(right.getFilledQuantity())
        && left.getFilledAmount().equals(right.getFilledAmount())
        && left.getAvgFilledPrice().equals(right.getAvgFilledPrice())
        && left.getOrderId().equals(right.getOrderId())
        && left.getCurrency().equals(right.getCurrency());
  }
}
