package io.github.leaderman.makemoney.hustle.web.interceptor;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ResponseTimeInterceptor implements HandlerInterceptor {
  private static final String X_RESPONSE_TIME = "X-Response-Time";

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    MDC.put(X_RESPONSE_TIME, String.valueOf(System.currentTimeMillis()));

    return true;
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
      throws Exception {
    String uri = request.getRequestURI();

    Long startTime = Long.parseLong(MDC.get(X_RESPONSE_TIME));
    Long responseTime = System.currentTimeMillis() - startTime;

    log.info("{} {} ms", uri, responseTime);

    MDC.remove(X_RESPONSE_TIME);
  }
}
