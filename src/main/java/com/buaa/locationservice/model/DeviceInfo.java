package com.buaa.locationservice.model;

import lombok.Data;

/**
 * @Description: 设备信息
 */
@Data
public class DeviceInfo {
    private long id;
    private String deviceNumber;
    private String deviceName;
}
