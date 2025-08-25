package io.github.leaderman.makemoney.domain.request;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class SyncPositionRequest {
  private BigDecimal totalAssets;
  private BigDecimal securitiesMarketValue;
  private BigDecimal availableFunds;
  private BigDecimal positionProfitLoss;
  private BigDecimal cashBalance;
  private BigDecimal withdrawableFunds;
  private BigDecimal dailyProfitLoss;
  private BigDecimal frozenFunds;

  @Data
  public static class Security {
    private String securityCode;
    private String securityName;
    private Integer holdingQuantity;
    private Integer availableQuantity;
    private BigDecimal costPrice;
    private BigDecimal currentPrice;
    private BigDecimal marketValue;
    private BigDecimal positionProfitLoss;
    private BigDecimal positionProfitLossRatio;
    private BigDecimal dailyProfitLoss;
    private BigDecimal dailyProfitLossRatio;
  }

  private List<Security> securities;
}
