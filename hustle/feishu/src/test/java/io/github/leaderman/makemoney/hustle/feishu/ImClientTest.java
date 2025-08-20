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

  @Test
  public void sendDebugMessageByOpenId() throws Exception {
    String messageId = this.imClient.sendDebugMessageByOpenId("ou_077685426241b03d9e864f775c0ae846", "调试标题", "调试内容");
    System.out.println(messageId);
  }

  @Test
  public void sendInfoMessageByOpenId() throws Exception {
    String messageId = this.imClient.sendInfoMessageByOpenId("ou_077685426241b03d9e864f775c0ae846", "信息标题", "信息内容");
    System.out.println(messageId);
  }

  @Test
  public void sendWarnMessageByOpenId() throws Exception {
    String messageId = this.imClient.sendWarnMessageByOpenId("ou_077685426241b03d9e864f775c0ae846", "警告标题", "警告内容");
    System.out.println(messageId);
  }

  @Test
  public void sendErrorMessageByOpenId() throws Exception {
    String messageId = this.imClient.sendErrorMessageByOpenId("ou_077685426241b03d9e864f775c0ae846", "错误标题", "错误内容");
    System.out.println(messageId);
  }
}
