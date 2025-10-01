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

  private void syncDb(PositionModel model) {
    PositionEntity entity = this.getOne(null);
    if (Objects.isNull(entity)) {
      entity = PositionModel.toEntity(model);
    } else {
      // 资金持仓更新时间必须是当天才进行监控。
      if (DatetimeUtil.isSameDay(entity.getUpdatedAt(), LocalDateTime.now())) {
        /*
         * 持仓盈利新高（总体）:
         * 
         * 持仓盈亏新值大于 0，
         * 持仓盈亏旧值大于 0，
         * 持仓盈亏新值大于持仓盈亏旧值；
         * 
         * 或者
         * 
         * 持仓盈亏旧值小于或等于 0，
         * 持仓盈亏新值大于 0。
         */
        if (NumberUtil.greaterThan(model.getPositionProfitLoss(), BigDecimal.ZERO)
            && NumberUtil.greaterThan(entity.getPositionProfitLoss(), BigDecimal.ZERO)
            && NumberUtil.greaterThan(model.getPositionProfitLoss(), entity.getPositionProfitLossMax())
            || NumberUtil.lessThanOrEqualTo(entity.getPositionProfitLoss(), BigDecimal.ZERO)
                && NumberUtil.greaterThan(model.getPositionProfitLoss(), BigDecimal.ZERO)) {
          String title = String.format("持仓盈利新高");
          String content = String.format("盈利金额：%s\\n日期时间：%s", model.getPositionProfitLoss(),
              DatetimeUtil.getDatetime());

          this.imClient.sendRedMessageByChatIdAsync(positionProfitHighTotalChat, title, content);
        }

        /*
         * 持仓亏损新低（总体）：
         * 
         * 持仓盈亏新值小于 0，
         * 持仓盈亏旧值小于 0，
         * 持仓盈亏新值小于持仓盈亏旧值；
         * 
         * 或者
         * 
         * 持仓盈亏旧值大于或等于 0，
         * 持仓盈亏新值小于 0。
         */
        if (NumberUtil.lessThan(model.getPositionProfitLoss(), BigDecimal.ZERO)
            && NumberUtil.lessThan(entity.getPositionProfitLoss(), BigDecimal.ZERO)
            && NumberUtil.lessThan(model.getPositionProfitLoss(), entity.getPositionProfitLossMin())
            || NumberUtil.greaterThanOrEqualTo(entity.getPositionProfitLoss(), BigDecimal.ZERO)
                && NumberUtil.lessThan(model.getPositionProfitLoss(), BigDecimal.ZERO)) {
          String title = String.format("持仓亏损新低");
          String content = String.format("盈利金额：%s\\n日期时间：%s", model.getPositionProfitLoss(),
              DatetimeUtil.getDatetime());

          this.imClient.sendGreenMessageByChatIdAsync(positionLossLowTotalChat, title, content);
        }

        /*
         * 当日盈利新高（总体）：
         * 
         * 当日盈亏新值大于 0，
         * 当日盈亏旧值大于 0，
         * 当日盈亏新值大于当日盈亏旧值；
         * 
         * 或者
         * 
         * 当日盈亏旧值小于或等于 0，
         * 当日盈亏新值大于 0。
         */
        if (NumberUtil.greaterThan(model.getDailyProfitLoss(), BigDecimal.ZERO)
            && NumberUtil.greaterThan(entity.getDailyProfitLoss(), BigDecimal.ZERO)
            && NumberUtil.greaterThan(model.getDailyProfitLoss(), entity.getDailyProfitLossMax())
            || NumberUtil.lessThanOrEqualTo(entity.getDailyProfitLoss(), BigDecimal.ZERO)
                && NumberUtil.greaterThan(model.getDailyProfitLoss(), BigDecimal.ZERO)) {
          String title = String.format("当日盈利新高");
          String content = String.format("盈利金额：%s\\n日期时间：%s", model.getDailyProfitLoss(), DatetimeUtil.getDatetime());

          this.imClient.sendRedMessageByChatIdAsync(dailyProfitHighTotalChat, title, content);
        }

        /*
         * 当日亏损新低（总体）：
         * 
         * 当日盈亏新值小于 0，
         * 当日盈亏旧值小于 0，
         * 当日盈亏新值小于当日盈亏旧值；
         * 
         * 或者
         * 
         * 当日盈亏旧值大于或等于 0，
         * 当日盈亏新值小于 0。
         */
        if (NumberUtil.lessThan(model.getDailyProfitLoss(), BigDecimal.ZERO)
            && NumberUtil.lessThan(entity.getDailyProfitLoss(), BigDecimal.ZERO)
            && NumberUtil.lessThan(model.getDailyProfitLoss(), entity.getDailyProfitLossMin())
            || NumberUtil.greaterThanOrEqualTo(entity.getDailyProfitLoss(), BigDecimal.ZERO)
                && NumberUtil.lessThan(model.getDailyProfitLoss(), BigDecimal.ZERO)) {
          String title = String.format("当日盈利新低");
          String content = String.format("盈利金额：%s\\n日期时间：%s", model.getDailyProfitLoss(), DatetimeUtil.getDatetime());

          this.imClient.sendGreenMessageByChatIdAsync(dailyLossLowTotalChat, title, content);
        }
      }

      // 更新实体。
      // 注意：更新时间必须手动设置当前时间。
      entity.setTotalAssets(model.getTotalAssets());
      entity.setSecuritiesMarketValue(model.getSecuritiesMarketValue());
      entity.setAvailableFunds(model.getAvailableFunds());

      entity.setPositionProfitLoss(model.getPositionProfitLoss());

      /*
       * 更新持仓盈亏最大值：
       * 持仓盈亏新值大于持仓盈亏旧值，
       * 或持仓盈亏更新时间不是当天。
       */
      if (NumberUtil.greaterThan(model.getPositionProfitLoss(), entity.getPositionProfitLossMax())
          || !DatetimeUtil.isSameDay(entity.getUpdatedAt(), LocalDateTime.now())) {
        entity.setPositionProfitLossMax(model.getPositionProfitLoss());
      }

      /*
       * 更新持仓盈亏最小值：
       * 持仓盈亏新值小于持仓盈亏旧值，
       * 或持仓盈亏更新时间不是当天。
       */
      if (NumberUtil.lessThan(model.getPositionProfitLoss(), entity.getPositionProfitLossMin())
          || !DatetimeUtil.isSameDay(entity.getUpdatedAt(), LocalDateTime.now())) {
        // 更新持仓盈亏最小值。
        entity.setPositionProfitLossMin(model.getPositionProfitLoss());
      }

      entity.setCashBalance(model.getCashBalance());
      entity.setWithdrawableFunds(model.getWithdrawableFunds());

      entity.setDailyProfitLoss(model.getDailyProfitLoss());

      /*
       * 更新当日盈亏最大值：
       * 当日盈亏新值大于当日盈亏旧值，
       * 或当日盈亏更新时间不是当天。
       */
      if (NumberUtil.greaterThan(model.getDailyProfitLoss(), entity.getDailyProfitLossMax())
          || !DatetimeUtil.isSameDay(entity.getUpdatedAt(), LocalDateTime.now())) {
        entity.setDailyProfitLossMax(model.getDailyProfitLoss());
      }

      /*
       * 更新当日盈亏最小值：
       * 当日盈亏新值小于当日盈亏旧值，
       * 或当日盈亏更新时间不是当天。
       */
      if (NumberUtil.lessThan(model.getDailyProfitLoss(), entity.getDailyProfitLossMin())
          || !DatetimeUtil.isSameDay(entity.getUpdatedAt(), LocalDateTime.now())) {
        entity.setDailyProfitLossMin(model.getDailyProfitLoss());
      }

      entity.setFrozenFunds(model.getFrozenFunds());
      entity.setUpdatedAt(LocalDateTime.now());
    }

    this.saveOrUpdate(entity);
  }

  private void loadPositionRecordIds() throws Exception {
    if (!this.positionRecordIds.isEmpty()) {
      return;
    }

    synchronized (this.positionRecordIds) {
      if (this.positionRecordIds.isEmpty()) {
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
    this.loadPositionRecordIds();

    List<String> recordIds = new ArrayList<>();
    List<Map<String, Object>> records = new ArrayList<>();

    recordIds.add(this.getPositionRecordId("总资产"));
    records.add(Map.of("资金值", model.getTotalAssets()));

    recordIds.add(this.getPositionRecordId("证券市值"));
    records.add(Map.of("资金值", model.getSecuritiesMarketValue()));

    recordIds.add(this.getPositionRecordId("可用资金"));
    records.add(Map.of("资金值", model.getAvailableFunds()));

    recordIds.add(this.getPositionRecordId("持仓盈亏"));
    records.add(Map.of("资金值", model.getPositionProfitLoss()));

    recordIds.add(this.getPositionRecordId("资金余额"));
    records.add(Map.of("资金值", model.getCashBalance()));

    recordIds.add(this.getPositionRecordId("可取资金"));
    records.add(Map.of("资金值", model.getWithdrawableFunds()));

    recordIds.add(this.getPositionRecordId("当日盈亏"));
    records.add(Map.of("资金值", model.getDailyProfitLoss()));

    recordIds.add(this.getPositionRecordId("冻结资金"));
    records.add(Map.of("资金值", model.getFrozenFunds()));

    this.bitableClient.batchUpdateRecords(this.bitable, this.positionTable, recordIds, records);
  }

  @Override
  @Transactional
  public void sync(PositionModel model) {
    try {
      this.syncDb(model);
      this.syncBitable(model);
      this.securityService.sync(model.getSecurities());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
