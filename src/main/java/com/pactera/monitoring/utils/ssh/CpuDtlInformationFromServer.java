package com.pactera.monitoring.utils.ssh;

import com.jcraft.jsch.JSchException;
import com.pactera.monitoring.entity.MonHardwareCpuInfoDtl;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**cpu明细信息查询服务器
 * @author 84483
 */
public class CpuDtlInformationFromServer extends AbstractAssessRemoteServer<MonHardwareCpuInfoDtl>{
    String serverHostNameCommand = "hostname";
    String cpuInfoDtlCommand = "iostat -c 1 2|grep -v '^$'|awk 'NR==5  {print $1 \";\" $2 \";\" $3 \";\" $4 \";\" $6}'";
    public CpuDtlInformationFromServer(String user, String passwd, String host, int port) {
        super(user, passwd, host, port);
    }

    public CpuDtlInformationFromServer(String user, String passwd, String host) {
        super(user, passwd, host);
    }

    /**
     * @param date 数据日期
     * @return 实体对象
     */
    @Override
    public MonHardwareCpuInfoDtl getObject(Date date) throws JSchException, IOException {
        jSchUtil.connect();
        String[] cpuUsageCommandArray = {cpuInfoDtlCommand, serverHostNameCommand};
        Map<String, List<String>> cpuUsageCollect = jSchUtil.execCmdMultipleCommands(cpuUsageCommandArray);
        return  cpuInfoDtlAnalyis(cpuUsageCollect, jSchUtil.getHost(),date);
    }

    /**
     * cpu明细结果集分析
     * @param cpuUsageCollect 查询结果
     * @param ip ip地址
     * @param date 查询日期
     * @return cpu明细对象
     */
    public MonHardwareCpuInfoDtl cpuInfoDtlAnalyis(Map<String, List<String>> cpuUsageCollect,
                                                 String ip,
                                                 Date date) {
        MonHardwareCpuInfoDtl monHardwareCpuInfoDtl = new MonHardwareCpuInfoDtl();
        if (cpuUsageCollect == null || cpuUsageCollect.size() <= 0) {
            return monHardwareCpuInfoDtl;
        }
        date = date==null?new Date():date;

        List<String> cpuCommandRes = cpuUsageCollect.get(cpuInfoDtlCommand);
        List<String> serverNameList = cpuUsageCollect.get(serverHostNameCommand);
        String serverName = serverNameList.get(0);
        if (cpuCommandRes != null && cpuCommandRes.size() > 0) {
            String[] cpuInfo = cpuCommandRes.get(0).split(";");
            monHardwareCpuInfoDtl.setUsCpuRate(cpuInfo[0]);
            monHardwareCpuInfoDtl.setNiCpuRate(cpuInfo[1]);
            monHardwareCpuInfoDtl.setSyCpuRate(cpuInfo[2]);
            monHardwareCpuInfoDtl.setWaCpuRate(cpuInfo[3]);
            monHardwareCpuInfoDtl.setIdCpuRate(cpuInfo[4]);
            monHardwareCpuInfoDtl.setDataDt(date);
            monHardwareCpuInfoDtl.setServiceIp(ip);
            if (StringUtils.isNotEmpty(serverName)) {
                monHardwareCpuInfoDtl.setServiceNm(serverName);
            }
        }
        return monHardwareCpuInfoDtl;
    }
}
