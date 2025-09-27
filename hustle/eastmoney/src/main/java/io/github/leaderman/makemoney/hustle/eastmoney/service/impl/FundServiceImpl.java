package io.github.leaderman.makemoney.hustle.eastmoney.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import io.github.leaderman.makemoney.hustle.config.ConfigClient;
import io.github.leaderman.makemoney.hustle.eastmoney.domain.entity.FundEntity;
import io.github.leaderman.makemoney.hustle.eastmoney.domain.model.FundModel;
import io.github.leaderman.makemoney.hustle.eastmoney.domain.model.SecurityModel;
import io.github.leaderman.makemoney.hustle.eastmoney.mapper.FuncMapper;
import io.github.leaderman.makemoney.hustle.eastmoney.service.FundService;
import io.github.leaderman.makemoney.hustle.eastmoney.service.SecurityService;
import io.github.leaderman.makemoney.hustle.feishu.ImClient;
import io.github.leaderman.makemoney.hustle.lang.DatetimeUtil;
import io.github.leaderman.makemoney.hustle.lang.NumberUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FundServiceImpl extends ServiceImpl<FuncMapper, FundEntity> implements FundService {
  private final ConfigClient configClient;
  private final ImClient imClient;

  private final SecurityService securityService;

  // 价格新高群组。
  private String priceHighChat;
  // 价格新低群组。
  private String priceLowChat;

  @PostConstruct
  public void init() {
    this.priceHighChat = this.configClient.getString("feishu.chat.price.high");
    this.priceLowChat = this.configClient.getString("feishu.chat.price.low");
  }

  @Override
  @Transactional
  public void sync(List<FundModel> models) {
    // 获取代码列表。
    List<String> codes = models.stream().map(FundModel::getCode).collect(Collectors.toList());

    // 从数据库中获取代码对应的基金。
    // 注意：字典中只会包含数据库中存在的基金。
    Map<String, FundEntity> codeToExistingFundEntities = this.lambdaQuery().in(FundEntity::getCode, codes).list()
        .stream().collect(Collectors.toMap(FundEntity::getCode, Function.identity()));

    // 基金模型列表转换为基金实体列表。
    // 转换过程中，已存在于数据库中的基金会设置 ID。
    List<FundEntity> entities = models.stream().map(FundModel::to)
        .peek(entity -> {
          FundEntity existingEntity = codeToExistingFundEntities.get(entity.getCode());
          // 如果基金不存在于数据库中，则跳过。
          if (Objects.isNull(existingEntity)) {
            return;
          }

          /*
           * 价格新高：
           * 基金的新最高价大于 0，
           * 基金的旧最高价大于 0，
           * 基金的新最高价大于基金的旧最高价。
           */
          if (NumberUtil.greaterThan(entity.getHighPrice(), BigDecimal.ZERO)
              && NumberUtil.greaterThan(existingEntity.getHighPrice(), BigDecimal.ZERO)
              && NumberUtil.greaterThan(entity.getHighPrice(), existingEntity.getHighPrice())) {
            String title = String.format("价格新高 - %s", entity.getName());
            String content = String.format("最高价格：%s\\n", entity.getHighPrice());

            if (securityService.hasPosition(entity.getCode())) {
              title += "（持仓）";

              SecurityModel securityModel = securityService.get(entity.getCode());
              content += String.format("成本价格：%s\\n盈亏金额：%s\\n盈亏比例：%s\\n", securityModel.getCostPrice(),
                  securityModel.getPositionProfitLoss(), securityModel.getPositionProfitLossRatio());
            }

            content += String.format("日期时间：%s", DatetimeUtil.getDatetime());

            try {
              this.imClient.sendRedMessageByChatId(priceHighChat, title, content);
            } catch (Exception e) {
              log.error("发送价格新高消息错误：{}", ExceptionUtils.getStackTrace(e));
            }
          }

          /*
           * 价格新低：
           * 基金的新最低价大于 0，
           * 基金的旧最低价大于 0，
           * 基金的新最低价小于基金的旧最低价。
           */
          if (NumberUtil.greaterThan(entity.getLowPrice(), BigDecimal.ZERO)
              && NumberUtil.greaterThan(existingEntity.getLowPrice(), BigDecimal.ZERO)
              && NumberUtil.lessThan(entity.getLowPrice(), existingEntity.getLowPrice())) {
            String title = String.format("价格新低 - %s", entity.getName());
            String content = String.format("最低价格：%s\\n", entity.getLowPrice());

            if (securityService.hasPosition(entity.getCode())) {
              title += "（持仓）";

              SecurityModel securityModel = securityService.get(entity.getCode());
              content += String.format("成本价格：%s\\n盈亏金额：%s\\n盈亏比例：%s\\n", securityModel.getCostPrice(),
                  securityModel.getPositionProfitLoss(),
                  securityModel.getPositionProfitLossRatio());
            }

            content += String.format("日期时间：%s", DatetimeUtil.getDatetime());

            try {
              this.imClient.sendGreenMessageByChatId(priceLowChat, title, content);
            } catch (Exception e) {
              log.error("发送价格新低消息错误：{}", ExceptionUtils.getStackTrace(e));
            }
          }

          // 设置 ID。
          entity.setId(existingEntity.getId());
        })
        .collect(Collectors.toList());

    // 保存或更新。
    this.saveOrUpdateBatch(entities);
  }
}
