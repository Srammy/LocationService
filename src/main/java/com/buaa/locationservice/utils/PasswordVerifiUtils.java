package com.buaa.locationservice.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 检验密码格式工具
 */
public class PasswordVerifiUtils {
    public static boolean Verification(String rawpassword) {
        boolean result = false;

        Pattern pattern = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])[a-zA-Z0-9]+$");//必须包含大小写字母和数字的组合，不能使用特殊字符
        Matcher matcher = pattern.matcher(rawpassword);

        if (matcher.matches()) {
            result = true;
        } else {
            result = false;
        }

        return result;
    }
}
