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

@Component
public class CozeClient {
  private final CozeAPI coze;

  public CozeClient(ConfigClient configClient) throws Exception {
    this.coze = new CozeAPI.Builder()
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
   * @throws RuntimeException
   */
  public String runWorkflow(String workflowID) {
    return runWorkflow(workflowID, null);
  }

  /**
   * 运行工作流。
   * 
   * @param workflowID 工作流 ID。
   * @param parameters 参数。
   * @return 运行结果。
   * @throws RuntimeException
   */
  public String runWorkflow(String workflowID, Map<String, Object> parameters) {
    RunWorkflowReq req = RunWorkflowReq.builder().workflowID(workflowID).parameters(parameters).build();

    RunWorkflowResp resp = this.coze.workflows().runs().create(req);
    if (resp.getCode() != 0) {
      throw new RuntimeException(
          String.format("错误码：%s，错误描述：%s，调试页面：%s", resp.getCode(), resp.getMsg(), resp.getDebugURL()));
    }

    return resp.getData();
  }
}
