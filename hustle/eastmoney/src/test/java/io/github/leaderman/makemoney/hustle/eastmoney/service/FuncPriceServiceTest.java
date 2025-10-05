package io.github.leaderman.makemoney.hustle.eastmoney.service;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import io.github.leaderman.makemoney.hustle.eastmoney.domain.model.FundBucketPriceModel;
import io.github.leaderman.makemoney.hustle.lang.DatetimeUtil;

@SpringBootTest
public class FuncPriceServiceTest {
  @Autowired
  private FundPriceService fundPriceService;

  @Test
  public void testCount() {
    System.out.println(fundPriceService.count("159568",
        DatetimeUtil.parseDatetime("2025-09-30 00:00:00"),
        DatetimeUtil.parseDatetime("2025-09-30 23:59:59")));

    System.out.println(fundPriceService.count("159568"));
  }

  @Test
  public void testBucket() {
    List<FundBucketPriceModel> bucketPrices = fundPriceService.bucket("513970", 300,
        DatetimeUtil.parseDatetime("2025-09-30 00:00:00"),
        DatetimeUtil.parseDatetime("2025-09-30 23:59:59"));
    bucketPrices.forEach(System.out::println);

    bucketPrices = fundPriceService.bucket("513970", 600);
    bucketPrices.forEach(System.out::println);
  }

  @Test
  public void testShouldIntervene() {
    String code = "520500";
    int size = 60;
    LocalDateTime start = DatetimeUtil.parseDatetime("2025-09-30 09:30:00");
    LocalDateTime end = DatetimeUtil.parseDatetime("2025-09-30 13:08:59");

    System.out.println(fundPriceService.shouldIntervene(code, size, start, end));
  }
}
