package com.buaa.locationservice.service;

import com.buaa.locationservice.dao.AuthDao;
import com.buaa.locationservice.model.Role;
import com.buaa.locationservice.model.SecurityUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(value="CustomUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private AuthDao authDao;

	/**
	 * @Description:加载用户信息&权限
	 * @param: username 用户名
	 * @Return: org.springframework.security.core.userdetails.UserDetails
	 */
	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		SecurityUserDetails userDetail = new SecurityUserDetails(authDao.findByUsername(userName));
		if (userDetail == null) {
			throw new UsernameNotFoundException(String.format("No userDetail found with username '%s'.", userName));
		}
		List<Role> roles = authDao.findRoleByUserId(userDetail.getId());
		userDetail.setRoles(roles);
		return userDetail;
	}
}