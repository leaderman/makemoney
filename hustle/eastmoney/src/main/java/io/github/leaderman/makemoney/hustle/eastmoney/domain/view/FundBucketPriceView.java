package io.github.leaderman.makemoney.hustle.eastmoney.domain.view;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class FundBucketPriceView {
  private String code;
  private BigDecimal price;
  private LocalDateTime bucketedAt;
}
