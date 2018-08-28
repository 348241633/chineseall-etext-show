package com.common.quartz;

import com.common.quartz.task.SyncDictTask;
import com.common.quartz.task.VideoFileTask;
import com.common.util.DateUtils;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

/**
 * Created by lego-jspx01 on 2018/7/30.
 */
@Configuration
public class QuartzConfiguration {

    @Value("${task.open}")
    private String taskOpen;


    // 配置定时任务3
    @Bean(name = "syncDictJobDetail")
    public MethodInvokingJobDetailFactoryBean syncDictJobDetail(SyncDictTask syncDictTask) {
        MethodInvokingJobDetailFactoryBean jobDetail = new MethodInvokingJobDetailFactoryBean();
        // 是否并发执行
        jobDetail.setConcurrent(false);
        // 为需要执行的实体类对应的对象
        jobDetail.setTargetObject(syncDictTask);
        // 需要执行的方法
        jobDetail.setTargetMethod("task");
        return jobDetail;
    }


    // 配置触发器3
    @Bean(name = "syncDictTrigger")
    public SimpleTriggerFactoryBean syncDictTrigger(JobDetail syncDictJobDetail) {
        SimpleTriggerFactoryBean trigger = new SimpleTriggerFactoryBean();
        trigger.setJobDetail(syncDictJobDetail);
        // 设置任务启动延迟
        trigger.setStartDelay(0);
        // 每5秒执行一次
        trigger.setStartTime(DateUtils.getDateFormat("2018-08-01 01:00:00","yyyy-MM-dd HH:mm:ss"));
        trigger.setRepeatInterval(1000 * 60 * 60 * 24);
        return trigger;
    }

    // 配置定时任务4
    @Bean(name = "videoFileDetail")
    public MethodInvokingJobDetailFactoryBean syncVideoFileDetail(VideoFileTask videoFileDetail) {
        MethodInvokingJobDetailFactoryBean jobDetail = new MethodInvokingJobDetailFactoryBean();
        // 是否并发执行
        jobDetail.setConcurrent(false);
        // 为需要执行的实体类对应的对象
        jobDetail.setTargetObject(videoFileDetail);
        // 需要执行的方法
        jobDetail.setTargetMethod("task");
        return jobDetail;
    }


    // 配置触发器4
    @Bean(name = "videoFileTaskTrigger")
    public SimpleTriggerFactoryBean syncVideoFileTaskTrigger(JobDetail videoFileDetail) {
        SimpleTriggerFactoryBean trigger = new SimpleTriggerFactoryBean();
        trigger.setJobDetail(videoFileDetail);
        // 设置任务启动延迟
        trigger.setStartDelay(0);
        // 每5秒执行一次
        trigger.setRepeatInterval(1000 * 60 * 10);
        return trigger;
    }


    @Bean(name = "scheduler")
    public SchedulerFactoryBean schedulerFactory(Trigger syncDictTrigger, Trigger videoFileTaskTrigger) {
        SchedulerFactoryBean bean = new SchedulerFactoryBean();
        // 延时启动，应用启动1秒后
        bean.setStartupDelay(1);
        // 注册触发器
        if("open".equals(taskOpen))
            bean.setTriggers(syncDictTrigger,videoFileTaskTrigger);
        return bean;
    }

}
