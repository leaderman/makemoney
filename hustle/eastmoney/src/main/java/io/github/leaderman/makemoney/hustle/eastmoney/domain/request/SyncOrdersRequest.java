package io.github.leaderman.makemoney.hustle.eastmoney.domain.request;

import java.util.List;

import lombok.Data;

@Data
public class SyncOrdersRequest {
  @Data
  public static class Order {
    // 委托时间。
    private String orderTime;
    // 证券代码。
    private String securityCode;
    // 证券名称。
    private String securityName;
    // 委托方向。
    private String orderSide;
    // 委托数量。
    private String orderQuantity;
    // 委托状态。
    private String orderStatus;
    // 委托价格。
    private String orderPrice;
    // 成交数量。
    private String filledQuantity;
    // 成交金额。
    private String filledAmount;
    // 成交价格。
    private String avgFilledPrice;
    // 委托编号。
    private String orderId;
    // 币种。
    private String currency;
  }

  private List<Order> orders;
}
