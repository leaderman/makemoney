package io.github.leaderman.makemoney.hustle.limiter;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.bucket4j.distributed.ExpirationAfterWriteStrategy;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import io.github.bucket4j.redis.lettuce.Bucket4jLettuce;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;

@Configuration
public class LimiterConfig {
  @Value("${limiter.redis.host}")
  private String host;

  @Value("${limiter.redis.port}")
  private int port;

  @Value("${limiter.redis.password}")
  private String password;

  @Value("${limiter.redis.expiration}")
  private int expiration;

  @Bean
  public RedisClient redisClient() {
    return RedisClient.create(RedisURI.builder()
        .withHost(host)
        .withPort(port)
        .withAuthentication("default", password)
        .build());
  }

  @Bean
  public StatefulRedisConnection<String, byte[]> redisConnection(RedisClient client) {
    return client.connect(new StringByteArrayCodec());
  }

  @Bean
  public ProxyManager<String> proxyManager(StatefulRedisConnection<String, byte[]> connection) {
    return Bucket4jLettuce.casBasedBuilder(connection)
        .expirationAfterWrite(
            ExpirationAfterWriteStrategy.basedOnTimeForRefillingBucketUpToMax(Duration.ofSeconds(expiration)))
        .build();
  }
}
