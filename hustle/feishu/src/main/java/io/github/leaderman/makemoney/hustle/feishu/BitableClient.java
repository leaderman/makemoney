package io.github.leaderman.makemoney.hustle.feishu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;

import com.lark.oapi.service.bitable.v1.model.AppTable;
import com.lark.oapi.service.bitable.v1.model.AppTableRecord;
import com.lark.oapi.service.bitable.v1.model.AppTableView;
import com.lark.oapi.service.bitable.v1.model.BatchGetAppTableRecordReq;
import com.lark.oapi.service.bitable.v1.model.BatchGetAppTableRecordReqBody;
import com.lark.oapi.service.bitable.v1.model.BatchGetAppTableRecordResp;
import com.lark.oapi.service.bitable.v1.model.CreateAppTableRecordReq;
import com.lark.oapi.service.bitable.v1.model.CreateAppTableRecordResp;
import com.lark.oapi.service.bitable.v1.model.ListAppTableRecordReq;
import com.lark.oapi.service.bitable.v1.model.ListAppTableRecordResp;
import com.lark.oapi.service.bitable.v1.model.ListAppTableRecordRespBody;
import com.lark.oapi.service.bitable.v1.model.ListAppTableReq;
import com.lark.oapi.service.bitable.v1.model.ListAppTableResp;
import com.lark.oapi.service.bitable.v1.model.ListAppTableRespBody;
import com.lark.oapi.service.bitable.v1.model.ListAppTableViewReq;
import com.lark.oapi.service.bitable.v1.model.ListAppTableViewResp;
import com.lark.oapi.service.bitable.v1.model.ListAppTableViewRespBody;

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
   * @return 所有数据表。
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

  /**
   * 列出多维表格的数据表的所有视图。
   * 
   * @param appToken 多维表格的唯一标识。
   * @param tableId  数据表的唯一标识。
   * @return 所有视图。
   * @throws Exception
   */
  public List<AppTableView> listViews(String appToken, String tableId) throws Exception {
    List<AppTableView> views = new ArrayList<>();

    boolean hasMore = true;
    String pageToken = null;

    while (hasMore) {
      ListAppTableViewReq req = ListAppTableViewReq.newBuilder()
          .appToken(appToken)
          .tableId(tableId)
          .pageToken(pageToken)
          .build();

      limiterClient.acquire("feishu.bitable.listViews", 20, 60);

      ListAppTableViewResp resp = feishuClient.getClient().bitable().v1().appTableView().list(req);
      if (!resp.success()) {
        throw new Exception(String.format("错误码：%s，错误描述：%s", resp.getCode(), resp.getMsg()));
      }

      ListAppTableViewRespBody data = resp.getData();
      views.addAll(Arrays.asList(data.getItems()));

      hasMore = data.getHasMore();
      pageToken = data.getPageToken();
    }

    return views;
  }

  /**
   * 列出多维表格的所有数据表的所有记录。
   * 
   * @param appToken 多维表格的唯一标识。
   * @return 所有记录。
   * @throws Exception
   */
  public List<AppTableRecord> listRecords(String appToken) throws Exception {
    List<AppTable> tables = listTables(appToken);
    if (CollectionUtils.isEmpty(tables)) {
      return Collections.emptyList();
    }

    List<AppTableRecord> records = new ArrayList<>();

    for (AppTable table : tables) {
      boolean hasMore = true;
      String pageToken = null;

      while (hasMore) {
        ListAppTableRecordReq req = ListAppTableRecordReq.newBuilder()
            .appToken(appToken)
            .tableId(table.getTableId())
            .pageToken(pageToken)
            .build();

        limiterClient.acquire("feishu.bitable.listRecords", 20, 60);

        ListAppTableRecordResp resp = feishuClient.getClient().bitable().v1().appTableRecord().list(req);
        if (!resp.success()) {
          throw new Exception(String.format("错误码：%s，错误描述：%s", resp.getCode(), resp.getMsg()));
        }

        ListAppTableRecordRespBody data = resp.getData();
        records.addAll(Arrays.asList(data.getItems()));

        hasMore = data.getHasMore();
        pageToken = data.getPageToken();
      }
    }

    return records;
  }

  /**
   * 创建多维表格的记录。
   * 
   * @param appToken 多维表格的唯一标识。
   * @param tableId  数据表的唯一标识。
   * @param fields   记录的字段。
   * @return 创建的记录。
   * @throws Exception
   */
  public AppTableRecord createRecord(String appToken, String tableId, Map<String, Object> fields) throws Exception {
    CreateAppTableRecordReq req = CreateAppTableRecordReq.newBuilder()
        .appToken(appToken)
        .tableId(tableId)
        .appTableRecord(AppTableRecord.newBuilder().fields(fields).build())
        .build();

    limiterClient.acquire("feishu.bitable.createRecord", 50, 60);

    CreateAppTableRecordResp resp = feishuClient.getClient().bitable().v1().appTableRecord().create(req);
    if (!resp.success()) {
      throw new Exception(String.format("错误码：%s，错误描述：%s", resp.getCode(), resp.getMsg()));
    }

    return resp.getData().getRecord();
  }

  /**
   * 获取多维表格的记录。
   * 
   * @param appToken 多维表格的唯一标识。
   * @param tableId  数据表的唯一标识。
   * @param recordId 记录的唯一标识。
   * @return 获取的记录。
   * @throws Exception
   */
  public AppTableRecord getRecord(String appToken, String tableId, String recordId) throws Exception {
    BatchGetAppTableRecordReq req = BatchGetAppTableRecordReq.newBuilder()
        .appToken(appToken)
        .tableId(tableId)
        .batchGetAppTableRecordReqBody(BatchGetAppTableRecordReqBody.newBuilder()
            .recordIds(new String[] { recordId })
            .build())
        .build();

    limiterClient.acquire("feishu.bitable.getRecord", 20, 60);

    BatchGetAppTableRecordResp resp = feishuClient.getClient().bitable().v1().appTableRecord().batchGet(req);
    if (!resp.success()) {
      throw new Exception(String.format("错误码：%s，错误描述：%s", resp.getCode(), resp.getMsg()));
    }

    AppTableRecord[] records = resp.getData().getRecords();
    if (ArrayUtils.isEmpty(records)) {
      return null;
    }

    return records[0];
  }
}
