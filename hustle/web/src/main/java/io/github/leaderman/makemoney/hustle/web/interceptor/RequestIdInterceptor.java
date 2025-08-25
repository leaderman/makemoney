package io.github.leaderman.makemoney.hustle.web.interceptor;

import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RequestIdInterceptor implements HandlerInterceptor {
  private static final String X_REQUEST_ID = "X-Request-Id";

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    String requestId = UUID.randomUUID().toString();
    MDC.put(X_REQUEST_ID, requestId);

    response.setHeader(X_REQUEST_ID, requestId);

    return true;
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
      throws Exception {
    MDC.remove(X_REQUEST_ID);
  }
}
