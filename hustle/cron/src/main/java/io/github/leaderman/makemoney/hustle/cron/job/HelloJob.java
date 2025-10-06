package io.github.leaderman.makemoney.hustle.cron.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
@DisallowConcurrentExecution
public class HelloJob extends QuartzJobBean {
  @Override
  protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
    System.out.println("Hello, Job!");
  }
}
