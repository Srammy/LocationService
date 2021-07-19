package com.buaa.locationservice.model;

import lombok.Data;

/**
 * @Description: 用户设备关联
 */
@Data
public class UserDevice {
    private long id;
    private long userId;
    private long deviceId;
}
