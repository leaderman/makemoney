package io.github.leaderman.makemoney.hustle.eastmoney.web.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.leaderman.makemoney.hustle.domain.response.Response;
import io.github.leaderman.makemoney.hustle.eastmoney.domain.request.SyncPositionRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/eastmoney")
@Slf4j
public class PositionController {
  @PostMapping("/position/sync")
  public Response<Void> syncPosition(@RequestBody SyncPositionRequest request) {
    log.info("syncPosition: {}", request);
    return Response.ok();
  }
}
