package com.buaa.locationservice.utils;

public class Constants {
    //telnet状态
    public static final String success = "0000";
    public static final String error = "0001";
    //job类型
    public static final String jobTypeSimple = "simple";//2
    public static final String jobTypeCron = "cron";//1
    //job默认定时规则一天一次
    public static final int time = 86400;
    public static final String cron = "0 0 1 * * ?";
    //quartz模式
    public static final String jobStoreTypeMemory = "memory";
    public static final String jobStoreTypeJdbc = "jdbc";
    //response状态
    public static final String responseCodeSuccess = "0000-处理成功";
    public static final String responseCodeError = "0001-处理失败";
}
