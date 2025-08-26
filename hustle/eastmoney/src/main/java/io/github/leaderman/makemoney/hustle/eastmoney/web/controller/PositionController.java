package io.github.leaderman.makemoney.hustle.eastmoney.web.controller;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.leaderman.makemoney.hustle.domain.response.Response;
import io.github.leaderman.makemoney.hustle.eastmoney.domain.model.PositionModel;
import io.github.leaderman.makemoney.hustle.eastmoney.domain.request.SyncPositionRequest;
import io.github.leaderman.makemoney.hustle.eastmoney.service.PositionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/eastmoney/position")
@Slf4j
public class PositionController {
  private final PositionService positionService;

  @PostMapping("/sync")
  public Response<Void> syncPosition(@RequestBody SyncPositionRequest request) {
    try {
      PositionModel model = PositionModel.from(request);
      positionService.sync(model);

      return Response.ok();
    } catch (Exception e) {
      log.error("同步资金持仓错误：{}", ExceptionUtils.getStackTrace(e));
      return Response.internalServerError();
    }
  }
}
