package com.pactera.monitoring.service;

import com.github.pagehelper.PageInfo;
import com.jcraft.jsch.JSchException;
import com.pactera.monitoring.entity.MonHardwareMemInfoDtl;
import com.pactera.monitoring.entity.MonHardwareServerInfo;
import com.pactera.monitoring.entity.SearchBaseEntity;
import com.pactera.monitoring.entity.dto.MonHardwareMemInfoDto;
import com.pactera.monitoring.exception.BussinessException;

import java.util.Date;
import java.util.List;

/**
 * @author 84483
 */
public interface MonHardwareMemInfoService {

    /**
     * 根据ip，查询服务器内存信息
     * @param ip ip地址
     * @return 服务器信息明细
     */
    MonHardwareMemInfoDto queryServerMemInfo(String ip) throws BussinessException, JSchException;

    /**
     * 查询服务器内存信息
     * @param serviceUser 用户名
     * @param servicePassword 密码
     * @param ip ip地址
     * @param port 端口号
     * @return 服务器信息
     */
    MonHardwareMemInfoDtl queryServerMemInfoFromRemote(String serviceUser, String servicePassword, String ip, int port) throws JSchException;


    /**
     * 服务器内存信息明细保存
     * @param monHardwareServerInfo  需要连接的服务器对象
     * @return  数据变更条数
     * @throws JSchException  连接失败
     */
    int saveServerMemInfoDtl(MonHardwareServerInfo monHardwareServerInfo, Date date) throws JSchException;

    /**
     * 根据条件从数据库查询内存信息
     * @param searchBaseEntity 搜索实体类
     * @return 符合条件的dto
     */
    PageInfo<MonHardwareMemInfoDto> queryServerMemInfoFromDbByCondition(SearchBaseEntity searchBaseEntity);
}
