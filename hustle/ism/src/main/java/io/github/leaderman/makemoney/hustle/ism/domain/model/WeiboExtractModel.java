package io.github.leaderman.makemoney.hustle.ism.domain.model;

import lombok.Data;

@Data
public class WeiboExtractModel {
  // 源代码。
  private String html;
  // 链接。
  private String href;
  // 昵称。
  private String nickname;
  // 时间。
  private String datetime;
  // 正文内容。
  private String mainContent;
  // 转发内容。
  private String retweetContent;
}
