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
    // 系统介绍
    @GetMapping("/system/main")
    public String main(ModelMap mmap) throws Exception {
        /*TestServer t1 = new TestServer("1","192.168.1.1","java");
        TestServer t2 = new TestServer("2","192.168.1.2","gp");
        TestServer t3 = new TestServer("3","192.168.1.3","oracle");
        List<TestServer> servers = new ArrayList<TestServer>();
        servers.add(t1);
        servers.add(t2);
        servers.add(t3);*/

        List<MonHardwareServerInfoDto> servers = monHardwareServerInfoService.queryAllServerInfo();
        mmap.put("servers",servers);
        return "main";
    }
    @GetMapping("/main/server/info")
    @ResponseBody
    public JsonResult serverInfo(@RequestParam String id){
        TestServer server = new TestServer();
        if(id.equals("1")){
            server = new TestServer(id,"192.168.1.1","java","linux","0","正常","3.6");
        }else if(id.equals("2")){
            server = new TestServer(id,"192.168.1.2","gp","linux","2","报警","3.6");
        }else{
            server = new TestServer(id,"192.168.1.3","oracle","linux","1","警告","3.6");
        }

        return new JsonResult(ResultCode.SUCCESS, ResultCode.SUCCESS.getMsg(), server);
    }
}
