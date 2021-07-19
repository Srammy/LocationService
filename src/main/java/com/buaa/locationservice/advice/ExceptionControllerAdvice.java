package com.buaa.locationservice.advice;

import com.buaa.locationservice.Exception.CustomException;
import com.buaa.locationservice.vo.ResponseCode;
import com.buaa.locationservice.vo.ResponseGenerator;
import com.buaa.locationservice.vo.ResponseVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestControllerAdvice
public class ExceptionControllerAdvice {
    private final static Logger LOGGER = LoggerFactory.getLogger(ExceptionControllerAdvice.class);
    @ExceptionHandler(Exception.class)
    public ResponseVo jsonErrorHandler(HttpServletRequest req, HttpServletResponse rsp, Exception e){
        e.printStackTrace();
        LOGGER.error("jsonErrorHandler",e);
        String message = "Uri:"+req.getRequestURI()+",Params:"+req.getQueryString()+",Message:"+e.getMessage();
        LOGGER.error(message);
        ResponseVo rv = null;
        if(e instanceof NoHandlerFoundException){
            rv = ResponseGenerator.genFailResult(ResponseCode.NOT_FOUND.code(),message);
        }else if(e instanceof HttpRequestMethodNotSupportedException){
            rv = ResponseGenerator.genFailResult(ResponseCode.NOT_SUPPORT.code(),message);
        }else if(e instanceof MissingServletRequestParameterException){
            rv = ResponseGenerator.genFailResult(ResponseCode.MISS_PARAM.code(),message);
        }else if(e instanceof MethodArgumentTypeMismatchException){
            rv = ResponseGenerator.genFailResult(ResponseCode.WRONG_PARAM.code(),message);
        }else if(e instanceof CustomException){
            rv = ((CustomException) e).getResponseVo();
        }else{
            rv = ResponseGenerator.genFailResult(ResponseCode.INTERNAL_SERVER_ERROR.code(),message);
        }
        return rv;
    }

}
