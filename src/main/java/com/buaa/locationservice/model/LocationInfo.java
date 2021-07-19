package com.buaa.locationservice.model;

import lombok.Data;

import java.util.Date;

/**
 * @Description: 位置信息
 */
@Data
public class LocationInfo {
    private long id;
    private String programNumber;
    private String platformNumber;
    private Date createDate;
    private String state;
    private String compressionIndex;
    private String mostRecentLocation;    // 原始的时间格式
    private String mostRecentDataCollection;    // 原始的时间格式
    private String mostRecentLocationChangeFormat;    // 处理后的时间格式（北京时间）
    private String mostRecentDataCollectionChangeFormat;    // 处理后的时间格式（北京时间）
    private String locationClass;
    private String latitude;
    private String longitude;
    private String locationInfo1;    // 前12字节
    private String locationInfo1Longitude;    // locationInfo1中的16进制转换为10进制后的经度(GPS)
    private String locationInfo1Latitude;    // locationInfo1中的16进制转换为10进制后的纬度(GPS)
    private String locationInfo1LongitudeBD;    // locationInfo1中的16进制转换为10进制后的经度(百度坐标)
    private String locationInfo1LatitudeBD;    // locationInfo1中的16进制转换为10进制后的纬度(百度坐标)
    private Date LocationInfo1Time;    // locationInfo1中的16进制转换为10进制后的时间
    private Date locationInfo1Date;
    private String locationInfo2;    // 中间12字节
    private String checkInfo;    // 后7字节
    private long deviceId;
    private double angle;
    private double speed;

    public LocationInfo(){
        this.state = "1";
        this.createDate = new Date();
    }
}
