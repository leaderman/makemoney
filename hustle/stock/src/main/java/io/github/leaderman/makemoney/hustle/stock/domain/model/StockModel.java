package io.github.leaderman.makemoney.hustle.stock.domain.model;

import io.github.leaderman.makemoney.hustle.domain.model.BaseModel;
import io.github.leaderman.makemoney.hustle.stock.domain.entity.StockEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StockModel extends BaseModel {
  private String market;
  private String code;
  private String name;

  public static StockModel from(StockEntity entity) {
    StockModel model = new StockModel();

    model.setId(entity.getId());
    model.setMarket(entity.getMarket());
    model.setCode(entity.getCode());
    model.setName(entity.getName());
    model.setCreatedAt(entity.getCreatedAt());
    model.setUpdatedAt(entity.getUpdatedAt());

    return model;
  }

  public static StockEntity to(StockModel model) {
    StockEntity entity = new StockEntity();

    entity.setId(model.getId());
    entity.setMarket(model.getMarket());
    entity.setCode(model.getCode());
    entity.setName(model.getName());

    return entity;
  }
}
