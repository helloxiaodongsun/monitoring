package com.pactera.monitoring.entity;

/**前台查询基础类
 * @author 8
 */
public class SearchBaseEntity  {
    //搜索ip
    private String ip;

    //开始日期
    private String startTime;

    //结束日期
    private String endTime;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
