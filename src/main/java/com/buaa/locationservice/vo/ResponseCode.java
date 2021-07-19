package com.buaa.locationservice.vo;

public enum ResponseCode {
    SUCCESS(200),//成功(注册成功、登录成功)
    FAIL(400),//失败
    UNAUTHORIZED(401),//未认证（签名错误）
    LOGIN_ERROR(401),//"登陆失败，用户名或密码无效"；http请求的header里没有token
    NOT_FOUND(404),//接口不存在
    NOT_SUPPORT(405),//请求方法不正确
    MISS_PARAM(400),//参数缺失
    WRONG_PARAM(4001),//参数类型错误
    INTERNAL_SERVER_ERROR(500),//服务器内部错误
    FORBIDDEN(402),//服务器内部错误
    //-------------20190922 注册用------------
    FAIL_USERNAME_NULL(403),
    FAIL_RAWPASSWORD_NULL(406),
    FAIL_REPASSWORD_NULL(407),
    FAIL_REPASSWORD_RAWPASSWORD_DIF(408),
    FAIL_RAWPASSWORD_WRONG_lENGTH(409),
    FAIL_USER_EXIST(410),
    WRONG_USER_NAME(411),
    WRONG_RAWPASSWORD(412),
    //---------------------------------
    //------20190923 Token用----------
    TOKEN_OUT(413); //token过期
    //-----------------------

    private final int code;

    ResponseCode(int code) {
        this.code = code;
    }

    public int code() {
        return code;
    }
}