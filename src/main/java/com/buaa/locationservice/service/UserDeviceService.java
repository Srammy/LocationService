package com.buaa.locationservice.service;

import com.buaa.locationservice.dao.UserDeviceDao;
import com.buaa.locationservice.model.UserDevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDeviceService {
    private final static Logger LOGGER = LoggerFactory.getLogger(UserDeviceService.class);

    @Autowired
    private UserDeviceDao userDeviceDao;

    /**
     * 删除用户和设备的关联信息
     * @param userDevice 用户设备关联
     */
    public boolean deleteUserDeviceByUserDevice(UserDevice userDevice) {
        LOGGER.info("删除用户id" + userDevice.getUserId() + "和设备id" + userDevice.getDeviceId() + "的关联信息");
        userDeviceDao.deleteByUserDevice(userDevice);

        // 删除该用户设备关联信息之后，查看该用户设备关联信息是否还存在
        List<UserDevice> userDeviceList = userDeviceDao.getByUserDevice(userDevice);

        if (userDeviceList == null || userDeviceList.size() == 0) {
            // 删除成功
            return true;
        } else {
            // 删除失败
            return false;
        }
    }

    /**
     * 新增用户和设备的关联信息
     * @param userDevice 用户设备关联
     * @return 操作是否成功
     */
    public boolean save(UserDevice userDevice) {
        LOGGER.info("新增用户id" + userDevice.getUserId() + "和设备id" + userDevice.getDeviceId() + "的关联信息");
        List<UserDevice> userDeviceList = userDeviceDao.getByUserDevice(userDevice);
        if (userDeviceList == null || userDeviceList.size() == 0) {
            userDeviceDao.saveUserDevice(userDevice);
            return true;
        } else {
            return false;
        }

    }
}
