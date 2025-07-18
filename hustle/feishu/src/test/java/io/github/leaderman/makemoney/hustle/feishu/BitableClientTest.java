package io.github.leaderman.makemoney.hustle.feishu;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.lark.oapi.service.bitable.v1.model.AppTable;

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
}
