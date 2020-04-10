package com.pactera.monitoring.quartz.service.impl;

import com.jcraft.jsch.JSchException;
import com.pactera.monitoring.entity.MonHardwareServerInfo;
import com.pactera.monitoring.quartz.QuartzJob;
import com.pactera.monitoring.quartz.service.QuartzJobService;
import com.pactera.monitoring.service.MonHardwareCpuInfoService;
import com.pactera.monitoring.service.MonHardwareDiskInfoService;
import com.pactera.monitoring.service.MonHardwareIoInfoService;
import com.pactera.monitoring.service.MonHardwareMemInfoService;
import com.pactera.monitoring.service.MonHardwareServerInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 8
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class QuartzJobServiceImpl implements QuartzJobService {

    public static final Logger log = LoggerFactory.getLogger(QuartzJobService.class);

    @Autowired
    MonHardwareServerInfoService monHardwareServerInfoService;
    @Autowired
    MonHardwareMemInfoService monHardwareMemInfoService;
    @Autowired
    MonHardwareIoInfoService monHardwareIoInfoService;
    @Autowired
    MonHardwareDiskInfoService monHardwareDiskInfoService;
    @Autowired
    MonHardwareCpuInfoService monHardwareCpuInfoService;

    /**
     * 访问远程主机获取信息，并保存
     */
    @Override
    public void getInformationAndSave() throws Exception {

        List<MonHardwareServerInfo> monHardwareServerInfos = monHardwareServerInfoService.queryAllServerInfo();
        if (monHardwareServerInfos == null || monHardwareServerInfos.size() <= 0) {
            return;
        }
        monHardwareServerInfos.forEach(monHardwareServerInfo -> {
            try {
                monHardwareMemInfoService.saveServerMemInfoDtl(monHardwareServerInfo);
            } catch (JSchException e) {
                log.error(e.getMessage(),e);
            }
            try {
                monHardwareIoInfoService.saveIoInfo(monHardwareServerInfo);
            } catch (JSchException e) {
                log.error(e.getMessage(),e);
            }
            try {
                monHardwareDiskInfoService.saveDiskInfoDtl(monHardwareServerInfo);
            } catch (JSchException e) {
                log.error(e.getMessage(),e);
            }
            try {
                monHardwareDiskInfoService.saveDiskInfoTol(monHardwareServerInfo);
            } catch (JSchException e) {
                log.error(e.getMessage(),e);
            }
            try {
                monHardwareCpuInfoService.saveCpuInfoDtl(monHardwareServerInfo);
            } catch (JSchException e) {
                log.error(e.getMessage(),e);
            }
            try {
                monHardwareCpuInfoService.saveCpuInfoTol(monHardwareServerInfo);
            } catch (JSchException e) {
                log.error(e.getMessage(),e);
            }

        });


    }
}
