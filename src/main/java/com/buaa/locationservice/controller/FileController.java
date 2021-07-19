package com.buaa.locationservice.controller;

import com.buaa.locationservice.service.FileService;
import com.buaa.locationservice.utils.ExcelUtils;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

@RestController
@RequestMapping("/download")
public class FileController {
	private final static Logger LOGGER = LoggerFactory.getLogger(FileController.class);

	@Autowired
	private FileService fileService;

	@ApiOperation(value ="用户登录:/exportDeviceInfo",notes ="导出设备信息",httpMethod = "GET")
	@RequestMapping(value = "/exportDeviceInfo",method = RequestMethod.GET)
	public void exportDeviceInfo(HttpServletResponse response) throws IOException {
		LOGGER.info("导出设备信息");
		response.setContentType("multipart/form-data");
		response.setHeader("content-type", "application/octet-stream");
		response.setHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode("测试生成Excel文件.xlsx", "utf-8"));
		OutputStream outputStream = response.getOutputStream();
		ExcelUtils.writeWithTemplate(fileService.getDeviceAllExcelModel(),outputStream);
		LOGGER.info("导出设备信息成功");
		return ;
	}
}
