/*
 * 文件名：JsonResult.java 描述： 修改人：ll 修改时间：2017年8月17日 跟踪单号： 修改单号： 修改内容：
 */

package com.pactera.monitoring.utils;


import com.pactera.monitoring.enums.ResultCode;

public class JsonResult {
    private static final long serialVersionUID = 1L;
    private String code;

    private String message;

    private Object data;


    public JsonResult(String code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public JsonResult() {
        this.setCode(ResultCode.SUCCESS);
        this.setMessage("成功");

    }

    public JsonResult(ResultCode code) {
        this.setCode(code);
    }

    public JsonResult(ResultCode code, String message) {
        this.setCode(code);
        this.setMessage(message);
    }

    public JsonResult(ResultCode code, String message, Object data) {
        this.setCode(code);
        this.setMessage(message);
        this.setData(data);
    }

    public String getCode() {
        return code;
    }

    public void setCode(ResultCode code) {
        this.code = code.getCode();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
