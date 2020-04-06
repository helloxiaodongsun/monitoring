package com.pactera.monitoring.entity.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * cpu汇总信息dto
 * @author 84483
 */
public class MonHardwareCpuInfoTolDto implements Serializable {
    private static final long serialVersionUID = -5145280578584006390L;
    /**
     * 服务器名称
     */
    private String serviceNm;

    /**
     * 服务器IP
     */
    private String serviceIp;

    /**
     * 日期
     */
    private Date dataDt;

    /**
     * 记录时间戳
     */
    private Date recordDt;

    /**
     * CPU型号
     */
    private String cpuType;

    /**
     * CPU个数
     */
    private String cpuNum;

    /**
     * CPU线程数
     */
    private String cpuThread;

    /**
     * 服务器类型1数据库服务器2ETL服务器3应用服务器
     */
    private String serviceType;

    public String getServiceNm() {
        return serviceNm;
    }

    public void setServiceNm(String serviceNm) {
        this.serviceNm = serviceNm;
    }

    public String getServiceIp() {
        return serviceIp;
    }

    public void setServiceIp(String serviceIp) {
        this.serviceIp = serviceIp;
    }

    public Date getDataDt() {
        return dataDt;
    }

    public void setDataDt(Date dataDt) {
        this.dataDt = dataDt;
    }

    public Date getRecordDt() {
        return recordDt;
    }

    public void setRecordDt(Date recordDt) {
        this.recordDt = recordDt;
    }

    public String getCpuType() {
        return cpuType;
    }

    public void setCpuType(String cpuType) {
        this.cpuType = cpuType;
    }

    public String getCpuNum() {
        return cpuNum;
    }

    public void setCpuNum(String cpuNum) {
        this.cpuNum = cpuNum;
    }

    public String getCpuThread() {
        return cpuThread;
    }

    public void setCpuThread(String cpuThread) {
        this.cpuThread = cpuThread;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }
}
