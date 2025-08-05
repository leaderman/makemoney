package io.github.leaderman.makemoney.hustle.stock.command.trend.sync;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.lark.oapi.Client;
import com.lark.oapi.service.bitable.v1.model.AppTableCreateHeader;
import com.lark.oapi.service.bitable.v1.model.AppTableFieldProperty;
import com.lark.oapi.service.bitable.v1.model.CreateAppTableReq;
import com.lark.oapi.service.bitable.v1.model.CreateAppTableReqBody;
import com.lark.oapi.service.bitable.v1.model.CreateAppTableResp;
import com.lark.oapi.service.bitable.v1.model.ReqTable;

import io.github.leaderman.makemoney.hustle.feishu.FeishuClient;
import io.github.leaderman.makemoney.hustle.stock.command.trend.analyze.ConsecutiveDownUpRule;
import io.github.leaderman.makemoney.hustle.stock.command.trend.analyze.ConsecutiveUpDownRule;
import io.github.leaderman.makemoney.hustle.stock.command.trend.analyze.OverallDownUpRule;
import io.github.leaderman.makemoney.hustle.stock.command.trend.analyze.OverallUpDownRule;

@SpringBootTest
public class CreateTableTest {
  @Autowired
  private FeishuClient feishuClient;

  @Test
  public void testCreateTableCud() throws Exception {
    List<ConsecutiveUpDownRule> rules = new ArrayList<>();

    rules.add(new ConsecutiveUpDownRule(1, 1));
    rules.add(new ConsecutiveUpDownRule(1, 2));
    rules.add(new ConsecutiveUpDownRule(1, 3));
    rules.add(new ConsecutiveUpDownRule(2, 1));
    rules.add(new ConsecutiveUpDownRule(2, 2));
    rules.add(new ConsecutiveUpDownRule(2, 3));
    rules.add(new ConsecutiveUpDownRule(3, 1));
    rules.add(new ConsecutiveUpDownRule(3, 2));
    rules.add(new ConsecutiveUpDownRule(3, 3));
    rules.add(new ConsecutiveUpDownRule(4, 1));
    rules.add(new ConsecutiveUpDownRule(4, 2));
    rules.add(new ConsecutiveUpDownRule(4, 3));
    rules.add(new ConsecutiveUpDownRule(5, 1));
    rules.add(new ConsecutiveUpDownRule(5, 2));
    rules.add(new ConsecutiveUpDownRule(5, 3));
    rules.add(new ConsecutiveUpDownRule(6, 1));
    rules.add(new ConsecutiveUpDownRule(6, 2));
    rules.add(new ConsecutiveUpDownRule(6, 3));
    rules.add(new ConsecutiveUpDownRule(7, 1));
    rules.add(new ConsecutiveUpDownRule(7, 2));
    rules.add(new ConsecutiveUpDownRule(7, 3));
    rules.add(new ConsecutiveUpDownRule(8, 1));
    rules.add(new ConsecutiveUpDownRule(8, 2));
    rules.add(new ConsecutiveUpDownRule(8, 3));
    rules.add(new ConsecutiveUpDownRule(9, 1));
    rules.add(new ConsecutiveUpDownRule(9, 2));
    rules.add(new ConsecutiveUpDownRule(9, 3));
    rules.add(new ConsecutiveUpDownRule(10, 1));
    rules.add(new ConsecutiveUpDownRule(10, 2));
    rules.add(new ConsecutiveUpDownRule(10, 3));

    Client client = feishuClient.getClient();

    for (ConsecutiveUpDownRule rule : rules) {
      String name = "连续上涨 " + rule.getUpDays() + " 天，连续下跌 " + rule.getDownDays() + " 天";

      List<AppTableCreateHeader> headers = new ArrayList<>();

      headers.add(AppTableCreateHeader.newBuilder().fieldName("股票代码").type(1).build());
      headers.add(AppTableCreateHeader.newBuilder().fieldName("股票名称").type(1).build());

      for (int index = 0; index < rule.getDays(); index++) {
        headers.add(AppTableCreateHeader.newBuilder().fieldName("最近 " + (rule.getDays() - index) + " 个交易日价格").type(2)
            .property(AppTableFieldProperty.newBuilder().formatter("0.0000").build()).build());
      }

      headers.add(AppTableCreateHeader.newBuilder().fieldName("股票趋势").type(2)
          .property(AppTableFieldProperty.newBuilder().formatter("0.0000").build()).build());

      CreateAppTableReq req = CreateAppTableReq.newBuilder()
          .appToken("EwoAbpVJnavJpfsOqL5caBLNnPh")
          .createAppTableReqBody(CreateAppTableReqBody.newBuilder()
              .table(ReqTable.newBuilder()
                  .name(name)
                  .fields(headers.toArray(new AppTableCreateHeader[0]))
                  .build())
              .build())
          .build();

      CreateAppTableResp resp = client.bitable().v1().appTable().create(req);
      if (!resp.success()) {
        System.out.println(resp.getCode() + " " + resp.getMsg());
        return;
      }

      System.out.println(name + ", " + resp.getData().getTableId());
    }
  }

  @Test
  public void testCreateTableCdu() throws Exception {
    List<ConsecutiveDownUpRule> rules = new ArrayList<>();

    rules.add(new ConsecutiveDownUpRule(1, 1));
    rules.add(new ConsecutiveDownUpRule(1, 2));
    rules.add(new ConsecutiveDownUpRule(1, 3));
    rules.add(new ConsecutiveDownUpRule(2, 1));
    rules.add(new ConsecutiveDownUpRule(2, 2));
    rules.add(new ConsecutiveDownUpRule(2, 3));
    rules.add(new ConsecutiveDownUpRule(3, 1));
    rules.add(new ConsecutiveDownUpRule(3, 2));
    rules.add(new ConsecutiveDownUpRule(3, 3));
    rules.add(new ConsecutiveDownUpRule(4, 1));
    rules.add(new ConsecutiveDownUpRule(4, 2));
    rules.add(new ConsecutiveDownUpRule(4, 3));
    rules.add(new ConsecutiveDownUpRule(5, 1));
    rules.add(new ConsecutiveDownUpRule(5, 2));
    rules.add(new ConsecutiveDownUpRule(5, 3));
    rules.add(new ConsecutiveDownUpRule(6, 1));
    rules.add(new ConsecutiveDownUpRule(6, 2));
    rules.add(new ConsecutiveDownUpRule(6, 3));
    rules.add(new ConsecutiveDownUpRule(7, 1));
    rules.add(new ConsecutiveDownUpRule(7, 2));
    rules.add(new ConsecutiveDownUpRule(7, 3));
    rules.add(new ConsecutiveDownUpRule(8, 1));
    rules.add(new ConsecutiveDownUpRule(8, 2));
    rules.add(new ConsecutiveDownUpRule(8, 3));
    rules.add(new ConsecutiveDownUpRule(9, 1));
    rules.add(new ConsecutiveDownUpRule(9, 2));
    rules.add(new ConsecutiveDownUpRule(9, 3));
    rules.add(new ConsecutiveDownUpRule(10, 1));
    rules.add(new ConsecutiveDownUpRule(10, 2));
    rules.add(new ConsecutiveDownUpRule(10, 3));

    Client client = feishuClient.getClient();

    for (ConsecutiveDownUpRule rule : rules) {
      String name = "连续下跌 " + rule.getDownDays() + " 天，连续上涨 " + rule.getUpDays() + " 天";

      List<AppTableCreateHeader> headers = new ArrayList<>();

      headers.add(AppTableCreateHeader.newBuilder().fieldName("股票代码").type(1).build());
      headers.add(AppTableCreateHeader.newBuilder().fieldName("股票名称").type(1).build());

      for (int index = 0; index < rule.getDays(); index++) {
        headers.add(AppTableCreateHeader.newBuilder().fieldName("最近 " + (rule.getDays() - index) + " 个交易日价格").type(2)
            .property(AppTableFieldProperty.newBuilder().formatter("0.0000").build()).build());
      }

      headers.add(AppTableCreateHeader.newBuilder().fieldName("股票趋势").type(2)
          .property(AppTableFieldProperty.newBuilder().formatter("0.0000").build()).build());

      CreateAppTableReq req = CreateAppTableReq.newBuilder()
          .appToken("EwoAbpVJnavJpfsOqL5caBLNnPh")
          .createAppTableReqBody(CreateAppTableReqBody.newBuilder()
              .table(ReqTable.newBuilder()
                  .name(name)
                  .fields(headers.toArray(new AppTableCreateHeader[0]))
                  .build())
              .build())
          .build();

      CreateAppTableResp resp = client.bitable().v1().appTable().create(req);
      if (!resp.success()) {
        System.out.println(resp.getCode() + " " + resp.getMsg());
        return;
      }

      System.out.println(name + ", " + resp.getData().getTableId());
    }
  }

  @Test
  public void testCreateTableOud() throws Exception {
    List<OverallUpDownRule> rules = new ArrayList<>();

    rules.add(new OverallUpDownRule(1, 1));
    rules.add(new OverallUpDownRule(1, 2));
    rules.add(new OverallUpDownRule(1, 3));
    rules.add(new OverallUpDownRule(2, 1));
    rules.add(new OverallUpDownRule(2, 2));
    rules.add(new OverallUpDownRule(2, 3));
    rules.add(new OverallUpDownRule(3, 1));
    rules.add(new OverallUpDownRule(3, 2));
    rules.add(new OverallUpDownRule(3, 3));
    rules.add(new OverallUpDownRule(4, 1));
    rules.add(new OverallUpDownRule(4, 2));
    rules.add(new OverallUpDownRule(4, 3));
    rules.add(new OverallUpDownRule(5, 1));
    rules.add(new OverallUpDownRule(5, 2));
    rules.add(new OverallUpDownRule(5, 3));
    rules.add(new OverallUpDownRule(6, 1));
    rules.add(new OverallUpDownRule(6, 2));
    rules.add(new OverallUpDownRule(6, 3));
    rules.add(new OverallUpDownRule(7, 1));
    rules.add(new OverallUpDownRule(7, 2));
    rules.add(new OverallUpDownRule(7, 3));
    rules.add(new OverallUpDownRule(8, 1));
    rules.add(new OverallUpDownRule(8, 2));
    rules.add(new OverallUpDownRule(8, 3));
    rules.add(new OverallUpDownRule(9, 1));
    rules.add(new OverallUpDownRule(9, 2));
    rules.add(new OverallUpDownRule(9, 3));
    rules.add(new OverallUpDownRule(10, 1));
    rules.add(new OverallUpDownRule(10, 2));
    rules.add(new OverallUpDownRule(10, 3));

    Client client = feishuClient.getClient();

    for (OverallUpDownRule rule : rules) {
      String name = "整体上涨 " + rule.getUpDays() + " 天，整体下跌 " + rule.getDownDays() + " 天";

      List<AppTableCreateHeader> headers = new ArrayList<>();

      headers.add(AppTableCreateHeader.newBuilder().fieldName("股票代码").type(1).build());
      headers.add(AppTableCreateHeader.newBuilder().fieldName("股票名称").type(1).build());

      for (int index = 0; index < rule.getDays(); index++) {
        headers.add(AppTableCreateHeader.newBuilder().fieldName("最近 " + (rule.getDays() - index) + " 个交易日价格").type(2)
            .property(AppTableFieldProperty.newBuilder().formatter("0.0000").build()).build());
      }

      headers.add(AppTableCreateHeader.newBuilder().fieldName("股票趋势").type(2)
          .property(AppTableFieldProperty.newBuilder().formatter("0.0000").build()).build());

      CreateAppTableReq req = CreateAppTableReq.newBuilder()
          .appToken("EwoAbpVJnavJpfsOqL5caBLNnPh")
          .createAppTableReqBody(CreateAppTableReqBody.newBuilder()
              .table(ReqTable.newBuilder()
                  .name(name)
                  .fields(headers.toArray(new AppTableCreateHeader[0]))
                  .build())
              .build())
          .build();

      CreateAppTableResp resp = client.bitable().v1().appTable().create(req);
      if (!resp.success()) {
        System.out.println(resp.getCode() + " " + resp.getMsg());
        return;
      }

      System.out.println(name + ", " + resp.getData().getTableId());
    }
  }

  @Test
  public void testCreateTableOdu() throws Exception {
    List<OverallDownUpRule> rules = new ArrayList<>();

    rules.add(new OverallDownUpRule(1, 1));
    rules.add(new OverallDownUpRule(1, 2));
    rules.add(new OverallDownUpRule(1, 3));
    rules.add(new OverallDownUpRule(2, 1));
    rules.add(new OverallDownUpRule(2, 2));
    rules.add(new OverallDownUpRule(2, 3));
    rules.add(new OverallDownUpRule(3, 1));
    rules.add(new OverallDownUpRule(3, 2));
    rules.add(new OverallDownUpRule(3, 3));
    rules.add(new OverallDownUpRule(4, 1));
    rules.add(new OverallDownUpRule(4, 2));
    rules.add(new OverallDownUpRule(4, 3));
    rules.add(new OverallDownUpRule(5, 1));
    rules.add(new OverallDownUpRule(5, 2));
    rules.add(new OverallDownUpRule(5, 3));
    rules.add(new OverallDownUpRule(6, 1));
    rules.add(new OverallDownUpRule(6, 2));
    rules.add(new OverallDownUpRule(6, 3));
    rules.add(new OverallDownUpRule(7, 1));
    rules.add(new OverallDownUpRule(7, 2));
    rules.add(new OverallDownUpRule(7, 3));
    rules.add(new OverallDownUpRule(8, 1));
    rules.add(new OverallDownUpRule(8, 2));
    rules.add(new OverallDownUpRule(8, 3));
    rules.add(new OverallDownUpRule(9, 1));
    rules.add(new OverallDownUpRule(9, 2));
    rules.add(new OverallDownUpRule(9, 3));
    rules.add(new OverallDownUpRule(10, 1));
    rules.add(new OverallDownUpRule(10, 2));
    rules.add(new OverallDownUpRule(10, 3));

    Client client = feishuClient.getClient();

    for (OverallDownUpRule rule : rules) {
      String name = "整体下跌 " + rule.getDownDays() + " 天，整体上涨 " + rule.getUpDays() + " 天";

      List<AppTableCreateHeader> headers = new ArrayList<>();

      headers.add(AppTableCreateHeader.newBuilder().fieldName("股票代码").type(1).build());
      headers.add(AppTableCreateHeader.newBuilder().fieldName("股票名称").type(1).build());

      for (int index = 0; index < rule.getDays(); index++) {
        headers.add(AppTableCreateHeader.newBuilder().fieldName("最近 " + (rule.getDays() - index) + " 个交易日价格").type(2)
            .property(AppTableFieldProperty.newBuilder().formatter("0.0000").build()).build());
      }

      headers.add(AppTableCreateHeader.newBuilder().fieldName("股票趋势").type(2)
          .property(AppTableFieldProperty.newBuilder().formatter("0.0000").build()).build());

      CreateAppTableReq req = CreateAppTableReq.newBuilder()
          .appToken("EwoAbpVJnavJpfsOqL5caBLNnPh")
          .createAppTableReqBody(CreateAppTableReqBody.newBuilder()
              .table(ReqTable.newBuilder()
                  .name(name)
                  .fields(headers.toArray(new AppTableCreateHeader[0]))
                  .build())
              .build())
          .build();

      CreateAppTableResp resp = client.bitable().v1().appTable().create(req);
      if (!resp.success()) {
        System.out.println(resp.getCode() + " " + resp.getMsg());
        return;
      }

      System.out.println(name + ", " + resp.getData().getTableId());
    }
  }
}
