package com.pactera.monitoring.utils.ssh;

import com.jcraft.jsch.JSchException;
import com.pactera.monitoring.entity.MonHardwareCpuInfoTol;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**从服务器查询cpu汇总信息
 * @author 84483
 */
public class CpuTolInformationFromServer extends AbstractAssessRemoteServer<MonHardwareCpuInfoTol> {
    //查询cpu核心数
    private String  cpuCoreNumCommand = "grep 'cpu cores' /proc/cpuinfo|uniq|awk -F '[: ]' '{print $NF}'";
    //cpu型号
    private String cpuModel = "grep 'model name' /proc/cpuinfo|awk -F ':' '{if(NR==1) print $NF}'";
    //查询物理cpu数目
    private String cpuNumCommand = "grep 'physical id' /proc/cpuinfo|sort -u|wc -l";
    //查询cpu总线程
    private String cpuTotalThreadsNumCommand = "grep 'processor' /proc/cpuinfo | sort -u | wc -l";
    //服务名
    private String serverHostNameCommand = "hostname";

    public CpuTolInformationFromServer(String user, String passwd, String host, int port) {
        super(user, passwd, host, port);
    }

    public CpuTolInformationFromServer(String user, String passwd, String host) {
        super(user, passwd, host);
    }


    /**
     * @param date 数据日期
     * @return 实体对象
     */
    @Override
    public MonHardwareCpuInfoTol getObject(Date date) throws JSchException, IOException {
        jSchUtil.connect();
        String[] cpuUsageCommandArray = {cpuCoreNumCommand,
                cpuModel, cpuNumCommand, cpuTotalThreadsNumCommand, serverHostNameCommand};
        Map<String, List<String>> cpuUsageCollect = jSchUtil.execCmdMultipleCommands(cpuUsageCommandArray);
        return cpuTolInfoUsageAnalyis(cpuUsageCollect, jSchUtil.getHost(), date);
    }

    /**
     * cpu汇总集格式化
     *
     * @param cpuUsageCollect 查询结果集
     * @return cpu汇总信息
     */
    public MonHardwareCpuInfoTol cpuTolInfoUsageAnalyis(Map<String, List<String>> cpuUsageCollect,
                                                        String ip,
                                                        Date date) {
        MonHardwareCpuInfoTol monHardwareCpuInfoTol = new MonHardwareCpuInfoTol();
        if (cpuUsageCollect == null || cpuUsageCollect.size() <= 0) {
            return monHardwareCpuInfoTol;
        }
        date = date == null ? new Date() : date;
        String cpuModelInfo = cpuUsageCollect.get(cpuModel) != null
                && cpuUsageCollect.get(cpuModel).size() > 0
                ? cpuUsageCollect.get(cpuModel).get(0) : "";
        String cpuNumCommandRes = cpuUsageCollect.get(cpuNumCommand) != null
                && cpuUsageCollect.get(cpuNumCommand).size() > 0
                ? cpuUsageCollect.get(cpuNumCommand).get(0) : "";
        String cpuTotalThreadsNumCommandRes = cpuUsageCollect.get(cpuTotalThreadsNumCommand) != null
                && cpuUsageCollect.get(cpuTotalThreadsNumCommand).size() > 0
                ? cpuUsageCollect.get(cpuTotalThreadsNumCommand).get(0) : "";
        String serverName = cpuUsageCollect.get(serverHostNameCommand) != null
                && cpuUsageCollect.get(serverHostNameCommand).size() > 0
                ? cpuUsageCollect.get(serverHostNameCommand).get(0) : "";
        monHardwareCpuInfoTol.setCpuNum(cpuNumCommandRes);
        monHardwareCpuInfoTol.setCpuType(cpuModelInfo);
        monHardwareCpuInfoTol.setCpuThread(cpuTotalThreadsNumCommandRes);
        monHardwareCpuInfoTol.setDataDt(date);
        monHardwareCpuInfoTol.setServiceNm(serverName);
        monHardwareCpuInfoTol.setServiceIp(ip);
        return monHardwareCpuInfoTol;
    }
}
