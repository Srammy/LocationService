package com.buaa.locationservice.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class DeviceExcelModel extends BaseRowModel {
	/**
	 * value: 表头名称
	 * index: 列的号, 0表示第一列
	 */
	@ExcelProperty(value = "设备id", index = 0)
	private long id;

	@ExcelProperty(value = "设备编号",index = 1)
	private String deviceNumber;

	@ExcelProperty(value = "设备名称",index = 2)
	private String deviceName;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDeviceNumber() {
		return deviceNumber;
	}

	public void setDeviceNumber(String deviceNumber) {
		this.deviceNumber = deviceNumber;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
}
