package com.pactera.monitoring.controller;

import com.jcraft.jsch.JSchException;
import com.pactera.monitoring.annotation.MethodExplain;
import com.pactera.monitoring.entity.dto.MonHardwareMemInfoDto;
import com.pactera.monitoring.enums.MethodType;
import com.pactera.monitoring.enums.ResultCode;
import com.pactera.monitoring.exception.BussinessException;
import com.pactera.monitoring.service.MonHardwareMemInfoService;
import com.pactera.monitoring.utils.JsonResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 服务器信息
 *
 * @author 84483
 */
@Controller
@RequestMapping("/memInfo")
public class MonHardwareMemInfoController {

    public static final Logger log = LoggerFactory.getLogger(MonHardwareMemInfoController.class);
    @Autowired
    MonHardwareMemInfoService monHardwareMemInfoService;

    @GetMapping(value = "/summary")
    @MethodExplain(methodType = MethodType.SELECT, methodName = "服务器内存信息查询")
    @ResponseBody
    public JsonResult getMemoInfo(@RequestParam(value = "ip") String ip) {
        MonHardwareMemInfoDto monHardwareMemInfoDto;
        try {
            monHardwareMemInfoDto = monHardwareMemInfoService.queryServerMemInfoByIp(ip);
        } catch (BussinessException | JSchException e) {
            log.error(e.getMessage(), e);
            return new JsonResult(ResultCode.EXCEPTION, e.getMessage());
        }
        return new JsonResult(ResultCode.SUCCESS, ResultCode.SUCCESS.getMsg(), monHardwareMemInfoDto);
    }

}
