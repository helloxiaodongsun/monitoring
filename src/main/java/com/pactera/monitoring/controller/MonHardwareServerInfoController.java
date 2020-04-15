package com.pactera.monitoring.controller;

import com.jcraft.jsch.JSchException;
import com.pactera.monitoring.annotation.MethodExplain;
import com.pactera.monitoring.entity.dto.MonHardwareServerInfoDto;
import com.pactera.monitoring.enums.MethodType;
import com.pactera.monitoring.exception.BussinessException;
import com.pactera.monitoring.service.MonHardwareServerInfoService;
import com.pactera.monitoring.utils.AjaxResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

/**
 * 服务器信息
 *
 * @author 84483
 */
@Controller
@RequestMapping("/serverInfo")
@Slf4j
public class MonHardwareServerInfoController {


    @Autowired
    MonHardwareServerInfoService monHardwareServerInfoService;

    /**
     * 根据ip,查询该服务器的基本信息
     *
     * @param ip ip地址
     * @return 结果集
     */
    @GetMapping(value = "/summary")
    @MethodExplain(methodType = MethodType.SELECT, methodName = "服务器基本信息查询")
    @ResponseBody
    public AjaxResult getServerInfo(@RequestParam(value = "ip") String ip) throws BussinessException, JSchException, IOException {
        MonHardwareServerInfoDto monHardwareServerInfoDto = monHardwareServerInfoService.queryServerInfoByIp(ip);
        return AjaxResult.success(monHardwareServerInfoDto);
    }

}
