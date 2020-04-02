package com.pactera.monitoring.service;

import com.pactera.monitoring.entity.SysMenu;

import java.util.List;

public interface MenuService {
    public List<SysMenu> selectMenuNormalAll();
    public List<SysMenu> selectMenuAll();
}
