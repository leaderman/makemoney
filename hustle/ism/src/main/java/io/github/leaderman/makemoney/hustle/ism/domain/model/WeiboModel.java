package io.github.leaderman.makemoney.hustle.ism.domain.model;

import io.github.leaderman.makemoney.hustle.domain.model.BaseModel;
import io.github.leaderman.makemoney.hustle.ism.domain.request.PushWeiboRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class WeiboModel extends BaseModel {
  private String href;
  private String html;

  public static WeiboModel from(PushWeiboRequest request) {
    WeiboModel model = new WeiboModel();

    model.setHref(request.getHref());
    model.setHtml(request.getHtml());

    return model;
  }
}
