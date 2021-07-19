package com.buaa.locationservice.Job;

import com.buaa.locationservice.utils.Constants;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @des schedule任务管理类，动态实现开始，暂停，停止等操作
 */
@Component
public class ScheduleManager {
    private final static Logger LOGGER = LoggerFactory.getLogger(ScheduleManager.class);
    @Autowired
    private Scheduler scheduler;

    /**
     * 使用SimpleScheduleBuilder创建一个定时任务
     * @param jobName job名称
     * @param jobGroupName job分组名称
     * @param triggerName 触发器名称
     * @param triggerGroupName 触发器分组名称
     * @param jobClass job的class名
     * @param durability 是否持久化
     * @param interval 间隔时间，单位秒
     * @param delay 延迟启动时间，单位秒
     * @return
     */
    public Boolean addJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName, Class jobClass, boolean durability, int interval, int delay) {
        /*
         * 1.取到任务调度器Scheduler
         * 2.定义jobDetail;
         * 3.定义trigger;
         * 4.使用Scheduler添加任务;
         */
        try {
            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroupName).storeDurably(durability).usingJobData("test","test").build();

            SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                    .withIntervalInSeconds(interval).repeatForever();

            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(triggerName, triggerGroupName)
                    .withSchedule(scheduleBuilder)
                    .usingJobData("test1","test1")
                    .startAt(new Date(new Date().getTime() + delay * 1000))
                    .build();

            scheduler.scheduleJob(jobDetail, trigger);
            return true;
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 使用CronScheduleBuilder创建一个定时任务
     *
     * @param jobName job名称
     * @param jobGroupName job分组名称
     * @param triggerName 触发器名称
     * @param triggerGroupName 触发器分组名称
     * @param jobClass job的class名
     * @param durability 是否持久化
     * @param cronExpression 定时表达式
     * @param delay 延迟启动时间，单位秒
     * @return
     */
    public Boolean addJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName, Class jobClass, boolean durability, String cronExpression, int delay) {
        /*
         * 1.取到任务调度器Scheduler
         * 2.定义jobDetail;
         * 3.定义trigger;
         * 4.使用Scheduler添加任务;
         */
        try {
            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroupName).storeDurably(durability).build();

            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(triggerName, triggerGroupName)
                    .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                    .startAt(new Date(new Date().getTime() + delay * 1000))
                    .build();

            scheduler.scheduleJob(jobDetail, trigger);
            return true;
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return false;
    }
    /**
     * 动态停止job任务
     * @param jobName job名称
     * @param jobGroupName job分组名称
     * @return
     */
    public Boolean pause(String jobName, String jobGroupName) {
        try {
            JobKey key = new JobKey(jobName, jobGroupName);
            scheduler.pauseJob(key);
            return true;
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 检查job任务是否存在
     * @param jobName job名称
     * @param jobGroupName job分组名称
     * @return
     */
    public Boolean checkJob(String jobName, String jobGroupName) {
        try {
            JobKey key = new JobKey(jobName, jobGroupName);
            return scheduler.checkExists(key);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return false;
    }
    /**
     * 检查trigger是否存在
     * @param triggerName trigger名称
     * @param triggerGroupName trigger分组名称
     * @return
     */
    public Boolean checkTrigger(String triggerName, String triggerGroupName) {
        try {
            TriggerKey key = new TriggerKey(triggerName, triggerGroupName);
            return scheduler.checkExists(key);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 动态开始job任务
     *
     * @param jobName job名称
     * @param jobGroupName job分组名称
     * @return
     */
    public Boolean start(String jobName, String jobGroupName) {
        try {
            JobKey key = new JobKey(jobName, jobGroupName);
            scheduler.resumeJob(key);
            return true;
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 移除定时任务
     *
     * @param jobName job名称
     * @param jobGroupName job分组名称
     * @param triggerName 触发器名称
     * @param triggerGroupName 触发器分组名称
     * @return
     */
    public Boolean delete(String jobName, String jobGroupName, String triggerName, String triggerGroupName) {
        try {
            Integer status =getJobStatus(triggerName, triggerGroupName);
            if (status == 0) {
                //NONE - 0,该job不存在
                LOGGER.warn("NONE - 0,该job不存在");
                return null;
            }

            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);//通过触发器名和组名获取TriggerKey
            JobKey jobKey = new JobKey(jobName, jobGroupName); //通过任务名和组名获取JobKey
            scheduler.pauseTrigger(triggerKey);    //停止触发器
            scheduler.unscheduleJob(triggerKey);//移除触发器
            scheduler.deleteJob(jobKey);// 删除任务
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 动态修改任务执行的时间
     *
     * @param type 创建类型，1-cron,2-simple
     * @param jobName job名称
     * @param jobGroupName job分组名称
     * @param triggerName 触发器名称
     * @param triggerGroupName 触发器分组名称
     * @param time type为1时-填cron表达式，为2-整数
     * @return
     */
    public Boolean modify(String type,String jobName, String jobGroupName, String triggerName, String triggerGroupName, String time) {

        try {
            Integer status =getJobStatus(triggerName, triggerGroupName);
            if (status == 0) {
                //NONE - 0,该job不存在
                LOGGER.warn("NONE - 0,该job不存在");
                return false;
            }

            //获取任务
            JobKey key = new JobKey(jobName, jobGroupName);
            //获取jobDetail
            JobDetail jobDetail = scheduler.getJobDetail(key);
            //生成trigger
            if (Constants.jobTypeCron.equals(type)) {
                // 1.CronSchedule
                Trigger trigger = TriggerBuilder
                        .newTrigger()
                        .withIdentity(triggerName, triggerGroupName)
                        .withSchedule(CronScheduleBuilder.cronSchedule(time))
                        .build();
                //删除旧的任务，否则报错
                //scheduler.deleteJob(key);
                delete(jobName, jobGroupName, triggerName, triggerGroupName);
                //重新启动任务
                scheduler.scheduleJob(jobDetail, trigger);
                return true;
            }

            if (Constants.jobTypeSimple.equals(type)) {
                // 2.SimpleSchedule
                Trigger trigger = TriggerBuilder
                        .newTrigger()
                        .withIdentity(triggerName, triggerGroupName)
                        .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(Integer.valueOf(time)))
                        .build();
                //删除旧的任务，否则报错
                //scheduler.deleteJob(key);
                delete(jobName, jobGroupName, triggerName, triggerGroupName);
                //重新启动任务
                scheduler.scheduleJob(jobDetail, trigger);
                return true;
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
    /**
     * 动态修改任务执行的时间
     *
     * @param type 创建类型，1-cron,2-simple
     * @param triggerName 触发器名称
     * @param triggerGroupName 触发器分组名称
     * @param time type为1时-填cron表达式，为2-整数
     * @return
     */
    public Boolean modifyTrigger(String type, String triggerName, String triggerGroupName, String time) {

        try {
            Integer status =getJobStatus(triggerName, triggerGroupName);
            if (status == 0) {
                //NONE - 0,该job不存在
                LOGGER.warn("NONE - 0,该job不存在");
                return false;
            }

            if (Constants.jobTypeCron.equals(type)) {
                // 1.CronSchedule
                Trigger trigger = TriggerBuilder
                        .newTrigger()
                        .withIdentity(triggerName, triggerGroupName)
                        .withSchedule(CronScheduleBuilder.cronSchedule(time))
                        .build();

                //重新启动任务
                scheduler.rescheduleJob(new TriggerKey(triggerName,triggerGroupName),trigger);
                return true;
            }

            if (Constants.jobTypeSimple.equals(type)) {
                // 2.SimpleSchedule
                Trigger trigger = TriggerBuilder
                        .newTrigger()
                        .withIdentity(triggerName, triggerGroupName)
                        .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(Integer.valueOf(time)))
                        .build();

                //重新启动任务
                scheduler.rescheduleJob(new TriggerKey(triggerName,triggerGroupName),trigger);
                return true;
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
    /**
     * 查看job运行状态
     * @param triggerName 触发器名称
     * @param triggerGroupName 触发器分组名称
     * @return TriggerState枚举值： NONE - 0；NORMAL - 1；PAUSED - 2；COMPLETE - 3；ERROR - 4；BLOCKED - 5
     */
    public Integer getJobStatus(String triggerName, String triggerGroupName) {

        /*
            TriggerState枚举值
            NONE - 0,
            NORMAL -1,
            PAUSED - 2,
            COMPLETE - 3,
            ERROR - 4,
            BLOCKED - 5;
        */
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
            return scheduler.getTriggerState(triggerKey).ordinal();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 启动所有定时任务
     */
    public Boolean startAll() {
        try {
            scheduler.start();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 关闭所有定时任务
     */
    public Boolean shutdownAll() {
        try {
            if (!scheduler.isShutdown()) {
                scheduler.shutdown();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 清理数据库中的定时任务
     */
    public Boolean taskClear() {
        try {
            scheduler.clear();
            return true;
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return false;
    }
}