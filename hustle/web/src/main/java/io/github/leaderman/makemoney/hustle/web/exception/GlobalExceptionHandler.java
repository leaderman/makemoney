package io.github.leaderman.makemoney.hustle.web.exception;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import io.github.leaderman.makemoney.hustle.domain.response.Response;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
  @ExceptionHandler({ Throwable.class })
  @ResponseBody
  public Response<Void> handle(Throwable t) {
    log.error(ExceptionUtils.getStackTrace(t));

    return Response.internalServerError();
  }
}
