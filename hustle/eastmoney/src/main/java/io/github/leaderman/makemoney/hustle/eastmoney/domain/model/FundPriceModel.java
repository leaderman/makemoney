package io.github.leaderman.makemoney.hustle.eastmoney.domain.model;

import java.math.BigDecimal;

import io.github.leaderman.makemoney.hustle.domain.model.BaseModel;
import io.github.leaderman.makemoney.hustle.eastmoney.domain.entity.FundPriceEntity;
import io.github.leaderman.makemoney.hustle.eastmoney.domain.request.SyncFundRequest;
import io.github.leaderman.makemoney.hustle.lang.NumberUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FundPriceModel extends BaseModel {
  // 代码。
  private String code;
  // 价格。
  private BigDecimal price;

  public static FundPriceModel from(SyncFundRequest.Fund fund) {
    FundPriceModel model = new FundPriceModel();

    model.setCode(fund.getCode());
    model.setPrice(NumberUtil.toBigDecimal(fund.getLatestPrice(), BigDecimal.ZERO));

    return model;
  }

  public static FundPriceEntity to(FundPriceModel model) {
    FundPriceEntity entity = new FundPriceEntity();

    entity.setCode(model.getCode());
    entity.setPrice(model.getPrice());

    return entity;
  }
}
