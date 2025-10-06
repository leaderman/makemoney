package io.github.leaderman.makemoney.hustle.eastmoney.cron.job;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import io.github.leaderman.makemoney.hustle.eastmoney.service.FundPriceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@DisallowConcurrentExecution
@Slf4j
public class FundPriceInterveneJob extends QuartzJobBean {
  private final FundPriceService fundPriceService;

  @Override
  protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
    try {
      this.fundPriceService.intervene();
    } catch (Exception e) {
      log.error("基金价格介入错误：{}", ExceptionUtils.getStackTrace(e));
    }
  }

}
