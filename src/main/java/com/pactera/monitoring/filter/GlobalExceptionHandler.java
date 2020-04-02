package com.pactera.monitoring.filter;

import com.pactera.monitoring.enums.ResultCode;
import com.pactera.monitoring.utils.JsonResult;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
	
	/**
     * log
     */
    private static final Logger LOGGER = Logger.getLogger(GlobalExceptionHandler.class);
    

	@ExceptionHandler(value=Exception.class)
	public JsonResult allException(HttpServletRequest request, Exception exception) {
        LOGGER.error("===============请求内容-start===============");
        LOGGER.error("请求地址:" + request.getRequestURL());
        LOGGER.error("请求方式:" + request.getMethod());
        LOGGER.error("===============请求内容-end===============");
		
        LOGGER.error("--------------返回内容-start----------------");
        LOGGER.error("异常描述："+exception.getMessage());
        LOGGER.error("异常信息",exception);
        LOGGER.error("--------------返回内容-end----------------");
		
        return new JsonResult(ResultCode.EXCEPTION,ResultCode.EXCEPTION.getMsg(),null);
	}
}
