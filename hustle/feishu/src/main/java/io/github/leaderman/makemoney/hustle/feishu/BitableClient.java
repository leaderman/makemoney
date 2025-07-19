package io.github.leaderman.makemoney.hustle.feishu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.lark.oapi.service.bitable.v1.model.AppTable;
import com.lark.oapi.service.bitable.v1.model.ListAppTableReq;
import com.lark.oapi.service.bitable.v1.model.ListAppTableResp;
import com.lark.oapi.service.bitable.v1.model.ListAppTableRespBody;

import io.github.leaderman.makemoney.hustle.limiter.LimiterClient;
import lombok.RequiredArgsConstructor;

/**
 * 多维表格客户端。
 */
@Component
@RequiredArgsConstructor
public class BitableClient {
  private final LimiterClient limiterClient;
  private final FeishuClient feishuClient;

  /**
   * 列出多维表格的所有数据表。
   * 
   * @param appToken 多维表格的唯一标识。
   * @return 多维表格的所有数据表。
   * @throws Exception
   */
  public List<AppTable> listTables(String appToken) throws Exception {
    List<AppTable> tables = new ArrayList<>();

    boolean hasMore = true;
    String pageToken = null;

    while (hasMore) {
      ListAppTableReq req = ListAppTableReq.newBuilder()
          .appToken(appToken)
          .pageToken(pageToken)
          .pageSize(1)
          .build();

      limiterClient.acquire("feishu.bitable.listTables", 20, 60);

      ListAppTableResp resp = feishuClient.getClient().bitable().v1().appTable().list(req);
      if (!resp.success()) {
        throw new Exception(String.format("错误码：%s，错误描述：%s", resp.getCode(), resp.getMsg()));
      }

      ListAppTableRespBody data = resp.getData();
      tables.addAll(Arrays.asList(data.getItems()));

      hasMore = data.getHasMore();
      pageToken = data.getPageToken();
    }

    return tables;
  }
}
