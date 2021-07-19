package com.buaa.locationservice.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 设备号校验
 */
public class DeviceNumVerifiUtils {
    public static boolean Verification(String deviceNumber) {
        boolean result = false;

        Pattern pattern = Pattern.compile("^[0-9]+$");
        Matcher matcher = pattern.matcher(deviceNumber);

        if (matcher.matches()) {
            result = true;
        } else {
            result = false;
        }

        return result;
    }
}
