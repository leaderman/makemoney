package io.github.leaderman.makemoney.hustle.eastmoney.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import io.github.leaderman.makemoney.hustle.config.ConfigClient;
import io.github.leaderman.makemoney.hustle.eastmoney.domain.entity.PositionEntity;
import io.github.leaderman.makemoney.hustle.eastmoney.domain.model.PositionModel;
import io.github.leaderman.makemoney.hustle.eastmoney.mapper.PositionMapper;
import io.github.leaderman.makemoney.hustle.eastmoney.service.PositionService;
import io.github.leaderman.makemoney.hustle.eastmoney.service.SecurityService;
import io.github.leaderman.makemoney.hustle.feishu.BitableClient;
import io.github.leaderman.makemoney.hustle.feishu.ImClient;
import io.github.leaderman.makemoney.hustle.lang.DatetimeUtil;
import io.github.leaderman.makemoney.hustle.lang.NumberUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PositionServiceImpl extends ServiceImpl<PositionMapper, PositionEntity> implements PositionService {
  private final ConfigClient configClient;
  private final BitableClient bitableClient;
  private final ImClient imClient;

  private final SecurityService securityService;

  // 多维表格。
  private String bitable;
  // 资金持仓数据表。
  private String positionTable;

  // 持仓盈利新高（总体）群组。
  private String positionProfitHighTotalChat;
  // 持仓亏损新低（总体）群组。
  private String positionLossLowTotalChat;

  // 当日盈利新高（总体）群组。
  private String dailyProfitHighTotalChat;
  // 当日亏损新低（总体）群组。
  private String dailyLossLowTotalChat;

  // 多维表格记录 ID 缓存。
  private final Map<String, String> positionRecordIds = new ConcurrentHashMap<>();

  @PostConstruct
  public void init() {
    this.bitable = this.configClient.getString("eastmoney.bitable");
    this.positionTable = this.configClient.getString("eastmoney.bitable.position");

    this.positionProfitHighTotalChat = this.configClient.getString("feishu.chat.position.profit.high.total");
    this.positionLossLowTotalChat = this.configClient.getString("feishu.chat.position.loss.low.total");

    this.dailyProfitHighTotalChat = this.configClient.getString("feishu.chat.daily.profit.high.total");
    this.dailyLossLowTotalChat = this.configClient.getString("feishu.chat.daily.loss.low.total");
  }

  private void newPositionProfitLossMax(PositionEntity entity, PositionEntity existingEntity) {
    /*
     * 持仓盈利新高:
     * 
     * 持仓盈亏新最大值大于 0，
     * 持仓盈亏新最大值大于旧最大值；
     * 
     */
    if (NumberUtil.greaterThan(entity.getPositionProfitLossMax(), BigDecimal.ZERO)
        && NumberUtil.greaterThan(entity.getPositionProfitLossMax(), existingEntity.getPositionProfitLossMax())) {
      String title = String.format("持仓盈利新高");
      String content = String.format("盈利金额：%s\\n日期时间：%s", entity.getPositionProfitLossMax(),
          DatetimeUtil.getDatetime());

      this.imClient.sendRedMessageByChatIdAsync(positionProfitHighTotalChat, title, content);
    }
  }

  private void newPositionProfitLossMin(PositionEntity entity, PositionEntity existingEntity) {
    /*
     * 持仓亏损新低:
     * 
     * 持仓盈亏新最小值小于 0，
     * 持仓盈亏新最小值小于旧最小值；
     * 
     */
    if (NumberUtil.lessThan(entity.getPositionProfitLossMin(), BigDecimal.ZERO)
        && NumberUtil.lessThan(entity.getPositionProfitLossMin(), existingEntity.getPositionProfitLossMin())) {
      String title = String.format("持仓亏损新低");
      String content = String.format("亏损金额：%s\\n日期时间：%s", entity.getPositionProfitLossMin(),
          DatetimeUtil.getDatetime());

      this.imClient.sendGreenMessageByChatIdAsync(positionLossLowTotalChat, title, content);
    }
  }

  private void newDailyProfitLossMax(PositionEntity entity, PositionEntity existingEntity) {
    /*
     * 当日盈利新高:
     * 
     * 当日盈亏新最大值大于 0，
     * 当日盈亏新最大值大于旧最大值；
     * 
     */
    if (NumberUtil.greaterThan(entity.getDailyProfitLossMax(), BigDecimal.ZERO)
        && NumberUtil.greaterThan(entity.getDailyProfitLossMax(), existingEntity.getDailyProfitLossMax())) {
      String title = String.format("当日盈利新高");
      String content = String.format("盈利金额：%s\\n日期时间：%s", entity.getDailyProfitLossMax(),
          DatetimeUtil.getDatetime());

      this.imClient.sendRedMessageByChatIdAsync(dailyProfitHighTotalChat, title, content);
    }
  }

  private void newDailyProfitLossMin(PositionEntity entity, PositionEntity existingEntity) {
    /*
     * 当日亏损新低:
     * 
     * 当日盈亏新最小值小于 0，
     * 当日盈亏新最小值小于旧最小值；
     * 
     */
    if (NumberUtil.lessThan(entity.getDailyProfitLossMin(), BigDecimal.ZERO)
        && NumberUtil.lessThan(entity.getDailyProfitLossMin(), existingEntity.getDailyProfitLossMin())) {
      String title = String.format("当日亏损新低");
      String content = String.format("亏损金额：%s\\n日期时间：%s", entity.getDailyProfitLossMin(),
          DatetimeUtil.getDatetime());

      this.imClient.sendGreenMessageByChatIdAsync(dailyLossLowTotalChat, title, content);
    }
  }

  private boolean syncDb(PositionModel model) {
    PositionEntity entity = PositionModel.toEntity(model);

    PositionEntity existingEntity = this.getOne(null);
    if (Objects.isNull(existingEntity)) {
      // 资金持仓不存在于数据库中，需要新增。
      return this.save(entity);
    }

    if (PositionEntity.equals(entity, existingEntity)) {
      // 资金持仓存在于数据库中，且数据相同，跳过。
      return false;
    }

    // 资金持仓存在于数据库中，但数据不同，需要更新。
    // 注意：设置 ID。
    entity.setId(existingEntity.getId());

    /*
     * 修正持仓盈亏最大值：
     * 资金持仓更新更新时间是当天，
     * 且持仓盈亏旧值大于新值。
     */
    if (DatetimeUtil.isSameDay(existingEntity.getUpdatedAt(), LocalDateTime.now())
        && NumberUtil.greaterThan(existingEntity.getPositionProfitLossMax(), entity.getPositionProfitLossMax())) {
      // 使用旧值。
      entity.setPositionProfitLossMax(existingEntity.getPositionProfitLossMax());
    }

    /*
     * 修正持仓盈亏最小值：
     * 资金持仓更新时间是当天，
     * 且持仓盈亏旧值小于新值。
     */
    if (DatetimeUtil.isSameDay(existingEntity.getUpdatedAt(), LocalDateTime.now())
        && NumberUtil.lessThan(existingEntity.getPositionProfitLossMin(), entity.getPositionProfitLossMin())) {
      // 使用旧值。
      entity.setPositionProfitLossMin(existingEntity.getPositionProfitLossMin());
    }

    /*
     * 修正当日盈亏最大值：
     * 资金持仓更新时间是当天，
     * 且当日盈亏旧值小于新值。
     */
    if (DatetimeUtil.isSameDay(existingEntity.getUpdatedAt(), LocalDateTime.now())
        && NumberUtil.greaterThan(existingEntity.getDailyProfitLossMax(), entity.getDailyProfitLossMax())) {
      // 使用旧值。
      entity.setDailyProfitLossMax(existingEntity.getDailyProfitLossMax());
    }

    /*
     * 修正当日盈亏最小值：
     * 资金持仓更新时间是当天，
     * 且当日盈亏旧值小于新值。
     */
    if (DatetimeUtil.isSameDay(existingEntity.getUpdatedAt(), LocalDateTime.now())
        && NumberUtil.lessThan(existingEntity.getDailyProfitLossMin(), entity.getDailyProfitLossMin())) {
      // 使用旧值。
      entity.setDailyProfitLossMin(existingEntity.getDailyProfitLossMin());
    }

    // 资金持仓更新时间是当天才进行盈亏监测。
    if (DatetimeUtil.isSameDay(existingEntity.getUpdatedAt(), LocalDateTime.now())) {
      // 持仓盈利新高。
      this.newPositionProfitLossMax(entity, existingEntity);
      // 持仓亏损新低。
      this.newPositionProfitLossMin(entity, existingEntity);
      // 当日盈利新高。
      this.newDailyProfitLossMax(entity, existingEntity);
      // 当日亏损新低。
      this.newDailyProfitLossMin(entity, existingEntity);
    }

    return this.updateById(entity);
  }

  private boolean hasPositionRecordIds() {
    return !this.positionRecordIds.isEmpty();
  }

  private void loadPositionRecordIds() throws Exception {
    if (this.hasPositionRecordIds()) {
      return;
    }

    synchronized (this.positionRecordIds) {
      if (!this.hasPositionRecordIds()) {
        this.positionRecordIds.putAll(this.bitableClient.listTableRecords(this.bitable, this.positionTable)
            .stream()
            .collect(
                Collectors.toMap(record -> (String) record.getFields().get("资金名称"), record -> record.getRecordId())));
      }
    }
  }

  private String getPositionRecordId(String fieldName) {
    return this.positionRecordIds.get(fieldName);
  }

  private void syncBitable(PositionModel model) throws Exception {
    List<Map<String, Object>> records = new ArrayList<>();

    records.add(Map.of("资金名称", "总资产", "资金值", model.getTotalAssets()));
    records.add(Map.of("资金名称", "证券市值", "资金值", model.getSecuritiesMarketValue()));
    records.add(Map.of("资金名称", "可用资金", "资金值", model.getAvailableFunds()));
    records.add(Map.of("资金名称", "持仓盈亏", "资金值", model.getPositionProfitLoss()));
    records.add(Map.of("资金名称", "资金余额", "资金值", model.getCashBalance()));
    records.add(Map.of("资金名称", "可取资金", "资金值", model.getWithdrawableFunds()));
    records.add(Map.of("资金名称", "当日盈亏", "资金值", model.getDailyProfitLoss()));
    records.add(Map.of("资金名称", "冻结资金", "资金值", model.getFrozenFunds()));

    this.loadPositionRecordIds();
    if (!this.hasPositionRecordIds()) {
      // 资金持仓不存在于多维表格中，需要新增。
      this.bitableClient.batchCreateRecords(this.bitable, this.positionTable, records);
      return;
    }

    // 资金持仓已存在于多维表格中，且数据不同，需要更新。
    List<String> recordIds = new ArrayList<>();

    recordIds.add(this.getPositionRecordId("总资产"));
    recordIds.add(this.getPositionRecordId("证券市值"));
    recordIds.add(this.getPositionRecordId("可用资金"));
    recordIds.add(this.getPositionRecordId("持仓盈亏"));
    recordIds.add(this.getPositionRecordId("资金余额"));
    recordIds.add(this.getPositionRecordId("可取资金"));
    recordIds.add(this.getPositionRecordId("当日盈亏"));
    recordIds.add(this.getPositionRecordId("冻结资金"));

    this.bitableClient.batchUpdateRecords(this.bitable, this.positionTable, recordIds, records);
  }

  @Override
  @Transactional
  public void sync(PositionModel model) {
    try {
      // 同步资金持仓。
      if (this.syncDb(model)) {
        this.syncBitable(model);
      }

      // 同步证券。
      this.securityService.sync(model.getSecurities());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
