package com.pactera.monitoring.service;

import com.jcraft.jsch.JSchException;
import com.pactera.monitoring.entity.MonHardwareServerInfo;
import com.pactera.monitoring.entity.dto.MonHardwareServerInfoDto;
import com.pactera.monitoring.exception.BussinessException;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * 查询服务器信息
 * @author 84483
 */
public interface MonHardwareServerInfoService {
    /**
     * 根据ip,查询linux服务器信息
     *
     * @param ip 远程服务器ip
     * @return 服务器基本信息
     */
    MonHardwareServerInfoDto queryServerInfoByIp(String ip) throws BussinessException, JSchException, IOException;

    /**
     * 查询linux服务器信息
     *
     * @param serviceUser     用户名
     * @param servicePassword 密码
     * @param ip              ip
     * @param port            端口
     * @return 服务器信息
     * @throws JSchException      连接服务器失败
     */
    MonHardwareServerInfo queryServerInfoFromRemote(String serviceUser,
                                                    String servicePassword,
                                                    String ip,
                                                    int port,
                                                    Date date) throws JSchException, IOException;

    /**
     * 根据ip,从数据库中获得密码、端口
     *
     * @param ip ip地址
     * @return 服务器配置信息
     * @throws BussinessException 未在数据库中查询到该服务器的配置信息
     */
    MonHardwareServerInfo queryServerInfoFromDb(String ip) throws BussinessException;

    /**
     * 查询所有服务器列表,转换成DTO,返回前台
     * @return
     * @throws Exception
     */
    List<MonHardwareServerInfoDto> queryAllServerInfoToDto() throws Exception;


    /**
     * 查询所有服务器列表
     * @return
     * @throws Exception
     */
    List<MonHardwareServerInfo> queryAllServerInfo() throws Exception ;

    /**
     * 按条件查询服务器列表
     * @param monHardwareServerInfo
     * @return
     */
    List<MonHardwareServerInfo> queryListFromDbByCondition(MonHardwareServerInfo monHardwareServerInfo);

    /**
     * 增加服务器
     * @param monHardwareServerInfo
     * @param editFlag 修改标志
     */
    void addServer(MonHardwareServerInfo monHardwareServerInfo,boolean editFlag) throws BussinessException;

    /**
     * 删除服务器信息
     * @param id
     */
    void remove(String[] id);
}
