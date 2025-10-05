package io.github.leaderman.makemoney.hustle.eastmoney.domain.model;

import java.math.BigDecimal;

import io.github.leaderman.makemoney.hustle.domain.model.BaseModel;
import io.github.leaderman.makemoney.hustle.eastmoney.domain.entity.FundEntity;
import io.github.leaderman.makemoney.hustle.eastmoney.domain.request.SyncFundRequest;
import io.github.leaderman.makemoney.hustle.lang.NumberUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FundModel extends BaseModel {
  // 代码。
  private String code;
  // 名称。
  private String name;
  // 开盘价。
  private BigDecimal openPrice;
  // 最新价。
  private BigDecimal latestPrice;
  // 最高价。
  private BigDecimal highPrice;
  // 最低价。
  private BigDecimal lowPrice;
  // 涨跌幅。
  private BigDecimal changePercent;
  // 涨跌额。
  private BigDecimal changeAmount;
  // 昨收价。
  private BigDecimal prevClose;

  public static FundModel from(SyncFundRequest.Fund fund) {
    FundModel model = new FundModel();

    model.setCode(fund.getCode());
    model.setName(fund.getName());
    model.setOpenPrice(NumberUtil.toBigDecimal(fund.getOpenPrice(), BigDecimal.ZERO));
    model.setLatestPrice(NumberUtil.toBigDecimal(fund.getLatestPrice(), BigDecimal.ZERO));
    model.setHighPrice(NumberUtil.toBigDecimal(fund.getHighPrice(), BigDecimal.ZERO));
    model.setLowPrice(NumberUtil.toBigDecimal(fund.getLowPrice(), BigDecimal.ZERO));

    String changePercent = fund.getChangePercent();
    // 如果涨跌幅以 % 结尾，则去掉 %。
    if (changePercent.endsWith("%")) {
      changePercent = changePercent.substring(0, changePercent.length() - 1);
    }
    model.setChangePercent(NumberUtil.toBigDecimal(changePercent, BigDecimal.ZERO));

    model.setChangeAmount(NumberUtil.toBigDecimal(fund.getChangeAmount(), BigDecimal.ZERO));
    model.setPrevClose(NumberUtil.toBigDecimal(fund.getPrevClose(), BigDecimal.ZERO));

    return model;
  }

  public static FundEntity to(FundModel model) {
    FundEntity entity = new FundEntity();

    entity.setCode(model.getCode());
    entity.setName(model.getName());
    entity.setOpenPrice(model.getOpenPrice());
    entity.setLatestPrice(model.getLatestPrice());
    entity.setHighPrice(model.getHighPrice());
    entity.setLowPrice(model.getLowPrice());
    entity.setChangePercent(model.getChangePercent());
    entity.setChangeAmount(model.getChangeAmount());
    entity.setPrevClose(model.getPrevClose());

    return entity;
  }

  public static FundModel from(FundEntity entity) {
    FundModel model = new FundModel();

    model.setId(entity.getId());
    model.setCode(entity.getCode());
    model.setName(entity.getName());
    model.setOpenPrice(entity.getOpenPrice());
    model.setLatestPrice(entity.getLatestPrice());
    model.setHighPrice(entity.getHighPrice());
    model.setLowPrice(entity.getLowPrice());
    model.setChangePercent(entity.getChangePercent());
    model.setChangeAmount(entity.getChangeAmount());
    model.setPrevClose(entity.getPrevClose());
    model.setCreatedAt(entity.getCreatedAt());
    model.setUpdatedAt(entity.getUpdatedAt());

    return model;
  }
}
