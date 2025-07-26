package io.github.leaderman.makemoney.hustle.coze;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.coze.openapi.client.workflows.run.RunWorkflowReq;
import com.coze.openapi.client.workflows.run.RunWorkflowResp;
import com.coze.openapi.service.auth.JWTOAuth;
import com.coze.openapi.service.auth.JWTOAuthClient;
import com.coze.openapi.service.config.Consts;
import com.coze.openapi.service.service.CozeAPI;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.leaderman.makemoney.hustle.config.ConfigClient;
import io.github.leaderman.makemoney.hustle.coze.workflow.stock.Stock;
import io.github.leaderman.makemoney.hustle.coze.workflow.stock.StockData;
import io.github.leaderman.makemoney.hustle.coze.workflow.stock.StockDataContainer;
import io.github.leaderman.makemoney.hustle.coze.workflow.stock.StocksContainer;
import io.github.leaderman.makemoney.hustle.limiter.LimiterClient;

@Component
public class CozeClient {
  private final ConfigClient configClient;
  private final LimiterClient limiterClient;
  private final CozeAPI cozeApi;
  private final ObjectMapper objectMapper;

  public CozeClient(ConfigClient configClient, LimiterClient limiterClient, ObjectMapper objectMapper)
      throws Exception {
    this.configClient = configClient;
    this.limiterClient = limiterClient;
    this.objectMapper = objectMapper;

    this.cozeApi = new CozeAPI.Builder()
        .auth(new JWTOAuth(new JWTOAuthClient.JWTOAuthBuilder()
            .clientID(this.configClient.getString("coze.jwt.oauth.client.id"))
            .publicKey(this.configClient.getString("coze.jwt.oauth.public.key"))
            .privateKey(this.configClient.getString("coze.jwt.oauth.private.key"))
            .baseURL(Consts.COZE_CN_BASE_URL)
            .build()))
        .baseURL(Consts.COZE_CN_BASE_URL)
        .readTimeout(5 * 60 * 1000)
        .build();
  }

  /**
   * 运行工作流。
   * 
   * @param workflowId 工作流 ID。
   * @return 运行结果。
   * @throws Exception
   */
  public String runWorkflow(String workflowId) throws Exception {
    return runWorkflow(workflowId, null);
  }

  /**
   * 运行工作流。
   * 
   * @param workflowID 工作流 ID。
   * @param parameters 参数。
   * @return 运行结果。
   * @throws Exception
   */
  public String runWorkflow(String workflowId, Map<String, Object> parameters) throws Exception {
    RunWorkflowReq req = RunWorkflowReq.builder().workflowID(workflowId).parameters(parameters).build();

    limiterClient.acquire("coze.api.workflow", 200, 1);

    RunWorkflowResp resp = this.cozeApi.workflows().runs().create(req);
    if (resp.getCode() != 0) {
      throw new Exception(
          String.format("错误码：%s，错误描述：%s，调试页面：%s", resp.getCode(), resp.getMsg(), resp.getDebugURL()));
    }

    return resp.getData();
  }

  /**
   * 获取股票数据。
   * 
   * @param code        股票代码。
   * @param tradingDate 交易日期。
   * @return 股票数据。
   * @throws Exception
   */
  public StockData getStockData(String code, String tradingDate) throws Exception {
    String workflowId = this.configClient.getString("coze.workflow.get.stock.data");
    Map<String, Object> parameters = Map.of("code", code, "tradingDate", tradingDate);

    String data = runWorkflow(workflowId, parameters);
    return this.objectMapper.readValue(data, StockDataContainer.class).getStockData();
  }

  /**
   * 获取上海证券交易所股票列表（A 股）。
   * 
   * @return 股票列表。
   * @throws Exception
   */
  public List<Stock> getSseStocks() throws Exception {
    String workflowId = this.configClient.getString("coze.workflow.get.sse.stocks");

    String data = runWorkflow(workflowId);
    return this.objectMapper.readValue(data, StocksContainer.class).getStocks();
  }

  /**
   * 获取深圳证券交易所股票列表（A 股）。
   * 
   * @return 股票列表。
   * @throws Exception
   */
  public List<Stock> getSzseStocks() throws Exception {
    String workflowId = this.configClient.getString("coze.workflow.get.szse.stocks");

    String data = runWorkflow(workflowId);
    return this.objectMapper.readValue(data, StocksContainer.class).getStocks();
  }
}
