package com.pactera.monitoring.entity;

import lombok.Data;

@Data
public class TestServer {
    private String id;
    private String ip;
    private String name;
    private String system;
    private String statusCode;
    private String status;
    private String os;

    public TestServer(){}
    public TestServer(String id, String ip, String name) {
        this.id = id;
        this.ip = ip;
        this.name = name;
    }

    public TestServer(String id, String ip, String name, String system, String statusCode,String status, String os) {
        this.id = id;
        this.ip = ip;
        this.name = name;
        this.system = system;
        this.statusCode = statusCode;
        this.status = status;
        this.os = os;
    }
}
