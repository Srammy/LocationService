package com.buaa.locationservice.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用户名格式校验工具
 */
public class UserNameVerifi {

    public static boolean Verification(String userName) {
        boolean result = false;

        Pattern pattern = Pattern.compile("^[a-zA-Z0-9-]+$");
        Matcher matcher = pattern.matcher(userName);

        if (matcher.matches()) {
            result = true;
        } else {
            result = false;
        }

        return result;
    }

}
