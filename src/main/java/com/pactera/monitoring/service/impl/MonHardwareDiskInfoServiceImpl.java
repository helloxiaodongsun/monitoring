package com.pactera.monitoring.service.impl;

import com.github.pagehelper.PageInfo;
import com.jcraft.jsch.JSchException;
import com.pactera.monitoring.core.page.PageInfoSetVal;
import com.pactera.monitoring.dao.ds1.MonHardwareDiskInfoDtlDao;
import com.pactera.monitoring.dao.ds1.MonHardwareDiskInfoTolDao;
import com.pactera.monitoring.entity.MonHardwareDiskInfoDtl;
import com.pactera.monitoring.entity.MonHardwareDiskInfoTol;
import com.pactera.monitoring.entity.MonHardwareServerInfo;
import com.pactera.monitoring.entity.SearchBaseEntity;
import com.pactera.monitoring.entity.dto.MonHardwareDiskInfoDtlDto;
import com.pactera.monitoring.entity.dto.MonHardwareDiskInfoTolDto;
import com.pactera.monitoring.exception.BussinessException;
import com.pactera.monitoring.service.MonHardwareDiskInfoService;
import com.pactera.monitoring.service.MonHardwareServerInfoService;
import com.pactera.monitoring.utils.DateUtils;
import com.pactera.monitoring.utils.bean.BaseConverter;
import com.pactera.monitoring.utils.bean.BeanUtils;
import com.pactera.monitoring.utils.ssh.DiskDtlInformationFromServer;
import com.pactera.monitoring.utils.ssh.DiskTolInformationFromServer;
import com.pactera.monitoring.utils.ssh.QueryContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 硬盘信息
 *
 * @author 84483
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
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
    public MonHardwareDiskInfoTolDto queryDiskInfoTol(String serverIp) throws BussinessException, JSchException, IOException {
        MonHardwareServerInfo monHardwareServerInfo = monHardwareServerInfoService.queryServerInfoFromDb(serverIp);
        String servicePassword = monHardwareServerInfo.getServicePassword();
        String servicePort = monHardwareServerInfo.getServicePort();
        String serviceUser = monHardwareServerInfo.getServiceUser();
        int port = Integer.parseInt(servicePort);
        MonHardwareDiskInfoTol monHardwareDiskInfoTol = queryDiskInfoTolFromRemote(serviceUser, servicePassword,
                serverIp, port,new Date());
        MonHardwareDiskInfoTolDto monHardwareDiskInfoTolDto = new MonHardwareDiskInfoTolDto();
        BeanUtils.copyProperties(monHardwareDiskInfoTol, monHardwareDiskInfoTolDto);
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
    public MonHardwareDiskInfoTol queryDiskInfoTolFromRemote(String serviceUser,
                                                             String servicePassword,
                                                             String ip,
                                                             int port,
                                                             Date date) throws JSchException ,IOException {
        QueryContext<MonHardwareDiskInfoTol> monHardwareDiskInfoTolQueryContext =
                new QueryContext<>(new DiskTolInformationFromServer(serviceUser, servicePassword, ip, port));
        MonHardwareDiskInfoTol monHardwareDiskInfoTol;
        try {
            monHardwareDiskInfoTol = monHardwareDiskInfoTolQueryContext.getObject(date);
        }finally {
            monHardwareDiskInfoTolQueryContext.close();
        }

        return monHardwareDiskInfoTol;
    }

    /**
     * 查询硬盘明细信息
     *
     * @param serverIp 服务器ip
     * @return 返回硬盘明细
     */
    @Override
    public List<MonHardwareDiskInfoDtlDto> queryDiskInfoDtl(String serverIp) throws BussinessException, JSchException, IOException {
        MonHardwareServerInfo monHardwareServerInfo = monHardwareServerInfoService.queryServerInfoFromDb(serverIp);
        String servicePassword = monHardwareServerInfo.getServicePassword();
        String servicePort = monHardwareServerInfo.getServicePort();
        String serviceUser = monHardwareServerInfo.getServiceUser();
        int port = Integer.parseInt(servicePort);
        List<MonHardwareDiskInfoDtl> monHardwareDiskInfoDtlList = queryDiskInfoDtlFromRemote(serviceUser,
                servicePassword, serverIp, port,new Date());
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
    public List<MonHardwareDiskInfoDtl> queryDiskInfoDtlFromRemote(String serviceUser,
                                                                   String servicePassword,
                                                                   String ip,
                                                                   int port,
                                                                   Date date) throws JSchException,IOException {
        QueryContext<MonHardwareDiskInfoDtl> monHardwareDiskInfoDtlQueryContext =
                new QueryContext<>(new DiskDtlInformationFromServer(serviceUser, servicePassword, ip, port));
        List<MonHardwareDiskInfoDtl> monHardwareDiskInfoDtlList;
        try {
            monHardwareDiskInfoDtlList = monHardwareDiskInfoDtlQueryContext.getCollection(date);
        } finally {
            monHardwareDiskInfoDtlQueryContext.close();
        }

        return monHardwareDiskInfoDtlList;
    }

    /**
     * 硬盘明细信息保存
     *
     * @param monHardwareServerInfo 需要连接的服务器对象
     * @return 数据变更条数
     * @throws JSchException 连接失败
     */
    @Override
    public int saveDiskInfoDtl(MonHardwareServerInfo monHardwareServerInfo, Date date) throws JSchException, IOException {
        String servicePassword = monHardwareServerInfo.getServicePassword();
        String servicePort = monHardwareServerInfo.getServicePort();
        String serviceUser = monHardwareServerInfo.getServiceUser();
        String serverIp = monHardwareServerInfo.getServiceIp();
        int port = Integer.parseInt(servicePort);
        date = date == null ? new Date() : date;
        List<MonHardwareDiskInfoDtl> monHardwareDiskInfoDtlList
                = queryDiskInfoDtlFromRemote(serviceUser, servicePassword, serverIp, port,date);
        for (MonHardwareDiskInfoDtl monHardwareDiskInfoDtl : monHardwareDiskInfoDtlList) {
            monHardwareDiskInfoDtl.setRecordDt(date);
        }
        return monHardwareDiskinfoDtlDao.insertSelectiveBatch(monHardwareDiskInfoDtlList);
    }

    /**
     * 硬盘汇总信息保存
     *
     * @param monHardwareServerInfo 需要连接的服务器对象
     * @return 数据变更条数
     * @throws JSchException 连接失败
     */
    @Override
    public int saveDiskInfoTol(MonHardwareServerInfo monHardwareServerInfo,Date date) throws JSchException, IOException {
        String servicePassword = monHardwareServerInfo.getServicePassword();
        String servicePort = monHardwareServerInfo.getServicePort();
        String serviceUser = monHardwareServerInfo.getServiceUser();
        String serverIp = monHardwareServerInfo.getServiceIp();
        int port = Integer.parseInt(servicePort);
        String serviceType = monHardwareServerInfo.getServiceType();
        date = date == null ? new Date() : date;
        MonHardwareDiskInfoTol monHardwareDiskInfoTol = queryDiskInfoTolFromRemote(serviceUser, servicePassword,
                serverIp, port,date);
        monHardwareDiskInfoTol.setRecordDt(date);
        monHardwareDiskInfoTol.setServiceType(serviceType);
        return monHardwareDiskinfoTolDao.insertSelective(monHardwareDiskInfoTol);
    }

    //do to dto
    static class DoToDTOConverter extends BaseConverter<MonHardwareDiskInfoDtl, MonHardwareDiskInfoDtlDto> {
        @Override
        protected void convert(MonHardwareDiskInfoDtl from, MonHardwareDiskInfoDtlDto to) {
            super.convert(from, to);
        }
    }

    /**
     * 根据条件从数据库查询硬盘信息
     *
     * @param searchBaseEntity 搜索实体类
     * @return 符合条件的dto
     */
    @Override
    public PageInfo<MonHardwareDiskInfoDtlDto> queryDiskInfoDtlFromDbByCondition(SearchBaseEntity searchBaseEntity) {
        List<MonHardwareDiskInfoDtl> monHardwareDiskInfoDtlList = monHardwareDiskinfoDtlDao.selectByCondition(searchBaseEntity);
        if (monHardwareDiskInfoDtlList.size() == 0) {
            return new PageInfo<>();
        }
        PageInfo<MonHardwareDiskInfoDtl> oldPage = new PageInfo<>(monHardwareDiskInfoDtlList);
        DoToDTOConverter dtoConverter = new DoToDTOConverter();
        List<MonHardwareDiskInfoDtlDto> monHardwareDiskInfoDtlDtoList;
        monHardwareDiskInfoDtlDtoList = dtoConverter.convert(monHardwareDiskInfoDtlList, MonHardwareDiskInfoDtlDto.class);
        PageInfo<MonHardwareDiskInfoDtlDto> page = new PageInfo<>(monHardwareDiskInfoDtlDtoList);
        PageInfoSetVal.setVal(page, oldPage);
        return page;
    }

    /**
     * 生成硬盘图表所需数据
     *
     * @param searchBaseEntity
     * @return
     */
    @Override
    public Map<Object, Object> diskChartData(SearchBaseEntity searchBaseEntity) {
        Map<Object,Object> result = new HashMap<>();

        List<String> legendData = new ArrayList<String>();

        List<Object> xAxisData = new ArrayList<>();


        Map<String,ArrayList> rawData = new HashMap<>();
        result.put("rawData",rawData);

        legendData = monHardwareDiskinfoDtlDao.getMountedOnData(searchBaseEntity);
        if(legendData.size()!=0){
            for(String le:legendData){
                rawData.put(le,new ArrayList<>());
            }
            List<MonHardwareDiskInfoDtl> monHardwareDiskInfoDtlList = monHardwareDiskinfoDtlDao.diskChartData(searchBaseEntity);
            if(monHardwareDiskInfoDtlList.size()!=0){
                for(MonHardwareDiskInfoDtl m:monHardwareDiskInfoDtlList){
                    xAxisData.add(DateUtils.parseDataDt(m.getDataDt()));
                    rawData.get(m.getMountedOn()).add(m.getDiskUsedSize());
                }
                xAxisData = xAxisData.stream().distinct().collect(Collectors.toList());
            }
        }


        result.put("xAxisData",xAxisData);
        result.put("legendData",legendData);
        return result;
    }
}
