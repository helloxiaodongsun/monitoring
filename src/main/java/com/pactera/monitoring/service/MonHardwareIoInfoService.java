package com.pactera.monitoring.service;

import com.github.pagehelper.PageInfo;
import com.jcraft.jsch.JSchException;
import com.pactera.monitoring.entity.MonHardwareIoInfo;
import com.pactera.monitoring.entity.MonHardwareServerInfo;
import com.pactera.monitoring.entity.SearchBaseEntity;
import com.pactera.monitoring.entity.dto.MonHardwareIoInfoDto;
import com.pactera.monitoring.exception.BussinessException;

import java.util.Date;
import java.util.Map;

/**
 * io信息查询
 * @author 84483
 */
public interface MonHardwareIoInfoService {
    /**
     * 根据ip，查询服务器IO信息
     * @param ip ip地址
     * @return io信息明细
     */
    MonHardwareIoInfoDto queryIoInfo(String ip) throws BussinessException, JSchException;

    /**
     * 连接Linux服务器，查询服务器IO信息
     * @param serviceUser 用户名
     * @param servicePassword 密码
     * @param ip ip地址
     * @param port 端口号
     * @return io信息明细
     */
    MonHardwareIoInfo queryIoInfoFromRemote(String serviceUser, String servicePassword, String ip, int port) throws JSchException;

    /**
     * IO信息保存
     * @param monHardwareServerInfo  需要连接的服务器对象
     * @return  数据变更条数
     * @throws JSchException  连接失败
     */
    int saveIoInfo(MonHardwareServerInfo monHardwareServerInfo, Date date) throws JSchException;

    /**
     * 根据条件从数据库查询IO信息
     * @param searchBaseEntity 搜索实体类
     * @return 符合条件的dto
     */
    PageInfo<MonHardwareIoInfoDto> queryIoInfoFromDbByCondition(SearchBaseEntity searchBaseEntity);

    /**
     * 生成io图表所需数据
     * @param searchBaseEntity
     * @return
     */
    Map<Object,Object> ioChartData(SearchBaseEntity searchBaseEntity);
}
