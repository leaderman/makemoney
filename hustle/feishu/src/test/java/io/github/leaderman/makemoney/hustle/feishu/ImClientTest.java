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

  @Test
  public void sendBlueMessageByOpenId() throws Exception {
    String messageId = this.imClient.sendBlueMessageByOpenId("ou_077685426241b03d9e864f775c0ae846", "蓝色标题", "蓝色内容");
    System.out.println(messageId);
  }

  @Test
  public void sendGreenMessageByOpenId() throws Exception {
    String messageId = this.imClient.sendGreenMessageByOpenId("ou_077685426241b03d9e864f775c0ae846", "绿色标题", "绿色内容");
    System.out.println(messageId);
  }

  @Test
  public void sendYellowMessageByOpenId() throws Exception {
    String messageId = this.imClient.sendYellowMessageByOpenId("ou_077685426241b03d9e864f775c0ae846", "黄色标题", "黄色内容");
    System.out.println(messageId);
  }

  @Test
  public void sendRedMessageByOpenId() throws Exception {
    String messageId = this.imClient.sendRedMessageByOpenId("ou_077685426241b03d9e864f775c0ae846", "红色标题", "红色内容");
    System.out.println(messageId);
  }

  @Test
  public void sendDebugMessageByChatId() throws Exception {
    String messageId = this.imClient.sendDebugMessageByChatId("oc_50f45dfbad5a4681fe78bc3fc84ebb41", "调试标题", "调试内容");
    System.out.println(messageId);
  }

  @Test
  public void sendInfoMessageByChatId() throws Exception {
    String messageId = this.imClient.sendInfoMessageByChatId("oc_50f45dfbad5a4681fe78bc3fc84ebb41", "信息标题", "信息内容");
    System.out.println(messageId);
  }

  @Test
  public void sendWarnMessageByChatId() throws Exception {
    String messageId = this.imClient.sendWarnMessageByChatId("oc_50f45dfbad5a4681fe78bc3fc84ebb41", "警告标题", "警告内容");
    System.out.println(messageId);
  }

  @Test
  public void sendErrorMessageByChatId() throws Exception {
    String messageId = this.imClient.sendErrorMessageByChatId("oc_50f45dfbad5a4681fe78bc3fc84ebb41", "错误标题", "错误内容");
    System.out.println(messageId);
  }

  @Test
  public void sendBlueMessageByChatId() throws Exception {
    String messageId = this.imClient.sendBlueMessageByChatId("oc_50f45dfbad5a4681fe78bc3fc84ebb41", "蓝色标题", "蓝色内容");
    System.out.println(messageId);
  }

  @Test
  public void sendGreenMessageByChatId() throws Exception {
    String messageId = this.imClient.sendGreenMessageByChatId("oc_50f45dfbad5a4681fe78bc3fc84ebb41", "绿色标题", "绿色内容");
    System.out.println(messageId);
  }

  @Test
  public void sendYellowMessageByChatId() throws Exception {
    String messageId = this.imClient.sendYellowMessageByChatId("oc_50f45dfbad5a4681fe78bc3fc84ebb41", "黄色标题", "黄色内容");
    System.out.println(messageId);
  }

  @Test
  public void sendRedMessageByChatId() throws Exception {
    String messageId = this.imClient.sendRedMessageByChatId("oc_50f45dfbad5a4681fe78bc3fc84ebb41", "红色标题", "红色内容");
    System.out.println(messageId);
  }
}
