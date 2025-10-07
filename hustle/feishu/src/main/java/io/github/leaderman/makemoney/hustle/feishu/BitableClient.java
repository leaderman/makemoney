package io.github.leaderman.makemoney.hustle.feishu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.lark.oapi.service.bitable.v1.model.AppTable;
import com.lark.oapi.service.bitable.v1.model.AppTableRecord;
import com.lark.oapi.service.bitable.v1.model.AppTableView;
import com.lark.oapi.service.bitable.v1.model.BatchCreateAppTableRecordReq;
import com.lark.oapi.service.bitable.v1.model.BatchCreateAppTableRecordReqBody;
import com.lark.oapi.service.bitable.v1.model.BatchCreateAppTableRecordResp;
import com.lark.oapi.service.bitable.v1.model.BatchDeleteAppTableRecordReq;
import com.lark.oapi.service.bitable.v1.model.BatchDeleteAppTableRecordReqBody;
import com.lark.oapi.service.bitable.v1.model.BatchDeleteAppTableRecordResp;
import com.lark.oapi.service.bitable.v1.model.BatchGetAppTableRecordReq;
import com.lark.oapi.service.bitable.v1.model.BatchGetAppTableRecordReqBody;
import com.lark.oapi.service.bitable.v1.model.BatchGetAppTableRecordResp;
import com.lark.oapi.service.bitable.v1.model.BatchUpdateAppTableRecordReq;
import com.lark.oapi.service.bitable.v1.model.BatchUpdateAppTableRecordReqBody;
import com.lark.oapi.service.bitable.v1.model.BatchUpdateAppTableRecordResp;
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

      this.limiterClient.acquire("feishu.bitable.listTables", 20, 1);

      ListAppTableResp resp = this.feishuClient.getClient().bitable().v1().appTable().list(req);
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

      this.limiterClient.acquire("feishu.bitable.listViews", 20, 1);

      ListAppTableViewResp resp = this.feishuClient.getClient().bitable().v1().appTableView().list(req);
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
   * 列出多维表格的数据表的视图的所有记录。
   * 
   * @param appToken 多维表格的唯一标识。
   * @param tableId  数据表的唯一标识。
   * @return 所有记录。
   * @throws Exception
   */
  public List<AppTableRecord> listTableRecords(String appToken, String tableId) throws Exception {
    List<AppTableRecord> records = null;

    boolean hasMore = true;
    String pageToken = null;

    while (hasMore) {
      ListAppTableRecordReq req = ListAppTableRecordReq.newBuilder()
          .appToken(appToken)
          .tableId(tableId)
          .pageToken(pageToken)
          .build();

      this.limiterClient.acquire("feishu.bitable.listRecords", 20, 1);

      ListAppTableRecordResp resp = this.feishuClient.getClient().bitable().v1().appTableRecord().list(req);
      if (!resp.success()) {
        throw new Exception(String.format("错误码：%s，错误描述：%s", resp.getCode(), resp.getMsg()));
      }

      ListAppTableRecordRespBody data = resp.getData();

      AppTableRecord[] items = data.getItems();
      if (ArrayUtils.isNotEmpty(items)) {
        if (records == null) {
          records = new ArrayList<>();
        }

        records.addAll(Arrays.asList(items));
      }

      hasMore = data.getHasMore();
      pageToken = data.getPageToken();
    }

    return records;
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
      List<AppTableRecord> tableRecords = listTableRecords(appToken, table.getTableId());
      records.addAll(tableRecords);
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

    this.limiterClient.acquire("feishu.bitable.createRecord", 50, 1);

    CreateAppTableRecordResp resp = this.feishuClient.getClient().bitable().v1().appTableRecord().create(req);
    if (!resp.success()) {
      throw new Exception(String.format("错误码：%s，错误描述：%s", resp.getCode(), resp.getMsg()));
    }

    return resp.getData().getRecord();
  }

  private List<AppTableRecord> internalBatchCreateRecords(String appToken, String tableId,
      List<Map<String, Object>> records)
      throws Exception {
    AppTableRecord[] appTableRecords = records.stream()
        .map(record -> AppTableRecord.newBuilder().fields(record).build())
        .toArray(AppTableRecord[]::new);

    BatchCreateAppTableRecordReq req = BatchCreateAppTableRecordReq.newBuilder()
        .appToken(appToken)
        .tableId(tableId)
        .batchCreateAppTableRecordReqBody(BatchCreateAppTableRecordReqBody.newBuilder()
            .records(appTableRecords)
            .build())
        .build();

    this.limiterClient.acquire("feishu.bitable.batchCreateRecords", 50, 1);

    BatchCreateAppTableRecordResp resp = this.feishuClient.getClient().bitable().v1().appTableRecord().batchCreate(req);
    if (!resp.success()) {
      throw new Exception(String.format("错误码：%s，错误描述：%s", resp.getCode(), resp.getMsg()));
    }

    return Arrays.asList(resp.getData().getRecords());
  }

  /**
   * 批量创建多维表格的记录。
   * 
   * @param appToken 多维表格的唯一标识。
   * @param tableId  数据表的唯一标识。
   * @param records  记录列表。
   * @return 创建的记录列表。
   * @throws Exception
   */
  public List<AppTableRecord> batchCreateRecords(String appToken, String tableId, List<Map<String, Object>> records)
      throws Exception {
    int batchSize = 1000;
    int total = records.size();

    List<AppTableRecord> createdRecords = new ArrayList<>();

    for (int from = 0; from < total; from += batchSize) {
      int to = Math.min(from + batchSize, total);

      List<Map<String, Object>> batch = records.subList(from, to);
      List<AppTableRecord> createdBatch = this.internalBatchCreateRecords(appToken, tableId, batch);
      createdRecords.addAll(createdBatch);
    }

    return createdRecords;
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

    this.limiterClient.acquire("feishu.bitable.getRecord", 20, 1);

    BatchGetAppTableRecordResp resp = this.feishuClient.getClient().bitable().v1().appTableRecord().batchGet(req);
    if (!resp.success()) {
      throw new Exception(String.format("错误码：%s，错误描述：%s", resp.getCode(), resp.getMsg()));
    }

    AppTableRecord[] records = resp.getData().getRecords();
    if (ArrayUtils.isEmpty(records)) {
      return null;
    }

    return records[0];
  }

  /**
   * 获取多维表格的记录的 AI 输出。
   * 
   * @param appToken      多维表格的唯一标识。
   * @param tableId       数据表的唯一标识。
   * @param recordId      记录的唯一标识。
   * @param name          字段名称。
   * @param maxRetries    最大重试次数。
   * @param retryInterval 重试间隔时间（秒）。
   * @return AI 输出。
   * @throws Exception
   */
  public String getAiOutput(String appToken, String tableId, String recordId, String name, int maxRetries,
      int retryInterval) throws Exception {
    for (int retry = 0; retry < maxRetries; retry++) {
      Thread.sleep(retryInterval * 1000);

      AppTableRecord record = getRecord(appToken, tableId, recordId);
      if (record == null) {
        throw new RuntimeException(String.format("记录不存在：%s", recordId));
      }

      @SuppressWarnings("unchecked")
      List<Map<String, String>> values = (List<Map<String, String>>) record.getFields().get(name);
      if (CollectionUtils.isNotEmpty(values)) {
        String value = values.get(0).get("text");
        if (StringUtils.isNotEmpty(value)) {
          return value;
        }
      }
    }

    throw new RuntimeException(String.format("获取 AI 输出超时：%s", recordId));
  }

  /**
   * 获取多维表格的记录的 AI 输出。
   * 
   * @param appToken      多维表格的唯一标识。
   * @param tableId       数据表的唯一标识。
   * @param recordId      记录的唯一标识。
   * @param names         字段名称列表。
   * @param maxRetries    最大重试次数。
   * @param retryInterval 重试间隔时间（秒）。
   * @return AI 输出。
   * @throws Exception
   */
  public Map<String, String> getAiOutput(String appToken, String tableId, String recordId, List<String> names,
      int maxRetries, int retryInterval) throws Exception {
    for (int retry = 0; retry < maxRetries; retry++) {
      Thread.sleep(retryInterval * 1000);

      AppTableRecord record = getRecord(appToken, tableId, recordId);
      if (record == null) {
        throw new RuntimeException(String.format("记录不存在：%s", recordId));
      }

      Map<String, String> output = new HashMap<>();

      for (String name : names) {
        @SuppressWarnings("unchecked")
        List<Map<String, String>> values = (List<Map<String, String>>) record.getFields().get(name);
        if (CollectionUtils.isEmpty(values)) {
          break;
        }

        String value = values.get(0).get("text");
        if (StringUtils.isEmpty(value)) {
          break;
        }

        if (value.equals("无")) {
          value = "";
        }

        output.put(name, value);
      }

      if (output.size() == names.size()) {
        return output;
      }
    }

    throw new RuntimeException(String.format("获取 AI 输出超时：%s", recordId));
  }

  private void internalBatchDeleteRecords(String appToken, String tableId, List<String> recordIds) throws Exception {
    BatchDeleteAppTableRecordReq req = BatchDeleteAppTableRecordReq.newBuilder()
        .appToken(appToken)
        .tableId(tableId)
        .batchDeleteAppTableRecordReqBody(BatchDeleteAppTableRecordReqBody.newBuilder()
            .records(recordIds.toArray(new String[0]))
            .build())
        .build();

    this.limiterClient.acquire("feishu.bitable.batchDeleteRecords", 50, 1);

    BatchDeleteAppTableRecordResp resp = this.feishuClient.getClient().bitable().v1().appTableRecord().batchDelete(req);
    if (!resp.success()) {
      throw new Exception(String.format("错误码：%s，错误描述：%s", resp.getCode(), resp.getMsg()));
    }
  }

  /**
   * 批量删除多维表格的记录。
   * 
   * @param appToken  多维表格的唯一标识。
   * @param tableId   数据表的唯一标识。
   * @param recordIds 记录的唯一标识列表。
   * @throws Exception
   */
  public void batchDeleteRecords(String appToken, String tableId, List<String> recordIds) throws Exception {
    int batchSize = 500;
    int total = recordIds.size();

    for (int from = 0; from < total; from += batchSize) {
      int to = Math.min(from + batchSize, total);

      List<String> batch = recordIds.subList(from, to);
      this.internalBatchDeleteRecords(appToken, tableId, batch);
    }
  }

  /**
   * 删除多维表格的记录。
   * 
   * @param appToken 多维表格的唯一标识。
   * @param tableId  数据表的唯一标识。
   * @param recordId 记录的唯一标识。
   * @throws Exception
   */
  public void deleteRecord(String appToken, String tableId, String recordId) throws Exception {
    this.batchDeleteRecords(appToken, tableId, Collections.singletonList(recordId));
  }

  /**
   * 清空多维表格的数据表。
   * 
   * @param appToken 多维表格的唯一标识。
   * @param tableId  数据表的唯一标识。
   * @throws Exception
   */
  public void truncateTable(String appToken, String tableId) throws Exception {
    List<AppTableRecord> records = listTableRecords(appToken, tableId);
    if (CollectionUtils.isEmpty(records)) {
      return;
    }

    List<String> recordIds = records.stream().map(AppTableRecord::getRecordId).collect(Collectors.toList());
    this.batchDeleteRecords(appToken, tableId, recordIds);
  }

  private List<AppTableRecord> internalBatchUpdateRecords(String appToken, String tableId,
      List<String> recordIds, List<Map<String, Object>> records)
      throws Exception {
    AppTableRecord[] appTableRecords = IntStream.range(0, recordIds.size())
        .mapToObj(index -> AppTableRecord.newBuilder().recordId(recordIds.get(index)).fields(records.get(index))
            .build())
        .toArray(AppTableRecord[]::new);

    BatchUpdateAppTableRecordReq req = BatchUpdateAppTableRecordReq.newBuilder()
        .appToken(appToken)
        .tableId(tableId)
        .batchUpdateAppTableRecordReqBody(BatchUpdateAppTableRecordReqBody.newBuilder()
            .records(appTableRecords)
            .build())
        .build();

    this.limiterClient.acquire("feishu.bitable.batchUpdateRecords", 50, 1);

    BatchUpdateAppTableRecordResp resp = this.feishuClient.getClient().bitable().v1().appTableRecord().batchUpdate(req);
    if (!resp.success()) {
      throw new Exception(String.format("错误码：%s，错误描述：%s", resp.getCode(), resp.getMsg()));
    }

    return Arrays.asList(resp.getData().getRecords());
  }

  /**
   * 批量更新多维表格的记录。
   * 
   * @param appToken  多维表格的唯一标识。
   * @param tableId   数据表的唯一标识。
   * @param recordIds 记录的唯一标识列表。
   * @param records   记录列表。
   * @return 更新的记录列表。
   * @throws Exception
   */
  public List<AppTableRecord> batchUpdateRecords(String appToken, String tableId, List<String> recordIds,
      List<Map<String, Object>> records)
      throws Exception {
    int batchSize = 1000;
    int total = records.size();

    List<AppTableRecord> updatedRecords = new ArrayList<>();

    for (int from = 0; from < total; from += batchSize) {
      int to = Math.min(from + batchSize, total);

      List<String> batchRecordIds = recordIds.subList(from, to);
      List<Map<String, Object>> batchRecords = records.subList(from, to);

      List<AppTableRecord> updatedBatch = this.internalBatchUpdateRecords(appToken, tableId, batchRecordIds,
          batchRecords);
      updatedRecords.addAll(updatedBatch);
    }

    return updatedRecords;
  }

  /**
   * 更新多维表格的记录。
   * 
   * @param appToken 多维表格的唯一标识。
   * @param tableId  数据表的唯一标识。
   * @param recordId 记录的唯一标识。
   * @param fields   记录的字段。
   * @return 更新的记录。
   * @throws Exception
   */
  public AppTableRecord updateRecord(String appToken, String tableId, String recordId, Map<String, Object> fields)
      throws Exception {
    List<AppTableRecord> updatedRecords = this.batchUpdateRecords(appToken, tableId,
        Collections.singletonList(recordId), Collections.singletonList(fields));

    return updatedRecords.get(0);
  }
}
