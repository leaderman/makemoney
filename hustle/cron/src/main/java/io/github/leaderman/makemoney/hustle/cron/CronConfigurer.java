package io.github.leaderman.makemoney.hustle.cron;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.leaderman.makemoney.hustle.config.ConfigClient;
import io.github.leaderman.makemoney.hustle.cron.listener.CronJobListener;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class CronConfigurer {
  private final ConfigClient configClient;
  private final ObjectMapper objectMapper;

  private final Scheduler scheduler;
  private final CronJobListener cronJobListener;

  @Data
  private static class JobSpec {
    private String name;
    private String cron;
    private String impl;
  }

  private JobKey buildJobKey(String name) {
    return new JobKey(name);
  }

  private TriggerKey buildTriggerKey(String name) {
    return new TriggerKey(name);
  }

  private CronTrigger buildCronTrigger(String name, String cron) {
    return TriggerBuilder.newTrigger()
        .forJob(name)
        .withIdentity(name)
        .withSchedule(CronScheduleBuilder
            .cronSchedule(cron)
            .withMisfireHandlingInstructionDoNothing())
        .build();
  }

  private JobDetail buildJobDetail(String name, String impl) throws Exception {
    return JobBuilder.newJob((Class.forName(impl)).asSubclass(Job.class))
        .withIdentity(name)
        .build();
  }

  @PostConstruct
  public void configure() {
    try {
      // 添加监听器。
      scheduler.getListenerManager().addJobListener(cronJobListener);

      String json = this.configClient.getString("cron.jobs");
      if (StringUtils.isEmpty(json)) {
        return;
      }

      List<JobSpec> jobSpecs = this.objectMapper.readValue(json, new TypeReference<List<JobSpec>>() {
      });
      if (CollectionUtils.isEmpty(jobSpecs)) {
        return;
      }

      Set<String> names = jobSpecs.stream().map(JobSpec::getName).collect(Collectors.toSet());
      if (jobSpecs.size() != names.size()) {
        throw new IllegalArgumentException("定时任务名称重复");
      }

      for (JobSpec jobSpec : jobSpecs) {
        String name = jobSpec.getName();
        String cron = jobSpec.getCron();

        JobKey jobKey = buildJobKey(name);
        TriggerKey triggerKey = buildTriggerKey(name);

        if (scheduler.checkExists(jobKey)) {
          // 定时任务已存在。
          CronTrigger existingTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
          if (cron.equals(existingTrigger.getCronExpression())) {
            // 定时任务调度时间相同，跳过。
            continue;
          }

          // 定时任务调度时间不同，更新。
          scheduler.rescheduleJob(triggerKey, buildCronTrigger(name, cron));
          log.info("定时任务 {} 更新调度时间: {}", name, cron);

          continue;
        }

        // 定时任务不存在，新增。
        scheduler.scheduleJob(buildJobDetail(name, jobSpec.getImpl()), buildCronTrigger(name, cron));
        log.info("新增定时任务 {}，调度时间: {}，实现类: {}", name, cron, jobSpec.getImpl());
      }

      for (JobKey existingJobKey : scheduler.getJobKeys(GroupMatcher.anyGroup())) {
        if (names.contains(existingJobKey.getName())) {
          // 定时任务存在，跳过。
          continue;
        }

        // 定时任务不存在，删除。
        scheduler.deleteJob(existingJobKey);
        log.info("删除定时任务 {}", existingJobKey.getName());
      }
    } catch (Exception e) {
      log.error("定时任务配置错误: {}", ExceptionUtils.getStackTrace(e));
    }
  }
}
