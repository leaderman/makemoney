package io.github.leaderman.makemoney.hustle.coze;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import io.github.leaderman.makemoney.hustle.coze.workflow.stock.StockData;

@SpringBootTest
public class CozeClientTest {
  @Autowired
  private CozeClient cozeClient;

  @Test
  public void testRunWorkflow() throws Exception {
    String data = cozeClient.runWorkflow("7484159891621101603");
    System.out.println(data);
  }

  @Test
  public void testRunWorkflowWithParameters() throws Exception {
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("code", "sz002594");
    parameters.put("tradingDate", "2025-07-24");

    String data = cozeClient.runWorkflow("7498162044832677939", parameters);
    System.out.println(data);
  }

  @Test
  public void testGetStockData() throws Exception {
    StockData stockData = cozeClient.getStockData("sz002594", "2025-07-24");
    System.out.println(stockData);
  }
}
