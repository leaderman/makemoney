package io.github.leaderman.makemoney.hustle.feishu;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.lark.oapi.Client;

import lombok.Getter;

/**
 * 飞书客户端。
 */
@Component
@Getter
public class FeishuClient {
  private final Client client;

  public FeishuClient(@Value("${feishu.appId}") String appId, @Value("${feishu.appSecret}") String appSecret) {
    this.client = Client.newBuilder(appId, appSecret).build();
  }
}
