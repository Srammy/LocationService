package com.buaa.locationservice.handler;

import com.alibaba.fastjson.JSON;
import com.buaa.locationservice.vo.ResponseCode;
import com.buaa.locationservice.vo.ResponseGenerator;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Description: 权限不足处理类
 *               自定义权限不足需要做的业务操作
 *               包括：处理权限不足的逻辑
 */
@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        StringBuffer msg = new StringBuffer("请求: ");
        msg.append(request.getRequestURI()).append(" 权限不足，无法访问该资源.");
        response.setStatus(200);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter printWriter = response.getWriter();
        String body = JSON.toJSONString(ResponseGenerator.genFailResult(ResponseCode.FORBIDDEN.code(),msg.toString()));
        printWriter.write(body);
        printWriter.flush();
    }

}
