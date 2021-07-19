package com.buaa.locationservice.utils;

import java.math.BigInteger;

/**
 * 字符串处理工具
 */
public class StringUtils {
    public static String sixteenToTen(String sixteenStr) {
      return new BigInteger(sixteenStr, 16).toString(10);
    }

    public static boolean testIsLong(String str) {
        try {
            Long.parseLong(str);
            return true;
        }  catch(Exception e){
            return false;
        }
    }

    public static boolean testIsInt(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean testIsDateYYYYMMDD(String str) {
        try {
            DateUtils.getDateYYYYMMDD(str);
            return true;
         } catch (Exception e) {
            return false;
        }
    }

    public static String addCharToStringLeft(String value, char c, int len) {
        int strLen = value.length();
        StringBuffer strBuff = new StringBuffer();
        while (strLen < len) {
            strBuff.append(c);
            strLen++;
        }
        strBuff.append(value);
        return strBuff.toString();
    }

    public static String addCharToStringRight(String value, char c, int len) {
        int strLen = value.length();
        StringBuffer strBuff = new StringBuffer();
        strBuff.append(value);
        while (strLen < len) {
            strBuff.append(c);
            strLen++;
        }
        return strBuff.toString();
    }
}
