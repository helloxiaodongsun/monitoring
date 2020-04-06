package com.pactera.monitoring.service.impl;

import com.jcraft.jsch.JSchException;
import com.pactera.monitoring.dao.ds1.MonHardwareDiskInfoDtlDao;
import com.pactera.monitoring.dao.ds1.MonHardwareDiskInfoTolDao;
import com.pactera.monitoring.entity.MonHardwareCpuInfoDtl;
import com.pactera.monitoring.entity.MonHardwareCpuInfoTol;
import com.pactera.monitoring.entity.MonHardwareDiskInfoDtl;
import com.pactera.monitoring.entity.MonHardwareDiskInfoTol;
import com.pactera.monitoring.entity.MonHardwareServerInfo;
import com.pactera.monitoring.entity.dto.MonHardwareCpuInfoDtlDto;
import com.pactera.monitoring.entity.dto.MonHardwareDiskInfoDtlDto;
import com.pactera.monitoring.entity.dto.MonHardwareDiskInfoTolDto;
import com.pactera.monitoring.entity.dto.MonHardwareIoInfoDto;
import com.pactera.monitoring.exception.BussinessException;
import com.pactera.monitoring.service.MonHardwareDiskInfoService;
import com.pactera.monitoring.service.MonHardwareServerInfoService;
import com.pactera.monitoring.utils.bean.BeanUtils;
import com.pactera.monitoring.utils.ssh.RemoteComputerMonitorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**硬盘信息
 * @author 84483
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MonHardwareDiskInfoServiceImpl implements MonHardwareDiskInfoService {
    @Autowired
    MonHardwareDiskInfoDtlDao monHardwareDiskinfoDtlDao;
    @Autowired
    MonHardwareDiskInfoTolDao monHardwareDiskinfoTolDao;
    @Autowired
    MonHardwareServerInfoService monHardwareServerInfoService;


    /**
     * 查询硬盘汇总信息
     *
     * @param serverIp 服务器ip
     * @return 返回硬盘汇总
     */
    @Override
    public MonHardwareDiskInfoTolDto queryDiskInfoTol(String serverIp) throws BussinessException, JSchException {
        MonHardwareServerInfo monHardwareServerInfo = monHardwareServerInfoService.queryServerInfoFromDb(serverIp);
        String servicePassword = monHardwareServerInfo.getServicePassword();
        String servicePort = monHardwareServerInfo.getServicePort();
        String serviceUser = monHardwareServerInfo.getServiceUser();
        int port = Integer.parseInt(servicePort);
        MonHardwareDiskInfoTol monHardwareDiskInfoTol = queryDiskInfoTolFromRemote(serviceUser, servicePassword, serverIp, port);
        MonHardwareDiskInfoTolDto monHardwareDiskInfoTolDto = new MonHardwareDiskInfoTolDto();
        BeanUtils.copyProperties(monHardwareDiskInfoTol,monHardwareDiskInfoTolDto);
        return monHardwareDiskInfoTolDto;
    }

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
    @Override
    public MonHardwareDiskInfoTol queryDiskInfoTolFromRemote(String serviceUser, String servicePassword, String ip, int port) throws JSchException {
        RemoteComputerMonitorUtil remoteComputerMonitorUtil
                = new RemoteComputerMonitorUtil(serviceUser, servicePassword, ip, port);
        MonHardwareDiskInfoTol monHardwareDiskInfoTol;
        try {
            monHardwareDiskInfoTol = remoteComputerMonitorUtil.getDiskUsageTol();
        }finally {
            remoteComputerMonitorUtil.close();
        }

        return  monHardwareDiskInfoTol;
    }

    /**
     * 查询硬盘明细信息
     *
     * @param serverIp 服务器ip
     * @return 返回硬盘明细
     */
    @Override
    public List<MonHardwareDiskInfoDtlDto> queryDiskInfoDtl(String serverIp) throws BussinessException, JSchException {
        MonHardwareServerInfo monHardwareServerInfo = monHardwareServerInfoService.queryServerInfoFromDb(serverIp);
        String servicePassword = monHardwareServerInfo.getServicePassword();
        String servicePort = monHardwareServerInfo.getServicePort();
        String serviceUser = monHardwareServerInfo.getServiceUser();
        int port = Integer.parseInt(servicePort);
        List<MonHardwareDiskInfoDtl> monHardwareDiskInfoDtlList = queryDiskInfoDtlFromRemote(serviceUser, servicePassword, serverIp, port);
        return monHardwareDiskInfoDtlList.stream().map(item -> {
            MonHardwareDiskInfoDtlDto monHardwareDiskInfoDtlDto = new MonHardwareDiskInfoDtlDto();
            BeanUtils.copyProperties(item, monHardwareDiskInfoDtlDto);
            return monHardwareDiskInfoDtlDto;
        }).collect(Collectors.toList());
    }

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
    @Override
    public List<MonHardwareDiskInfoDtl> queryDiskInfoDtlFromRemote(String serviceUser, String servicePassword, String ip, int port) throws JSchException {
        RemoteComputerMonitorUtil remoteComputerMonitorUtil
                = new RemoteComputerMonitorUtil(serviceUser, servicePassword, ip, port);
        List<MonHardwareDiskInfoDtl> monHardwareDiskInfoDtlList;
        try {
            monHardwareDiskInfoDtlList = remoteComputerMonitorUtil.getDiskUsageDtl();
        }finally {
            remoteComputerMonitorUtil.close();
        }

        return  monHardwareDiskInfoDtlList;
    }
}
