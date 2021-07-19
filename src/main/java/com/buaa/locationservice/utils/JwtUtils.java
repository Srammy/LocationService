package com.buaa.locationservice.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.buaa.locationservice.model.Role;
import com.buaa.locationservice.model.SecurityUserDetails;
import com.buaa.locationservice.model.User;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * JSON Web Token工具
 */
@Component
public class JwtUtils {

	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.expiration}")
	private Long accessTokenExpiration;

	@Value("${jwt.header}")
	private String tokenHeader;

	@Value("${jwt.tokenHead}")
	private String tokenHead;

	public String getTokenHead() {
		return tokenHead;
	}

	public String getTokenHeader() {
		return tokenHeader;
	}

	/**
	 * @Description: 利用Jwts生成token
	 * @param: userDetails
	 * @Return: java.lang.String
	 */
	public String generateAccessToken(UserDetails userDetails) {
		SecurityUserDetails securityUserDetails = (SecurityUserDetails) userDetails;

		//登陆成功生成JWT
		return Jwts.builder()
				//主题 放入用户名
				.setSubject(userDetails.getUsername())
				.setId(securityUserDetails.getId() + "")
				//自定义属性 放入用户拥有权限
				.claim(tokenHeader, JSON.toJSONString(securityUserDetails.getAuthorities()))
				//失效时间
				.setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
				//签名算法和密钥
				.signWith(SignatureAlgorithm.HS512, secret)
				.compact();

	}

	/**
	 * @Description: 从Token中获取用户信息
	 * @param token
	 * @return 用户信息
	 * @throws ExpiredJwtException
	 * @throws UnsupportedJwtException
	 * @throws MalformedJwtException
	 * @throws SignatureException
	 * @throws IllegalArgumentException
	 */
	public UserDetails getUserFromToken(String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {

		Claims claims = Jwts.parser()
				.setSigningKey(secret)
				.parseClaimsJws(token.replace(tokenHead, ""))
				.getBody();

		User user = new User();
		user.setId(Long.parseLong(claims.getId()));
		user.setUserName(claims.getSubject());
		List<Role> roles = new ArrayList<>();
		Role role = null;
		String authority = claims.get(tokenHeader).toString();
		if (!org.springframework.util.StringUtils.isEmpty(authority)) {
			List<Map<String,String>> authrityMap = JSONObject.parseObject(authority, List.class);
			for (Map<String,String> auth : authrityMap) {
				role = new Role();
				role.setName(auth.get("authority"));
				roles.add(role);
			}
		}
		user.setRoles(roles);

		return new SecurityUserDetails(user);
	}
}
