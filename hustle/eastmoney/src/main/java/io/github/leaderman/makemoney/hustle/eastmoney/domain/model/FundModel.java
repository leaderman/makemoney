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
  private String code;
  private String name;
  private BigDecimal openPrice;
  private BigDecimal latestPrice;
  private BigDecimal highPrice;
  private BigDecimal lowPrice;
  private BigDecimal changePercent;
  private BigDecimal changeAmount;
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
}
