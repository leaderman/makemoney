package io.github.leaderman.makemoney.hustle.web;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.MutablePropertySources;
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

  @Bean
  ApplicationRunner dumpRedisKeys(org.springframework.core.env.Environment env) {
    return args -> {
      if (env instanceof org.springframework.core.env.ConfigurableEnvironment ce) {
        System.out.println("=== All spring.redis.* from Environment ===");
        ce.getPropertySources().forEach(ps -> {
          if (ps instanceof org.springframework.core.env.EnumerablePropertySource<?> eps) {
            for (String k : eps.getPropertyNames()) {
              if (k.startsWith("spring.redis")) {
                System.out.printf("[%s] %s=%s%n", ps.getName(), k, eps.getProperty(k));
              }
            }
          }
        });
      }
    };
  }

  @Bean
  ApplicationRunner dumpSpringRedisProperties(ConfigurableEnvironment env) {
    return args -> {
      // 想看的键（可自行加减）
      List<String> keys = List.of(
          "spring.redis.url",
          "spring.redis.host",
          "spring.redis.port",
          "spring.redis.username",
          "spring.redis.password",
          "spring.redis.database",
          "spring.redis.timeout");

      System.out.println("=== RESOLVED (Environment#getProperty) ===");
      for (String k : keys) {
        System.out.printf("%s = %s%n", k, env.getProperty(k));
      }

      System.out.println("\n=== SOURCES by precedence (first wins) ===");
      // Spring 环境里，前面的 PropertySource 优先级更高
      MutablePropertySources sources = env.getPropertySources();
      int order = 0;
      for (PropertySource<?> ps : sources) {
        System.out.printf("[%02d] %s (%s)%n", order++, ps.getName(),
            ps.getClass().getSimpleName());
        if (ps instanceof EnumerablePropertySource<?> eps) {
          Set<String> nameSet = new HashSet<>(Arrays.asList(eps.getPropertyNames()));
          for (String k : keys) {
            if (nameSet.contains(k)) {
              Object v = eps.getProperty(k);
              System.out.printf("   - %s = %s%n", k, v);
            }
          }
        }
      }

      // 再看下 RedisProperties 实际绑定的值（Spring Boot 的绑定结果）
      try {
        var props = env.getProperty("spring.redis.host"); // 触发无意义，但示例留着
      } catch (Exception ignore) {
      }
      System.out.println("\n=== RedisProperties (bound) ===");
    };
  }

}
