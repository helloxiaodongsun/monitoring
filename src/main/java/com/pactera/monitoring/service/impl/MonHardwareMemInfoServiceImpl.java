package com.pactera.monitoring.service.impl;

import com.jcraft.jsch.JSchException;
import com.pactera.monitoring.dao.ds1.MonHardwareMemInfoDtlDao;
import com.pactera.monitoring.dao.ds1.MonHardwareMemInfoTolDao;
import com.pactera.monitoring.entity.MonHardwareMemInfoDtl;
import com.pactera.monitoring.entity.MonHardwareServerInfo;
import com.pactera.monitoring.entity.dto.MonHardwareMemInfoDto;
import com.pactera.monitoring.exception.BussinessException;
import com.pactera.monitoring.service.MonHardwareMemInfoService;
import com.pactera.monitoring.service.MonHardwareServerInfoService;
import com.pactera.monitoring.utils.bean.BeanUtils;
import com.pactera.monitoring.utils.ssh.RemoteComputerMonitorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**服务器内存插叙
 * @author 84483
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MonHardwareMemInfoServiceImpl implements MonHardwareMemInfoService {
    @Autowired
    MonHardwareMemInfoDtlDao monHardwareMeminfoDtlDao;
    @Autowired
    MonHardwareMemInfoTolDao monHardwareMeminfoTolDao;
    @Autowired
    MonHardwareServerInfoService monHardwareServerInfoService;

    /**
     * 根据ip，查询服务器内存信息
     *
     * @param ip ip地址
     * @return 服务器信息明细
     */
    @Override
    public MonHardwareMemInfoDto queryServerMemInfoByIp(String ip) throws BussinessException, JSchException {
        MonHardwareServerInfo monHardwareServerInfo = monHardwareServerInfoService.queryServerInfoFromDb(ip);
        String servicePassword = monHardwareServerInfo.getServicePassword();
        String servicePort = monHardwareServerInfo.getServicePort();
        String serviceUser = monHardwareServerInfo.getServiceUser();
        int port = Integer.parseInt(servicePort);
        MonHardwareMemInfoDtl monHardwareMemInfoDtl = queryServerMemInfo(serviceUser, servicePassword, ip, port);
        MonHardwareMemInfoDto monHardwareMemInfoDto = new MonHardwareMemInfoDto();
        BeanUtils.copyProperties(monHardwareMemInfoDtl,monHardwareMemInfoDto);
        return monHardwareMemInfoDto;
    }

    /**
     * 查询服务器内存信息
     *
     * @param serviceUser     用户名
     * @param servicePassword 密码
     * @param ip              ip地址
     * @param port            端口号
     * @return 服务器信息
     */
    @Override
    public MonHardwareMemInfoDtl queryServerMemInfo(String serviceUser, String servicePassword, String ip, int port) throws JSchException {

        RemoteComputerMonitorUtil remoteComputerMonitorUtil
                = new RemoteComputerMonitorUtil(serviceUser, servicePassword, ip, port);
        MonHardwareMemInfoDtl memUsage;
        try {
            memUsage = remoteComputerMonitorUtil.getMemUsage();
        }finally {
            remoteComputerMonitorUtil.close();
        }
        return memUsage;
    }
}
