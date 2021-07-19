package com.buaa.locationservice.model;

import lombok.Data;

import java.util.List;

/**
 * @desc: 用户信息
 */
@Data
public class User {
    private long id;
    private String userName;
    private String password; //加密的密码
    private String rawPassword; //未加密的密码
    private Integer status;
    private List<Role> roles;
}
