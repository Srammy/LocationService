package com.buaa.locationservice.handler;

import com.alibaba.fastjson.JSON;
import com.buaa.locationservice.vo.ResponseCode;
import com.buaa.locationservice.vo.ResponseGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;

/**
 * @desc: 验证为未登陆状态会进入此方法，认证错误
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {
	private final static Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);
	private static final long serialVersionUID = -8970718410437077606L;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
		LOGGER.error("认证失败：" + authException.getMessage());
		response.setStatus(200);
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		PrintWriter printWriter = response.getWriter();
		String body = JSON.toJSONString(ResponseGenerator.genFailResult(ResponseCode.UNAUTHORIZED.code(),authException.getMessage()));
		printWriter.write(body);
		printWriter.flush();
	}
}