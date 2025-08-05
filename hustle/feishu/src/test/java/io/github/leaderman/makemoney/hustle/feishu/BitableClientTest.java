package io.github.leaderman.makemoney.hustle.feishu;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.lark.oapi.service.bitable.v1.model.AppTable;
import com.lark.oapi.service.bitable.v1.model.AppTableRecord;
import com.lark.oapi.service.bitable.v1.model.AppTableView;

@SpringBootTest
public class BitableClientTest {
  @Autowired
  private BitableClient bitableClient;

  @Test
  public void testListTables() throws Exception {
    List<AppTable> tables = bitableClient.listTables("XGG1blHrkangStsSV82cvhi1n9d");
    tables.forEach(table -> {
      System.out.println(table.getTableId() + "\t" + table.getName());
    });
  }

  @Test
  public void testListViews() throws Exception {
    List<AppTableView> views = bitableClient.listViews("XGG1blHrkangStsSV82cvhi1n9d", "tblnxanpg2zpK9qn");
    views.forEach(view -> {
      System.out.println(view.getViewId() + "\t" + view.getViewName());
    });
  }

  @Test
  public void testListRecords() throws Exception {
    List<AppTableRecord> records = bitableClient.listRecords("XGG1blHrkangStsSV82cvhi1n9d");
    records.forEach(record -> {
      System.out.println(record.getRecordId() + "\t" + record.getFields());
    });
  }

  @Test
  public void testListTableRecords() throws Exception {
    List<AppTableRecord> records = bitableClient.listTableRecords("XGG1blHrkangStsSV82cvhi1n9d", "tblNB7kgiANos6TZ");
    records.forEach(record -> {
      System.out.println(record.getRecordId() + "\t" + record.getFields());
    });
  }

  @Test
  public void testCreateRecord() throws Exception {
    AppTableRecord record = bitableClient.createRecord("XGG1blHrkangStsSV82cvhi1n9d", "tblnxanpg2zpK9qn",
        Map.of("名称", "hustle.test.createRecord", "值", "createRecord"));
    System.out.println(record.getRecordId() + "\t" + record.getFields());
  }

  @Test
  public void testGetRecord() throws Exception {
    AppTableRecord record = bitableClient.getRecord("XGG1blHrkangStsSV82cvhi1n9d", "tblnxanpg2zpK9qn",
        "recuRFdbaLSkc2");
    System.out.println(record.getRecordId() + "\t" + record.getFields());
  }

  @Test
  public void testGetAiOutput() throws Exception {
    AppTableRecord record = bitableClient.createRecord("XGG1blHrkangStsSV82cvhi1n9d", "tblnxanpg2zpK9qn",
        Map.of("名称", "hustle.test.getAiOutput", "值", "getAiOutput"));
    System.out.println(record.getRecordId() + "\t" + record.getFields());

    String aiOutput = bitableClient.getAiOutput("XGG1blHrkangStsSV82cvhi1n9d", "tblnxanpg2zpK9qn",
        record.getRecordId(), "说明", 10, 3);
    System.out.println(aiOutput);
  }

  @Test
  public void testDeleteRecord() throws Exception {
    bitableClient.deleteRecord("XGG1blHrkangStsSV82cvhi1n9d", "tblNB7kgiANos6TZ", "recuRz2905jyLW");
  }

  @Test
  public void testTruncateTable() throws Exception {
    bitableClient.truncateTable("XGG1blHrkangStsSV82cvhi1n9d", "tblNB7kgiANos6TZ");
  }
}
