package io.github.leaderman.makemoney.hustle.eastmoney.web.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.leaderman.makemoney.hustle.domain.response.Response;
import io.github.leaderman.makemoney.hustle.eastmoney.domain.model.FundModel;
import io.github.leaderman.makemoney.hustle.eastmoney.domain.request.SyncFundRequest;
import io.github.leaderman.makemoney.hustle.eastmoney.service.FundService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/eastmoney/fund")
@Slf4j
public class FundController {
  private final FundService fundService;

  @PostMapping("/sync")
  public Response<Void> sync(@RequestBody SyncFundRequest request) {
    try {
      List<SyncFundRequest.Fund> funds = request.getFunds();
      if (CollectionUtils.isEmpty(funds)) {
        return Response.ok();
      }

      fundService.sync(funds.stream().map(FundModel::from).collect(Collectors.toList()));

      return Response.ok();
    } catch (Exception e) {
      log.error("同步今日委托错误：{}", ExceptionUtils.getStackTrace(e));
      return Response.internalServerError();
    }
  }
}
