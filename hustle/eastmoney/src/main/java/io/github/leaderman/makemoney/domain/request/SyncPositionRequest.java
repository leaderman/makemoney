package io.github.leaderman.makemoney.domain.request;

import java.util.List;

import lombok.Data;

@Data
public class SyncPositionRequest {
  private String totalAssets;
  private String securitiesMarketValue;
  private String availableFunds;
  private String positionProfitLoss;
  private String cashBalance;
  private String withdrawableFunds;
  private String dailyProfitLoss;
  private String frozenFunds;

  @Data
  public static class Security {
    private String securityCode;
    private String securityName;
    private String holdingQuantity;
    private String availableQuantity;
    private String costPrice;
    private String currentPrice;
    private String marketValue;
    private String positionProfitLoss;
    private String positionProfitLossRatio;
    private String dailyProfitLoss;
    private String dailyProfitLossRatio;
  }

  private List<Security> securities;
}
