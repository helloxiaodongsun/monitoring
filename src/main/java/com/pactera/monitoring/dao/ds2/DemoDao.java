package com.pactera.monitoring.dao.ds2;

import com.pactera.monitoring.entity.Demo;

import java.util.List;

public interface DemoDao {
    List<Demo> getDemoList() throws Exception;
}
