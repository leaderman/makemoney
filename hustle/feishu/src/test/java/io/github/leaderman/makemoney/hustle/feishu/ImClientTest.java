package io.github.leaderman.makemoney.hustle.feishu;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ImClientTest {
  @Autowired
  private ImClient imClient;

  @Test
  public void sendTextMessageByOpenId() throws Exception {
    String messageId = this.imClient.sendTextMessageByOpenId("ou_077685426241b03d9e864f775c0ae846", "Hello, world!");
    System.out.println(messageId);
  }

  @Test
  public void sendTextMessageByChatId() throws Exception {
    String messageId = this.imClient.sendTextMessageByChatId("oc_bc40aeadb8d45002314eca8c5f07e55a", "Hello, world!");
    System.out.println(messageId);
  }
}
