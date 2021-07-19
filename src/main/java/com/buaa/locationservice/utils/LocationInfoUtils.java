package com.buaa.locationservice.utils;

import java.text.DecimalFormat;

/**
 * 位置信息中的进制转换工具
 */
public class LocationInfoUtils {
    //16进制转换为10进制
    public static String HexToDec(String Hex) {
        DecimalFormat df =new DecimalFormat("0.0000");//设置保留位数

        String result = String.valueOf(df.format((float)Integer.parseInt(Hex,16)/1000000)); //16进制转10进制
        return result;
    }
}
