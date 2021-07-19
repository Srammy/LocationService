package com.buaa.locationservice.dao;

import com.buaa.locationservice.model.DeviceInfo;
import com.buaa.locationservice.model.LocationInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Mapper
//@Repository
public interface DeviceServiceDao {
    /**
     * 根据设备号查询设备信息
     * @param deviceNumber 设备号
     * @return 设备信息
     */
    DeviceInfo getByDeviceNumber(@Param("deviceNumber") String deviceNumber);

    /**
     * 获取所有设备信息
     * @return 设备信息
     */
    List<DeviceInfo> getAll();

    /**
     * 查询设备信息
     * @param deviceInfo 设备信息
     * @return 设备信息列表
     */
    List<DeviceInfo> getByDeviceInfo(@Param("deviceInfo") DeviceInfo deviceInfo);

    /**
     * 新增设备信息
     * @param deviceInfo 设备信息
     * @return
     */
    void saveDeviceInfo(@Param("deviceInfo") DeviceInfo deviceInfo);

    /**
     * 查询某用户的所有设备
     * @param userId 用户ID
     * @return 设备信息列表
     */
    List<DeviceInfo> getByUser(@Param("userId") long userId);

    /**
     * 删除设备
     * @param deviceNumber 设备号
     */
    void deleteByDeviceNumber(@Param("deviceNumber") String deviceNumber);

    /**
     * 获取最后一个设备的id
     * @return 设备信息
     */
    DeviceInfo getLastDeviceId();
}
