package com.pactera.monitoring.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * 内存基础信息-明细
 */
public class MonHardwareMemInfoDtl {
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
     * 记录时间戳
     */
    private Date recordDt;

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

    /**
     * 字符备用字段2
     */
    private String charBak2;

    /**
     * 字符备用字段3
     */
    private String charBak3;

    /**
     * 字符备用字段4
     */
    private String charBak4;

    /**
     * 字符备用字段5
     */
    private String charBak5;

    /**
     * 数字备用字段1
     */
    private Short numBak1;

    /**
     * 数字备用字段2
     */
    private Short numBak2;

    /**
     * 数字备用字段3
     */
    private Short numBak3;

    /**
     * 数字备用字段4
     */
    private Short numBak4;

    /**
     * 数字备用字段5
     */
    private Short numBak5;

    public String getServiceNm() {
        return serviceNm;
    }

    public void setServiceNm(String serviceNm) {
        this.serviceNm = serviceNm == null ? null : serviceNm.trim();
    }

    public String getServiceIp() {
        return serviceIp;
    }

    public void setServiceIp(String serviceIp) {
        this.serviceIp = serviceIp == null ? null : serviceIp.trim();
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

    public String getMemTotal() {
        return memTotal;
    }

    public void setMemTotal(String memTotal) {
        this.memTotal = memTotal == null ? null : memTotal.trim();
    }

    public String getMemUseTotal() {
        return memUseTotal;
    }

    public void setMemUseTotal(String memUseTotal) {
        this.memUseTotal = memUseTotal == null ? null : memUseTotal.trim();
    }

    public String getFreeMemTotal() {
        return freeMemTotal;
    }

    public void setFreeMemTotal(String freeMemTotal) {
        this.freeMemTotal = freeMemTotal == null ? null : freeMemTotal.trim();
    }

    public String getSharedMemTotal() {
        return sharedMemTotal;
    }

    public void setSharedMemTotal(String sharedMemTotal) {
        this.sharedMemTotal = sharedMemTotal == null ? null : sharedMemTotal.trim();
    }

    public String getBufferCacheUseMemTotal() {
        return bufferCacheUseMemTotal;
    }

    public void setBufferCacheUseMemTotal(String bufferCacheUseMemTotal) {
        this.bufferCacheUseMemTotal = bufferCacheUseMemTotal == null ? null : bufferCacheUseMemTotal.trim();
    }

    public String getBufferCacheFreeMemTotal() {
        return bufferCacheFreeMemTotal;
    }

    public void setBufferCacheFreeMemTotal(String bufferCacheFreeMemTotal) {
        this.bufferCacheFreeMemTotal = bufferCacheFreeMemTotal == null ? null : bufferCacheFreeMemTotal.trim();
    }

    public String getSwapMemTotal() {
        return swapMemTotal;
    }

    public void setSwapMemTotal(String swapMemTotal) {
        this.swapMemTotal = swapMemTotal == null ? null : swapMemTotal.trim();
    }

    public String getSwapUseMemTotal() {
        return swapUseMemTotal;
    }

    public void setSwapUseMemTotal(String swapUseMemTotal) {
        this.swapUseMemTotal = swapUseMemTotal == null ? null : swapUseMemTotal.trim();
    }

    public String getSwapFreeMemTotal() {
        return swapFreeMemTotal;
    }

    public void setSwapFreeMemTotal(String swapFreeMemTotal) {
        this.swapFreeMemTotal = swapFreeMemTotal == null ? null : swapFreeMemTotal.trim();
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType == null ? null : serviceType.trim();
    }

    public String getMemUsedRate() {
        return memUsedRate;
    }

    public void setMemUsedRate(String memUsedRate) {
        this.memUsedRate = memUsedRate == null ? null : memUsedRate.trim();
    }

    public String getCharBak2() {
        return charBak2;
    }

    public void setCharBak2(String charBak2) {
        this.charBak2 = charBak2 == null ? null : charBak2.trim();
    }

    public String getCharBak3() {
        return charBak3;
    }

    public void setCharBak3(String charBak3) {
        this.charBak3 = charBak3 == null ? null : charBak3.trim();
    }

    public String getCharBak4() {
        return charBak4;
    }

    public void setCharBak4(String charBak4) {
        this.charBak4 = charBak4 == null ? null : charBak4.trim();
    }

    public String getCharBak5() {
        return charBak5;
    }

    public void setCharBak5(String charBak5) {
        this.charBak5 = charBak5 == null ? null : charBak5.trim();
    }

    public Short getNumBak1() {
        return numBak1;
    }

    public void setNumBak1(Short numBak1) {
        this.numBak1 = numBak1;
    }

    public Short getNumBak2() {
        return numBak2;
    }

    public void setNumBak2(Short numBak2) {
        this.numBak2 = numBak2;
    }

    public Short getNumBak3() {
        return numBak3;
    }

    public void setNumBak3(Short numBak3) {
        this.numBak3 = numBak3;
    }

    public Short getNumBak4() {
        return numBak4;
    }

    public void setNumBak4(Short numBak4) {
        this.numBak4 = numBak4;
    }

    public Short getNumBak5() {
        return numBak5;
    }

    public void setNumBak5(Short numBak5) {
        this.numBak5 = numBak5;
    }
}
