package com.pactera.monitoring.service;

import com.jcraft.jsch.JSchException;
import com.pactera.monitoring.entity.MonHardwareDiskInfoDtl;
import com.pactera.monitoring.entity.MonHardwareDiskInfoTol;
import com.pactera.monitoring.entity.dto.MonHardwareDiskInfoDtlDto;
import com.pactera.monitoring.entity.dto.MonHardwareDiskInfoTolDto;
import com.pactera.monitoring.exception.BussinessException;

import java.util.List;

/**硬盘信息
 * @author 84483
 */
public interface MonHardwareDiskInfoService {
    /**
     * 查询硬盘汇总信息
     *
     * @param serverIp 服务器ip
     * @return 返回硬盘汇总
     */
    MonHardwareDiskInfoTolDto queryDiskInfoTol(String serverIp) throws BussinessException, JSchException;

    /**
     * 访问linux服务器，查询硬盘汇总信息
     *
     * @param serviceUser     用户名
     * @param servicePassword 密码
     * @param ip              ip
     * @param port            端口
     * @return 硬盘汇总信息
     * @throws JSchException 连接服务器失败
     */
    MonHardwareDiskInfoTol queryDiskInfoTolFromRemote(String serviceUser,
                                                      String servicePassword,
                                                      String ip,
                                                      int port) throws JSchException;

    /**
     * 查询硬盘明细信息
     *
     * @param serverIp 服务器ip
     * @return 返回硬盘明细
     */
    List<MonHardwareDiskInfoDtlDto> queryDiskInfoDtl(String serverIp) throws BussinessException, JSchException;

    /**
     * 访问linux服务器，查询硬盘明细信息
     *
     * @param serviceUser     用户名
     * @param servicePassword 密码
     * @param ip              ip
     * @param port            端口
     * @return 硬盘明细信息
     * @throws JSchException 连接服务器失败
     */
    List<MonHardwareDiskInfoDtl> queryDiskInfoDtlFromRemote(String serviceUser,
                                                    String servicePassword,
                                                    String ip,
                                                    int port) throws JSchException;

}
