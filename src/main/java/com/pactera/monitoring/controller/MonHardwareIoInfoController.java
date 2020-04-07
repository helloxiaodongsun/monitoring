package com.pactera.monitoring.controller;

import com.jcraft.jsch.JSchException;
import com.pactera.monitoring.annotation.MethodExplain;
import com.pactera.monitoring.entity.dto.MonHardwareIoInfoDto;
import com.pactera.monitoring.enums.MethodType;
import com.pactera.monitoring.enums.ResultCode;
import com.pactera.monitoring.exception.BussinessException;
import com.pactera.monitoring.service.MonHardwareIoInfoService;
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
 * @author 84483
 */
@Controller
@RequestMapping("/ioInfo")
public class MonHardwareIoInfoController {

    public static final Logger log = LoggerFactory.getLogger(MonHardwareIoInfoController.class);

    @Autowired
    MonHardwareIoInfoService monHardwareIoInfoService;

    /**
     * 根据ip,查询该服务器的io信息
     * @param ip ip地址
     * @return 结果集
     */
    @GetMapping(value = "/summary")
    @MethodExplain(methodType= MethodType.SELECT,methodName = "io信息查询")
    @ResponseBody
    public JsonResult getIoInfo(@RequestParam(value = "ip") String ip) {
        MonHardwareIoInfoDto monHardwareIoInfoDto;
        try {
             monHardwareIoInfoDto = monHardwareIoInfoService.queryIoInfoByIp(ip);
        } catch (BussinessException | JSchException e) {
            log.error(e.getMessage(),e);
            return new JsonResult(ResultCode.EXCEPTION, e.getMessage());
        }
        return new JsonResult(ResultCode.SUCCESS,ResultCode.SUCCESS.getMsg(),monHardwareIoInfoDto);
    }

}
