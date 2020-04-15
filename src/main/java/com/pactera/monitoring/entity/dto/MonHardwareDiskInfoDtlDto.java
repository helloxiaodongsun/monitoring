package com.pactera.monitoring.entity.dto;

import java.util.Date;

/**
 * 磁盘明细dto
 *
 * @author 84483
 */
public class MonHardwareDiskInfoDtlDto {
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
     * 文件系统名称
     */
    private String filesystemNm;

    /**
     * 所在磁盘大小
     */
    private String diskTotalSize;

    /**
     * 所在磁盘使用大小
     */
    private String diskUsedSize;

    /**
     * 所在磁盘使用率
     */
    private String diskUsedRate;

    /**
     * 磁盘剩余容量
     */
    private String diskAvailSize;

    /**
     * 文件系统挂载点
     */
    private String mountedOn;

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

    public String getFilesystemNm() {
        return filesystemNm;
    }

    public void setFilesystemNm(String filesystemNm) {
        this.filesystemNm = filesystemNm;
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

    public String getDiskAvailSize() {
        return diskAvailSize;
    }

    public void setDiskAvailSize(String diskAvailSize) {
        this.diskAvailSize = diskAvailSize;
    }

    public String getMountedOn() {
        return mountedOn;
    }

    public void setMountedOn(String mountedOn) {
        this.mountedOn = mountedOn;
    }
}
