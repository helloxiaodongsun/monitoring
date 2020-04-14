package com.pactera.monitoring.filter;

import com.jcraft.jsch.JSchException;
import com.pactera.monitoring.exception.BussinessException;
import com.pactera.monitoring.utils.AjaxResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@ResponseBody
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(value = {BussinessException.class, JSchException.class})
    public AjaxResult branchException(HttpServletRequest request, Exception exception) {
        log.error("===============请求内容-start===============");
        log.error("请求地址:" + request.getRequestURL());
        log.error("请求方式:" + request.getMethod());
        log.error("===============请求内容-end===============");

        log.error("--------------返回内容-start----------------");
        log.error("异常描述：" + exception.getMessage());
        log.error("异常信息", exception);
        log.error("--------------返回内容-end----------------");
        return AjaxResult.error(exception.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    public AjaxResult allException(HttpServletRequest request, Exception exception) {
        log.error("===============请求内容-start===============");
        log.error("请求地址:" + request.getRequestURL());
        log.error("请求方式:" + request.getMethod());
        log.error("===============请求内容-end===============");

        log.error("--------------返回内容-start----------------");
        log.error("异常描述：" + exception.getMessage());
        log.error("异常信息", exception);
        log.error("--------------返回内容-end----------------");
        return AjaxResult.error();
    }
}
