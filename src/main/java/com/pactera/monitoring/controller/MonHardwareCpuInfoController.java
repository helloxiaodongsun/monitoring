package com.pactera.monitoring.controller;

import com.jcraft.jsch.JSchException;
import com.pactera.monitoring.annotation.MethodExplain;
import com.pactera.monitoring.entity.dto.MonHardwareCpuInfoDtlDto;
import com.pactera.monitoring.entity.dto.MonHardwareCpuInfoTolDto;
import com.pactera.monitoring.enums.MethodType;
import com.pactera.monitoring.enums.ResultCode;
import com.pactera.monitoring.exception.BussinessException;
import com.pactera.monitoring.service.MonHardwareCpuInfoService;
import com.pactera.monitoring.utils.JsonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**cpu信息查询
 * @author 84483
 */
@Controller
@RequestMapping("/cpuInfo")
public class MonHardwareCpuInfoController {

    public static final Logger log = LoggerFactory.getLogger(MonHardwareCpuInfoController.class);
    @Autowired
    MonHardwareCpuInfoService monHardwareCpuInfoService;

    /**
     * 根据ip,查询该服务器cpu的详细信息
     * @param ip ip地址
     * @return 结果集
     */
    @GetMapping(value = "/detail")
    @MethodExplain(methodType= MethodType.SELECT,methodName = "cpu详细信息查询")
    @ResponseBody
    public JsonResult getCpuInfoDtl(@RequestParam(value = "ip") String ip) {

        MonHardwareCpuInfoDtlDto monHardwareCpuInfoDtlDto;
        try {
            monHardwareCpuInfoDtlDto = monHardwareCpuInfoService.queryCpuInfoDtl(ip);
        } catch (BussinessException | JSchException e) {
            log.error(e.getMessage(),e);
            return new JsonResult(ResultCode.EXCEPTION, e.getMessage());
        }
        return new JsonResult(ResultCode.SUCCESS,ResultCode.SUCCESS.getMsg(),monHardwareCpuInfoDtlDto);
    }

    /**
     * 根据ip,查询该服务器cpu的汇总信息
     * @param ip ip地址
     * @return 结果集
     */
    @GetMapping(value = "/summary")
    @MethodExplain(methodType= MethodType.SELECT,methodName = "cpu汇总信息查询")
    @ResponseBody
    public JsonResult getCpuInfoTol(@RequestParam(value = "ip") String ip) {

        MonHardwareCpuInfoTolDto monHardwareCpuInfoTolDto;
        try {
            monHardwareCpuInfoTolDto = monHardwareCpuInfoService.queryCpuInfoTol(ip);
        } catch (BussinessException | JSchException e) {
            log.error(e.getMessage(),e);
            return new JsonResult(ResultCode.EXCEPTION, e.getMessage());
        }
        return new JsonResult(ResultCode.SUCCESS,ResultCode.SUCCESS.getMsg(),monHardwareCpuInfoTolDto);
    }
}
