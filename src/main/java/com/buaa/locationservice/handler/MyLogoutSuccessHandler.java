package com.buaa.locationservice.handler;

import com.alibaba.fastjson.JSON;

import com.buaa.locationservice.vo.ResponseGenerator;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Description: 用户退出系统成功后,需要做的业务操作
 *               包括：清空上下文&Session，记录退出日志等等
 */
@Component
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 清空上下文
        SecurityContextHolder.clearContext();
        // 从session中移除
        httpServletRequest.getSession().removeAttribute("SPRING_SECURITY_CONTEXT");
        // 记录退出日志
        System.out.println("退出登录成功！");
        response.setStatus(200);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter printWriter = response.getWriter();
        String body = JSON.toJSONString(ResponseGenerator.genSuccessResult("退出系统成功"));
        printWriter.write(body);
        printWriter.flush();
    }

}
