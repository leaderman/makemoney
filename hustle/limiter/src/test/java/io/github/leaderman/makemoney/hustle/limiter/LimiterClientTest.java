package io.github.leaderman.makemoney.hustle.limiter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LimiterClientTest {
  @Autowired
  private LimiterClient limiterClient;

  @Test
  void testTryAcquire() throws InterruptedException {
    int count = 0;

    while (count < 100) {
      boolean acquired = limiterClient.tryAcquire("try_acquire", 3, 5);
      if (acquired) {
        count++;
        System.out.println("acquired: " + count);
      } else {
        System.out.println("not acquired");
      }

      Thread.sleep(1000);
    }
  }

  @Test
  void testAcquire() throws InterruptedException {
    int count = 0;

    while (count < 100) {
      limiterClient.acquire("acquire", 3, 5);
      System.out.println("acquired: " + count++);
    }
  }
}
