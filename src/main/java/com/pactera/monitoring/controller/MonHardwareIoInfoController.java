package com.pactera.monitoring.controller;

import com.github.pagehelper.PageInfo;
import com.jcraft.jsch.JSchException;
import com.pactera.monitoring.annotation.MethodExplain;
import com.pactera.monitoring.core.controller.BaseController;
import com.pactera.monitoring.core.page.TableDataInfo;
import com.pactera.monitoring.entity.SearchBaseEntity;
import com.pactera.monitoring.entity.dto.MonHardwareIoInfoDto;
import com.pactera.monitoring.enums.MethodType;
import com.pactera.monitoring.exception.BussinessException;
import com.pactera.monitoring.service.MonHardwareIoInfoService;
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
 * @author 84483
 */
@Controller
@RequestMapping("/ioInfo")
@Slf4j
public class MonHardwareIoInfoController extends BaseController {


    @Autowired
    MonHardwareIoInfoService monHardwareIoInfoService;

    /**
     * 根据ip,查询该服务器的io信息
     *
     * @param ip ip地址
     * @return 结果集
     */
    @GetMapping(value = "/summary")
    @MethodExplain(methodType = MethodType.SELECT, methodName = "io信息查询")
    @ResponseBody
    public AjaxResult getIoInfo(@RequestParam(value = "ip") String ip) throws BussinessException, JSchException {
        MonHardwareIoInfoDto monHardwareIoInfoDto = monHardwareIoInfoService.queryIoInfo(ip);
        return AjaxResult.success(monHardwareIoInfoDto);
    }

    @PostMapping(value = "/list")
    @MethodExplain(methodType = MethodType.SELECT, methodName = "服务器历史IO信息查询")
    @ResponseBody
    public TableDataInfo getIoInfoByCondition(SearchBaseEntity searchBaseEntity) {
        startPage();
        PageInfo<MonHardwareIoInfoDto> monHardwareIoInfoDtoPageInfo = monHardwareIoInfoService.queryIoInfoFromDbByCondition(searchBaseEntity);
        return getDataTable(monHardwareIoInfoDtoPageInfo);
    }

}
