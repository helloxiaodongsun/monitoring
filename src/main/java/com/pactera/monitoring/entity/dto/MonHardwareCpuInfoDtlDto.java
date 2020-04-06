package com.pactera.monitoring.entity.dto;

import java.io.Serializable;
import java.util.Date;

/**cpu明细信息Dto
 * @author 84483
 */
public class MonHardwareCpuInfoDtlDto implements Serializable {

    private static final long serialVersionUID = 4144979621949126470L;
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
     * 用户空间占用CPU百分比
     */
    private String usCpuRate;

    /**
     * 内核空间占用CPU的百分比
     */
    private String syCpuRate;

    /**
     * 改变过优先级的进程占用CPU的百分比
     */
    private String niCpuRate;

    /**
     * 空闲CPU百分比
     */
    private String idCpuRate;

    /**
     * IO等待占用CPU的百分比
     */
    private String waCpuRate;

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

    public String getUsCpuRate() {
        return usCpuRate;
    }

    public void setUsCpuRate(String usCpuRate) {
        this.usCpuRate = usCpuRate;
    }

    public String getSyCpuRate() {
        return syCpuRate;
    }

    public void setSyCpuRate(String syCpuRate) {
        this.syCpuRate = syCpuRate;
    }

    public String getNiCpuRate() {
        return niCpuRate;
    }

    public void setNiCpuRate(String niCpuRate) {
        this.niCpuRate = niCpuRate;
    }

    public String getIdCpuRate() {
        return idCpuRate;
    }

    public void setIdCpuRate(String idCpuRate) {
        this.idCpuRate = idCpuRate;
    }

    public String getWaCpuRate() {
        return waCpuRate;
    }

    public void setWaCpuRate(String waCpuRate) {
        this.waCpuRate = waCpuRate;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }
}
