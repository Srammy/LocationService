package com.buaa.locationservice.handler;

import com.alibaba.fastjson.JSON;
import com.buaa.locationservice.vo.ResponseCode;
import com.buaa.locationservice.vo.ResponseGenerator;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Description: 登录失败处理类
 *               用户登录系统失败后需要做的业务操作
 */
@Component
public class MyAuthenticationFailHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,AuthenticationException e) throws IOException, ServletException {
        String body = "";
        if (e instanceof UsernameNotFoundException || e instanceof BadCredentialsException) {
            body = JSON.toJSONString(ResponseGenerator.genFailResult(ResponseCode.UNAUTHORIZED.code(),"用户名或密码错误"));
        } else if (e instanceof DisabledException) {
            body = JSON.toJSONString(ResponseGenerator.genFailResult(ResponseCode.UNAUTHORIZED.code(),"账户被禁用，请联系管理员"));
        } else {
            body = JSON.toJSONString(ResponseGenerator.genFailResult(ResponseCode.UNAUTHORIZED.code(),"登录失败"));
        }

        response.setStatus(200);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter printWriter = response.getWriter();
        printWriter.write(body);
        printWriter.flush();
    }
}
