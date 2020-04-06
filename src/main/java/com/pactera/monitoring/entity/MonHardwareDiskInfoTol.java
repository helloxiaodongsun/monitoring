package com.pactera.monitoring.entity;

import java.util.Date;

/**
 * 磁盘基础信息表-汇总
 */
public class MonHardwareDiskInfoTol {

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
     * 磁盘总大小
     */
    private String diskTotalSize;


    /**
     * 磁盘使用大小
     */
    private String diskUsedSize;


    /**
     * 磁盘使用率
     */
    private String diskUsedRate;


    /**
     * 服务器类型1数据库服务器2ETL服务器3应用服务器
     */
    private String serviceType;


    /**
     * 磁盘剩余容量
     */
    private String diskAvailSize;


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

    public String getDiskTotalSize() {
        return diskTotalSize;
    }

    public void setDiskTotalSize(String diskTotalSize) {
        this.diskTotalSize = diskTotalSize == null ? null : diskTotalSize.trim();
    }

    public String getDiskUsedSize() {
        return diskUsedSize;
    }

    public void setDiskUsedSize(String diskUsedSize) {
        this.diskUsedSize = diskUsedSize == null ? null : diskUsedSize.trim();
    }

    public String getDiskUsedRate() {
        return diskUsedRate;
    }

    public void setDiskUsedRate(String diskUsedRate) {
        this.diskUsedRate = diskUsedRate == null ? null : diskUsedRate.trim();
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType == null ? null : serviceType.trim();
    }

    public String getDiskAvailSize() {
        return diskAvailSize;
    }

    public void setDiskAvailSize(String diskAvailSize) {
        this.diskAvailSize = diskAvailSize == null ? null : diskAvailSize.trim();
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
