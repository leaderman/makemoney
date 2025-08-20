package io.github.leaderman.makemoney.hustle.feishu;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lark.oapi.service.im.v1.model.CreateMessageReq;
import com.lark.oapi.service.im.v1.model.CreateMessageReqBody;
import com.lark.oapi.service.im.v1.model.CreateMessageResp;

import io.github.leaderman.makemoney.hustle.limiter.LimiterClient;
import lombok.RequiredArgsConstructor;

/**
 * 消息客户端。
 */
@Component
@RequiredArgsConstructor
public class ImClient {
  private final LimiterClient limiterClient;
  private final FeishuClient feishuClient;
  private final ObjectMapper objectMapper;

  /**
   * 发送消息。
   * 
   * @param receiveIdType 用户 ID 类型。
   * @param receiveId     消息接收者的 ID。
   * @param msgType       消息类型。
   * @param content       消息内容。
   * @return 消息ID。
   * @throws Exception
   */
  public String createMessage(String receiveIdType, String receiveId, String msgType, String content) throws Exception {
    CreateMessageReq req = CreateMessageReq.newBuilder()
        .receiveIdType(receiveIdType)
        .createMessageReqBody(CreateMessageReqBody.newBuilder()
            .receiveId(receiveId)
            .msgType(msgType)
            .content(content)
            .build())
        .build();

    this.limiterClient.acquire("feishu.im.createMessage.second", 50, 1);
    this.limiterClient.acquire("feishu.im.createMessage.minute", 1000, 60);

    CreateMessageResp resp = this.feishuClient.getClient().im().v1().message().create(req);
    if (!resp.success()) {
      throw new Exception(String.format("错误码：%s，错误描述：%s", resp.getCode(), resp.getMsg()));
    }

    return resp.getData().getMessageId();
  }

  /**
   * 发送文本消息。
   * 
   * @param openId 用户 ID。
   * @param text   文本内容。
   * @return 消息 ID。
   * @throws Exception
   */
  public String sendTextMessageByOpenId(String openId, String text) throws Exception {
    return this.createMessage("open_id", openId, "text", this.objectMapper.writeValueAsString(Map.of("text", text)));
  }

  /**
   * 发送消息卡片。
   * 
   * @param openId 用户 ID。
   * @param json   消息卡片 JSON。
   * @return 消息 ID。
   * @throws Exception
   */
  public String sendInteractiveMessageByOpenId(String openId, String json) throws Exception {
    return this.createMessage("open_id", openId, "interactive", json);
  }

  /**
   * 发送文本消息。
   * 
   * @param chatId 群 ID。
   * @param text   文本内容。
   * @return 消息 ID。
   * @throws Exception
   */
  public String sendTextMessageByChatId(String chatId, String text) throws Exception {
    return this.createMessage("chat_id", chatId, "text", this.objectMapper.writeValueAsString(Map.of("text", text)));
  }

  private String buildLogMessage(String title, String content, String color) {
    String template = """
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
                            "content": "%s",
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
                    "content": "%s"
                },
                "subtitle": {
                    "tag": "plain_text",
                    "content": ""
                },
                "template": "%s",
                "padding": "12px 12px 12px 12px"
            }
        }
        """;

    return String.format(template, content, title, color);
  }

  /**
   * 发送调试消息。
   * 
   * @param openId  用户 ID。
   * @param title   标题。
   * @param content 内容。
   * @return 消息 ID。
   * @throws Exception
   */
  public String sendDebugMessageByOpenId(String openId, String title, String content) throws Exception {
    return this.sendInteractiveMessageByOpenId(openId, this.buildLogMessage(title, content, "blue"));
  }

  /**
   * 发送信息消息。
   * 
   * @param openId  用户 ID。
   * @param title   标题。
   * @param content 内容。
   * @return 消息 ID。
   * @throws Exception
   */
  public String sendInfoMessageByOpenId(String openId, String title, String content) throws Exception {
    return this.sendInteractiveMessageByOpenId(openId, this.buildLogMessage(title, content, "green"));
  }

  /**
   * 发送警告消息。
   * 
   * @param openId  用户 ID。
   * @param title   标题。
   * @param content 内容。
   * @return 消息 ID。
   * @throws Exception
   */
  public String sendWarnMessageByOpenId(String openId, String title, String content) throws Exception {
    return this.sendInteractiveMessageByOpenId(openId, this.buildLogMessage(title, content, "yellow"));
  }

  /**
   * 发送错误消息。
   * 
   * @param openId  用户 ID。
   * @param title   标题。
   * @param content 内容。
   * @return 消息 ID。
   * @throws Exception
   */
  public String sendErrorMessageByOpenId(String openId, String title, String content) throws Exception {
    return this.sendInteractiveMessageByOpenId(openId, this.buildLogMessage(title, content, "red"));
  }

  /**
   * 发送消息卡片。
   * 
   * @param chatId 群 ID。
   * @param json   消息卡片 JSON。
   * @return 消息 ID。
   * @throws Exception
   */
  public String sendInteractiveMessageByChatId(String chatId, String json) throws Exception {
    return this.createMessage("chat_id", chatId, "interactive", json);
  }
}
