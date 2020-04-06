package com.pactera.monitoring.entity.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * 服务器信息Dto
 * @author 84483
 */
public class MonHardwareServerInfoDto implements Serializable {

    private static final long serialVersionUID = 419642269252388291L;

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
     * 服务器版本信息
     */
    private String serviceVersion;

    /**
     * 服务器运行状态1正常0死亡
     */
    private String serviceActive;

    /**
     * 服务器内核版本信息
     */
    private String serviceCoreVersion;

    /**
     * 服务器类型1数据库服务器2ETL服务器3应用服务器
     */
    private String serviceType;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

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

    public String getServiceVersion() {
        return serviceVersion;
    }

    public void setServiceVersion(String serviceVersion) {
        this.serviceVersion = serviceVersion;
    }

    public String getServiceActive() {
        return serviceActive;
    }

    public void setServiceActive(String serviceActive) {
        this.serviceActive = serviceActive;
    }

    public String getServiceCoreVersion() {
        return serviceCoreVersion;
    }

    public void setServiceCoreVersion(String serviceCoreVersion) {
        this.serviceCoreVersion = serviceCoreVersion;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }
}
