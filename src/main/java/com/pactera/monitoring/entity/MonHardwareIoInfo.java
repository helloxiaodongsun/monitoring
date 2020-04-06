package com.pactera.monitoring.entity;

import java.util.Date;

/**
 * I/O基础信息表
 * @author 84483
 */
public class MonHardwareIoInfo {
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
     * 磁盘名称
     */
    private String diskNm;

    /**
     * 磁盘每秒传输速度
     */
    private String diskTrans;

    /**
     * 磁盘每秒读取数
     */
    private String diskRead;

    /**
     * 磁盘每秒写入数
     */
    private String diskWrite;

    /**
     * 磁盘使用率
     */
    private String diskUseRate;

    /**
     * 磁盘平均响应时间
     */
    private String diskAvgRespond;

    /**
     * 服务器类型1数据库服务器2ETL服务器3应用服务器
     */
    private String serviceType;

    /**
     * 字符备用字段1
     */
    private String charBak1;

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

    public String getDiskNm() {
        return diskNm;
    }

    public void setDiskNm(String diskNm) {
        this.diskNm = diskNm == null ? null : diskNm.trim();
    }

    public String getDiskTrans() {
        return diskTrans;
    }

    public void setDiskTrans(String diskTrans) {
        this.diskTrans = diskTrans == null ? null : diskTrans.trim();
    }

    public String getDiskRead() {
        return diskRead;
    }

    public void setDiskRead(String diskRead) {
        this.diskRead = diskRead == null ? null : diskRead.trim();
    }

    public String getDiskWrite() {
        return diskWrite;
    }

    public void setDiskWrite(String diskWrite) {
        this.diskWrite = diskWrite == null ? null : diskWrite.trim();
    }

    public String getDiskUseRate() {
        return diskUseRate;
    }

    public void setDiskUseRate(String diskUseRate) {
        this.diskUseRate = diskUseRate == null ? null : diskUseRate.trim();
    }

    public String getDiskAvgRespond() {
        return diskAvgRespond;
    }

    public void setDiskAvgRespond(String diskAvgRespond) {
        this.diskAvgRespond = diskAvgRespond == null ? null : diskAvgRespond.trim();
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType == null ? null : serviceType.trim();
    }

    public String getCharBak1() {
        return charBak1;
    }

    public void setCharBak1(String charBak1) {
        this.charBak1 = charBak1 == null ? null : charBak1.trim();
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
