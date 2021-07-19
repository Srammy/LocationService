package com.buaa.locationservice.service;

import com.buaa.locationservice.dao.LocationServiceDao;
import com.buaa.locationservice.model.LocationInfo;
import com.buaa.locationservice.utils.DateUtils;
import com.buaa.locationservice.utils.StringUtils;
import com.buaa.locationservice.utils.LocationInfoUtils;
import com.buaa.locationservice.utils.GPS2BaiDu;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.text.ParseException;
import java.util.List;

@Service
public class LocationService {
    private final static Logger LOGGER = LoggerFactory.getLogger(LocationService.class);

    @Autowired
    private LocationServiceDao locationServiceDao;

    /**
     * 根据位置信息的编号查询位置信息
     * @param id 位置信息的编号
     * @return 位置信息
     */
    public LocationInfo getLocationInfoById(long id) {
        LOGGER.info("根据位置信息的编号查询位置信息");
        LocationInfo locationInfo = locationServiceDao.getById(id);
        // 数据库中的mostRecentLocation和mostRecentDataCollection是原始格式，这里在查询数据的同时，修改了mostRecentLocation和mostRecentDataCollection的格式为正常时间格式
        locationInfo.setMostRecentLocation(DateUtils.StringToDateStr(locationInfo.getMostRecentLocation()));
        locationInfo.setMostRecentDataCollection(DateUtils.StringToDateStr(locationInfo.getMostRecentDataCollection()));
        return locationInfo;
    }

    /**
     * 获取（某设备）所有的位置信息
     * @param pageNum 页号
     * @param pageSize 页面大小
     * @param deviceId 设备ID
     * @return 位置信息
     */
    public PageInfo<LocationInfo> getLocationInfoAll(int pageNum, int pageSize,String deviceId) {
        LOGGER.info("查询所有位置信息/查询某设备的所有位置信息");
        PageHelper.startPage(pageNum, pageSize);
        PageInfo<LocationInfo> pageInfo = null;
        List<LocationInfo> locationInfoList = null;

        if (deviceId == null || "".equals(deviceId.trim()) || !(StringUtils.testIsLong(deviceId))) {
            locationInfoList = locationServiceDao.getAll();
        } else {
            locationInfoList = locationServiceDao.getAllByDeviceId(Long.parseLong(deviceId));
        }

        for (LocationInfo locationInfo : locationInfoList) {
            locationInfo.setMostRecentLocation(DateUtils.StringToDateStr(locationInfo.getMostRecentLocation()));
            locationInfo.setMostRecentDataCollection(DateUtils.StringToDateStr(locationInfo.getMostRecentDataCollection()));
        }

        pageInfo = new PageInfo<>(locationInfoList);
        return pageInfo;
    }

    /**
     * 查询某设备最新的位置信息
     * @param deviceId 设备ID
     * @return 最新的位置信息
     */
    public LocationInfo getLocationInfoLast(long deviceId) {
        LOGGER.info("查询某设备最新的位置信息");
        LocationInfo locationInfo = locationServiceDao.getLast(deviceId);
        //locationInfo.setMostRecentLocation(DateUtils.StringToDateStr(li.getMostRecentLocation()));
        //locationInfo.setMostRecentDataCollection(DateUtils.StringToDateStr(li.getMostRecentDataCollection()));
        locationInfo.setMostRecentLocationChangeFormat(DateUtils.StringToDateStr(locationInfo.getMostRecentLocation()));
        locationInfo.setMostRecentDataCollectionChangeFormat(DateUtils.StringToDateStr(locationInfo.getMostRecentDataCollection()));

        locationInfo.setLocationInfo1Longitude(LocationInfoUtils.HexToDec(locationInfo.getLocationInfo1().substring(0,8)));
        locationInfo.setLocationInfo1Latitude(LocationInfoUtils.HexToDec(locationInfo.getLocationInfo1().substring(8,16)));

        locationInfo.setLocationInfo1LongitudeBD(String.valueOf(GPS2BaiDu.wgs2bd(Double.valueOf(locationInfo.getLocationInfo1Latitude()),Double.valueOf(locationInfo.getLocationInfo1Longitude()))[1]));
        locationInfo.setLocationInfo1LatitudeBD(String.valueOf(GPS2BaiDu.wgs2bd(Double.valueOf(locationInfo.getLocationInfo1Latitude()),Double.valueOf(locationInfo.getLocationInfo1Longitude()))[0]));

        try {
            locationInfo.setLocationInfo1Time(DateUtils.UTCToLocal(StringUtils.sixteenToTen(locationInfo.getLocationInfo1().substring(16))));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return locationInfo;
    }

    /**
     * 查询某设备在某一时间段内（日级）的所有位置信息
     * @param deviceId 设备ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 位置信息列表
     */
    public List<LocationInfo> getLocationInfoByTime(long deviceId,String startDate,String endDate) {
        LOGGER.info("查询某设备在某一时间段内的位置信息");
        List<LocationInfo> locationInfoList = locationServiceDao.getByTime(deviceId, startDate, endDate);

        for (LocationInfo locationInfo : locationInfoList) {
            //locationInfo.setMostRecentLocation(DateUtils.StringToDateStr(locationInfo.getMostRecentLocation()));
            //locationInfo.setMostRecentDataCollection(DateUtils.StringToDateStr(locationInfo.getMostRecentDataCollection()));

            // 设置处理后的时间格式（北京时间）
            locationInfo.setMostRecentLocationChangeFormat(DateUtils.StringToDateStr(locationInfo.getMostRecentLocation()));
            locationInfo.setMostRecentDataCollectionChangeFormat(DateUtils.StringToDateStr(locationInfo.getMostRecentDataCollection()));

            // 设置GPS经纬度
            locationInfo.setLocationInfo1Longitude(LocationInfoUtils.HexToDec(locationInfo.getLocationInfo1().substring(0,8)));
            locationInfo.setLocationInfo1Latitude(LocationInfoUtils.HexToDec(locationInfo.getLocationInfo1().substring(8,16)));

            // 设置百度地图经纬度
            locationInfo.setLocationInfo1LongitudeBD(String.valueOf(GPS2BaiDu.wgs2bd(Double.valueOf(locationInfo.getLocationInfo1Latitude()),Double.valueOf(locationInfo.getLocationInfo1Longitude()))[1]));
            locationInfo.setLocationInfo1LatitudeBD(String.valueOf(GPS2BaiDu.wgs2bd(Double.valueOf(locationInfo.getLocationInfo1Latitude()),Double.valueOf(locationInfo.getLocationInfo1Longitude()))[0]));

            try {
                locationInfo.setLocationInfo1Time(DateUtils.UTCToLocal(StringUtils.sixteenToTen(locationInfo.getLocationInfo1().substring(16))));
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        return locationInfoList;
    }
}
