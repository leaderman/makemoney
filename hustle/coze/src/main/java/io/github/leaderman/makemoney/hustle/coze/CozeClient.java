package io.github.leaderman.makemoney.hustle.coze;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.coze.openapi.client.workflows.run.RunWorkflowReq;
import com.coze.openapi.client.workflows.run.RunWorkflowResp;
import com.coze.openapi.service.auth.JWTOAuth;
import com.coze.openapi.service.auth.JWTOAuthClient;
import com.coze.openapi.service.config.Consts;
import com.coze.openapi.service.service.CozeAPI;

import io.github.leaderman.makemoney.hustle.config.ConfigClient;
import io.github.leaderman.makemoney.hustle.limiter.LimiterClient;

@Component
public class CozeClient {
  private final LimiterClient limiterClient;
  private final CozeAPI cozeApi;

  public CozeClient(ConfigClient configClient, LimiterClient limiterClient) throws Exception {
    this.limiterClient = limiterClient;

    this.cozeApi = new CozeAPI.Builder()
        .auth(new JWTOAuth(new JWTOAuthClient.JWTOAuthBuilder()
            .clientID(configClient.getString("coze.jwt.oauth.client.id"))
            .publicKey(configClient.getString("coze.jwt.oauth.public.key"))
            .privateKey(configClient.getString("coze.jwt.oauth.private.key"))
            .baseURL(Consts.COZE_CN_BASE_URL)
            .build()))
        .baseURL(Consts.COZE_CN_BASE_URL)
        .build();
  }

  /**
   * 运行工作流。
   * 
   * @param workflowID 工作流 ID。
   * @return 运行结果。
   * @throws Exception
   */
  public String runWorkflow(String workflowID) throws Exception {
    return runWorkflow(workflowID, null);
  }

  /**
   * 运行工作流。
   * 
   * @param workflowID 工作流 ID。
   * @param parameters 参数。
   * @return 运行结果。
   * @throws Exception
   */
  public String runWorkflow(String workflowID, Map<String, Object> parameters) throws Exception {
    RunWorkflowReq req = RunWorkflowReq.builder().workflowID(workflowID).parameters(parameters).build();

    limiterClient.acquire("coze.api.workflow", 200, 1);

    RunWorkflowResp resp = this.cozeApi.workflows().runs().create(req);
    if (resp.getCode() != 0) {
      throw new Exception(
          String.format("错误码：%s，错误描述：%s，调试页面：%s", resp.getCode(), resp.getMsg(), resp.getDebugURL()));
    }

    return resp.getData();
  }
}
