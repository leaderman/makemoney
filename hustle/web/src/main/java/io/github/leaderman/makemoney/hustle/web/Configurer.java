package io.github.leaderman.makemoney.hustle.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import io.github.leaderman.makemoney.hustle.config.ConfigClient;
import io.github.leaderman.makemoney.hustle.web.interceptor.AuthInterceptor;
import io.github.leaderman.makemoney.hustle.web.interceptor.RequestIdInterceptor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class Configurer implements WebMvcConfigurer {
  private final RequestIdInterceptor requestIdInterceptor;
  private final AuthInterceptor authInterceptor;
  private final ConfigClient configClient;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(requestIdInterceptor);

    String[] excludes = configClient.getStringArray("web.auth.excludes", new String[] {});
    registry.addInterceptor(authInterceptor).excludePathPatterns(excludes);
  }
}
