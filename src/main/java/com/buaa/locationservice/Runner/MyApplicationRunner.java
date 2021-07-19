package com.buaa.locationservice.Runner;

import com.buaa.locationservice.Job.LocationJobHighFrequency;
import com.buaa.locationservice.Job.LocationJobLowFrequency;
import com.buaa.locationservice.Job.ScheduleManager;
import com.buaa.locationservice.utils.Constants;
import com.buaa.locationservice.utils.StringUtils;
import org.quartz.CronExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
* @des 继承ApplicationRunner，在此可进行一些程序启动后的操作，如启动一个定时任务
*      在此操作定时任务，将使得程序启动后便会执行该定时逻辑，适合伴随程序启动的定时场景
*/
@Component
public class MyApplicationRunner implements ApplicationRunner {
    private final static Logger LOGGER = LoggerFactory.getLogger(MyApplicationRunner.class);

    @Autowired
    private ScheduleManager scheduleManager;

    @Value("${quartz.jobType}")
    private String jobType;
    @Value("${quartz.jobName}")
    private String jobName;
    @Value("${quartz.jobGroupName}")
    private String jobGroupName;
    @Value("${quartz.triggerName}")
    private String triggerName;
    @Value("${quartz.triggerGroupName}")
    private String triggerGroupName;
    @Value("${quartz.time}")
    private String time;
    @Value("${spring.quartz.job-store-type}")
    private String jobStoreType;
    @Value("${quartz.jobHighFrequency}")
    private boolean jobHighFrequency;

    @Override
    public void run(ApplicationArguments applicationArguments) {
        LOGGER.info("--MyApplicationRunner--");
        boolean durability = false;
        Class jobClass =  LocationJobLowFrequency.class;

        if(Constants.jobStoreTypeJdbc.equals(jobStoreType)){
            jobClass =  LocationJobLowFrequency.class;
            durability = true;
            LOGGER.info("job存储方式："+Constants.jobStoreTypeJdbc);
        }else if(Constants.jobStoreTypeMemory.equals(jobStoreType)){
            jobClass =  LocationJobLowFrequency.class;
            durability = false;
            if(jobHighFrequency){
                jobClass = LocationJobHighFrequency.class;
            }
            LOGGER.info("job存储方式："+Constants.jobStoreTypeMemory);
        }else{
            LOGGER.warn("不存在存储类型："+jobStoreType+"，默认为："+Constants.jobStoreTypeMemory+"请选择:"+Constants.jobStoreTypeJdbc+"或者"+Constants.jobStoreTypeMemory);
        }

        if(Constants.jobTypeSimple.equals(jobType)){
            int interval = Constants.time;
            if(StringUtils.testIsInt(time)){
                interval = Integer.parseInt(time);
            }
            if(scheduleManager.checkJob(jobName,jobGroupName) && scheduleManager.checkTrigger(triggerName,triggerGroupName)){
                LOGGER.info("Job与Trigger已存在不新增");
            }else{
                scheduleManager.addJob(jobName,jobGroupName,triggerName,triggerGroupName,
                        jobClass,durability,interval,5);
            }


        }else if (Constants.jobTypeCron.equals(jobType)){
            if(!CronExpression.isValidExpression(time)){
                time = Constants.cron;
            }
            if(scheduleManager.checkJob(jobName,jobGroupName) && scheduleManager.checkTrigger(triggerName,triggerGroupName)){
                LOGGER.info("Job与Trigger已存在不新增");
            }else {
                scheduleManager.addJob(jobName, jobGroupName, triggerName, triggerGroupName,
                        jobClass, durability, time, 5);
            }
        }else{
            LOGGER.warn("--MyApplicationRunner--不存在jobType："+jobType+",请选择："+Constants.jobTypeSimple+"或者"+Constants.jobTypeCron);
        }

    }
}
