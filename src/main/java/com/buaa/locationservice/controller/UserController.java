package com.buaa.locationservice.controller;

import com.buaa.locationservice.model.User;
import com.buaa.locationservice.model.UserDevice;
import com.buaa.locationservice.service.AuthService;
import com.buaa.locationservice.service.DeviceService;
import com.buaa.locationservice.service.UserDeviceService;
import com.buaa.locationservice.service.UserService;
import com.buaa.locationservice.utils.BCryptPasswordUtils;
import com.buaa.locationservice.utils.MD5Utils;
import com.buaa.locationservice.utils.StringUtils;
import com.buaa.locationservice.vo.ResponseCode;
import com.buaa.locationservice.vo.ResponseGenerator;
import com.buaa.locationservice.vo.ResponseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import com.buaa.locationservice.utils.UserNameVerifi;
import com.buaa.locationservice.utils.PasswordVerifiUtils;

@Api(value = "userApi:/location",description = "用户管理")
@RestController
@RequestMapping("/location")
public class UserController {
    private final static Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @ApiOperation(value ="查看所有用户",notes ="查看用户",httpMethod = "GET")
    @RequestMapping(value = "/getAllUsers",method = RequestMethod.GET)
    public ResponseVo getAllUsers() {
        LOGGER.info("查看所有用户");
        return ResponseGenerator.genSuccessResult(userService.getAllUsers());
    }

    @ApiOperation(value ="增加用户",notes ="增加用户",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName",value = "用户名",paramType = "query",required = true,dataType = "String"),
            @ApiImplicitParam(name = "rawPassword",value = "未加密用户密码",paramType = "query",required = true,dataType = "String"),
    })
    @RequestMapping(value = "/addUser",method = RequestMethod.POST)
    public ResponseVo addUser(@RequestParam String userName, @RequestParam String rawPassword) {
        LOGGER.info("后台管理界面增加用户");
        ResponseVo rv = null;
        if (userName==null || "".equals(userName.trim())) {
            rv = ResponseGenerator.genFailResult("用户名不能为空");
        } else if (rawPassword==null || "".equals(rawPassword.trim())) {
            rv = ResponseGenerator.genFailResult("密码不能为空");
        } else if (!UserNameVerifi.Verification(userName)) {
            rv = ResponseGenerator.genFailResult("用户名不符合要求");
        } else if (!PasswordVerifiUtils.Verification(rawPassword)) {
            rv = ResponseGenerator.genFailResult("密码不符合要求");
        } else if (rawPassword.length() < 8 || rawPassword.length() > 10) {
            rv = ResponseGenerator.genFailResult("输入密码的位数为8-10");
        } else {
            User user = new User();
            user.setUserName(userName);
            user.setRawPassword(rawPassword);
            boolean result = userService.addUser(user);
            if (result) {
                rv =  ResponseGenerator.genSuccessResult();
            } else {
                rv = ResponseGenerator.genSuccessResult("新增失败，因存在该用户");
            }
        }
        return rv;
    }

    @ApiOperation(value ="删除用户",notes ="删除用户",httpMethod = "POST")
    @ApiImplicitParams({

            @ApiImplicitParam(name = "userName",value = "用户名",paramType = "query",required = true,dataType = "String"),
            @ApiImplicitParam(name = "password",value = "加密的用户密码",paramType = "query",required = true,dataType = "String")
    })
    @RequestMapping(value = "/deleteUser",method = RequestMethod.POST)
    public ResponseVo deleteUser(@RequestParam String userName, @RequestParam String password) {
        LOGGER.info("后台管理界面删除用户");
        boolean deleteUserSuccess = false;
        ResponseVo rv = null;
        deleteUserSuccess = userService.deleteByUserNameAndPassword(userName, password);

        if (deleteUserSuccess) {
            LOGGER.info("删除用户" + userName + "成功");
            rv =  ResponseGenerator.genSuccessResult();
        } else {
            LOGGER.info("删除用户" + userName + "失败");
            rv =  ResponseGenerator.genFailResult("删除用户" + userName + "失败");
        }
        return rv;
    }
}
