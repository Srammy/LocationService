package com.buaa.locationservice.service;

import com.buaa.locationservice.dao.DeviceServiceDao;
import com.buaa.locationservice.model.DeviceInfo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceService {
    private final static Logger LOGGER = LoggerFactory.getLogger(DeviceService.class);

    @Autowired
    private DeviceServiceDao deviceServiceDao;

    /**
     * 查询所有的设备信息
     * @return 设备信息列表
     */
    public List<DeviceInfo> getDeviceInfoAll() {
        LOGGER.info("查询所有设备信息");
        return deviceServiceDao.getAll();
    }

    /**
     * 查询某用户的所有设备
     * @param userId 用户ID
     * @return 设备信息列表
     */
    public List<DeviceInfo> getDeviceInfoByUser(String userId) {
        LOGGER.info("查询某用户的所有设备");
        if ("ALL".equals(userId)) {
            return getDeviceInfoAll();
        } else {
            return deviceServiceDao.getByUser(Long.parseLong(userId));
        }
    }

    /**
     * 新增设备
     * @param deviceInfo 设备信息
     * @return 操作是否成功
     */
    public boolean addDevice(DeviceInfo deviceInfo) {
        LOGGER.info("新增设备");
        List<DeviceInfo> deviceList = deviceServiceDao.getByDeviceInfo(deviceInfo);
        if (deviceList == null || deviceList.size() == 0) {
            deviceServiceDao.saveDeviceInfo(deviceInfo);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 删除设备
     * @param deviceNumber 设备号
     */
    public boolean deleteDeviceByDeviceNumber(String deviceNumber) {
        LOGGER.info("删除设备号为" + deviceNumber + "的设备");
        DeviceInfo deviceInfo = null;
        deviceServiceDao.deleteByDeviceNumber(deviceNumber);

        // 删除该设备之后，查看该设备是否还存在
        deviceInfo = deviceServiceDao.getByDeviceNumber(deviceNumber);
        if (deviceInfo == null) {
            // 成功删除该设备
            return true;
        } else {
            // 删除该设备失败
            return false;
        }
    }

}
