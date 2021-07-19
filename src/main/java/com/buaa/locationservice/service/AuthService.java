package com.buaa.locationservice.service;

import com.buaa.locationservice.Exception.CustomException;
import com.buaa.locationservice.dao.AuthDao;
import com.buaa.locationservice.dao.UserDao;
import com.buaa.locationservice.model.User;
import com.buaa.locationservice.utils.BCryptPasswordUtils;
import com.buaa.locationservice.vo.ResponseCode;
import com.buaa.locationservice.vo.ResponseGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
	private final static Logger LOGGER = LoggerFactory.getLogger(AuthService.class);

	@Autowired
	private AuthDao authDao;

	@Autowired
	private UserDao userDao;

	/**
	 * 注册用户
	 * @param user 注册用户信息
	 * @return 用户信息
	 */
	public User register(User user) {
		LOGGER.info("新用户注册，注册用户名是" + user.getUserName());

		// 数据库User表中没有用户名为userName的用户
		final String rawPassword = user.getRawPassword();
		// 设置加密密码
		user.setPassword(BCryptPasswordUtils.BCryptPassword(rawPassword));    // 使用该语句时，数据库里存的是加密后的密码。
		// 新增用户
		authDao.insert(user);
		// 因为要返回User，所以这里把密码都设置为null
		user.setPassword(null);
		user.setRawPassword(null);
		// 执行到这里时数据库中的最后一个用户就是新插入的用户。这里的id就是新插入的用户得id
		long id = userDao.getLastUserId().getId();
		// 为新插入的用户赋予角色。App注册的用户都是ROLE_USER
		authDao.insertRole(id, 2);
		return user;
	}

	/**
	 * 根据用户名查找用户
	 * @param userName 用户名
	 * @return
	 */
	public boolean findByUsername(String userName){
		LOGGER.info("查找" + userName + "用户是否存在");

		if (authDao.findByUsername(userName) != null) {
			//该用户存在
			return true;
		} else {
			//该用户不存在
			return false;
		}
	}
}
