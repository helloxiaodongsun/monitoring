package com.pactera.monitoring.quartz.service.impl;

import com.jcraft.jsch.JSchException;
import com.pactera.monitoring.entity.MonHardwareServerInfo;
import com.pactera.monitoring.quartz.service.QuartzJobService;
import com.pactera.monitoring.service.MonHardwareCpuInfoService;
import com.pactera.monitoring.service.MonHardwareDiskInfoService;
import com.pactera.monitoring.service.MonHardwareIoInfoService;
import com.pactera.monitoring.service.MonHardwareMemInfoService;
import com.pactera.monitoring.service.MonHardwareServerInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author 84483
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class QuartzJobServiceImpl implements QuartzJobService {

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
        Date date = new Date();
        monHardwareServerInfos.forEach(monHardwareServerInfo -> {
            try {
                monHardwareMemInfoService.saveServerMemInfoDtl(monHardwareServerInfo,date);
            } catch (JSchException | IOException e) {
                log.error(e.getMessage(),e);
            }
            try {
                monHardwareIoInfoService.saveIoInfo(monHardwareServerInfo,date);
            } catch (JSchException | IOException e) {
                log.error(e.getMessage(),e);
            }
            try {
                monHardwareDiskInfoService.saveDiskInfoDtl(monHardwareServerInfo,date);
            } catch (JSchException | IOException e) {
                log.error(e.getMessage(),e);
            }
            try {
                monHardwareDiskInfoService.saveDiskInfoTol(monHardwareServerInfo,date);
            } catch (JSchException | IOException e) {
                log.error(e.getMessage(),e);
            }
            try {
                monHardwareCpuInfoService.saveCpuInfoDtl(monHardwareServerInfo,date);
            } catch (JSchException | IOException e) {
                log.error(e.getMessage(),e);
            }
            try {
                monHardwareCpuInfoService.saveCpuInfoTol(monHardwareServerInfo,date);
            } catch (JSchException | IOException e) {
                log.error(e.getMessage(),e);
            }

        });


    }
}
