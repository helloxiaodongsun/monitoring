package com.pactera.monitoring.service;

import com.jcraft.jsch.JSchException;
import com.pactera.monitoring.entity.MonHardwareMemInfoDtl;
import com.pactera.monitoring.entity.dto.MonHardwareMemInfoDto;
import com.pactera.monitoring.exception.BussinessException;

/**
 * @author 84483
 */
public interface MonHardwareMemInfoService {

    /**
     * 根据ip，查询服务器内存信息
     * @param ip ip地址
     * @return 服务器信息明细
     */
    MonHardwareMemInfoDto queryServerMemInfoByIp(String ip) throws BussinessException, JSchException;

    /**
     * 查询服务器内存信息
     * @param serviceUser 用户名
     * @param servicePassword 密码
     * @param ip ip地址
     * @param port 端口号
     * @return 服务器信息
     */
    MonHardwareMemInfoDtl queryServerMemInfo(String serviceUser, String servicePassword, String ip, int port) throws JSchException;
}
