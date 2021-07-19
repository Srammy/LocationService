package com.buaa.locationservice.dao;

import com.buaa.locationservice.model.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
//@Repository
public interface RoleDao {
    /**
     * 查询所有角色
     * @return 角色列表
     */
    List<Role> getAll();

    /**
     * 查询某资源对应的角色
     * @param uri 资源
     * @return 角色列表
     */
    List<Role> getRoleByResource(@Param("uri") String uri);
}
