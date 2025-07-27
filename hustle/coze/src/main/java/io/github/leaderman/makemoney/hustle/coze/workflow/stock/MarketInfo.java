package io.github.leaderman.makemoney.hustle.coze.workflow.stock;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class MarketInfo {
  private String amount;
  private String change;
  private String close;
  private String currencyCode;
  private String currencyName;
  private String day;
  private String high;
  private String jumpLink;
  private String low;
  private String name;
  private String open;
  private String percent;
  private String preClose;
  private String price;
  private String symbol;
  private String volume;

  public BigDecimal getAmountDecimal() {
    if (this.amount.endsWith("亿")) {
      return new BigDecimal(this.amount.replace("亿", "")).multiply(BigDecimal.valueOf(100000000));
    } else if (this.amount.endsWith("万")) {
      return new BigDecimal(this.amount.replace("万", "")).multiply(BigDecimal.valueOf(10000));
    }

    return new BigDecimal(this.amount);
  }

  public BigDecimal getChangeDecimal() {
    return new BigDecimal(this.change);
  }

  public BigDecimal getCloseDecimal() {
    return new BigDecimal(this.close);
  }

  public BigDecimal getHighDecimal() {
    return new BigDecimal(this.high);
  }

  public BigDecimal getLowDecimal() {
    return new BigDecimal(this.low);
  }

  public BigDecimal getOpenDecimal() {
    return new BigDecimal(this.open);
  }

  public BigDecimal getPercentDecimal() {
    return new BigDecimal(this.percent.replace("%", ""));
  }

  public BigDecimal getPreCloseDecimal() {
    return new BigDecimal(this.preClose);
  }

  public BigDecimal getPriceDecimal() {
    return new BigDecimal(this.price);
  }

  public BigDecimal getVolumeDecimal() {
    if (this.volume.endsWith("万")) {
      return new BigDecimal(this.volume.replace("万", "")).multiply(BigDecimal.valueOf(10000));
    }

    return new BigDecimal(this.volume);
  }
}
