package io.github.leaderman.makemoney.hustle.eastmoney.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.lark.oapi.service.bitable.v1.model.AppTableRecord;

import io.github.leaderman.makemoney.hustle.config.ConfigClient;
import io.github.leaderman.makemoney.hustle.eastmoney.domain.model.OrderModel;
import io.github.leaderman.makemoney.hustle.eastmoney.service.OrderService;
import io.github.leaderman.makemoney.hustle.feishu.BitableClient;
import io.github.leaderman.makemoney.hustle.lang.NumberUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
  private final ConfigClient configClient;
  private final BitableClient bitableClient;

  private String bitable;
  private String ordersTable;

  @PostConstruct
  public void init() {
    this.bitable = this.configClient.getString("eastmoney.bitable");
    this.ordersTable = this.configClient.getString("eastmoney.bitable.orders");
  }

  private String getOrderId(AppTableRecord record) {
    return (String) record.getFields().get("委托编号");
  }

  private Map<String, Object> toRecord(OrderModel order) {
    Map<String, Object> record = new HashMap<>();

    record.put("委托时间", order.getOrderTime());
    record.put("证券代码", order.getSecurityCode());
    record.put("证券名称", order.getSecurityName());
    record.put("委托方向", order.getOrderSide());
    record.put("委托数量", order.getOrderQuantity());
    record.put("委托状态", order.getOrderStatus());
    record.put("委托价格", order.getOrderPrice());
    record.put("成交数量", order.getFilledQuantity());
    record.put("成交金额", order.getFilledAmount());
    record.put("成交价格", order.getAvgFilledPrice());
    record.put("委托编号", order.getOrderId());
    record.put("币种", order.getCurrency());

    return record;
  }

  private OrderModel toModel(AppTableRecord record) {
    OrderModel order = new OrderModel();

    order.setOrderTime((String) record.getFields().get("委托时间"));
    order.setSecurityCode((String) record.getFields().get("证券代码"));
    order.setSecurityName((String) record.getFields().get("证券名称"));
    order.setOrderSide((String) record.getFields().get("委托方向"));
    order.setOrderQuantity(NumberUtil.toInteger((String) record.getFields().get("委托数量"), 0));
    order.setOrderStatus((String) record.getFields().get("委托状态"));
    order.setOrderPrice(NumberUtil.toBigDecimal((String) record.getFields().get("委托价格"), BigDecimal.ZERO));
    order.setFilledQuantity(NumberUtil.toInteger((String) record.getFields().get("成交数量"), 0));
    order.setFilledAmount(NumberUtil.toBigDecimal((String) record.getFields().get("成交金额"), BigDecimal.ZERO));
    order.setAvgFilledPrice(NumberUtil.toBigDecimal((String) record.getFields().get("成交价格"), BigDecimal.ZERO));
    order.setOrderId(this.getOrderId(record));
    order.setCurrency((String) record.getFields().get("币种"));

    return order;
  }

  @Override
  public void sync(List<OrderModel> orders) {
    try {
      // 获取多维表格中的记录。
      Map<String, AppTableRecord> existingRecords = Optional
          .ofNullable(this.bitableClient.listTableRecords(this.bitable, this.ordersTable))
          .map(records -> records.stream().collect(Collectors.toMap(this::getOrderId, Function.identity())))
          .orElse(Collections.emptyMap());

      // 新增列表。
      List<Map<String, Object>> createRecords = new ArrayList<>();

      // 更新列表。
      List<String> updateRecordIds = new ArrayList<>();
      List<Map<String, Object>> updateRecords = new ArrayList<>();

      for (OrderModel order : orders) {
        // 委托编号。
        String orderId = order.getOrderId();

        if (!existingRecords.containsKey(orderId)) {
          // 委托不存在于多维表格中，需要新增。
          createRecords.add(toRecord(order));

          continue;
        }

        AppTableRecord existingRecord = existingRecords.get(orderId);
        log.info("equals: {}", OrderModel.equals(order, toModel(existingRecord)));
        if (OrderModel.equals(order, toModel(existingRecord))) {
          // 委托存在于多维表格中，且数据相同，跳过。
          continue;
        }

        // 委托存在于多维表格中，但数据不同，需要更新。
        updateRecordIds.add(existingRecord.getRecordId());
        updateRecords.add(toRecord(order));
      }

      if (!createRecords.isEmpty()) {
        // 新增。
        this.bitableClient.batchCreateRecords(this.bitable, this.ordersTable, createRecords);
        log.info("新增 {} 条记录", createRecords.size());
      }

      if (!updateRecordIds.isEmpty()) {
        // 更新。
        this.bitableClient.batchUpdateRecords(this.bitable, this.ordersTable, updateRecordIds, updateRecords);
        log.info("更新 {} 条记录", updateRecordIds.size());
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
