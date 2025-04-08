package com.heng.property_manager.config;

import com.heng.property_manager.quartz.AutoAddMaintenanceJob;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

@Configuration
public class QuartzConfig {

    @Bean
    public JobDetailFactoryBean autoAddMaintenanceJobDetail() {
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(AutoAddMaintenanceJob.class);
        factoryBean.setName("postScoreRefreshJob");
        factoryBean.setGroup("communityJobGroup");
        factoryBean.setDurability(true);
        factoryBean.setRequestsRecovery(true);
        return factoryBean;
    }

    @Bean
    public CronTriggerFactoryBean autoAddMaintenanceTrigger(JobDetail autoAddMaintenanceJobDetail) {
        CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean();
        factoryBean.setJobDetail(autoAddMaintenanceJobDetail);
        factoryBean.setName("autoAddMaintenanceTrigger");
        factoryBean.setGroup("autoAddMaintenanceTriggerGroup");
        factoryBean.setCronExpression( "0 0 0 * * ?"); //每天凌晨0:00:00执行一次
        factoryBean.setJobDataMap(new JobDataMap());  // Trigger 底层以 JobDataMap() 数据类型保存Job的一些状态
        return factoryBean;
    }

}
