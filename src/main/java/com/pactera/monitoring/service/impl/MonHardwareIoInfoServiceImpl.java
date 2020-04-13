package com.pactera.monitoring.service.impl;

import com.github.pagehelper.PageInfo;
import com.jcraft.jsch.JSchException;
import com.pactera.monitoring.core.page.PageInfoSetVal;
import com.pactera.monitoring.dao.ds1.MonHardwareIoInfoDao;
import com.pactera.monitoring.entity.MonHardwareIoInfo;
import com.pactera.monitoring.entity.MonHardwareServerInfo;
import com.pactera.monitoring.entity.SearchBaseEntity;
import com.pactera.monitoring.entity.dto.MonHardwareIoInfoDto;
import com.pactera.monitoring.exception.BussinessException;
import com.pactera.monitoring.service.MonHardwareIoInfoService;
import com.pactera.monitoring.service.MonHardwareServerInfoService;
import com.pactera.monitoring.utils.bean.BaseConverter;
import com.pactera.monitoring.utils.bean.BeanUtils;
import com.pactera.monitoring.utils.ssh.RemoteComputerMonitorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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
     * 根据ip，查询服务器IO信息
     *
     * @param ip ip地址
     * @return io信息明细
     */
    @Override
    public MonHardwareIoInfoDto queryIoInfo(String ip) throws BussinessException, JSchException {
        MonHardwareServerInfo monHardwareServerInfo = monHardwareServerInfoService.queryServerInfoFromDb(ip);
        String servicePassword = monHardwareServerInfo.getServicePassword();
        String servicePort = monHardwareServerInfo.getServicePort();
        String serviceUser = monHardwareServerInfo.getServiceUser();
        int port = Integer.parseInt(servicePort);
        MonHardwareIoInfo monHardwareIoInfo = queryIoInfoFromRemote(serviceUser, servicePassword, ip, port);
        if (monHardwareIoInfo == null) {
            return new MonHardwareIoInfoDto();
        }
        MonHardwareIoInfoDto monHardwareIoInfoDto = new MonHardwareIoInfoDto();
        BeanUtils.copyProperties(monHardwareIoInfo, monHardwareIoInfoDto);
        return monHardwareIoInfoDto;
    }

    /**
     * 连接Linux服务器，查询服务器IO信息
     *
     * @param serviceUser     用户名
     * @param servicePassword 密码
     * @param ip              ip地址
     * @param port            端口号
     * @return io信息明细
     */
    @Override
    public MonHardwareIoInfo queryIoInfoFromRemote(String serviceUser, String servicePassword, String ip, int port) throws JSchException {
        RemoteComputerMonitorUtil remoteComputerMonitorUtil
                = new RemoteComputerMonitorUtil(serviceUser, servicePassword, ip, port);
        MonHardwareIoInfo ioUsage;
        try {
            ioUsage = remoteComputerMonitorUtil.getIoUsage();
        } finally {
            remoteComputerMonitorUtil.close();
        }
        return ioUsage;
    }

    /**
     * IO信息保存
     *
     * @param monHardwareServerInfo 需要连接的服务器对象
     * @return 数据变更条数
     * @throws JSchException 连接失败
     */
    @Override
    public int saveIoInfo(MonHardwareServerInfo monHardwareServerInfo) throws JSchException {
        String servicePassword = monHardwareServerInfo.getServicePassword();
        String servicePort = monHardwareServerInfo.getServicePort();
        String serviceUser = monHardwareServerInfo.getServiceUser();
        String serverIp = monHardwareServerInfo.getServiceIp();
        String serviceType = monHardwareServerInfo.getServiceType();
        int port = Integer.parseInt(servicePort);
        Date date = new Date();
        MonHardwareIoInfo monHardwareIoInfo = queryIoInfoFromRemote(serviceUser, servicePassword, serverIp, port);
        monHardwareIoInfo.setDataDt(date);
        monHardwareIoInfo.setRecordDt(date);
        monHardwareIoInfo.setServiceType(serviceType);
        return monHardwareIoInfoDao.insertSelective(monHardwareIoInfo);
    }

    //do to dto
    class DoToDTOConverter extends BaseConverter<MonHardwareIoInfo, MonHardwareIoInfoDto> {
        @Override
        protected void convert(MonHardwareIoInfo from, MonHardwareIoInfoDto to) {
            super.convert(from, to);
        }
    }
    /**
     * 根据条件从数据库查询IO信息
     *
     * @param searchBaseEntity 搜索实体类
     * @return 符合条件的dto
     */
    @Override
    public PageInfo<MonHardwareIoInfoDto> queryIoInfoFromDbByCondition(SearchBaseEntity searchBaseEntity) {
        List<MonHardwareIoInfo> monHardwareIoInfoList = monHardwareIoInfoDao.selectByCondition(searchBaseEntity);
        if (monHardwareIoInfoList.size() == 0) {
            return new PageInfo<>();
        }
        PageInfo<MonHardwareIoInfo> oldPage = new PageInfo<>(monHardwareIoInfoList);
        DoToDTOConverter dtoConverter = new DoToDTOConverter();
        List<MonHardwareIoInfoDto> monHardwareIoInfoDtoList;
        monHardwareIoInfoDtoList = dtoConverter.convert(monHardwareIoInfoList, MonHardwareIoInfoDto.class);
        PageInfo<MonHardwareIoInfoDto> page = new PageInfo<>(monHardwareIoInfoDtoList);
        PageInfoSetVal.setVal(page, oldPage);
        return page;
    }
}
