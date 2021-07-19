package com.buaa.locationservice.controller;

import com.buaa.locationservice.model.DeviceInfo;
import com.buaa.locationservice.model.UserDevice;
import com.buaa.locationservice.service.DeviceService;
import com.buaa.locationservice.service.UserDeviceService;
import com.buaa.locationservice.utils.DeviceNameVerifiUtils;
import com.buaa.locationservice.utils.DeviceNumVerifiUtils;
import com.buaa.locationservice.utils.StringUtils;
import com.buaa.locationservice.vo.ResponseGenerator;
import com.buaa.locationservice.vo.ResponseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "deviceApi:/location",description = "设备管理")
@RestController
@RequestMapping("/location")
public class DeviceController {
    private final static Logger LOGGER = LoggerFactory.getLogger(DeviceController.class);

    @Autowired
    private DeviceService deviceService;
    @Autowired
    private UserDeviceService userDeviceService;

    @ApiOperation(value = "设备列表:/getDeviceList", notes = "根据userId获取设备列表", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID(取值为ALL/真正ID)", paramType = "query", required = true, dataType = "String")
    })
    @RequestMapping(value = "/getDeviceList", method = RequestMethod.GET)
    public ResponseVo getDeviceList(@RequestParam String userId) {
        LOGGER.info("查询设备列表");
        ResponseVo rv = new ResponseVo();

        if ("ALL".equals(userId) || StringUtils.testIsLong(userId)) {
            rv = ResponseGenerator.genSuccessResult(deviceService.getDeviceInfoByUser(userId));
        } else {
            rv = ResponseGenerator.genSuccessResult("请输入正确的参数");
        }
        return rv;
    }

    @ApiOperation(value = "新增用户设备关联关系:/saveUserDevice", notes = "新增用户设备关联关系", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", paramType = "query", required = true, dataType = "long"),
            @ApiImplicitParam(name = "deviceId", value = "设备ID", paramType = "query", required = true, dataType = "long"),
    })
    @RequestMapping(value = "/saveUserDevice", method = RequestMethod.POST)
    public ResponseVo saveUserDevice(@RequestParam long userId, @RequestParam long deviceId) {
        LOGGER.info("新增用户设备关联关系");
        ResponseVo rv = null;

        UserDevice userDevice = new UserDevice();
        userDevice.setUserId(userId);
        userDevice.setDeviceId(deviceId);
        boolean result = userDeviceService.save(userDevice);
        if (result) {
            rv = ResponseGenerator.genSuccessResult();
        } else {
            rv = ResponseGenerator.genSuccessResult("新增失败，因存在映射关系");
        }
        return rv;
    }

    @ApiOperation(value = "删除用户设备关联关系:/deleteUserDevice", notes = "删除用户设备关联关系", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", paramType = "query", required = true, dataType = "long"),
            @ApiImplicitParam(name = "deviceId", value = "设备ID", paramType = "query", required = true, dataType = "long")

    })
    @RequestMapping(value = "/deleteUserDevice", method = RequestMethod.POST)
    public ResponseVo deleteUserDevice(@RequestParam long userId, @RequestParam long deviceId) {
        LOGGER.info("删除用户设备关联");
        ResponseVo rv = null;
        boolean deleteUserDeviceSuccess = false;
        UserDevice userDevice = new UserDevice();
        userDevice.setUserId(userId);
        userDevice.setDeviceId(deviceId);
        deleteUserDeviceSuccess = userDeviceService.deleteUserDeviceByUserDevice(userDevice);
        if (deleteUserDeviceSuccess) {
            LOGGER.info("删除用户设备关联成功");
            rv = ResponseGenerator.genSuccessResult();
        } else {
            rv = ResponseGenerator.genFailResult("删除用户设备关联失败");
        }
        return rv;
    }

    @ApiOperation(value = "添加设备", notes = "增加一台设备", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deviceNumber", value = "设备号", paramType = "query", required = true, dataType = "String"),
            @ApiImplicitParam(name = "deviceName", value = "设备名", paramType = "query", required = true, dataType = "String")
    })
    @RequestMapping(value = "/addDevice", method = RequestMethod.POST)
    public ResponseVo addDevice(@RequestParam String deviceNumber, @RequestParam String deviceName) {
        LOGGER.info("新增设备");
        ResponseVo rv = null;
        if (deviceNumber==null || "".equals(deviceNumber.trim())) {
            rv = ResponseGenerator.genFailResult("设备号不能为空");
        } else if (deviceName==null || "".equals(deviceName.trim())) {
            rv = ResponseGenerator.genFailResult("设备名不能为空");
        } else if (!DeviceNameVerifiUtils.Verification(deviceName)) {
            rv = ResponseGenerator.genFailResult("设备名不符合要求");
        } else if (!DeviceNumVerifiUtils.Verification(deviceNumber)) {
            rv = ResponseGenerator.genFailResult("设备号不符合要求");
        } else {
            DeviceInfo deviceInfo = new DeviceInfo();
            deviceInfo.setDeviceNumber(deviceNumber);
            deviceInfo.setDeviceName(deviceName);
            boolean result = deviceService.addDevice(deviceInfo);
            if (result) {
                rv =  ResponseGenerator.genSuccessResult();
            } else {
                rv = ResponseGenerator.genSuccessResult("新增失败，因存在该设备");
            }
        }
        return rv;
    }

    @ApiOperation(value = "删除设备", notes = "删除一台设备", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deviceNumber", value = "设备号", paramType = "query", required = true, dataType = "String")

    })
    @RequestMapping(value = "/deleteDevice", method = RequestMethod.POST)
    public ResponseVo deleteDevice(@RequestParam String deviceNumber) {
        LOGGER.info("删除设备，设备号" + deviceNumber);
        ResponseVo rv = null;
        boolean deleteDeviceSuccess = false;
        deleteDeviceSuccess = deviceService.deleteDeviceByDeviceNumber(deviceNumber);

        if (deleteDeviceSuccess) {
            LOGGER.info("删除设备" + deviceNumber + "成功");
            rv =  ResponseGenerator.genSuccessResult();
        } else {
            LOGGER.info("删除设备" + deviceNumber + "失败");
            rv =  ResponseGenerator.genFailResult("删除设备" + deviceNumber + "失败");
        }
        return rv;
    }
}