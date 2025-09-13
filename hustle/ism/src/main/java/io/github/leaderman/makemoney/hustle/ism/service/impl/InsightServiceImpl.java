package io.github.leaderman.makemoney.hustle.ism.service.impl;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import io.github.leaderman.makemoney.hustle.config.ConfigClient;
import io.github.leaderman.makemoney.hustle.ism.domain.model.WeiboModel;
import io.github.leaderman.makemoney.hustle.ism.service.InsightService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class InsightServiceImpl implements InsightService {
  private final ConfigClient configClient;

  private String insightKeyPrefix;
  private int insightKeyExpire;
  private final StringRedisTemplate stringRedisTemplate;

  @PostConstruct
  public void init() {
    this.insightKeyPrefix = this.configClient.getString("ism.insight.key.prefix");
    this.insightKeyExpire = this.configClient.getInt("ism.insight.key.expire");
  }

  private boolean exists(String href) {
    return this.stringRedisTemplate.hasKey(insightKeyPrefix + href);
  }

  private void add(String href) {
    this.stringRedisTemplate.opsForValue().set(insightKeyPrefix + href, href, insightKeyExpire, TimeUnit.SECONDS);
  }

  @Override
  public void insight(WeiboModel model) {
    String href = model.getHref();
    if (this.exists(href)) {
      log.info("微博已洞察：{}，跳过", href);
      return;
    }
    this.add(href);

    String html = model.getHtml();
    log.info("微博洞察：{}，{}", href, html);
  }
}
