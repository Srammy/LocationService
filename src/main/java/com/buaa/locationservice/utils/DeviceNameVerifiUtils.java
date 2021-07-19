package com.buaa.locationservice.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 设备名校验
 */
public class DeviceNameVerifiUtils {
    public static boolean Verification(String deviceName) {
        boolean result = false;

        Pattern pattern = Pattern.compile("^[a-zA-Z0-9-]+$");
        Matcher matcher = pattern.matcher(deviceName);

        if (matcher.matches()) {
            result = true;
        } else {
            result = false;
        }

        return result;
    }
}
