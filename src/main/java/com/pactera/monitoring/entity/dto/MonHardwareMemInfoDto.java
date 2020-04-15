package com.pactera.monitoring.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;


/**内存信息dto
 * @author 84483
 */
public class MonHardwareMemInfoDto implements Serializable {
    private static final long serialVersionUID = 1469882549801235289L;
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date dataDt;


    /**
     * 内存总大小
     */
    private String memTotal;

    /**
     * 内存已使用大小
     */
    private String memUseTotal;

    /**
     * 空闲内存大小
     */
    private String freeMemTotal;

    /**
     * 进程共享内存总额
     */
    private String sharedMemTotal;

    /**
     * 缓存使用大小
     */
    private String bufferCacheUseMemTotal;

    /**
     * 缓存空闲大小
     */
    private String bufferCacheFreeMemTotal;

    /**
     * 交换区总大小
     */
    private String swapMemTotal;

    /**
     * 交换区使用大小
     */
    private String swapUseMemTotal;

    /**
     * 交换区空闲大小
     */
    private String swapFreeMemTotal;

    /**
     * 服务器类型1数据库服务器2ETL服务器3应用服务器
     */
    private String serviceType;

    /**
     * 服务器使用内存占比
     */
    private String memUsedRate;

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

    public String getMemTotal() {
        return memTotal;
    }

    public void setMemTotal(String memTotal) {
        this.memTotal = memTotal;
    }

    public String getMemUseTotal() {
        return memUseTotal;
    }

    public void setMemUseTotal(String memUseTotal) {
        this.memUseTotal = memUseTotal;
    }

    public String getFreeMemTotal() {
        return freeMemTotal;
    }

    public void setFreeMemTotal(String freeMemTotal) {
        this.freeMemTotal = freeMemTotal;
    }

    public String getSharedMemTotal() {
        return sharedMemTotal;
    }

    public void setSharedMemTotal(String sharedMemTotal) {
        this.sharedMemTotal = sharedMemTotal;
    }

    public String getBufferCacheUseMemTotal() {
        return bufferCacheUseMemTotal;
    }

    public void setBufferCacheUseMemTotal(String bufferCacheUseMemTotal) {
        this.bufferCacheUseMemTotal = bufferCacheUseMemTotal;
    }

    public String getBufferCacheFreeMemTotal() {
        return bufferCacheFreeMemTotal;
    }

    public void setBufferCacheFreeMemTotal(String bufferCacheFreeMemTotal) {
        this.bufferCacheFreeMemTotal = bufferCacheFreeMemTotal;
    }

    public String getSwapMemTotal() {
        return swapMemTotal;
    }

    public void setSwapMemTotal(String swapMemTotal) {
        this.swapMemTotal = swapMemTotal;
    }

    public String getSwapUseMemTotal() {
        return swapUseMemTotal;
    }

    public void setSwapUseMemTotal(String swapUseMemTotal) {
        this.swapUseMemTotal = swapUseMemTotal;
    }

    public String getSwapFreeMemTotal() {
        return swapFreeMemTotal;
    }

    public void setSwapFreeMemTotal(String swapFreeMemTotal) {
        this.swapFreeMemTotal = swapFreeMemTotal;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getMemUsedRate() {
        return memUsedRate;
    }

    public void setMemUsedRate(String memUsedRate) {
        this.memUsedRate = memUsedRate;
    }
}
