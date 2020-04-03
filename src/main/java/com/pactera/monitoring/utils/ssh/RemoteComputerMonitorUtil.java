package com.pactera.monitoring.utils.ssh;

import com.jcraft.jsch.JSchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author 84483
 */
public class RemoteComputerMonitorUtil {


    private static Logger logger = LoggerFactory.getLogger(RemoteComputerMonitorUtil.class);
    private String networkAdapter;
    private String cpuCommand = "vmstat|awk 'NR==3''{print $13, $14, $16, $15}'";
    //查询物理cpu数目
    private String cpuNumCommand = "grep 'physical id' /proc/cpuinfo /proc/cpuinfo|sort|uniq|wc -l";
    //查询逻辑cpu数目
    private String cpuLogicNumCommand = "cat /proc/cpuinfo| grep \"processor\"| wc -l";
    //查询cpu核心数
    private String cpuCoreNumCommand = "grep 'cpu cores' /proc/cpuinfo|uniq|awk -F '[:\\ ]' '{print $NF}'";
    //查询cpu总线程
    private String cpuTotalThreadsNumCommand = "grep 'processor' /proc/cpuinfo | sort -u | wc -l";

    private String memCommand = "cat /proc/meminfo |grep 'MemTotal\\|MemFree'|awk '{print $2}'";
    //private String diskCommand = "df -h|grep -v Filesystem";
    private String diskCommand = "df -h --total|awk 'NR==13''{print $2,$3,$4,$5}'";
    private String networkCommand = "cat /proc/net/dev|grep networkAdapter|awk '{print $2, $10}'";
    private String serverInfoCommand = "lsb_release  -a|awk '$1 ~/Description/ {print $2, $3, $4}'";
    private String serverCoreCommand = "uname -r ";
    private String ioCommand = "iostat -dx 1 5|awk '!/^$|Device:|CPU\\}|Linux/ {print $1,$4,$5,$6,$7,$10,$NF}'";
    private String test = "vmstat 2 10";
    private JSchUtil jschUtil;


    private RemoteComputerMonitorUtil(String user, String passwd, String host, int port) {
        jschUtil = new JSchUtil(user, passwd, host, port);
    }

    private RemoteComputerMonitorUtil(String user, String passwd, String host) {
        jschUtil = new JSchUtil(user, passwd, host);
    }

    private RemoteComputerMonitorUtil(String user, String passwd, String host, int port, String networkAdapter) {
        jschUtil = new JSchUtil(user, passwd, host, port);
        this.networkAdapter = networkAdapter;
    }

    private RemoteComputerMonitorUtil(String user, String passwd, String host, String networkAdapter) {
        jschUtil = new JSchUtil(user, passwd, host);
        this.networkAdapter = networkAdapter;
    }


    /**
     * 根据获取网卡流量
     *
     * @return
     */
    public Map<String, String> getNetworkFlow() throws JSchException {
        Map<String, String> map = new HashMap<>(2);
        String command = networkCommand.replace("networkAdapter", networkAdapter);
        jschUtil.connect();
        List<String> result = jschUtil.execCmd(command);
        if (result != null && result.size() > 0) {
            String[] networkInfo = result.get(0).split(" ");
            long receive = Math.round(Long.parseLong(networkInfo[0]) / 1024);
            long send = Math.round(Long.parseLong(networkInfo[1]) / 1024);
            map.put("networkReceive", String.valueOf(receive));
            map.put("networkSend", String.valueOf(send));
        } else {
            map.put("networkReceive", "0");
            map.put("networkSend", "0");
        }

        return map;
    }

    public Map<String, String> getServerInfo() {
        Map<String, String> serverRes = new HashMap<>(3);
        try {
            jschUtil.connect();
            List<String> sercerCoreResult = jschUtil.execCmd(serverCoreCommand);
            List<String> serverInfoResult = jschUtil.execCmd(serverInfoCommand);
            if (sercerCoreResult != null && sercerCoreResult.size() > 0) {
                serverRes.put("serverCore", sercerCoreResult.get(0));
            }
            if (serverInfoResult != null && serverInfoResult.size() > 0) {
                serverRes.put("serverInfo", serverInfoResult.get(0));
            }
            serverRes.put("serverStatus", "1");
        } catch (JSchException e) {
            logger.error(e.getMessage(), e);
            serverRes.put("serverCore", "");
            serverRes.put("serverInfo", "");
            serverRes.put("serverStatus", "0");
        } finally {
            close();
        }

        return serverRes;
    }

    /**
     * 获取CPU使用情况
     *
     * @return
     */
    public Map<String, String> getCpuUsage() throws JSchException {
        Map<String, String> map = new HashMap<>(5);
        jschUtil.connect();
        String[] cpuUsageCommandArray = {cpuCommand, cpuCoreNumCommand,
                cpuLogicNumCommand, cpuNumCommand, cpuTotalThreadsNumCommand};
        Map<String, List<String>> cpuUsageCollect = jschUtil.execCmdMultipleCommands(cpuUsageCommandArray);
        return cpuUsageAnalyis(cpuUsageCollect);
    }

    /**
     * cpu结果集格式化
     * @param cpuUsageCollect
     * @return
     */
    public Map<String, String> cpuUsageAnalyis(Map<String, List<String>> cpuUsageCollect) {
        if (cpuUsageCollect == null || cpuUsageCollect.size() <= 0) {
            return new HashMap<>(0);
        }
        HashMap<String, String> resultAnalyis = new HashMap<>();
        List<String> cpuCommandRes = cpuUsageCollect.get(cpuCommand);
        if (cpuCommandRes != null && cpuCommandRes.size() > 0) {
            String[] cpuInfo = cpuCommandRes.get(0).split(" ");
            resultAnalyis.put("cpuUser", cpuInfo[0]);
            resultAnalyis.put("cpuSys", cpuInfo[1]);
            resultAnalyis.put("cpuWait", cpuInfo[2]);
            resultAnalyis.put("cpuIdle", cpuInfo[3]);
            double others = 100.00 - Double.parseDouble(cpuInfo[0]) - Double.parseDouble(cpuInfo[1]) -
                    Double.parseDouble(cpuInfo[2]) - Double.parseDouble(cpuInfo[3]);
            DecimalFormat df = new DecimalFormat("0.0");
            resultAnalyis.put("cpuOthers", df.format(others));
        } else {
            resultAnalyis.put("cpuUser", "0");
            resultAnalyis.put("cpuSys", "0");
            resultAnalyis.put("cpuWait", "0");
            resultAnalyis.put("cpuIdle", "0");
            resultAnalyis.put("cpuOthers", "0");
        }
        String cpuCoreNumCommandRes = cpuUsageCollect.get(cpuCoreNumCommand) != null
                && cpuUsageCollect.get(cpuCoreNumCommand).size() > 0
                ? cpuUsageCollect.get(cpuCoreNumCommand).get(0) : "";
        String cpuLogicNumCommandRes = cpuUsageCollect.get(cpuLogicNumCommand) != null
                && cpuUsageCollect.get(cpuLogicNumCommand).size() > 0
                ? cpuUsageCollect.get(cpuLogicNumCommand).get(0) : "";
        String cpuNumCommandRes = cpuUsageCollect.get(cpuNumCommand) != null
                && cpuUsageCollect.get(cpuNumCommand).size() > 0
                ? cpuUsageCollect.get(cpuNumCommand).get(0) : "";
        String cpuTotalThreadsNumCommandRes = cpuUsageCollect.get(cpuTotalThreadsNumCommand) != null
                && cpuUsageCollect.get(cpuTotalThreadsNumCommand).size() > 0
                ? cpuUsageCollect.get(cpuTotalThreadsNumCommand).get(0) : "";
        resultAnalyis.put("cpuCoreNumCommand", cpuCoreNumCommandRes);
        resultAnalyis.put("cpuLogicNumCommand", cpuLogicNumCommandRes);
        resultAnalyis.put("cpuNumCommand", cpuNumCommandRes);
        resultAnalyis.put("cpuTotalThreadsNumCommand", cpuTotalThreadsNumCommandRes);
        return resultAnalyis;
    }

    /**
     * 获取内存使用情况
     *
     * @return
     */
    public Map<String, String> getMemUsage() throws JSchException {
        Map<String, String> map = new HashMap<>(3);
        DecimalFormat df1 = new DecimalFormat("0.000");
        jschUtil.connect();
        List<String> result = jschUtil.execCmd(memCommand);
        if (result != null && result.size() > 0) {
            double memTotal = Double.parseDouble(result.get(0).toString()) / 1024 / 1024;
            double memFree = Double.parseDouble(result.get(1).toString()) / 1024 / 1024;
            double memUsed = memTotal - memFree;
            double memUsedRate = memUsed / memTotal * 100;
            map.put("memTotal", df1.format(memTotal));
            map.put("memUsed", df1.format(memUsed));
            map.put("memFree", df1.format(memFree));
            map.put("memUsedRate", df1.format(memUsedRate));
        } else {
            map.put("memTotal", "0");
            map.put("memUsed", "0");
            map.put("memFree", "0");
        }
        return map;
    }

    /**
     * 获取io信息(此方法会使用5s时间去Linux收集数据求平均值)
     *
     * @return
     */
    public List<Map<String, String>> getIoUsage() throws JSchException {
        jschUtil.connect();
        List<String> remoteQueryRes = jschUtil.execCmd(ioCommand);
        Map<String, List<Map<String, String>>> parseIoUsageMsg = parseIoUsageMsg(remoteQueryRes);
        return ioUsageCalculation(parseIoUsageMsg);
    }

    /**
     * 解析从服务器拿到的io数据
     *
     * @param remoteQueryRes
     * @return
     */
    public Map<String, List<Map<String, String>>> parseIoUsageMsg(List<String> remoteQueryRes) {

        ArrayList<Map<String, String>> ioUsageParse = new ArrayList<>();
        if (remoteQueryRes == null || remoteQueryRes.size() <= 0) {
            return new HashMap<>(0);
        }

        HashMap<String, List<Map<String, String>>> queryResColl = new HashMap<>();
        for (String remoteQuery : remoteQueryRes) {
            if (remoteQuery == null || "".equals(remoteQuery)) {
                continue;
            }
            String[] splitQuery = remoteQuery.split(" ");
            if (splitQuery.length != 7) {
                continue;
            }
            HashMap<String, String> splitArrayCollect = new HashMap<>(7);
            String deviceName = splitQuery[0];
            //设备名
            splitArrayCollect.put("device", deviceName);
            //每秒读次数
            splitArrayCollect.put("readTimesPerSecond", splitQuery[1]);
            //每秒写次数
            splitArrayCollect.put("writeTimesPerSecond", splitQuery[2]);
            //每秒读数据量
            splitArrayCollect.put("readKbPerSecond", splitQuery[3]);
            //每秒写数据量
            splitArrayCollect.put("writeKbPerSecond", splitQuery[4]);
            //磁盘响应时间
            splitArrayCollect.put("await", splitQuery[5]);
            //磁盘使用率
            splitArrayCollect.put("util", splitQuery[6]);
            List<Map<String, String>> deviceNameListValue = queryResColl.computeIfAbsent(deviceName, a -> new ArrayList<Map<String, String>>());
            deviceNameListValue.add(splitArrayCollect);
        }

        return queryResColl;
    }

    /**
     * 计算获得的数据的平均值
     *
     * @param ioUsageParse
     * @return
     */
    public List<Map<String, String>> ioUsageCalculation(Map<String, List<Map<String, String>>> ioUsageParse) {
        if (ioUsageParse == null || ioUsageParse.size() <= 0) {
            return new ArrayList<>();
        }
        ArrayList<Map<String, String>> calculationRes = new ArrayList<>();
        ioUsageParse.forEach((key, valueList) -> {
            HashMap<String, Double> dataSum = new HashMap<>();
            HashMap<String, String> dataTrans = new HashMap<>();
            for (Map<String, String> item : valueList) {
                item.forEach((itemKey, itemValue) -> {
                    boolean strResult = itemValue.matches("-?[0-9]+.*[0-9]*");
                    if (strResult) {
                        double value = Double.parseDouble(itemValue);
                        dataSum.merge(itemKey, value, Double::sum);
                    } else {
                        dataTrans.putIfAbsent(itemKey, itemValue);
                    }
                });
            }
            int size = valueList.size() != 0 ? valueList.size() : 1;
            dataSum.forEach((dataSumKey, dataSumValue) -> {
                double avageValue = dataSumValue / size;
                dataTrans.putIfAbsent(dataSumKey, String.valueOf(avageValue));
            });
            calculationRes.add(dataTrans);
        });
        return calculationRes;
    }

    /**
     * 获取磁盘使用情况
     *
     * @return
     */
    public String getDiskUsage() throws JSchException {
        jschUtil.connect();
        List<String> result = jschUtil.execCmd(diskCommand);
        Map<String, String> diskInfoMapping = new HashMap<>(4);
        if (result != null && result.size() > 0) {
            String diskInfo = result.get(0);
            String[] diskInfoArray = diskInfo.split(" ");
            diskInfoMapping.put("total", diskInfoArray[0]);
            diskInfoMapping.put("used", diskInfoArray[1]);
            diskInfoMapping.put("avail", diskInfoArray[2]);
            diskInfoMapping.put("hardDiskUsage", diskInfoArray[3]);
        } else {
            diskInfoMapping.put("total", "0");
            diskInfoMapping.put("used", "0");
            diskInfoMapping.put("avail", "0");
            diskInfoMapping.put("hardDiskUsage", "0");
        }
        return diskInfoMapping.toString();
    }

    /**
     * 计算对应进程名称的进程数
     *
     * @param processName
     * @return
     */
    public String processExist(String processName) throws JSchException {
        String exist = "0";
        String command = "ps -ef|grep " + processName + "|grep -v \"grep\" |wc -l";
        jschUtil.connect();
        List<String> result = jschUtil.execCmd(command);
        int i = Integer.parseInt(result.get(0));
        if (i > 0) {
            exist = "1";
        }
        return exist;
    }


    public void close() {
        if (jschUtil != null) {
            jschUtil.disconnect();
        }
    }

    public JSchUtil getJschUtil() {
        return jschUtil;
    }

    public String getNetworkAdapter() {
        return networkAdapter;
    }

    public void setNetworkAdapter(String networkAdapter) {
        this.networkAdapter = networkAdapter;
    }

    /**
     * 执行远程服务器上的sheel脚本文件
     *
     * @param args
     */
   /*public static void main(String[] args) {
        RemoteComputerMonitorUtil remoteComputerMonitorUtil
                = new RemoteComputerMonitorUtil("root", "admin", "localhost", 49160);
        try {
            JSchUtil jschUtil = remoteComputerMonitorUtil.getJschUtil();
            jschUtil.connect();
            List<String> strings = jschUtil.execCmd(remoteComputerMonitorUtil.cpuCommand);
            System.out.println(strings);
            List<String> cpuNum = jschUtil.execCmd(remoteComputerMonitorUtil.cpuNumCommand);
            System.out.println(cpuNum);

        } catch (JSchException e) {
            logger.error(e.getMessage(), e);
        } finally {
            remoteComputerMonitorUtil.close();
        }
    }*/

    /**
     * 执行shell命令
     *
     * @param args
     */
    /*public static void main(String[] args) throws JSchException {
        RemoteComputerMonitorUtil remoteComputerMonitorUtil
                = new RemoteComputerMonitorUtil("root", "admin", "localhost", 49160, "eth0");
        try {

            Map<String, String> cpuUsage = remoteComputerMonitorUtil.getCpuUsage();
            System.out.println(cpuUsage);
        } finally {
            remoteComputerMonitorUtil.close();
        }

    }*/
}
