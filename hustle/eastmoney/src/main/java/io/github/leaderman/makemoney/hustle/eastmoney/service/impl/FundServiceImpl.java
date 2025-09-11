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

  private String positionPriceHighChat;
  private String positionPriceLowChat;

  private String priceHighChat;
  private String priceLowChat;

  @PostConstruct
  public void init() {
    this.positionPriceHighChat = this.configClient.getString("feishu.chat.position.price.high");
    this.positionPriceLowChat = this.configClient.getString("feishu.chat.position.price.low");

    this.priceHighChat = this.configClient.getString("feishu.chat.price.high");
    this.priceLowChat = this.configClient.getString("feishu.chat.price.low");
  }

  @Override
  @Transactional
  public void sync(List<FundModel> models) {
    // 获取代码列表。
    List<String> codes = models.stream().map(FundModel::getCode).collect(Collectors.toList());

    // 获取代码对应的 ID。
    Map<String, FundEntity> codeToEntities = this.lambdaQuery().in(FundEntity::getCode, codes).list().stream()
        .collect(Collectors.toMap(FundEntity::getCode, Function.identity()));

    // 转换为实体，且设置 ID。
    List<FundEntity> entities = models.stream().map(FundModel::to)
        .peek(entity -> {
          FundEntity existingEntity = codeToEntities.get(entity.getCode());
          if (Objects.isNull(existingEntity)) {
            return;
          }

          // 价格新高。
          if (NumberUtil.greaterThan(entity.getHighPrice(), BigDecimal.ZERO)
              && NumberUtil.greaterThan(existingEntity.getHighPrice(), BigDecimal.ZERO)
              && NumberUtil.greaterThan(entity.getHighPrice(), existingEntity.getHighPrice())) {
            String title = String.format("【价格新高】%s", entity.getName());
            String content = String.format("最高价格：%s\\n最新价格：%s\\n日期时间：%s", entity.getHighPrice(),
                entity.getLatestPrice(), DatetimeUtil.getDatetime());

            try {
              this.imClient.sendRedMessageByChatId(
                  securityService.hasPosition(entity.getCode()) ? positionPriceHighChat : priceHighChat, title,
                  content);
            } catch (Exception e) {
              log.error("发送价格新高消息错误：{}", ExceptionUtils.getStackTrace(e));
            }
          }

          // 价格新低。
          if (NumberUtil.greaterThan(entity.getLowPrice(), BigDecimal.ZERO)
              && NumberUtil.greaterThan(existingEntity.getLowPrice(), BigDecimal.ZERO)
              && NumberUtil.lessThan(entity.getLowPrice(), existingEntity.getLowPrice())) {
            String title = String.format("【价格新低】%s", entity.getName());
            String content = String.format("最低价格：%s\\n最新价格：%s\\n日期时间：%s", entity.getLowPrice(), entity.getLatestPrice(),
                DatetimeUtil.getDatetime());

            try {
              this.imClient.sendGreenMessageByChatId(
                  securityService.hasPosition(entity.getCode()) ? positionPriceLowChat : priceLowChat, title, content);
            } catch (Exception e) {
              log.error("发送价格新低消息错误：{}", ExceptionUtils.getStackTrace(e));
            }
          }

          entity.setId(existingEntity.getId());
        })
        .collect(Collectors.toList());

    // 保存或更新。
    this.saveOrUpdateBatch(entities);
  }
}
