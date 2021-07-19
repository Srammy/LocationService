package com.buaa.locationservice.controller;

import com.buaa.locationservice.model.User;
import com.buaa.locationservice.service.AuthService;
import com.buaa.locationservice.utils.PasswordVerifiUtils;
import com.buaa.locationservice.utils.UserNameVerifi;
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

@Api(value = "authApi:/auth",description = "用户管理")
@RestController
@RequestMapping("/auth")
public class AuthController {
	private final static Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private AuthService authService;

	@ApiOperation(value ="用户注册:/register",notes ="用户注册",httpMethod = "POST")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userName",value = "用户名称",paramType = "query",required = true,dataType = "String"),
			@ApiImplicitParam(name = "rawPassword",value = "用户未加密密码",paramType = "query",required = true,dataType = "String"),
			@ApiImplicitParam(name = "rePassword",value = "用户再次输入密码",paramType = "query",required = true,dataType = "String"),
	})
	@RequestMapping(value = "/register",method = RequestMethod.POST)
	public ResponseVo login(@RequestParam String userName, @RequestParam String rawPassword, @RequestParam String rePassword) {
		LOGGER.info("APP用户注册");
		ResponseVo rv = null;
		if (userName==null || "".equals(userName.trim())) {
			rv = ResponseGenerator.genFailResult(ResponseCode.FAIL_USERNAME_NULL.code(),"用户名不能为空");
		} else if (rawPassword==null || "".equals(rawPassword.trim())) {
			rv = ResponseGenerator.genFailResult(ResponseCode.FAIL_RAWPASSWORD_NULL.code(),"密码不能为空");
		} else if (rePassword==null || "".equals(rePassword.trim())) {
			rv = ResponseGenerator.genFailResult(ResponseCode.FAIL_REPASSWORD_NULL.code(),"确认密码不能为空");
		} else if (!rawPassword.equals(rePassword)) {
			rv = ResponseGenerator.genFailResult(ResponseCode.FAIL_REPASSWORD_RAWPASSWORD_DIF.code(),"两次输入的密码不相同");
		} else if (rawPassword.length() < 8 || rawPassword.length() > 10) {
			rv = ResponseGenerator.genFailResult(ResponseCode.FAIL_RAWPASSWORD_WRONG_lENGTH.code(),"输入密码的位数为8-10");
		} else if (authService.findByUsername(userName)) {
			rv = ResponseGenerator.genFailResult(ResponseCode.FAIL_USER_EXIST.code(),"用户已存在");
		} else if (!UserNameVerifi.Verification(userName)) {
			rv = ResponseGenerator.genFailResult(ResponseCode.WRONG_USER_NAME.code(),"用户名应为数字和字母的任意组合,不能使用特殊字符");
		} else if (!PasswordVerifiUtils.Verification(rawPassword)) {
			rv = ResponseGenerator.genFailResult(ResponseCode.WRONG_RAWPASSWORD.code(),"密码应为大小写字母和数字的组合,不能使用特殊字符");
		} else {
			User user = new User();
			user.setUserName(userName);
			user.setRawPassword(rawPassword);
			rv = ResponseGenerator.genSuccessResult(authService.register(user));
		}
		return rv;
	}

}
