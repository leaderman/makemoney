package io.github.leaderman.makemoney.hustle.ism.web.controller;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.leaderman.makemoney.hustle.domain.response.Response;
import io.github.leaderman.makemoney.hustle.ism.domain.model.WeiboModel;
import io.github.leaderman.makemoney.hustle.ism.domain.request.PushWeiboRequest;
import io.github.leaderman.makemoney.hustle.ism.service.InsightService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ism/push")
@Slf4j
public class PushController {
  private final InsightService insightService;

  @PostMapping("/weibo")
  public Response<Void> pushWeibo(@RequestBody PushWeiboRequest request) {
    try {
      WeiboModel model = WeiboModel.from(request);
      insightService.insight(model);

      return Response.ok();
    } catch (Exception e) {
      log.error("洞察微博错误：{}", ExceptionUtils.getStackTrace(e));
      return Response.internalServerError();
    }
  }
}
