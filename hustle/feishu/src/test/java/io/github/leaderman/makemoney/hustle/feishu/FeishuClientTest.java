package io.github.leaderman.makemoney.hustle.feishu;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.google.gson.JsonParser;
import com.lark.oapi.Client;
import com.lark.oapi.core.utils.Jsons;
import com.lark.oapi.service.bitable.v1.model.SearchAppTableRecordReq;
import com.lark.oapi.service.bitable.v1.model.SearchAppTableRecordReqBody;
import com.lark.oapi.service.bitable.v1.model.SearchAppTableRecordResp;

@SpringBootTest
public class FeishuClientTest {
  @Autowired
  private FeishuClient feishuClient;

  @Test
  public void testGetClient() throws Exception {
    // 构建client
    Client client = feishuClient.getClient();

    // 创建请求对象
    SearchAppTableRecordReq req = SearchAppTableRecordReq.newBuilder()
        .appToken("XGG1blHrkangStsSV82cvhi1n9d")
        .tableId("tblnxanpg2zpK9qn").searchAppTableRecordReqBody(
            SearchAppTableRecordReqBody.newBuilder().viewId("vew8b4LnTk").build())
        .build();

    // 发起请求
    SearchAppTableRecordResp resp = client.bitable().v1().appTableRecord().search(req);

    // 处理服务端错误
    if (!resp.success()) {
      System.out.printf("code:%s,msg:%s,reqId:%s, resp:%s%n", resp.getCode(), resp.getMsg(),
          resp.getRequestId(), Jsons.createGSON(true, false).toJson(JsonParser.parseString(
              new String(resp.getRawResponse().getBody(), StandardCharsets.UTF_8))));
      return;
    }

    // 业务数据处理
    System.out.println(Jsons.DEFAULT.toJson(resp.getData()));
  }
}
