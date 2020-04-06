package com.pactera.monitoring.service.impl;

import com.jcraft.jsch.JSchException;
import com.pactera.monitoring.dao.ds1.MonHardwareIoInfoDao;
import com.pactera.monitoring.entity.MonHardwareIoInfo;
import com.pactera.monitoring.entity.MonHardwareServerInfo;
import com.pactera.monitoring.entity.dto.MonHardwareIoInfoDto;
import com.pactera.monitoring.exception.BussinessException;
import com.pactera.monitoring.service.MonHardwareIoInfoService;
import com.pactera.monitoring.service.MonHardwareServerInfoService;
import com.pactera.monitoring.utils.bean.BeanUtils;
import com.pactera.monitoring.utils.ssh.RemoteComputerMonitorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 查询io信息
 * @author 84483
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MonHardwareIoInfoServiceImpl implements MonHardwareIoInfoService {
    @Autowired
    MonHardwareIoInfoDao monHardwareIoInfoDao;
    @Autowired
    MonHardwareServerInfoService monHardwareServerInfoService;

    /**
     * 根据ip，查询服务器内存信息
     *
     * @param ip ip地址
     * @return io信息明细
     */
    @Override
    public List<MonHardwareIoInfoDto> queryIoInfoByIp(String ip) throws BussinessException, JSchException {
        MonHardwareServerInfo monHardwareServerInfo = monHardwareServerInfoService.queryServerInfoFromDb(ip);
        String servicePassword = monHardwareServerInfo.getServicePassword();
        String servicePort = monHardwareServerInfo.getServicePort();
        String serviceUser = monHardwareServerInfo.getServiceUser();
        int port = Integer.parseInt(servicePort);
        List<MonHardwareIoInfo> monHardwareIoInfo = queryIoInfo(serviceUser, servicePassword, ip, port);
        return monHardwareIoInfo.stream().map(item->{
            MonHardwareIoInfoDto monHardwareIoInfoDto = new MonHardwareIoInfoDto();
            BeanUtils.copyProperties(item,monHardwareIoInfoDto);
            return monHardwareIoInfoDto;
        }).collect(Collectors.toList());
    }

    /**
     * 查询服务器内存信息
     *
     * @param serviceUser     用户名
     * @param servicePassword 密码
     * @param ip              ip地址
     * @param port            端口号
     * @return io信息明细
     */
    @Override
    public List<MonHardwareIoInfo> queryIoInfo(String serviceUser, String servicePassword, String ip, int port) throws JSchException {
        RemoteComputerMonitorUtil remoteComputerMonitorUtil
                = new RemoteComputerMonitorUtil(serviceUser, servicePassword, ip, port);
        List<MonHardwareIoInfo> ioUsage;
        try {
             ioUsage = remoteComputerMonitorUtil.getIoUsage();
        }finally {
            remoteComputerMonitorUtil.close();
        }
        return  ioUsage;
    }
}
