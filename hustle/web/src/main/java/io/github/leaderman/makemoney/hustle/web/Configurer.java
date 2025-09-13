package io.github.leaderman.makemoney.hustle.web;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import io.github.leaderman.makemoney.hustle.config.ConfigClient;
import io.github.leaderman.makemoney.hustle.web.interceptor.AuthInterceptor;
import io.github.leaderman.makemoney.hustle.web.interceptor.RequestIdInterceptor;
import io.github.leaderman.makemoney.hustle.web.interceptor.ResponseTimeInterceptor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class Configurer implements WebMvcConfigurer {
  private final RequestIdInterceptor requestIdInterceptor;
  private final ResponseTimeInterceptor responseTimeInterceptor;
  private final AuthInterceptor authInterceptor;
  private final ConfigClient configClient;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(requestIdInterceptor);
    registry.addInterceptor(responseTimeInterceptor);

    String[] excludes = configClient.getStringArray("web.auth.excludes", new String[] {});
    registry.addInterceptor(authInterceptor).excludePathPatterns(excludes);
  }

  @Bean
  ApplicationRunner redisInfo(org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory f,
      StringRedisTemplate tpl) {
    return args -> {
      System.out.printf("[REDIS] host=%s, port=%d%n",
          f.getHostName(), f.getPort());
      System.out.println("[REDIS] CF from template same? " + (tpl.getConnectionFactory() == f));
    };
  }

}
