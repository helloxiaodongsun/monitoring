package com.pactera.monitoring.service.impl;

import com.github.pagehelper.PageInfo;
import com.jcraft.jsch.JSchException;
import com.pactera.monitoring.core.page.PageInfoSetVal;
import com.pactera.monitoring.dao.ds1.MonHardwareMemInfoDtlDao;
import com.pactera.monitoring.dao.ds1.MonHardwareMemInfoTolDao;
import com.pactera.monitoring.entity.MonHardwareMemInfoDtl;
import com.pactera.monitoring.entity.MonHardwareServerInfo;
import com.pactera.monitoring.entity.SearchBaseEntity;
import com.pactera.monitoring.entity.dto.MonHardwareMemInfoDto;
import com.pactera.monitoring.exception.BussinessException;
import com.pactera.monitoring.service.MonHardwareMemInfoService;
import com.pactera.monitoring.service.MonHardwareServerInfoService;
import com.pactera.monitoring.utils.bean.BaseConverter;
import com.pactera.monitoring.utils.bean.BeanUtils;
import com.pactera.monitoring.utils.ssh.RemoteComputerMonitorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

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
    public MonHardwareMemInfoDto queryServerMemInfo(String ip) throws BussinessException, JSchException {
        MonHardwareServerInfo monHardwareServerInfo = monHardwareServerInfoService.queryServerInfoFromDb(ip);
        String servicePassword = monHardwareServerInfo.getServicePassword();
        String servicePort = monHardwareServerInfo.getServicePort();
        String serviceUser = monHardwareServerInfo.getServiceUser();
        int port = Integer.parseInt(servicePort);
        MonHardwareMemInfoDtl monHardwareMemInfoDtl = queryServerMemInfoFromRemote(serviceUser, servicePassword, ip, port);
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
    public MonHardwareMemInfoDtl queryServerMemInfoFromRemote(String serviceUser, String servicePassword, String ip, int port) throws JSchException {

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

    /**
     * 服务器内存信息明细保存
     *
     * @param monHardwareServerInfo 需要连接的服务器对象
     * @return 数据变更条数
     * @throws JSchException 连接失败
     */
    @Override
    public int saveServerMemInfoDtl(MonHardwareServerInfo monHardwareServerInfo) throws JSchException {
        String servicePassword = monHardwareServerInfo.getServicePassword();
        String servicePort = monHardwareServerInfo.getServicePort();
        String serviceUser = monHardwareServerInfo.getServiceUser();
        String serverIp = monHardwareServerInfo.getServiceIp();
        String serviceType = monHardwareServerInfo.getServiceType();
        int port = Integer.parseInt(servicePort);
        Date date = new Date();
        MonHardwareMemInfoDtl monHardwareMemInfoDtl
                = queryServerMemInfoFromRemote(serviceUser, servicePassword, serverIp, port);
        monHardwareMemInfoDtl.setDataDt(date);
        monHardwareMemInfoDtl.setRecordDt(date);
        monHardwareMemInfoDtl.setServiceType(serviceType);
       return monHardwareMeminfoDtlDao.insertSelective(monHardwareMemInfoDtl);
    }

    //do to dto
    class DoToDTOConverter extends BaseConverter<MonHardwareMemInfoDtl, MonHardwareMemInfoDto> {
        @Override
        protected void convert(MonHardwareMemInfoDtl from, MonHardwareMemInfoDto to) {
            super.convert(from, to);
        }
    }

    /**
     * 根据条件从数据库查询内存信息
     *
     * @param searchBaseEntity 搜索实体类
     * @return 符合条件的dto
     */
    @Override
   public PageInfo<MonHardwareMemInfoDto> queryServerMemInfoFromDbByCondition(SearchBaseEntity searchBaseEntity) {
        List<MonHardwareMemInfoDtl> monHardwareMemInfoDtls = monHardwareMeminfoDtlDao.selectByCondition(searchBaseEntity);
        if(monHardwareMemInfoDtls.size()==0){
            return new PageInfo<>();
        }
        PageInfo<MonHardwareMemInfoDtl> oldPage = new PageInfo<>(monHardwareMemInfoDtls);
        DoToDTOConverter dtoConverter = new DoToDTOConverter();
        List<MonHardwareMemInfoDto> monHardwareMemInfoDtos;
        monHardwareMemInfoDtos= dtoConverter.convert(monHardwareMemInfoDtls, MonHardwareMemInfoDto.class);
        PageInfo<MonHardwareMemInfoDto> page =new PageInfo<MonHardwareMemInfoDto>(monHardwareMemInfoDtos);
        PageInfoSetVal.setVal(page,oldPage);
        return  page;
    }
}
