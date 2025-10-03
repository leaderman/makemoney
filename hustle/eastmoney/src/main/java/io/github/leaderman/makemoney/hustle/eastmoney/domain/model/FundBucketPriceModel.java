package io.github.leaderman.makemoney.hustle.eastmoney.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import io.github.leaderman.makemoney.hustle.eastmoney.domain.view.FundBucketPriceView;
import lombok.Data;

@Data
public class FundBucketPriceModel {
  private String code;
  private BigDecimal price;
  private LocalDateTime bucketedAt;

  public static FundBucketPriceModel from(FundBucketPriceView view) {
    FundBucketPriceModel model = new FundBucketPriceModel();

    model.setCode(view.getCode());
    model.setPrice(view.getPrice());
    model.setBucketedAt(view.getBucketedAt());

    return model;
  }
}
