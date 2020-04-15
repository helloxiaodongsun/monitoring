package com.pactera.monitoring.service.impl;

import com.jcraft.jsch.JSchException;
import com.pactera.monitoring.dao.ds1.MonHardwareServerInfoDao;
import com.pactera.monitoring.entity.MonHardwareServerInfo;
import com.pactera.monitoring.entity.dto.MonHardwareServerInfoDto;
import com.pactera.monitoring.exception.BussinessException;
import com.pactera.monitoring.service.MonHardwareServerInfoService;
import com.pactera.monitoring.utils.bean.BaseConverter;
import com.pactera.monitoring.utils.bean.BeanUtils;
import com.pactera.monitoring.utils.ssh.QueryContext;
import com.pactera.monitoring.utils.ssh.ServerInformationFromServer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * 服务器基本信息查询
 *
 * @author 84483
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class MonHardwareServerInfoServiceImpl implements MonHardwareServerInfoService {

    @Resource
    MonHardwareServerInfoDao monHardwareServerInfoDao;

    /**
     * 根据ip,查询linux服务器信息
     *
     * @param ip 远程服务器ip
     * @return 服务器基本信息
     */
    @Override
    public MonHardwareServerInfoDto queryServerInfoByIp(String ip) throws BussinessException, JSchException, IOException {
        MonHardwareServerInfo monHardwareServerInfo = this.queryServerInfoFromDb(ip);
        String servicePassword = monHardwareServerInfo.getServicePassword();
        String servicePort = monHardwareServerInfo.getServicePort();
        String serviceUser = monHardwareServerInfo.getServiceUser();
        int port = Integer.parseInt(servicePort);
        MonHardwareServerInfo hardwareServerInfo = queryServerInfoFromRemote(serviceUser, servicePassword, ip, port,
                new Date());
        MonHardwareServerInfoDto monHardwareServerInfoDto = new MonHardwareServerInfoDto();
        BeanUtils.copyProperties(hardwareServerInfo, monHardwareServerInfoDto);
        monHardwareServerInfoDto.setServiceIp(ip);
        return monHardwareServerInfoDto;
    }

    /**
     * 查询linux服务器信息
     *
     * @param serviceUser     用户名
     * @param servicePassword 密码
     * @param ip              ip
     * @param port            端口
     * @return 服务器信息
     * @throws JSchException 连接服务器失败
     */
    @Override
    public MonHardwareServerInfo queryServerInfoFromRemote(String serviceUser,
                                                           String servicePassword,
                                                           String ip,
                                                           int port,
                                                           Date date) throws JSchException, IOException {
        QueryContext<MonHardwareServerInfo> monHardwareServerInfoQueryContext
                = new QueryContext<>(new ServerInformationFromServer(serviceUser, servicePassword, ip, port));
        MonHardwareServerInfo serverInfo;
        try {
            serverInfo = monHardwareServerInfoQueryContext.getObject(date);
        } finally {
            monHardwareServerInfoQueryContext.close();
        }
        return serverInfo;
    }


    /**
     * 根据ip,从数据库中获得密码、端口
     *
     * @param ip 远程服务器地址
     * @return 从数据库查询的服务器信息
     * @throws BussinessException ip为空、在数据库未获得该服务器信息、在数据库中获得的服务器信息不包含密码 时抛出此异常
     */
    @Override
    public MonHardwareServerInfo queryServerInfoFromDb(String ip) throws BussinessException {
        MonHardwareServerInfo byIp = monHardwareServerInfoDao.findByIp(ip);
        if (byIp == null
                || StringUtils.isEmpty(byIp.getServicePassword())
                || StringUtils.isEmpty(byIp.getServiceUser())) {
            throw new BussinessException("The IP is  configured incorrectly in the basic table");
        }
        return byIp;
    }

    //do to dto
    class DoToDTOConverter extends BaseConverter<MonHardwareServerInfo, MonHardwareServerInfoDto> {
        @Override
        protected void convert(MonHardwareServerInfo from, MonHardwareServerInfoDto to) {
            super.convert(from, to);
        }
    }

    @Override
    public List<MonHardwareServerInfo> queryAllServerInfo() throws Exception {
        return monHardwareServerInfoDao.findAll();
    }

    @Override
    public List<MonHardwareServerInfoDto> queryAllServerInfoToDto() throws Exception {
        List<MonHardwareServerInfo> monHardwareServerInfoList = queryAllServerInfo();
        List<MonHardwareServerInfoDto> monHardwareServerInfoDtoList;
        DoToDTOConverter doToDTOConverter = new DoToDTOConverter();
        monHardwareServerInfoDtoList = doToDTOConverter.convert(monHardwareServerInfoList, MonHardwareServerInfoDto.class);
        return monHardwareServerInfoDtoList;
    }


}
