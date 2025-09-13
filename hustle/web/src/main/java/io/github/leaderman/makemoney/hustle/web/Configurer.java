package io.github.leaderman.makemoney.hustle.web;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
  ApplicationRunner dumpRedis(ConfigurableApplicationContext ctx) {
    var bf = ctx.getBeanFactory();

    System.out.println("=== RedisConnectionFactory beans ===");
    bf.getBeansOfType(org.springframework.data.redis.connection.RedisConnectionFactory.class)
        .forEach((name, bean) -> {
          var src = bf.getBeanDefinition(name).getResourceDescription();
          System.out.printf("- %s -> %s | source=%s%n", name, bean.getClass().getName(), src);
        });

    bf.getBeansOfType(org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory.class)
        .forEach((name, f) -> System.out.printf(
            ">>>> %s host=%s port=%d db=%d username=%s%n",
            name, f.getHostName(), f.getPort(), f.getDatabase(), "none"));

    return args -> {
    };
  }

}
