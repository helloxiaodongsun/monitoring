package com.pactera.monitoring.service.impl;

import com.jcraft.jsch.JSchException;
import com.pactera.monitoring.dao.ds1.MonHardwareCpuInfoDtlDao;
import com.pactera.monitoring.dao.ds1.MonHardwareCpuInfoTolDao;
import com.pactera.monitoring.entity.MonHardwareCpuInfoDtl;
import com.pactera.monitoring.entity.MonHardwareCpuInfoTol;
import com.pactera.monitoring.entity.MonHardwareServerInfo;
import com.pactera.monitoring.entity.dto.MonHardwareCpuInfoDtlDto;
import com.pactera.monitoring.entity.dto.MonHardwareCpuInfoTolDto;
import com.pactera.monitoring.exception.BussinessException;
import com.pactera.monitoring.service.MonHardwareCpuInfoService;
import com.pactera.monitoring.service.MonHardwareServerInfoService;
import com.pactera.monitoring.utils.bean.BeanUtils;
import com.pactera.monitoring.utils.ssh.RemoteComputerMonitorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**cpu信息查询
 * @author 84483
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MonHardwareCpuInfoServiceImpl implements MonHardwareCpuInfoService {
    @Autowired
    MonHardwareCpuInfoDtlDao monHardwareCpuinfoDtlMapper;
    @Autowired
    MonHardwareCpuInfoTolDao monHardwareCpuinfoTolDao;
    @Autowired
    MonHardwareServerInfoService monHardwareServerInfoService;

    /**
     * 查询cpu汇总信息
     *
     * @param serverIp 服务器ip
     * @return 返回cpu汇总
     */
    @Override
    public MonHardwareCpuInfoTolDto queryCpuInfoTol(String serverIp) throws BussinessException, JSchException {
        MonHardwareServerInfo monHardwareServerInfo = monHardwareServerInfoService.queryServerInfoFromDb(serverIp);
        String servicePassword = monHardwareServerInfo.getServicePassword();
        String servicePort = monHardwareServerInfo.getServicePort();
        String serviceUser = monHardwareServerInfo.getServiceUser();
        int port = Integer.parseInt(servicePort);
        MonHardwareCpuInfoTol monHardwareCpuInfoTol = queryCpuInfoTolFromRemote(serviceUser, servicePassword, serverIp, port);
        MonHardwareCpuInfoTolDto monHardwareCpuInfoTolDto = new MonHardwareCpuInfoTolDto();
        BeanUtils.copyProperties(monHardwareCpuInfoTol,monHardwareCpuInfoTolDto);
        return monHardwareCpuInfoTolDto;
    }

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
    @Override
    public MonHardwareCpuInfoTol queryCpuInfoTolFromRemote(String serviceUser, String servicePassword, String ip, int port) throws JSchException {
        RemoteComputerMonitorUtil remoteComputerMonitorUtil
                = new RemoteComputerMonitorUtil(serviceUser, servicePassword, ip, port);
        MonHardwareCpuInfoTol cpuUsageTol;
        try {
             cpuUsageTol = remoteComputerMonitorUtil.getCpuUsageTol();
        }finally {
            remoteComputerMonitorUtil.close();
        }

        return  cpuUsageTol;
    }

    /**
     * 查询cpu明细信息
     *
     * @param serverIp 服务器ip
     * @return 返回cpu明细
     */
    @Override
    public MonHardwareCpuInfoDtlDto queryCpuInfoDtl(String serverIp) throws BussinessException, JSchException {
        MonHardwareServerInfo monHardwareServerInfo = monHardwareServerInfoService.queryServerInfoFromDb(serverIp);
        String servicePassword = monHardwareServerInfo.getServicePassword();
        String servicePort = monHardwareServerInfo.getServicePort();
        String serviceUser = monHardwareServerInfo.getServiceUser();
        int port = Integer.parseInt(servicePort);
        MonHardwareCpuInfoDtl monHardwareCpuInfoDtl = queryCpuInfoDtlFromRemote(serviceUser, servicePassword, serverIp, port);
        MonHardwareCpuInfoDtlDto monHardwareCpuInfoDtlDto = new MonHardwareCpuInfoDtlDto();
        BeanUtils.copyProperties(monHardwareCpuInfoDtl,monHardwareCpuInfoDtlDto);
        return monHardwareCpuInfoDtlDto;
    }

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
    @Override
    public MonHardwareCpuInfoDtl queryCpuInfoDtlFromRemote(String serviceUser, String servicePassword, String ip, int port) throws JSchException {
        RemoteComputerMonitorUtil remoteComputerMonitorUtil
                = new RemoteComputerMonitorUtil(serviceUser, servicePassword, ip, port);
        MonHardwareCpuInfoDtl cpuUsageDtl ;
        try {
            cpuUsageDtl  = remoteComputerMonitorUtil.getCpuUsageDtl();
        }finally {
            remoteComputerMonitorUtil.close();
        }
        return cpuUsageDtl;
    }
}
