package io.github.leaderman.makemoney.hustle.coze.workflow.stock;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class StockDataContainer {
  @JsonProperty("stock_data")
  private StockData stockData;
}
