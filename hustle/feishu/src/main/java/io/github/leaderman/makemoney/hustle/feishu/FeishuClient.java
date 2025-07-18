package io.github.leaderman.makemoney.hustle.feishu;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.lark.oapi.Client;

@Component
public class FeishuClient {
  private final Client client;

  public FeishuClient(@Value("${feishu.appId}") String appId, @Value("${feishu.appSecret}") String appSecret) {
    this.client = Client.newBuilder(appId, appSecret).build();
  }

  public Client getClient() {
    return this.client;
  }
}
