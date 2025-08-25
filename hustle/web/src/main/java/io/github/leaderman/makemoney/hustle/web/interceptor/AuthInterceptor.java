package io.github.leaderman.makemoney.hustle.web.interceptor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.leaderman.makemoney.hustle.config.ConfigClient;
import io.github.leaderman.makemoney.hustle.domain.response.Response;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {
  private static final String AUTHORIZATION = "Authorization";
  private static final String BEARER = "Bearer ";

  private final ConfigClient configClient;
  private final ObjectMapper objectMapper;

  private final Set<String> tokens = new HashSet<>();

  @PostConstruct
  public void init() {
    this.tokens.addAll(Arrays.asList(configClient.getStringArray("web.auth.tokens")));
  }

  private void writeUnauthorizedResponse(HttpServletResponse response) throws Exception {
    response.getWriter().write(objectMapper.writeValueAsString(Response.unauthorized()));
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    String authorization = request.getHeader(AUTHORIZATION);
    if (StringUtils.isEmpty(authorization) || !authorization.startsWith(BEARER)) {
      writeUnauthorizedResponse(response);
      return false;
    }

    String token = authorization.substring(BEARER.length());
    if (StringUtils.isEmpty(token)) {
      writeUnauthorizedResponse(response);
      return false;
    }

    if (!tokens.contains(token)) {
      writeUnauthorizedResponse(response);
      return false;
    }

    return true;
  }
}
