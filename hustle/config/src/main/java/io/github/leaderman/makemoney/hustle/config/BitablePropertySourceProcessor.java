package io.github.leaderman.makemoney.hustle.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;

import com.lark.oapi.Client;
import com.lark.oapi.service.bitable.v1.model.AppTable;
import com.lark.oapi.service.bitable.v1.model.AppTableRecord;
import com.lark.oapi.service.bitable.v1.model.ListAppTableRecordReq;
import com.lark.oapi.service.bitable.v1.model.ListAppTableRecordResp;
import com.lark.oapi.service.bitable.v1.model.ListAppTableRecordRespBody;
import com.lark.oapi.service.bitable.v1.model.ListAppTableReq;
import com.lark.oapi.service.bitable.v1.model.ListAppTableResp;
import com.lark.oapi.service.bitable.v1.model.ListAppTableRespBody;

public class BitablePropertySourceProcessor implements EnvironmentPostProcessor, Ordered {
  private final Map<String, Object> source = new ConcurrentHashMap<>();
  private final BitablePropertySource bitablePropertySource = new BitablePropertySource(this.source);

  @Override
  public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
    // 加载配置
    this.load(environment);

    // 添加配置源
    environment.getPropertySources().addLast(this.bitablePropertySource);
  }

  /**
   * 加载配置。
   */
  @SuppressWarnings("unchecked")
  private void load(ConfigurableEnvironment environment) {
    try {
      String appId = environment.getProperty("feishu.appId");
      if (StringUtils.isEmpty(appId)) {
        throw new RuntimeException("feishu.appId 没有配置");
      }

      String appSecret = environment.getProperty("feishu.appSecret");
      if (StringUtils.isEmpty(appSecret)) {
        throw new RuntimeException("feishu.appSecret 没有配置");
      }

      // 初始化飞书客户端
      Client client = Client.newBuilder(appId, appSecret).build();

      List<String> bitables = environment.getProperty("config.bitables", List.class);
      if (CollectionUtils.isEmpty(bitables)) {
        throw new RuntimeException("config.bitables 没有配置");
      }
      for (String bitable : bitables) {
        List<AppTableRecord> records = this.listRecords(client, bitable);
        for (AppTableRecord record : records) {
          String name = record.getFields().get("名称").toString();
          if (this.source.containsKey(name)) {
            System.err.println("多维表格加载配置已存在：" + name);
          }

          this.source.put(record.getFields().get("名称").toString(), record.getFields().get("值"));
        }
      }
      System.out.println("多维表格加载配置完成");
    } catch (Exception e) {
      throw new RuntimeException("多维表格加载配置错误", e);
    }
  }

  /**
   * 列出多维表格的所有数据表的所有记录。
   * 
   * @param appToken 多维表格的唯一标识。
   * @return 所有记录。
   * @throws Exception
   */
  private List<AppTableRecord> listRecords(Client client, String appToken) throws Exception {
    List<AppTable> tables = listTables(client, appToken);
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

        ListAppTableRecordResp resp = client.bitable().v1().appTableRecord().list(req);
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
   * 列出多维表格的所有数据表。
   * 
   * @param appToken 多维表格的唯一标识。
   * @return 所有数据表。
   * @throws Exception
   */
  private List<AppTable> listTables(Client client, String appToken) throws Exception {
    List<AppTable> tables = new ArrayList<>();

    boolean hasMore = true;
    String pageToken = null;

    while (hasMore) {
      ListAppTableReq req = ListAppTableReq.newBuilder()
          .appToken(appToken)
          .pageToken(pageToken)
          .build();

      ListAppTableResp resp = client.bitable().v1().appTable().list(req);
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

  @Override
  public int getOrder() {
    return Ordered.HIGHEST_PRECEDENCE;
  }
}
