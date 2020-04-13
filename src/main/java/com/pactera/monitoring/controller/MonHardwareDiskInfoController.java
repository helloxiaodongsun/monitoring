package com.pactera.monitoring.controller;

import com.github.pagehelper.PageInfo;
import com.jcraft.jsch.JSchException;
import com.pactera.monitoring.annotation.MethodExplain;
import com.pactera.monitoring.core.controller.BaseController;
import com.pactera.monitoring.core.page.TableDataInfo;
import com.pactera.monitoring.entity.SearchBaseEntity;
import com.pactera.monitoring.entity.dto.MonHardwareDiskInfoDtlDto;
import com.pactera.monitoring.entity.dto.MonHardwareDiskInfoTolDto;
import com.pactera.monitoring.enums.MethodType;
import com.pactera.monitoring.exception.BussinessException;
import com.pactera.monitoring.service.MonHardwareDiskInfoService;
import com.pactera.monitoring.utils.AjaxResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 硬盘信息查询
 *
 * @author 84483
 */
@Controller
@RequestMapping("/diskInfo")
@Slf4j
public class MonHardwareDiskInfoController extends BaseController {


    @Autowired
    MonHardwareDiskInfoService monHardwareDiskInfoService;

    /**
     * 根据ip,查询该服务器硬盘的详细信息
     *
     * @param ip ip地址
     * @return 结果集
     */
    @GetMapping(value = "/detail")
    @MethodExplain(methodType = MethodType.SELECT, methodName = "硬盘详细信息查询")
    @ResponseBody
    public AjaxResult getDiskInfoDtl(@RequestParam(value = "ip") String ip) throws BussinessException, JSchException {
        List<MonHardwareDiskInfoDtlDto> monHardwareDiskInfoDtlDtoList = monHardwareDiskInfoService.queryDiskInfoDtl(ip);
        return AjaxResult.success(monHardwareDiskInfoDtlDtoList);
    }

    /**
     * 根据ip,查询该服务器硬盘的汇总信息
     *
     * @param ip ip地址
     * @return 结果集
     */
    @GetMapping(value = "/summary")
    @MethodExplain(methodType = MethodType.SELECT, methodName = "硬盘汇总信息查询")
    @ResponseBody
    public AjaxResult getDiskInfoTol(@RequestParam(value = "ip") String ip) throws BussinessException, JSchException {
        MonHardwareDiskInfoTolDto monHardwareDiskInfoTolDto = monHardwareDiskInfoService.queryDiskInfoTol(ip);
        return AjaxResult.success(monHardwareDiskInfoTolDto);
    }

    @PostMapping(value = "/list")
    @MethodExplain(methodType = MethodType.SELECT, methodName = "服务器历史硬盘明细信息查询")
    @ResponseBody
    public TableDataInfo getDiskInfoByCondition(SearchBaseEntity searchBaseEntity) {
        startPage();
        PageInfo<MonHardwareDiskInfoDtlDto> monHardwareDiskInfoDtlDtoPageInfo = monHardwareDiskInfoService.queryDiskInfoDtlFromDbByCondition(searchBaseEntity);
        return getDataTable(monHardwareDiskInfoDtlDtoPageInfo);
    }

}
