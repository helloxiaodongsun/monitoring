package com.pactera.monitoring.controller;

import com.pactera.monitoring.entity.Test;
import com.pactera.monitoring.service.TestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class TestController {
    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @Autowired
    private TestService testService;

    @RequestMapping("/test")
    @ResponseBody
    public String test(HttpServletRequest request) {
        String name = request.getParameter("name");
        Test test = null;
        try {
            test = testService.findByName(name);
        } catch (Exception e) {
            test = new Test();
            logger.error("test error.",e);
        }
        logger.info("test....{}",name);
        return test.toString();
    }
}
