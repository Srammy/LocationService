package com.buaa.locationservice.dao;

import com.buaa.locationservice.model.UserDevice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
//@Repository
public interface UserDeviceDao {
    /**
     * 新增用户和设备的关联信息
     * @param userDevice 用户设备关联
     * @return
     */
    void saveUserDevice(@Param("userDevice") UserDevice userDevice);

    /**
     * 删除用户和设备的关联信息
     * @param userDevice 用户设备关联
     */
    void deleteByUserDevice(@Param("userDevice") UserDevice userDevice);

    /**
     * 查询用户和设备的关联信息
     * @param userDevice 用户设备关联
     * @return
     */
    List<UserDevice> getByUserDevice(@Param("userDevice") UserDevice userDevice);
}
