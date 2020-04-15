package com.pactera.monitoring.service;

import com.github.pagehelper.PageInfo;
import com.jcraft.jsch.JSchException;
import com.pactera.monitoring.entity.MonHardwareCpuInfoDtl;
import com.pactera.monitoring.entity.MonHardwareCpuInfoTol;
import com.pactera.monitoring.entity.MonHardwareServerInfo;
import com.pactera.monitoring.entity.SearchBaseEntity;
import com.pactera.monitoring.entity.dto.MonHardwareCpuInfoDtlDto;
import com.pactera.monitoring.entity.dto.MonHardwareCpuInfoTolDto;
import com.pactera.monitoring.entity.dto.MonHardwareMemInfoDto;
import com.pactera.monitoring.exception.BussinessException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    MonHardwareCpuInfoTolDto queryCpuInfoTol(String serverIp) throws BussinessException, JSchException, IOException;

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
                                                       int port,
                                                        Date date) throws JSchException, IOException;

    /**
     * 查询cpu明细信息
     *
     * @param serverIp 服务器ip
     * @return 返回cpu明细
     */
    MonHardwareCpuInfoDtlDto queryCpuInfoDtl(String serverIp) throws BussinessException, JSchException, IOException;

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
                                                       int port,
                                                        Date date) throws JSchException, IOException;

    /**
     * 从远程服务器查询cpu明细信息保存数据库
     * @param monHardwareServerInfo 需要连接的服务器对象
     * @throws JSchException 连接失败
     */
    int saveCpuInfoDtl(MonHardwareServerInfo monHardwareServerInfo, Date date) throws JSchException, IOException;


    /**
     * 从远程服务器查询cpu汇总信息保存数据库
     * @param monHardwareServerInfo 需要连接的服务器对象
     * @throws JSchException 连接失败
     */
    int saveCpuInfoTol(MonHardwareServerInfo monHardwareServerInfo,Date date) throws JSchException, IOException;

    /**
     * 根据条件从数据库查询cpu明细信息
     * @param searchBaseEntity 搜索实体类
     * @return 符合条件的dto
     */
    PageInfo<MonHardwareCpuInfoDtlDto> queryCpuInfoDtlFromDbByCondition(SearchBaseEntity searchBaseEntity);

    /**
     * 创建cpu图表所需数据
     * @param searchBaseEntity
     * @return
     */
    List<ArrayList<Object>> cpuChartData(SearchBaseEntity searchBaseEntity);
}
