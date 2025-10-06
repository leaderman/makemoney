package io.github.leaderman.makemoney.hustle.cron;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;

import io.github.leaderman.makemoney.hustle.config.ConfigClient;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class CronConfigurer {
  private final ConfigClient configClient;

  @PostConstruct
  public void configure() {
    String json = this.configClient.getString("cron.jobs");
    if (StringUtils.isEmpty(json)) {
      return;
    }

    log.info("Cron jobs: {}", json);
  }
}
