package com.pactera.monitoring.entity.dto;

import java.util.Date;

/**磁盘汇总dto
 * @author 84483
 */
public class MonHardwareDiskInfoTolDto {

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

    public String getDiskTotalSize() {
        return diskTotalSize;
    }

    public void setDiskTotalSize(String diskTotalSize) {
        this.diskTotalSize = diskTotalSize;
    }

    public String getDiskUsedSize() {
        return diskUsedSize;
    }

    public void setDiskUsedSize(String diskUsedSize) {
        this.diskUsedSize = diskUsedSize;
    }

    public String getDiskUsedRate() {
        return diskUsedRate;
    }

    public void setDiskUsedRate(String diskUsedRate) {
        this.diskUsedRate = diskUsedRate;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getDiskAvailSize() {
        return diskAvailSize;
    }

    public void setDiskAvailSize(String diskAvailSize) {
        this.diskAvailSize = diskAvailSize;
    }
}
