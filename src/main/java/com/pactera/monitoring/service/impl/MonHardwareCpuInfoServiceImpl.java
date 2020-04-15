package com.pactera.monitoring.service.impl;

import com.github.pagehelper.PageInfo;
import com.jcraft.jsch.JSchException;
import com.pactera.monitoring.core.page.PageInfoSetVal;
import com.pactera.monitoring.dao.ds1.MonHardwareCpuInfoDtlDao;
import com.pactera.monitoring.dao.ds1.MonHardwareCpuInfoTolDao;
import com.pactera.monitoring.entity.MonHardwareCpuInfoDtl;
import com.pactera.monitoring.entity.MonHardwareCpuInfoTol;
import com.pactera.monitoring.entity.MonHardwareServerInfo;
import com.pactera.monitoring.entity.SearchBaseEntity;
import com.pactera.monitoring.entity.dto.MonHardwareCpuInfoDtlDto;
import com.pactera.monitoring.entity.dto.MonHardwareCpuInfoTolDto;
import com.pactera.monitoring.exception.BussinessException;
import com.pactera.monitoring.service.MonHardwareCpuInfoService;
import com.pactera.monitoring.service.MonHardwareServerInfoService;
import com.pactera.monitoring.utils.DateUtils;
import com.pactera.monitoring.utils.bean.BaseConverter;
import com.pactera.monitoring.utils.bean.BeanUtils;
import com.pactera.monitoring.utils.ssh.CpuDtlInformationFromServer;
import com.pactera.monitoring.utils.ssh.CpuTolInformationFromServer;
import com.pactera.monitoring.utils.ssh.QueryContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * cpu信息查询
 *
 * @author 84483
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class MonHardwareCpuInfoServiceImpl implements MonHardwareCpuInfoService {
    @Autowired
    MonHardwareCpuInfoDtlDao monHardwareCpuInfoDtlDao;
    @Autowired
    MonHardwareCpuInfoTolDao monHardwareCpuInfoTolDao;
    @Autowired
    MonHardwareServerInfoService monHardwareServerInfoService;

    /**
     * 查询cpu汇总信息
     *
     * @param serverIp 服务器ip
     * @return 返回cpu汇总
     */
    @Override
    public MonHardwareCpuInfoTolDto queryCpuInfoTol(String serverIp) throws BussinessException, JSchException, IOException {
        MonHardwareServerInfo monHardwareServerInfo = monHardwareServerInfoService.queryServerInfoFromDb(serverIp);
        String servicePassword = monHardwareServerInfo.getServicePassword();
        String servicePort = monHardwareServerInfo.getServicePort();
        String serviceUser = monHardwareServerInfo.getServiceUser();
        int port = Integer.parseInt(servicePort);
        MonHardwareCpuInfoTol monHardwareCpuInfoTol = queryCpuInfoTolFromRemote(serviceUser, servicePassword, serverIp, port,new Date());
        MonHardwareCpuInfoTolDto monHardwareCpuInfoTolDto = new MonHardwareCpuInfoTolDto();
        BeanUtils.copyProperties(monHardwareCpuInfoTol, monHardwareCpuInfoTolDto);
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
    public MonHardwareCpuInfoTol queryCpuInfoTolFromRemote(String serviceUser,
                                                           String servicePassword,
                                                           String ip,
                                                           int port,
                                                           Date date) throws JSchException, IOException {
        QueryContext<MonHardwareCpuInfoTol> monHardwareCpuInfoTolQueryContext =
                new QueryContext<>(new CpuTolInformationFromServer(serviceUser, servicePassword, ip, port));
        MonHardwareCpuInfoTol cpuUsageTol;
        try {
            cpuUsageTol = monHardwareCpuInfoTolQueryContext.getObject(date);
        } finally {
            monHardwareCpuInfoTolQueryContext.close();
        }

        return cpuUsageTol;
    }

    /**
     * 查询cpu明细信息
     *
     * @param serverIp 服务器ip
     * @return 返回cpu明细
     */
    @Override
    public MonHardwareCpuInfoDtlDto queryCpuInfoDtl(String serverIp) throws BussinessException, JSchException, IOException {
        MonHardwareServerInfo monHardwareServerInfo = monHardwareServerInfoService.queryServerInfoFromDb(serverIp);
        String servicePassword = monHardwareServerInfo.getServicePassword();
        String servicePort = monHardwareServerInfo.getServicePort();
        String serviceUser = monHardwareServerInfo.getServiceUser();
        int port = Integer.parseInt(servicePort);
        MonHardwareCpuInfoDtl monHardwareCpuInfoDtl = queryCpuInfoDtlFromRemote(serviceUser, servicePassword, serverIp, port,new Date());
        MonHardwareCpuInfoDtlDto monHardwareCpuInfoDtlDto = new MonHardwareCpuInfoDtlDto();
        BeanUtils.copyProperties(monHardwareCpuInfoDtl, monHardwareCpuInfoDtlDto);
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
    public MonHardwareCpuInfoDtl queryCpuInfoDtlFromRemote(String serviceUser,
                                                           String servicePassword,
                                                           String ip,
                                                           int port,
                                                           Date date) throws JSchException, IOException {
        QueryContext<MonHardwareCpuInfoDtl> monHardwareCpuInfoDtlQueryContext =
                new QueryContext<>(new CpuDtlInformationFromServer(serviceUser, servicePassword, ip, port));
        MonHardwareCpuInfoDtl cpuUsageDtl;
        try {
            cpuUsageDtl = monHardwareCpuInfoDtlQueryContext.getObject(date);
        } finally {
            monHardwareCpuInfoDtlQueryContext.close();
        }
        return cpuUsageDtl;
    }

    /**
     * 从远程服务器查询cpu明细信息保存数据库
     *
     * @param monHardwareServerInfo 需要连接的服务器对象
     * @throws JSchException 连接失败
     */
    @Override
    public int saveCpuInfoDtl(MonHardwareServerInfo monHardwareServerInfo, Date date) throws JSchException, IOException {
        String servicePassword = monHardwareServerInfo.getServicePassword();
        String servicePort = monHardwareServerInfo.getServicePort();
        String serviceUser = monHardwareServerInfo.getServiceUser();
        String serverIp = monHardwareServerInfo.getServiceIp();
        int port = Integer.parseInt(servicePort);
        String serviceType = monHardwareServerInfo.getServiceType();
        date = date == null ? new Date() : date;
        MonHardwareCpuInfoDtl monHardwareCpuInfoDtl
                = queryCpuInfoDtlFromRemote(serviceUser, servicePassword, serverIp, port,date);
        monHardwareCpuInfoDtl.setRecordDt(date);
        monHardwareCpuInfoDtl.setServiceType(serviceType);
        return monHardwareCpuInfoDtlDao.insertSelective(monHardwareCpuInfoDtl);
    }

    /**
     * 从远程服务器查询cpu汇总信息保存数据库
     *
     * @param monHardwareServerInfo 需要连接的服务器对象
     * @throws JSchException 连接失败
     */
    @Override
    public int saveCpuInfoTol(MonHardwareServerInfo monHardwareServerInfo, Date date) throws JSchException, IOException {
        String servicePassword = monHardwareServerInfo.getServicePassword();
        String servicePort = monHardwareServerInfo.getServicePort();
        String serviceUser = monHardwareServerInfo.getServiceUser();
        String serverIp = monHardwareServerInfo.getServiceIp();
        int port = Integer.parseInt(servicePort);
        String serviceType = monHardwareServerInfo.getServiceType();
        date = date == null ? new Date() : date;
        MonHardwareCpuInfoTol monHardwareCpuInfoTol
                = queryCpuInfoTolFromRemote(serviceUser, servicePassword, serverIp, port,date);
        monHardwareCpuInfoTol.setRecordDt(date);
        monHardwareCpuInfoTol.setServiceType(serviceType);
        return monHardwareCpuInfoTolDao.insertSelective(monHardwareCpuInfoTol);

    }

    //do to dto
    class DoToDTOConverter extends BaseConverter<MonHardwareCpuInfoDtl, MonHardwareCpuInfoDtlDto> {
        @Override
        protected void convert(MonHardwareCpuInfoDtl from, MonHardwareCpuInfoDtlDto to) {
            super.convert(from, to);
        }
    }

    /**
     * 根据条件从数据库查询cpu明细信息
     *
     * @param searchBaseEntity 搜索实体类
     * @return 符合条件的dto
     */
    @Override
    public PageInfo<MonHardwareCpuInfoDtlDto> queryCpuInfoDtlFromDbByCondition(SearchBaseEntity searchBaseEntity) {
        List<MonHardwareCpuInfoDtl> monHardwareCpuInfoDtls = monHardwareCpuInfoDtlDao.selectByCondition(searchBaseEntity);
        if (monHardwareCpuInfoDtls.size() == 0) {
            return new PageInfo<>();
        }
        PageInfo<MonHardwareCpuInfoDtl> oldPage = new PageInfo<>(monHardwareCpuInfoDtls);
        DoToDTOConverter dtoConverter = new DoToDTOConverter();
        List<MonHardwareCpuInfoDtlDto> monHardwareCpuInfoDtlDtos;
        monHardwareCpuInfoDtlDtos = dtoConverter.convert(monHardwareCpuInfoDtls, MonHardwareCpuInfoDtlDto.class);
        PageInfo<MonHardwareCpuInfoDtlDto> page = new PageInfo<>(monHardwareCpuInfoDtlDtos);
        PageInfoSetVal.setVal(page, oldPage);
        return page;
    }

    /**
     * 创建cpu图表所需数据
     *
     * @param searchBaseEntity
     * @return
     */
    @Override
    public List<ArrayList<Object>> cpuChartData(SearchBaseEntity searchBaseEntity) {
        List<ArrayList<Object>> result = new ArrayList<>();
        ArrayList<Object> arr1 = new ArrayList<>();
        ArrayList<Object> arr2 = new ArrayList<>();
        ArrayList<Object> arr3 = new ArrayList<>();
        ArrayList<Object> arr4 = new ArrayList<>();
        ArrayList<Object> arr5 = new ArrayList<>();
        ArrayList<Object> arr6 = new ArrayList<>();
        result.add(arr1);
        result.add(arr2);
        result.add(arr3);
        result.add(arr4);
        result.add(arr5);
        result.add(arr6);

        arr1.add("product");
        arr2.add("用户空间占用");
        arr3.add("内核空间占用");
        arr4.add("改变优先级进程占用");
        arr5.add("IO等待占用");
        arr6.add("空闲cpu占用");

        List<MonHardwareCpuInfoDtl> monHardwareCpuInfoDtls = monHardwareCpuInfoDtlDao.cpuChartData(searchBaseEntity);
        if(monHardwareCpuInfoDtls.size()!=0){
            for(MonHardwareCpuInfoDtl m:monHardwareCpuInfoDtls){
                arr1.add(DateUtils.parseDataDt(m.getDataDt()));
                arr2.add(m.getUsCpuRate());
                arr3.add(m.getSyCpuRate());
                arr4.add(m.getNiCpuRate());
                arr5.add(m.getWaCpuRate());
                arr6.add(m.getIdCpuRate());
            }
        }
        return result;
    }
}
