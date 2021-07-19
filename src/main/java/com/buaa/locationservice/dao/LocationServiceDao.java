package com.buaa.locationservice.dao;

import com.buaa.locationservice.model.LocationInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Mapper
//@Repository
public interface LocationServiceDao {
    /**
     * 根据位置信息的编号查询位置信息
     * @param id 位置信息的编号
     * @return 位置信息
     */
    LocationInfo getById(@Param("id") long id);

    /**
     * 获取（某设备）所有的位置信息
     * @return 位置信息
     */
    List<LocationInfo> getAll();

    /**
     * 获取（某设备）所有的位置信息
     * @param id 设备ID
     * @return 位置信息
     */
    List<LocationInfo> getAllByDeviceId(@Param("id") long id);

    /**
     * 新增位置信息
     * @param locationInfo 位置信息
     */
    void saveLocationInfo(@Param("locationInfo") LocationInfo locationInfo);

    /**
     * 查询某设备最新的位置信息
     * @param deviceId 设备ID
     * @return 最新的位置信息
     */
    LocationInfo getLast(@Param("deviceId") long deviceId);

    /**
     * 查询某设备在某一时间段内（日级）的所有位置信息
     * @param deviceId 设备ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 位置信息列表
     */
    List<LocationInfo> getByTime(@Param("deviceId") long deviceId,@Param("startDate") String startDate, @Param("endDate") String endDate);
}
