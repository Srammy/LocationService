package com.buaa.locationservice.service;

import com.buaa.locationservice.dao.FileDao;
import com.buaa.locationservice.excel.DeviceExcelModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {
	private final static Logger LOGGER = LoggerFactory.getLogger(FileService.class);

	@Autowired
	private FileDao fileDao;

	/**
	 * 查询所有位置信息
	 * @return 位置信息列表
	 */
	public List<DeviceExcelModel> getDeviceAllExcelModel(){
		LOGGER.info("需要以文件形式下载位置信息，获取所有位置信息");
		return fileDao.getAll();
	}
}
