package com.buaa.locationservice.filter;

import com.alibaba.fastjson.JSON;
import com.buaa.locationservice.model.SecurityUserDetails;
import com.buaa.locationservice.utils.JwtUtils;
import com.buaa.locationservice.vo.ResponseCode;
import com.buaa.locationservice.vo.ResponseGenerator;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Description:
 * jwt认证token
 * 每次请求接口时，就会进入这里验证token是否合法token，
 */
@Component
public class JWTAuthenticationTokenFilter extends OncePerRequestFilter {
	private final static Logger LOGGER = LoggerFactory.getLogger(JWTAuthenticationTokenFilter.class);

	@Value("${jwt.header}")
	private String tokenHeader;

	@Value("${jwt.tokenHead}")
	private String tokenHead;

	@Autowired
	JwtUtils jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        LOGGER.info("开始认证JWT_TOKEN，tokenHeader"+tokenHeader+",tokenHead:"+tokenHead);
		//获得accessToken
		String accessToken = request.getHeader(tokenHeader);
		if (null!=accessToken && accessToken.startsWith(tokenHead)) {
			try {
				UsernamePasswordAuthenticationToken authentication = getAuthentication(request, response);
				SecurityContextHolder.getContext().setAuthentication(authentication);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		chain.doFilter(request, response);
		return;
	}

	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String token = request.getHeader(tokenHeader);
		if (!StringUtils.isEmpty(token)) {
			try {
				SecurityUserDetails userDetail = (SecurityUserDetails)jwtUtil.getUserFromToken(token);
				if(!StringUtils.isEmpty(userDetail.getUsername())) {
					//此处password不能为null
					User principal = new User(userDetail.getUsername(), "", userDetail.getAuthorities());
					return new UsernamePasswordAuthenticationToken(principal, userDetail.getId(), userDetail.getAuthorities());
				}
			} catch (ExpiredJwtException e) {
				LOGGER.error("toekn超过有效期，请重新登");
				response.setStatus(200);
				response.setCharacterEncoding("UTF-8");
				response.setContentType("application/json; charset=utf-8");
				PrintWriter printWriter = response.getWriter();
				//--------------20190923修改，将LOGIN_ERROR改为TOKEN_OUT----------
				String body = JSON.toJSONString(ResponseGenerator.genFailResult(ResponseCode.TOKEN_OUT.code(),"toekn超过有效期，请重新登"));
				//--------------------------------------------------------------
				printWriter.write(body);
				printWriter.flush();
			} catch (Exception e){
				LOGGER.error("token invalid"+e.getMessage());
				response.setStatus(200);
				response.setCharacterEncoding("UTF-8");
				response.setContentType("application/json; charset=utf-8");
				PrintWriter printWriter = response.getWriter();
				String body = JSON.toJSONString(ResponseGenerator.genFailResult(ResponseCode.LOGIN_ERROR.code(),"token invalid"));
				printWriter.write(body);
				printWriter.flush();
			}
		}
		return null;
	}

}


