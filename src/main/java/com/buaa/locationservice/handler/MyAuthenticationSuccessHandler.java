package com.buaa.locationservice.handler;

import com.alibaba.fastjson.JSON;
import com.buaa.locationservice.model.SecurityUserDetails;
import com.buaa.locationservice.model.User;
import com.buaa.locationservice.utils.JwtUtils;
import com.buaa.locationservice.vo.ResponseGenerator;
import com.buaa.locationservice.vo.ResponseUserToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Description: 登录成功处理类
 *               用户登录系统成功后，需要做的业务操作
 */
@Component
public class MyAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private final static Logger LOGGER = LoggerFactory.getLogger(MyAuthenticationSuccessHandler.class);

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    JwtUtils jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        LOGGER.debug("认证通过，开始获取token");
        SecurityUserDetails securityUserDetails = (SecurityUserDetails)authentication.getPrincipal();
        String token = jwtUtil.generateAccessToken(securityUserDetails);
        token = tokenHead + token;
        LOGGER.debug("获取token："+token);
        User user = new User();
        user.setId(securityUserDetails.getId());
        user.setUserName(securityUserDetails.getUsername());
        user.setRoles(securityUserDetails.getRoles());
        response.setStatus(200);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter printWriter = response.getWriter();
        String body = JSON.toJSONString(ResponseGenerator.genSuccessResult(new ResponseUserToken(token,user)));
        printWriter.write(body);
        printWriter.flush();
    }
}
