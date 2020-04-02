package com.pactera.monitoring.service;

import java.util.List;

import com.pactera.monitoring.dao.ds1.TestDao;
import com.pactera.monitoring.dao.ds2.DemoDao;
import com.pactera.monitoring.entity.Demo;
import com.pactera.monitoring.entity.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestService{
    private static final Logger logger = LoggerFactory.getLogger(TestService.class);

    @Autowired
    private TestDao testDao;
    @Autowired
    private DemoDao demoDao;

    public Test findByName(String name) throws Exception{
        List<Demo> demoList = demoDao.getDemoList();
        logger.info(demoList.toString());
        return testDao.findByName(name);
    }
}