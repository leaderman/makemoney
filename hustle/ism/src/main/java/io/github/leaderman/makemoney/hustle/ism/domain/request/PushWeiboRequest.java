package io.github.leaderman.makemoney.hustle.ism.domain.request;

import lombok.Data;

@Data
public class PushWeiboRequest {
  private String href;
  private String html;
}
