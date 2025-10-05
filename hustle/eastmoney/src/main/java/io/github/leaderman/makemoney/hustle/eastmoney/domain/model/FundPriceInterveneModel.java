package io.github.leaderman.makemoney.hustle.eastmoney.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FundPriceInterveneModel {
  private String code;
  private boolean intervene;
  private String reason;
}
