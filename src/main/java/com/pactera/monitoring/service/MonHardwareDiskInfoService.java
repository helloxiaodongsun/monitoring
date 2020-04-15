package com.pactera.monitoring.service;

import com.github.pagehelper.PageInfo;
import com.jcraft.jsch.JSchException;
import com.pactera.monitoring.entity.MonHardwareDiskInfoDtl;
import com.pactera.monitoring.entity.MonHardwareDiskInfoTol;
import com.pactera.monitoring.entity.MonHardwareServerInfo;
import com.pactera.monitoring.entity.SearchBaseEntity;
import com.pactera.monitoring.entity.dto.MonHardwareDiskInfoDtlDto;
import com.pactera.monitoring.entity.dto.MonHardwareDiskInfoTolDto;
import com.pactera.monitoring.exception.BussinessException;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 硬盘信息
 *
 * @author 84483
 */
public interface MonHardwareDiskInfoService {
    /**
     * 查询硬盘汇总信息
     *
     * @param serverIp 服务器ip
     * @return 返回硬盘汇总
     */
    MonHardwareDiskInfoTolDto queryDiskInfoTol(String serverIp) throws BussinessException, JSchException, IOException;

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
                                                      int port,
                                                      Date date) throws JSchException, IOException;

    /**
     * 查询硬盘明细信息
     *
     * @param serverIp 服务器ip
     * @return 返回硬盘明细
     */
    List<MonHardwareDiskInfoDtlDto> queryDiskInfoDtl(String serverIp) throws BussinessException, JSchException, IOException;

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
                                                            int port,
                                                            Date date) throws JSchException, IOException;

    /**
     * 硬盘明细信息保存
     *
     * @param monHardwareServerInfo 需要连接的服务器对象
     * @return 数据变更条数
     * @throws JSchException 连接失败
     */
    int saveDiskInfoDtl(MonHardwareServerInfo monHardwareServerInfo, Date date) throws JSchException, IOException;

    /**
     * 硬盘汇总信息保存
     *
     * @param monHardwareServerInfo 需要连接的服务器对象
     * @return 数据变更条数
     * @throws JSchException 连接失败
     */
    int saveDiskInfoTol(MonHardwareServerInfo monHardwareServerInfo, Date date) throws JSchException, IOException;

    /**
     * 根据条件从数据库查询硬盘信息
     *
     * @param searchBaseEntity 搜索实体类
     * @return 符合条件的dto
     */
    PageInfo<MonHardwareDiskInfoDtlDto> queryDiskInfoDtlFromDbByCondition(SearchBaseEntity searchBaseEntity);

    /**
     * 生成硬盘图表所需数据
     *
     * @param searchBaseEntity
     * @return
     */
    Map<Object, Object> diskChartData(SearchBaseEntity searchBaseEntity);
}
