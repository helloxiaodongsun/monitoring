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
import com.pactera.monitoring.utils.DateUtils;
import com.pactera.monitoring.utils.bean.BaseConverter;
import com.pactera.monitoring.utils.bean.BeanUtils;
import com.pactera.monitoring.utils.ssh.MemoDtlInformationFromServer;
import com.pactera.monitoring.utils.ssh.QueryContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

/**
 * 服务器内存插叙
 *
 * @author 84483
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
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
    public MonHardwareMemInfoDto queryServerMemInfo(String ip) throws BussinessException, JSchException, IOException {
        MonHardwareServerInfo monHardwareServerInfo = monHardwareServerInfoService.queryServerInfoFromDb(ip);
        String servicePassword = monHardwareServerInfo.getServicePassword();
        String servicePort = monHardwareServerInfo.getServicePort();
        String serviceUser = monHardwareServerInfo.getServiceUser();
        int port = Integer.parseInt(servicePort);
        MonHardwareMemInfoDtl monHardwareMemInfoDtl = queryServerMemInfoFromRemote(serviceUser, servicePassword, ip,
                port,new Date());
        MonHardwareMemInfoDto monHardwareMemInfoDto = new MonHardwareMemInfoDto();
        BeanUtils.copyProperties(monHardwareMemInfoDtl, monHardwareMemInfoDto);
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
    public MonHardwareMemInfoDtl queryServerMemInfoFromRemote(String serviceUser,
                                                              String servicePassword,
                                                              String ip,
                                                              int port,
                                                              Date date) throws JSchException, IOException {
        QueryContext<MonHardwareMemInfoDtl> monHardwareMemInfoDtlQueryContext =
                new QueryContext<>(new MemoDtlInformationFromServer(serviceUser, servicePassword, ip, port));
        MonHardwareMemInfoDtl memUsage;
        try {
            memUsage = monHardwareMemInfoDtlQueryContext.getObject(date);
        } finally {
            monHardwareMemInfoDtlQueryContext.close();
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
    public int saveServerMemInfoDtl(MonHardwareServerInfo monHardwareServerInfo, Date date) throws JSchException, IOException {
        String servicePassword = monHardwareServerInfo.getServicePassword();
        String servicePort = monHardwareServerInfo.getServicePort();
        String serviceUser = monHardwareServerInfo.getServiceUser();
        String serverIp = monHardwareServerInfo.getServiceIp();
        String serviceType = monHardwareServerInfo.getServiceType();
        int port = Integer.parseInt(servicePort);
        date = date == null ? new Date() : date;
        MonHardwareMemInfoDtl monHardwareMemInfoDtl
                = queryServerMemInfoFromRemote(serviceUser, servicePassword, serverIp, port,date);
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
        if (monHardwareMemInfoDtls.size() == 0) {
            return new PageInfo<>();
        }
        PageInfo<MonHardwareMemInfoDtl> oldPage = new PageInfo<>(monHardwareMemInfoDtls);
        DoToDTOConverter dtoConverter = new DoToDTOConverter();
        List<MonHardwareMemInfoDto> monHardwareMemInfoDtos;
        monHardwareMemInfoDtos = dtoConverter.convert(monHardwareMemInfoDtls, MonHardwareMemInfoDto.class);
        PageInfo<MonHardwareMemInfoDto> page = new PageInfo<MonHardwareMemInfoDto>(monHardwareMemInfoDtos);
        PageInfoSetVal.setVal(page, oldPage);
        return page;
    }

    /**
     * 生成内存图表所需数据
     *
     * @param searchBaseEntity
     * @return
     */
    @Override
    public Map<Object, Object> memChartData(SearchBaseEntity searchBaseEntity) {
        Map<Object,Object> result = new HashMap<>();

        List<Object> xAxisData = new ArrayList<>();
        result.put("xAxisData",xAxisData);

        Map<Object,Object> seriesData = new HashMap<>();
        result.put("seriesData",seriesData);

        List<Object> memUseTotal = new ArrayList<>();
        List<Object> freeMemTotal = new ArrayList<>();
        List<Object> bufferCacheUseMemTotal = new ArrayList<>();
        seriesData.put("memUseTotal",memUseTotal);
        seriesData.put("freeMemTotal",freeMemTotal);
        seriesData.put("bufferCacheUseMemTotal",bufferCacheUseMemTotal);

        List<MonHardwareMemInfoDtl> monHardwareMemInfoDtls = monHardwareMeminfoDtlDao.memChartData(searchBaseEntity);
        if(monHardwareMemInfoDtls.size()!=0){
            for(MonHardwareMemInfoDtl m:monHardwareMemInfoDtls){
                xAxisData.add(DateUtils.parseDataDt(m.getDataDt()));
                memUseTotal.add(m.getMemUseTotal());
                freeMemTotal.add(m.getFreeMemTotal());
                bufferCacheUseMemTotal.add(m.getBufferCacheUseMemTotal());
            }
        }
        return result;
    }
}
