package com.buaa.locationservice.dao;

import com.buaa.locationservice.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
//@Repository
public interface UserDao {
    /**
     * 新增用户
     * @param user 用户信息
     * @return
     */
    void save(@Param("user") User user);

    /**
     * 查询所有用户
     * @return 用户信息列表
     */
    List<User> getAll();

    /**
     * 查询用户列表
     * @param user 用户信息
     * @return 用户信息列表
     */
    List<User> getByUser(@Param("user") User user);

    /**
     * 删除用户
     * @param userName 用户名
     * @param password 密码
     */
    void deleteByUserNameAndPassword(@Param("userName") String userName, @Param("password") String password);

    /**
     * 查询最后一个用户ID
     * @return 用户信息
     */
    User getLastUserId();
}
