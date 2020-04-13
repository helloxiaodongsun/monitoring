package com.pactera.monitoring.controller;

import com.github.pagehelper.PageInfo;
import com.jcraft.jsch.JSchException;
import com.pactera.monitoring.annotation.MethodExplain;
import com.pactera.monitoring.core.controller.BaseController;
import com.pactera.monitoring.core.page.TableDataInfo;
import com.pactera.monitoring.entity.SearchBaseEntity;
import com.pactera.monitoring.entity.dto.MonHardwareMemInfoDto;
import com.pactera.monitoring.enums.MethodType;
import com.pactera.monitoring.exception.BussinessException;
import com.pactera.monitoring.service.MonHardwareMemInfoService;
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
 * 服务器信息
 *
 * @author 84483
 */
@Controller
@RequestMapping("/memInfo")
@Slf4j
public class MonHardwareMemInfoController extends BaseController {

    @Autowired
    MonHardwareMemInfoService monHardwareMemInfoService;

    @GetMapping(value = "/summary")
    @MethodExplain(methodType = MethodType.SELECT, methodName = "服务器内存信息查询")
    @ResponseBody
    public AjaxResult getMemoInfo(@RequestParam(value = "ip") String ip) throws BussinessException, JSchException {
        MonHardwareMemInfoDto monHardwareMemInfoDto = monHardwareMemInfoService.queryServerMemInfo(ip);
        return AjaxResult.success(monHardwareMemInfoDto);
    }

    @PostMapping(value = "/list")
    @MethodExplain(methodType = MethodType.SELECT, methodName = "服务器历史内存信息查询")
    @ResponseBody
    public TableDataInfo getMemoInfoByCondition(SearchBaseEntity searchBaseEntity) {
        startPage();
        PageInfo<MonHardwareMemInfoDto> monHardwareMemInfoDtoPageInfo = monHardwareMemInfoService.queryServerMemInfoFromDbByCondition(searchBaseEntity);
        return getDataTable(monHardwareMemInfoDtoPageInfo);
    }

}
