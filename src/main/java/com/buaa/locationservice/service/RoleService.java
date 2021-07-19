package com.buaa.locationservice.service;

import com.buaa.locationservice.dao.RoleDao;
import com.buaa.locationservice.model.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
	private final static Logger LOGGER = LoggerFactory.getLogger(RoleService.class);

	@Autowired
	private RoleDao roleDao;

	/**
	 * 查询所有角色
	 * @return 角色列表
	 */
	public List<Role> getAll() {
		LOGGER.info("查询所有角色");
		return roleDao.getAll();
	}
}
