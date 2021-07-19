package com.buaa.locationservice.Job;

import com.buaa.locationservice.dao.DeviceServiceDao;
import com.buaa.locationservice.dao.LocationServiceDao;
import com.buaa.locationservice.model.DeviceInfo;
import com.buaa.locationservice.model.LocationInfo;
import com.buaa.locationservice.utils.Constants;
import com.buaa.locationservice.utils.DateUtils;
import com.buaa.locationservice.utils.StringUtils;
import com.buaa.locationservice.utils.TelnetClientUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution    //设置任务能否并行执行，加注解则不能
public class LocationJobHighFrequency extends QuartzJobBean {
    //2.0之后StatefulJob接口直接被deprecated,不然可以实现StatefulJob接口来实现任务不能并发，并且传值可持久化
    //目前推荐使用继承QuartzJobBean，使用@PersistJobDataAfterExecution，@DisallowConcurrentExecution实现任务不能并发，并且传值可持久化
    private final static Logger LOGGER = LoggerFactory.getLogger(LocationJobHighFrequency.class);

    @Autowired
    private LocationServiceDao locationServiceDao;
    @Autowired
    private DeviceServiceDao deviceServiceDao;

    @Value("${telnet.hostname}")
    private String hostName;
    @Value("${telnet.username}")
    private String userName;
    @Value("${telnet.password}")
    private String password;
    @Value("${telnet.command}")
    private String command;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        TelnetClientUtils tcu = null;
        String result = null;
        try{
            LocationInfo locationInfo = null;

            result = String.valueOf(jobExecutionContext.getJobDetail().getJobDataMap().get("result"));
            LOGGER.info("上一次登录状态："+result);
            if (result == null || "".equals(result.trim()) || "null".equals(result.toLowerCase())) {
                result = Constants.error;
                LOGGER.info("初始化登录状态："+result);
            }

            if (Constants.error.equals(result)) {
                tcu =  new TelnetClientUtils();
                result = tcu.login(hostName, userName, password);
                jobExecutionContext.getJobDetail().getJobDataMap().put("result",result);
                jobExecutionContext.getJobDetail().getJobDataMap().put("client",tcu);
                LOGGER.info("登录："+result);
            }

            if (Constants.success.equals(result)) {
                LOGGER.info("已登录成功,"+result);
                if (tcu == null) {
                    tcu = (TelnetClientUtils)jobExecutionContext.getJobDetail().getJobDataMap().get("client");
                }
                result = tcu.sendCommand(command);
                LOGGER.info("获取命令结果："+result);
                if (result == null || "".equals(result.trim())) {
                    LOGGER.info("断开连接："+result);
                    tcu.distinct();
                    result = Constants.error;
                    jobExecutionContext.getJobDetail().getJobDataMap().put("result",result);
                } else {
                    if (result.contains("Prog")) {
                        locationInfo = getLocationInfo(result);
                        DeviceInfo deviceInfo = null;

                        deviceInfo = deviceServiceDao.getByDeviceNumber(locationInfo.getPlatformNumber());

                        if (deviceInfo != null){
                            LOGGER.info("已存在设备："+deviceInfo.getDeviceNumber());
                            locationInfo.setDeviceId(deviceInfo.getId());
                        } else {
                            LOGGER.info("不存在设备："+locationInfo.getPlatformNumber());
                            deviceInfo = new DeviceInfo();
                            deviceInfo.setDeviceNumber(locationInfo.getPlatformNumber());
                            deviceInfo.setDeviceName("设备_"+locationInfo.getPlatformNumber());
                            LOGGER.info("新增设备："+deviceInfo.getDeviceNumber());
                            deviceServiceDao.saveDeviceInfo(deviceInfo);
                            // 查询新增加的设备在数据库device_info表中的id
                            long deviceId = deviceServiceDao.getLastDeviceId().getId();
                            // 为该条位置信息设置对应的设备
                            locationInfo.setDeviceId(deviceId);
                        }
                        LOGGER.info("新增位置信息");
                        locationServiceDao.saveLocationInfo(locationInfo);
                    } else {
                        LOGGER.info("返回数据不正确："+result);
                    }

                }
            } else {
                LOGGER.info("登录失败,"+result);
                LOGGER.info("断开连接："+result);
                tcu.distinct();
                result = Constants.error;
                jobExecutionContext.getJobDetail().getJobDataMap().put("result",result);
            }
        } catch (Exception e) {
            LOGGER.info("登录失败,"+result+e);
            LOGGER.info("断开连接："+result+e);
            tcu.distinct();
            result = Constants.error;
            jobExecutionContext.getJobDetail().getJobDataMap().put("result",result);
        } finally {

        }

    }

    public LocationInfo getLocationInfo(String result) {
        //去除字符串中的换行符
        //String tmp = result.replace('\n',' ');
        String tmp = result.replaceAll("\r|\n", " ");
        //根据" "分数组
        String[] info = tmp.split(" ");
        ArrayList<String> infoList = new ArrayList<>();
        LocationInfo locationInfo = new LocationInfo();
        //去掉所有的空元素" "
        for (int i=0; i<info.length; i++) {
            String temp = info[i];
            if (!"".equals(temp.trim())) {
                infoList.add(temp);
            }
        }

        for (int i=0;i<infoList.size();i++) {
            String temp = infoList.get(i);
            if ("Prog".equals(temp)) {
                locationInfo.setProgramNumber(temp + infoList.get(i+1));
                locationInfo.setPlatformNumber(infoList.get(i+2));
                i=i+2;
            } else if (temp.contains("N")) {
                locationInfo.setLongitude(temp);
            } else if (temp.contains("E")) {
                locationInfo.setLatitude(temp);
                locationInfo.setLocationClass(infoList.get(i+1));
                i=i+1;
            } else if (temp.contains("-")&&temp.contains("/")) {
                String[] tm = temp.split("-");
                locationInfo.setMostRecentDataCollection(tm[0]);
                locationInfo.setMostRecentLocation(tm[1]);
            } else if("(".equals(temp)) {
                locationInfo.setCompressionIndex(temp+infoList.get(i+1));
                StringBuffer locationInfo1 = new StringBuffer();
                locationInfo1.append(infoList.get(i+2));
                locationInfo1.append(infoList.get(i+3));
                locationInfo1.append(infoList.get(i+4));
                locationInfo1.append(infoList.get(i+5));
                locationInfo1.append(infoList.get(i+6));
                locationInfo1.append(infoList.get(i+7));
                locationInfo1.append(infoList.get(i+8));
                locationInfo1.append(infoList.get(i+9));
                locationInfo1.append(infoList.get(i+10));
                locationInfo1.append(infoList.get(i+11));
                locationInfo1.append(infoList.get(i+12));
                locationInfo1.append(infoList.get(i+13));
                StringBuffer locationInfo2 = new StringBuffer();
                locationInfo2.append(infoList.get(i+14));
                locationInfo2.append(infoList.get(i+15));
                locationInfo2.append(infoList.get(i+16));
                locationInfo2.append(infoList.get(i+17));
                locationInfo2.append(infoList.get(i+18));
                locationInfo2.append(infoList.get(i+19));
                locationInfo2.append(infoList.get(i+20));
                locationInfo2.append(infoList.get(i+21));
                locationInfo2.append(infoList.get(i+22));
                locationInfo2.append(infoList.get(i+23));
                locationInfo2.append(infoList.get(i+24));
                locationInfo2.append(infoList.get(i+25));
                StringBuffer checkinfo= new StringBuffer();
                checkinfo.append(infoList.get(i+26));
                checkinfo.append(infoList.get(i+27));
                checkinfo.append(infoList.get(i+28));
                checkinfo.append(infoList.get(i+29));
                checkinfo.append(infoList.get(i+30));
                checkinfo.append(infoList.get(i+31));
                checkinfo.append(infoList.get(i+32));
                StringBuffer locationInfo1DateStr = new StringBuffer();
                locationInfo1DateStr.append(infoList.get(i+10));
                locationInfo1DateStr.append(infoList.get(i+11));
                locationInfo1DateStr.append(infoList.get(i+12));
                locationInfo1DateStr.append(infoList.get(i+13));

                try {
                    locationInfo.setLocationInfo1Date(DateUtils.UTCToLocal(StringUtils.sixteenToTen(locationInfo1DateStr.toString())));
                } catch (ParseException e) {
                    locationInfo.setLocationInfo1Date(new Date());
                    e.printStackTrace();
                }
                locationInfo.setLocationInfo1(locationInfo1.toString());
                locationInfo.setLocationInfo2(locationInfo2.toString());
                locationInfo.setCheckInfo(checkinfo.toString());
                i=i+32;
                break;
            }
        }
        return locationInfo;
    }


}
