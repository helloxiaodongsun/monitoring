package com.pactera.monitoring.entity.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * io信息dto
 * @author 84483
 */
public class MonHardwareIoInfoDto implements Serializable {
    private static final long serialVersionUID = -1994828326534062870L;
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
    //private String diskNm;

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

    /*public String getDiskNm() {
        return diskNm;
    }

    public void setDiskNm(String diskNm) {
        this.diskNm = diskNm;
    }
*/
    public String getDiskTrans() {
        return diskTrans;
    }

    public void setDiskTrans(String diskTrans) {
        this.diskTrans = diskTrans;
    }

    public String getDiskRead() {
        return diskRead;
    }

    public void setDiskRead(String diskRead) {
        this.diskRead = diskRead;
    }

    public String getDiskWrite() {
        return diskWrite;
    }

    public void setDiskWrite(String diskWrite) {
        this.diskWrite = diskWrite;
    }

    public String getDiskUseRate() {
        return diskUseRate;
    }

    public void setDiskUseRate(String diskUseRate) {
        this.diskUseRate = diskUseRate;
    }

    public String getDiskAvgRespond() {
        return diskAvgRespond;
    }

    public void setDiskAvgRespond(String diskAvgRespond) {
        this.diskAvgRespond = diskAvgRespond;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }
}
