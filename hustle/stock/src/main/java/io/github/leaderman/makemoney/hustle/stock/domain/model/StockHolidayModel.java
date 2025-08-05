package io.github.leaderman.makemoney.hustle.stock.domain.model;

import io.github.leaderman.makemoney.hustle.domain.model.BaseModel;
import io.github.leaderman.makemoney.hustle.stock.domain.entity.StockHolidayEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class StockHolidayModel extends BaseModel {
  private String market;
  private String holiday;

  public static StockHolidayModel from(StockHolidayEntity entity) {
    StockHolidayModel model = new StockHolidayModel();

    model.setId(entity.getId());
    model.setMarket(entity.getMarket());
    model.setHoliday(entity.getHoliday());
    model.setCreatedAt(entity.getCreatedAt());
    model.setUpdatedAt(entity.getUpdatedAt());

    return model;
  }
}
