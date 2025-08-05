package io.github.leaderman.makemoney.hustle.stock.domain.model;

import java.math.BigDecimal;
import java.util.List;

import io.github.leaderman.makemoney.hustle.domain.model.BaseModel;
import io.github.leaderman.makemoney.hustle.stock.domain.entity.StockTrendEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StockTrendModel extends BaseModel {
  private String code;
  private String trend;
  private BigDecimal slope;
  private List<BigDecimal> prices;

  public static StockTrendModel from(StockTrendEntity entity) {
    StockTrendModel model = new StockTrendModel();

    model.setId(entity.getId());
    model.setCode(entity.getCode());
    model.setTrend(entity.getTrend());
    model.setSlope(entity.getSlope());
    model.setPrices(entity.getPrices());
    model.setCreatedAt(entity.getCreatedAt());
    model.setUpdatedAt(entity.getUpdatedAt());

    return model;
  }
}
