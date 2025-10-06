package io.github.leaderman.makemoney.hustle.cron.listener;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CronJobListener implements JobListener {
  @Override
  public String getName() {
    return "CronJobListener";
  }

  @Override
  public void jobToBeExecuted(JobExecutionContext context) {
    log.info("定时任务 {} 即将执行", context.getJobDetail().getKey());
  }

  @Override
  public void jobExecutionVetoed(JobExecutionContext context) {
    log.info("定时任务 {} 执行被拒绝", context.getJobDetail().getKey());
  }

  @Override
  public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
    log.info("定时任务 {} 执行完成", context.getJobDetail().getKey());
  }
}
