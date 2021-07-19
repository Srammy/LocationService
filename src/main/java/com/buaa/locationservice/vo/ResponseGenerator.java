package com.buaa.locationservice.vo;


import com.buaa.locationservice.utils.Constants;

public class ResponseGenerator {

    public static ResponseVo genSuccessResult() {
        return new ResponseVo()
                .setResponseCode(ResponseCode.SUCCESS.code())
                .setResponseMessage(Constants.responseCodeSuccess);
    }

    public static ResponseVo genSuccessResult(Object data) {
        return new ResponseVo()
                .setResponseCode(ResponseCode.SUCCESS.code())
                .setResponseMessage(Constants.responseCodeSuccess)
                .setResponseBody(data);
    }

    public static ResponseVo genSuccessResult(String message) {
        return new ResponseVo()
                .setResponseCode(ResponseCode.SUCCESS.code())
                .setResponseMessage(Constants.responseCodeError+","+message);
    }

    public static ResponseVo genFailResult(String message) {
        return new ResponseVo()
                .setResponseCode(ResponseCode.FAIL.code())
                .setResponseMessage(Constants.responseCodeError+","+message);
    }

    public static ResponseVo genFailResult(int code, String message) {
        return new ResponseVo()
                .setResponseCode(code)
                .setResponseMessage(Constants.responseCodeError+","+message);
    }
}
