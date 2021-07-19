package com.buaa.locationservice.controller;

import com.buaa.locationservice.Job.ScheduleManager;
import com.buaa.locationservice.model.LocationInfo;
import com.buaa.locationservice.service.LocationService;
import com.buaa.locationservice.utils.Constants;
import com.buaa.locationservice.utils.DateUtils;
import com.buaa.locationservice.utils.StringUtils;
import com.buaa.locationservice.vo.ResponseGenerator;
import com.buaa.locationservice.vo.ResponseVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.quartz.CronExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;


@Api(value = "locationApi:/location",description = "位置信息管理")
@RestController
@RequestMapping("/location")
public class LocationController {
    private final static Logger LOGGER = LoggerFactory.getLogger(LocationController.class);

    @Autowired
    private LocationService locationService;
    @Autowired
    private ScheduleManager scheduleManager;

    @Value("${quartz.jobType}")
    private String jobType;
    @Value("${quartz.jobName}")
    private String jobName;
    @Value("${quartz.jobGroupName}")
    private String jobGroupName;
    @Value("${quartz.triggerName}")
    private String triggerName;
    @Value("${quartz.triggerGroupName}")
    private String triggerGroupName;

    @ApiOperation(value ="获取位置信息:/getLocation",notes ="根据编号获取设备列表",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "位置信息编号(默认为1)",paramType = "query",required = true,dataType = "Long")
    })
    @RequestMapping(value = "/getLocation",method = RequestMethod.GET)
    public ResponseVo getLocationById(@RequestParam(defaultValue = "1") long id) {
        LOGGER.info("根据编号查询位置信息");
        return ResponseGenerator.genSuccessResult(locationService.getLocationInfoById(id));
    }

    @ApiOperation(value ="获取位置信息:/getLocationLast",notes ="根据设备编号获取最近位置信息",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deviceId",value = "设备编号",paramType = "query",required = true,dataType = "Long")
    })
    @RequestMapping(value = "/getLocationLast",method = RequestMethod.GET)
    public ResponseVo getLocationLast(@RequestParam long deviceId) {
        LOGGER.info("查询设备"+deviceId+"最新位置信息");
        return ResponseGenerator.genSuccessResult(locationService.getLocationInfoLast(deviceId));
    }


    @ApiOperation(value ="获取位置信息:/getLocationAll",notes ="分页获取所有位置信息",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum",value = "分页页码",paramType = "query",required = true,dataType = "int"),
            @ApiImplicitParam(name = "pageSize",value = "分页大小",paramType = "query",required = true,dataType = "int"),
            @ApiImplicitParam(name = "deviceId",value = "设备编号",paramType = "query",required = false,dataType = "String")
    })
    @RequestMapping(value = "/getLocationAll",method = RequestMethod.GET)
    public ResponseVo getLocationAll(@RequestParam int pageNum, @RequestParam int pageSize, String deviceId) {
        LOGGER.info("分页查询所有位置信息");
        return ResponseGenerator.genSuccessResult(locationService.getLocationInfoAll(pageNum,pageSize,deviceId));
    }

    @ApiOperation(value ="获取位置信息:/getLocationByTime",notes ="根据时间段获取设备位置信息",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deviceId",value = "设备ID",paramType = "query",required = true,dataType = "long"),
            @ApiImplicitParam(name = "startDate",value = "开始日期",paramType = "query",required = true,dataType = "String"),
            @ApiImplicitParam(name = "endDate",value = "结束日期",paramType = "query",required = true,dataType = "String")
    })
    @RequestMapping(value = "/getLocationByTime",method = RequestMethod.GET)
    public ResponseVo getLocationByTime(@RequestParam long deviceId,@RequestParam String startDate, @RequestParam String endDate) {
        LOGGER.info("查询时间段"+startDate+"至"+endDate+"内所有位置信息");
        ResponseVo rv;
        if (StringUtils.testIsDateYYYYMMDD(startDate) && StringUtils.testIsDateYYYYMMDD(endDate)) {
            rv = ResponseGenerator.genSuccessResult(locationService.getLocationInfoByTime(deviceId,startDate,endDate));
        }else{
            rv = ResponseGenerator.genSuccessResult("请检查日期格式");
        }
        return rv;
    }

    @ApiOperation(value ="定时任务:/modifyJobTrigger",notes ="修改定时触发器",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "time",value = "定时时间",paramType = "query",required = true,dataType = "String"),
            @ApiImplicitParam(name = "type",value = "定时器类型",paramType = "query",required = false,dataType = "String")
    })
    @RequestMapping(value = "/modifyJobTrigger",method = RequestMethod.POST)
    public ResponseVo modifyJobTrigger(@RequestParam String time, String type) {
        String typeLocal = jobType;
        if (type != null && !"".equals(type.trim())) {
            if (Constants.jobTypeCron.equals(type) || Constants.jobTypeSimple.equals(type)) {
                typeLocal = type;
            } else {
                LOGGER.info("请求参数type："+type+"，类型不存在，默认为："+typeLocal);
            }
        }

        LOGGER.info("修改定时任务时间，定时类型："+typeLocal+",修改为："+time);
        ResponseVo rv = new ResponseVo();
        if (time == null || "".equals(time.trim())) {
            LOGGER.error("请输入正确的时间参数");
            rv = ResponseGenerator.genSuccessResult("请输入正确的时间参数");
            return rv;
        }
        if (Constants.jobTypeSimple.equals(typeLocal)) {
            try {
                Integer.parseInt(time);
            } catch (Exception e){
                LOGGER.error("请输入正整数");
                rv = ResponseGenerator.genSuccessResult("请输入正整数");
                return rv;
            }

        } else if (Constants.jobTypeCron.equals(typeLocal)){
            if (!CronExpression.isValidExpression(time)) {
                LOGGER.error("请输入正确的cron表达式");
                rv = ResponseGenerator.genSuccessResult("请输入正确的cron表达式");
                return rv;
            }

        }
        if (scheduleManager.modify(typeLocal,jobName,jobGroupName,triggerName,triggerGroupName,time)) {
            rv = ResponseGenerator.genSuccessResult();
        } else {
            rv = ResponseGenerator.genSuccessResult("修改失败");
        }
        return rv;
    }
}
