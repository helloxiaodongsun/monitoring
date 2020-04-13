package com.pactera.monitoring.controller;

import com.github.pagehelper.PageInfo;
import com.jcraft.jsch.JSchException;
import com.pactera.monitoring.annotation.MethodExplain;
import com.pactera.monitoring.core.controller.BaseController;
import com.pactera.monitoring.core.page.TableDataInfo;
import com.pactera.monitoring.entity.SearchBaseEntity;
import com.pactera.monitoring.entity.dto.MonHardwareCpuInfoDtlDto;
import com.pactera.monitoring.entity.dto.MonHardwareCpuInfoTolDto;
import com.pactera.monitoring.enums.MethodType;
import com.pactera.monitoring.exception.BussinessException;
import com.pactera.monitoring.service.MonHardwareCpuInfoService;
import com.pactera.monitoring.utils.AjaxResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * cpu信息查询
 *
 * @author 84483
 */
@Controller
@RequestMapping("/cpuInfo")
@Slf4j
public class MonHardwareCpuInfoController extends BaseController {

    @Autowired
    MonHardwareCpuInfoService monHardwareCpuInfoService;

    /**
     * 根据ip,查询该服务器cpu的详细信息
     *
     * @param ip ip地址
     * @return 结果集
     */
    @GetMapping(value = "/detail")
    @MethodExplain(methodType = MethodType.SELECT, methodName = "cpu详细信息查询")
    @ResponseBody
    public AjaxResult getCpuInfoDtl(@RequestParam(value = "ip") String ip) throws BussinessException, JSchException {
        MonHardwareCpuInfoDtlDto monHardwareCpuInfoDtlDto = monHardwareCpuInfoService.queryCpuInfoDtl(ip);
        return AjaxResult.success(monHardwareCpuInfoDtlDto);
    }

    /**
     * 根据ip,查询该服务器cpu的汇总信息
     *
     * @param ip ip地址
     * @return 结果集
     */
    @GetMapping(value = "/summary")
    @MethodExplain(methodType = MethodType.SELECT, methodName = "cpu汇总信息查询")
    @ResponseBody
    public AjaxResult getCpuInfoTol(@RequestParam(value = "ip") String ip) throws BussinessException, JSchException {
        MonHardwareCpuInfoTolDto monHardwareCpuInfoTolDto = monHardwareCpuInfoService.queryCpuInfoTol(ip);
        return AjaxResult.success(monHardwareCpuInfoTolDto);
    }

    @PostMapping(value = "/list")
    @MethodExplain(methodType = MethodType.SELECT, methodName = "服务器历史cpu明细信息查询")
    @ResponseBody
    public TableDataInfo getCpuInfoByCondition(SearchBaseEntity searchBaseEntity) {
        startPage();
        PageInfo<MonHardwareCpuInfoDtlDto> monHardwareCpuInfoDtlDtoPageInfo = monHardwareCpuInfoService.queryCpuInfoDtlFromDbByCondition(searchBaseEntity);
        return getDataTable(monHardwareCpuInfoDtlDtoPageInfo);
    }
}
