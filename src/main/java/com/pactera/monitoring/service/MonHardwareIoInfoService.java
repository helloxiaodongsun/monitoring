package com.pactera.monitoring.service;

import com.jcraft.jsch.JSchException;
import com.pactera.monitoring.entity.MonHardwareIoInfo;
import com.pactera.monitoring.entity.dto.MonHardwareIoInfoDto;
import com.pactera.monitoring.exception.BussinessException;

import java.util.List;

/**
 * io信息查询
 * @author 84483
 */
public interface MonHardwareIoInfoService {
    /**
     * 根据ip，查询服务器内存信息
     * @param ip ip地址
     * @return io信息明细
     */
    MonHardwareIoInfoDto queryIoInfoByIp(String ip) throws BussinessException, JSchException;

    /**
     * 查询服务器内存信息
     * @param serviceUser 用户名
     * @param servicePassword 密码
     * @param ip ip地址
     * @param port 端口号
     * @return io信息明细
     */
    List<MonHardwareIoInfo> queryIoInfo(String serviceUser, String servicePassword, String ip, int port) throws JSchException;

}
