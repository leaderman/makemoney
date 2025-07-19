package io.github.leaderman.makemoney.hustle.limiter;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.distributed.proxy.ProxyManager;

/**
 * 限流器客户端，基于令牌桶算法实现。
 */
@Component
public class LimiterClient {
  private final String prefix;
  private final ProxyManager<String> proxyManager;
  private final ConcurrentMap<String, Bucket> buckets = new ConcurrentHashMap<>();

  public LimiterClient(@Value("${limiter.redis.prefix}") String prefix, ProxyManager<String> proxyManager) {
    this.prefix = prefix;
    this.proxyManager = proxyManager;
  }

  private Bucket getOrCreateBucket(String key, int capacity, int seconds) {
    String fullKey = prefix + key;

    return buckets.computeIfAbsent(fullKey,
        k -> proxyManager.builder().build(k, () -> BucketConfiguration.builder()
            .addLimit((limit -> limit.capacity(capacity)
                .refillGreedy(capacity, Duration.ofSeconds(seconds))
                .initialTokens(capacity)))
            .build()));
  }

  /**
   * 尝试获取令牌。
   * 
   * @param key      限流器名称。
   * @param capacity 令牌桶容量。
   * @param seconds  令牌桶填充时间。
   * @return 是否获取成功。
   */
  public boolean tryAcquire(String key, int capacity, int seconds) {
    return getOrCreateBucket(key, capacity, seconds).tryConsume(1);
  }

  /**
   * 获取令牌。
   * 
   * @param key      限流器名称。
   * @param capacity 令牌桶容量。
   * @param seconds  令牌桶填充时间。
   * @throws InterruptedException
   */
  public void acquire(String key, int capacity, int seconds) throws InterruptedException {
    getOrCreateBucket(key, capacity, seconds).asBlocking().consume(1);
  }
}
