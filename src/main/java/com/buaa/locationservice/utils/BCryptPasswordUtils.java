package com.buaa.locationservice.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 加密工具
 */
public class BCryptPasswordUtils {
	public static String BCryptPassword(String rawPassword) {
		return new BCryptPasswordEncoder().encode(rawPassword);
	}

	public static boolean matches(String rawPassword,String encodedPassword) {
		return new BCryptPasswordEncoder().matches(rawPassword,encodedPassword);
	}
}
