package com.buaa.locationservice.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

public class ResponseVo {

    private int responseCode;
    private String responseMessage;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object responseBody;

    public int getResponseCode() {
        return responseCode;
    }

    public ResponseVo setResponseCode(int responseCode) {
        this.responseCode = responseCode;
        return this;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public ResponseVo setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
        return this;
    }

    public Object getResponseBody() {
        return responseBody;
    }

    public ResponseVo setResponseBody(Object responseBody) {
        this.responseBody = responseBody;
        return this;
    }

}
