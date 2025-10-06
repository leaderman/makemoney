package io.github.leaderman.makemoney.hustle.cron.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@DisallowConcurrentExecution
@Slf4j
public class HelloJob extends QuartzJobBean {
  @Override
  protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
    log.info("Hello, Job!");
  }
}
