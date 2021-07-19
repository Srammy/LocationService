package com.buaa.locationservice.Exception;


import com.buaa.locationservice.vo.ResponseVo;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException{
    private ResponseVo responseVo;

    public CustomException(ResponseVo responseVo) {
        this.responseVo = responseVo;
    }

    public ResponseVo getResponseVo() {
        return responseVo;
    }
}