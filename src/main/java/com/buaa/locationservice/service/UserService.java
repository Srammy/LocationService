package com.buaa.locationservice.service;

import com.buaa.locationservice.dao.AuthDao;
import com.buaa.locationservice.dao.UserDao;
import com.buaa.locationservice.model.User;
import com.buaa.locationservice.utils.BCryptPasswordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final static Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private AuthDao authDao;

    /**
     * 查询所有用户
     * @return 用户列表
     */
    public List<User> getAllUsers(){
        LOGGER.info("查询所有用户");
        return userDao.getAll();
    }

    /**
     * 新增用户
     * @param user 新增用户的用户信息
     * @return 操作是否成功
     */
    public boolean addUser(User user) {
        LOGGER.info("新增用户，用户名是" + user.getUserName());
        // 查询数据库中是否存在用户名为user.getUserName()的用户
        List<User> userList = userDao.getByUser(user);

        // 用户名为user.getUserName()的用户不存在
        if(userList == null || userList.size() == 0){
            user.setPassword(BCryptPasswordUtils.BCryptPassword(user.getRawPassword()));
            userDao.save(user);
            // 查询新增加的用户在数据库user表中的id
            long id = userDao.getLastUserId().getId();
            // 为新增加的用户赋予“普通用户”的角色
            authDao.insertRole(id, 2);
            return true;
        } else {
            // 用户名为user.getUserName()的用户不存在
            return false;
        }
    }

    /**
     * 根据用户名和密码删除用户
     * @param userName 用户名
     * @param password 密码
     */
    public boolean deleteByUserNameAndPassword(String userName, String password){
        LOGGER.info("删除" + userName + "用户");
        User user = null;
        userDao.deleteByUserNameAndPassword(userName, password);

        // 删除后查看该用户是否还存在
        user = authDao.findByUsername(userName);
        if (user == null) {
            // 删除成功
            return true;
        } else {
            // 删除失败
            return false;
        }
    }
}
