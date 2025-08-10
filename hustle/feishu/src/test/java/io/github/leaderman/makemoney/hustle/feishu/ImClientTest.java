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

  @Test
  public void sendInteractiveMessage() throws Exception {
    String json = """
        {
            "schema": "2.0",
            "config": {
                "update_multi": true,
                "style": {
                    "text_size": {
                        "normal_v2": {
                            "default": "normal",
                            "pc": "normal",
                            "mobile": "heading"
                        }
                    }
                }
            },
            "body": {
                "direction": "vertical",
                "padding": "12px 12px 12px 12px",
                "elements": [
                    {
                        "tag": "div",
                        "text": {
                            "tag": "plain_text",
                            "content": "Hello, world!",
                            "text_size": "normal_v2",
                            "text_align": "left",
                            "text_color": "default"
                        },
                        "margin": "0px 0px 0px 0px"
                    }
                ]
            },
            "header": {
                "title": {
                    "tag": "plain_text",
                    "content": "文本标题"
                },
                "subtitle": {
                    "tag": "plain_text",
                    "content": ""
                },
                "template": "blue",
                "padding": "12px 12px 12px 12px"
            }
        }
        """;
    String messageId = this.imClient.sendInteractiveMessageByOpenId("ou_077685426241b03d9e864f775c0ae846", json);
    System.out.println(messageId);

    messageId = this.imClient.sendInteractiveMessageByChatId("oc_bc40aeadb8d45002314eca8c5f07e55a", json);
    System.out.println(messageId);
  }
}
