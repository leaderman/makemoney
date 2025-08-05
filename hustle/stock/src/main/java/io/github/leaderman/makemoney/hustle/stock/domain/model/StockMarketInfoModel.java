package io.github.leaderman.makemoney.hustle.stock.domain.model;

import java.math.BigDecimal;

import io.github.leaderman.makemoney.hustle.domain.model.BaseModel;
import io.github.leaderman.makemoney.hustle.stock.domain.entity.StockMarketInfoEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StockMarketInfoModel extends BaseModel {
  private String code;
  private String day;
  private BigDecimal amount;
  private BigDecimal change;
  private BigDecimal close;
  private String currencyCode;
  private String currencyName;
  private BigDecimal high;
  private String jumpLink;
  private BigDecimal low;
  private BigDecimal open;
  private BigDecimal percent;
  private BigDecimal preClose;
  private BigDecimal price;
  private BigDecimal volume;

  public static StockMarketInfoModel from(StockMarketInfoEntity entity) {
    StockMarketInfoModel model = new StockMarketInfoModel();

    model.setId(entity.getId());
    model.setCode(entity.getCode());
    model.setDay(entity.getDay());
    model.setAmount(entity.getAmount());
    model.setChange(entity.getChange());
    model.setClose(entity.getClose());
    model.setCurrencyCode(entity.getCurrencyCode());
    model.setCurrencyName(entity.getCurrencyName());
    model.setHigh(entity.getHigh());
    model.setJumpLink(entity.getJumpLink());
    model.setLow(entity.getLow());
    model.setOpen(entity.getOpen());
    model.setPercent(entity.getPercent());
    model.setPreClose(entity.getPreClose());
    model.setPrice(entity.getPrice());
    model.setVolume(entity.getVolume());
    model.setCreatedAt(entity.getCreatedAt());
    model.setUpdatedAt(entity.getUpdatedAt());

    return model;
  }

  public static StockMarketInfoEntity to(StockMarketInfoModel model) {
    StockMarketInfoEntity entity = new StockMarketInfoEntity();

    entity.setId(model.getId());
    entity.setCode(model.getCode());
    entity.setDay(model.getDay());
    entity.setAmount(model.getAmount());
    entity.setChange(model.getChange());
    entity.setClose(model.getClose());
    entity.setCurrencyCode(model.getCurrencyCode());
    entity.setCurrencyName(model.getCurrencyName());
    entity.setHigh(model.getHigh());
    entity.setJumpLink(model.getJumpLink());
    entity.setLow(model.getLow());
    entity.setOpen(model.getOpen());
    entity.setPercent(model.getPercent());
    entity.setPreClose(model.getPreClose());
    entity.setPrice(model.getPrice());
    entity.setVolume(model.getVolume());

    return entity;
  }
}
