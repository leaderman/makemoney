package io.github.leaderman.makemoney.hustle.eastmoney.domain.request;

import java.util.List;

import lombok.Data;

@Data
public class SyncFundRequest {
  @Data
  public static class Fund {
    private String code;
    private String name;
    private String openPrice;
    private String latestPrice;
    private String highPrice;
    private String lowPrice;
    private String changePercent;
    private String changeAmount;
    private String prevClose;
  }

  private List<Fund> funds;
}
