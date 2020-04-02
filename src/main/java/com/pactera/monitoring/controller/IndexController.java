package com.pactera.monitoring.controller;

import com.pactera.monitoring.entity.SysMenu;
import com.pactera.monitoring.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@Slf4j
public class IndexController {

    @Autowired
    MenuService menuService;

    @GetMapping("/index")
    public String index(ModelMap mmap){
        List<SysMenu> menus = menuService.selectMenuNormalAll();
        mmap.put("menus",menus);
        return "index";
    }
    // 系统介绍
    @GetMapping("/system/main")
    public String main(ModelMap mmap)
    {
        return "main";
    }
}
