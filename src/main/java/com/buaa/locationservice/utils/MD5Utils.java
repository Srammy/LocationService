package com.buaa.locationservice.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * MD5工具类
 */
public class MD5Utils {

	/**
	 * 传入一个文件，返回一个对应md5码，32位小写
	 * @param file
	 * @return
	 */
	public static String getMD5ByFile(File file) {
		String value = null;
		FileInputStream is = null;
		try {
			is = new FileInputStream(file);
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			//每次读取块大小=102400
			byte[] buffer = new byte[102400];
			int len;
			while ((len = is.read(buffer)) != -1) {
				md5.update(buffer, 0, len);
			}
			BigInteger bi = new BigInteger(1, md5.digest());
			value = bi.toString(16);
		} catch (Exception e) {
			try {
				throw new Exception("XIOA0183BO02"+"==="+file.getName()+"==="+e);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} finally {
			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
					// e.printStackTrace();log002
					//System.out.println(log002);
				}
			}
		}
		return StringUtils.addCharToStringLeft(value, '0', 32);
	}

	public static String getMD5ByString(String str) {
		String value = null;
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");

			BigInteger bi = new BigInteger(1,md5.digest(str.getBytes()));

			value = bi.toString(16);
		} catch (Exception e) {
			try {
				throw new Exception("XIOA0183BO02"+"==="+str+"==="+e);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return StringUtils.addCharToStringLeft(value, '0', 32);
	}
}
