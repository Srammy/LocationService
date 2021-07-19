package com.buaa.locationservice.dao;

import com.buaa.locationservice.model.Role;
import com.buaa.locationservice.model.SecurityUserDetails;
import com.buaa.locationservice.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
//@Repository
public interface AuthDao {
	/**
	 * 根据用户名查找用户
	 * @param name
	 * @return
	 */
	User findByUsername(@Param("name") String name);

	/**
	 * 创建新用户
	 * @param user
	 */
	void insert(User user);

	/**
	 * 创建用户角色
	 * @param userId
	 * @param roleId
	 * @return
	 */
	void insertRole(@Param("userId") long userId, @Param("roleId") long roleId);

	/**
	 * 根据用户id查找该用户的角色
	 * @param userId
	 * @return
	 */
	List<Role> findRoleByUserId(@Param("userId") long userId);
}
