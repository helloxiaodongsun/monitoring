package com.pactera.monitoring.controller;

import com.pactera.monitoring.entity.SysMenu;
import com.pactera.monitoring.entity.TestServer;
import com.pactera.monitoring.entity.dto.MonHardwareServerInfoDto;
import com.pactera.monitoring.enums.ResultCode;
import com.pactera.monitoring.service.MenuService;
import com.pactera.monitoring.service.MonHardwareServerInfoService;
import com.pactera.monitoring.utils.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@Slf4j
public class IndexController {

    @Autowired
    MenuService menuService;
    @Autowired
    MonHardwareServerInfoService monHardwareServerInfoService;

    @GetMapping("/index")
    public String index(ModelMap mmap){
        List<SysMenu> menus = menuService.selectMenuNormalAll();
        mmap.put("menus",menus);
        return "index";
    }

    /**
     * 主页
     * @param mmap
     * @return
     * @throws Exception
     */
    @GetMapping("/system/main")
    public String main(ModelMap mmap) throws Exception {
        List<MonHardwareServerInfoDto> servers = monHardwareServerInfoService.queryAllServerInfo();
        mmap.put("servers",servers);
        return "main";
    }

    /**
     * 进入硬盘信息监控页面
     * @param mmap
     * @return
     */
    @RequestMapping("/hardware/disk")
    public String diskIndex(ModelMap mmap) throws Exception {
        List<MonHardwareServerInfoDto> servers = monHardwareServerInfoService.queryAllServerInfo();
        mmap.put("servers",servers);
        return "hardware/disk";
    }

}
