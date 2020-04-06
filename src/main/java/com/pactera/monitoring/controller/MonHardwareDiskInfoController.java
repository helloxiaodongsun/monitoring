package com.pactera.monitoring.controller;

import com.jcraft.jsch.JSchException;
import com.pactera.monitoring.annotation.MethodExplain;
import com.pactera.monitoring.entity.dto.MonHardwareDiskInfoDtlDto;
import com.pactera.monitoring.entity.dto.MonHardwareDiskInfoTolDto;
import com.pactera.monitoring.enums.MethodType;
import com.pactera.monitoring.enums.ResultCode;
import com.pactera.monitoring.exception.BussinessException;
import com.pactera.monitoring.service.MonHardwareDiskInfoService;
import com.pactera.monitoring.utils.JsonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 硬盘信息查询
 * @author 84483
 */
@Controller
@RequestMapping("/diskInfo")
public class MonHardwareDiskInfoController {

    public static final Logger log = LoggerFactory.getLogger(MonHardwareDiskInfoController.class);

    @Autowired
    MonHardwareDiskInfoService monHardwareDiskInfoService;

    /**
     * 根据ip,查询该服务器硬盘的详细信息
     * @param ip ip地址
     * @return 结果集
     */
    @GetMapping(value = "/detail")
    @MethodExplain(methodType= MethodType.SELECT,methodName = "硬盘详细信息查询")
    @ResponseBody
    public JsonResult getDiskInfoDtl(@RequestParam(value = "ip") String ip) {
        List<MonHardwareDiskInfoDtlDto> monHardwareDiskInfoDtlDtoList;
        try {
            monHardwareDiskInfoDtlDtoList = monHardwareDiskInfoService.queryDiskInfoDtl(ip);
        } catch (BussinessException | JSchException e) {
            log.error(e.getMessage(),e);
            return new JsonResult(ResultCode.EXCEPTION, e.getMessage());
        }
        return new JsonResult(ResultCode.SUCCESS,ResultCode.SUCCESS.getMsg(),monHardwareDiskInfoDtlDtoList);
    }

    /**
     * 根据ip,查询该服务器硬盘的汇总信息
     * @param ip ip地址
     * @return 结果集
     */
    @GetMapping(value = "/summary")
    @MethodExplain(methodType= MethodType.SELECT,methodName = "硬盘汇总信息查询")
    @ResponseBody
    public JsonResult getDiskInfoTol(@RequestParam(value = "ip") String ip) {

        MonHardwareDiskInfoTolDto monHardwareDiskInfoTolDto;
        try {
            monHardwareDiskInfoTolDto = monHardwareDiskInfoService.queryDiskInfoTol(ip);
        } catch (BussinessException | JSchException e) {
            log.error(e.getMessage(),e);
            return new JsonResult(ResultCode.EXCEPTION, e.getMessage());
        }
        return new JsonResult(ResultCode.SUCCESS,ResultCode.SUCCESS.getMsg(),monHardwareDiskInfoTolDto);
    }

}
