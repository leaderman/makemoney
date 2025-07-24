package io.github.leaderman.makemoney.hustle.coze.workflow.stock;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class StockData {
  @JsonProperty("base_info")
  private BaseInfo baseInfo;
  @JsonProperty("finance_info")
  private FinanceInfo financeInfo;
  @JsonProperty("market_info")
  private MarketInfo marketInfo;
}
