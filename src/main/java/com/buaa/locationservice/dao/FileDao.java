package com.buaa.locationservice.dao;

import com.buaa.locationservice.excel.DeviceExcelModel;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
//@Repository
public interface FileDao {
    /**
     * 查询所有位置信息
     * @return 位置信息列表
     */
    List<DeviceExcelModel> getAll();
}
