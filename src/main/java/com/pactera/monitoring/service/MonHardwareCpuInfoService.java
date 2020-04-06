package com.pactera.monitoring.service;

import com.jcraft.jsch.JSchException;
import com.pactera.monitoring.entity.MonHardwareCpuInfoDtl;
import com.pactera.monitoring.entity.MonHardwareCpuInfoTol;
import com.pactera.monitoring.entity.dto.MonHardwareCpuInfoDtlDto;
import com.pactera.monitoring.entity.dto.MonHardwareCpuInfoTolDto;
import com.pactera.monitoring.exception.BussinessException;

/**
 * cpu信息service
 * @author 84483
 */
public interface MonHardwareCpuInfoService {

    /**
     * 查询cpu汇总信息
     *
     * @param serverIp 服务器ip
     * @return 返回cpu汇总
     */
    MonHardwareCpuInfoTolDto queryCpuInfoTol(String serverIp) throws BussinessException, JSchException;

    /**
     * 访问linux服务器，查询cpu汇总信息
     *
     * @param serviceUser     用户名
     * @param servicePassword 密码
     * @param ip              ip
     * @param port            端口
     * @return cpu汇总信息
     * @throws JSchException 连接服务器失败
     */
    MonHardwareCpuInfoTol queryCpuInfoTolFromRemote(String serviceUser,
                                                       String servicePassword,
                                                       String ip,
                                                       int port) throws JSchException;

    /**
     * 查询cpu明细信息
     *
     * @param serverIp 服务器ip
     * @return 返回cpu明细
     */
    MonHardwareCpuInfoDtlDto queryCpuInfoDtl(String serverIp) throws BussinessException, JSchException;

    /**
     * 访问linux服务器，查询cpu明细信息
     *
     * @param serviceUser     用户名
     * @param servicePassword 密码
     * @param ip              ip
     * @param port            端口
     * @return cpu明细信息
     * @throws JSchException 连接服务器失败
     */
    MonHardwareCpuInfoDtl queryCpuInfoDtlFromRemote(String serviceUser,
                                                       String servicePassword,
                                                       String ip,
                                                       int port) throws JSchException;


}
