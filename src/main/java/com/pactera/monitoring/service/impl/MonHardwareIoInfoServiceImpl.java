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

import java.text.DecimalFormat;
import java.util.List;

/**
 * 查询io信息
 *
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
    public MonHardwareIoInfoDto queryIoInfoByIp(String ip) throws BussinessException, JSchException {
        MonHardwareServerInfo monHardwareServerInfo = monHardwareServerInfoService.queryServerInfoFromDb(ip);
        String servicePassword = monHardwareServerInfo.getServicePassword();
        String servicePort = monHardwareServerInfo.getServicePort();
        String serviceUser = monHardwareServerInfo.getServiceUser();
        int port = Integer.parseInt(servicePort);
        List<MonHardwareIoInfo> monHardwareIoInfo = queryIoInfo(serviceUser, servicePassword, ip, port);
        if (monHardwareIoInfo == null || monHardwareIoInfo.size() <= 0) {
            return new MonHardwareIoInfoDto();
        }
        MonHardwareIoInfoDto monHardwareIoInfoDto = new MonHardwareIoInfoDto();
        BeanUtils.copyProperties(monHardwareIoInfo.get(0), monHardwareIoInfoDto);
        double readKbPerSecondSum = 0.0;
        double writeKbPerSecondSum = 0.0;
        double diskTranSecondSum;
        double awaitSum = 0.0;
        DecimalFormat df = new DecimalFormat("0.000");
        for (MonHardwareIoInfo hardwareIoInfo : monHardwareIoInfo) {
            readKbPerSecondSum += Double.parseDouble(hardwareIoInfo.getDiskRead());
            writeKbPerSecondSum +=Double.parseDouble(hardwareIoInfo.getDiskWrite());
            awaitSum +=Double.parseDouble(hardwareIoInfo.getDiskAvgRespond());
        }
        double awaitSumAvg = awaitSum / monHardwareIoInfo.size();
        diskTranSecondSum=readKbPerSecondSum+writeKbPerSecondSum;
        monHardwareIoInfoDto.setDiskAvgRespond(df.format(awaitSumAvg));
        monHardwareIoInfoDto.setDiskTrans(df.format(diskTranSecondSum));
        monHardwareIoInfoDto.setDiskRead(df.format(readKbPerSecondSum));
        monHardwareIoInfoDto.setDiskWrite(df.format(writeKbPerSecondSum));
        return monHardwareIoInfoDto;
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
        } finally {
            remoteComputerMonitorUtil.close();
        }
        return ioUsage;
    }
}
