package com.pactera.monitoring.utils.ssh;

import com.jcraft.jsch.JSchException;
import com.pactera.monitoring.entity.MonHardwareCpuInfoDtl;
import com.pactera.monitoring.entity.MonHardwareCpuInfoTol;
import com.pactera.monitoring.entity.MonHardwareDiskInfoDtl;
import com.pactera.monitoring.entity.MonHardwareDiskInfoTol;
import com.pactera.monitoring.entity.MonHardwareIoInfo;
import com.pactera.monitoring.entity.MonHardwareMemInfoDtl;
import com.pactera.monitoring.entity.MonHardwareServerInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
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
    private String cpuNumCommand = "grep 'physical id' /proc/cpuinfo|sort -u|wc -l";
    //查询cpu核心数
    private String cpuCoreNumCommand = "grep 'cpu cores' /proc/cpuinfo|uniq|awk -F '[: ]' '{print $NF}'";
    //查询cpu总线程
    private String cpuTotalThreadsNumCommand = "grep 'processor' /proc/cpuinfo | sort -u | wc -l";
    //cpu型号
    private String cpuModel = "grep 'model name' /proc/cpuinfo|awk -F ':' '{if(NR==1) print $NF}'";

    //private String memCommand = "cat /proc/meminfo |grep 'MemTotal\\|MemFree'|awk '{print $2}'";
    private String memCommand = "free |awk 'NR==2 || NR==4 {print $2,$3,$4,$5,$6,$7}'";
    //private String diskCommand = "df -h|grep -v Filesystem";
    private String diskCommandTol = "df -h --total|awk 'NR==13 {print $2,$3,$4,$5}'";
    private String diskCommandDtl = "df -h |awk 'NR>1 {print $1,$2,$3,$4,$5}'";
    private String networkCommand = "cat /proc/net/dev|grep networkAdapter|awk '{print $2, $10}'";
    private String serverInfoCommand = "lsb_release  -a|awk '$1 ~/Description/ {print $2, $3, $4}'";
    //服务名
    private String serverHostNameCommand = "hostname";
    private String serverCoreCommand = "uname -r ";
    private String ioCommand = "iostat -dx 1 5|awk '!/^$|Device:|CPU\\}|Linux/ {print $1,$4,$5,$6,$7,$10,$NF}'";
    private JSchUtil jschUtil;
    //cpu明细键
    private String cpuDtlKey = "cpuDtl";
    //cpu汇总键
    private String cpuTolKey = "cpuTol";
    DecimalFormat df = new DecimalFormat("0.000");


    public RemoteComputerMonitorUtil(String user, String passwd, String host, int port) {
        jschUtil = new JSchUtil(user, passwd, host, port);
    }

    public RemoteComputerMonitorUtil(String user, String passwd, String host) {
        jschUtil = new JSchUtil(user, passwd, host);
    }

    public RemoteComputerMonitorUtil(String user, String passwd, String host, int port, String networkAdapter) {
        jschUtil = new JSchUtil(user, passwd, host, port);
        this.networkAdapter = networkAdapter;
    }

    public RemoteComputerMonitorUtil(String user, String passwd, String host, String networkAdapter) {
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


    public MonHardwareServerInfo getServerInfo() throws JSchException {
        MonHardwareServerInfo monHardwareServerInfo = new MonHardwareServerInfo();
        try {
            jschUtil.connect();
            List<String> sercerCoreResult = jschUtil.execCmd(serverCoreCommand);
            List<String> serverInfoResult = jschUtil.execCmd(serverInfoCommand);
            List<String> serverHostNameResult = jschUtil.execCmd(serverHostNameCommand);
            if (sercerCoreResult != null && sercerCoreResult.size() > 0) {
                monHardwareServerInfo.setServiceCoreVersion(sercerCoreResult.get(0));
            }
            if (serverInfoResult != null && serverInfoResult.size() > 0) {
                monHardwareServerInfo.setServiceVersion(serverInfoResult.get(0));
            }
            monHardwareServerInfo.setServiceActive("1");
            if(serverHostNameResult !=null && serverHostNameResult.size()>0){
                monHardwareServerInfo.setServiceNm(serverHostNameResult.get(0));
            }
        }  finally {
            close();
        }

        return monHardwareServerInfo;
    }


    /**
     * 获取CPU使用情况_明细
     *
     * @return
     */
    public MonHardwareCpuInfoDtl getCpuUsageDtl() throws JSchException {
        Map<String, String> map = new HashMap<>(5);
        jschUtil.connect();
        String[] cpuUsageCommandArray = {cpuCommand,serverHostNameCommand};
        Map<String, List<String>> cpuUsageCollect = jschUtil.execCmdMultipleCommands(cpuUsageCommandArray);
        return (MonHardwareCpuInfoDtl)cpuUsageAnalyis(cpuUsageCollect, jschUtil.getHost()).get(cpuDtlKey);
    }

    /**
     * 获取CPU使用情况(汇总)
     *
     * @return
     */
    public MonHardwareCpuInfoTol getCpuUsageTol() throws JSchException {
        jschUtil.connect();
        String[] cpuUsageCommandArray = {cpuCommand, cpuCoreNumCommand,
                cpuModel, cpuNumCommand, cpuTotalThreadsNumCommand,serverHostNameCommand};
        Map<String, List<String>> cpuUsageCollect = jschUtil.execCmdMultipleCommands(cpuUsageCommandArray);
        return (MonHardwareCpuInfoTol)cpuUsageAnalyis(cpuUsageCollect, jschUtil.getHost()).get(cpuTolKey);
    }

    /**
     * 获取全量cpu信息
     * @return
     * @throws JSchException
     */
    public Map<String, Object> getCpuUsageInfo() throws JSchException {
        jschUtil.connect();
        String[] cpuUsageCommandArray = {cpuCommand,
                cpuModel, cpuNumCommand, cpuTotalThreadsNumCommand,serverHostNameCommand};
        Map<String, List<String>> cpuUsageCollect = jschUtil.execCmdMultipleCommands(cpuUsageCommandArray);
        return cpuUsageAnalyis(cpuUsageCollect,jschUtil.getHost());
    }

    /**
     * cpu结果集格式化
     *
     * @param cpuUsageCollect
     * @return
     */
    public Map<String, Object> cpuUsageAnalyis(Map<String, List<String>> cpuUsageCollect,String ip) {
        if (cpuUsageCollect == null || cpuUsageCollect.size() <= 0) {
            return new HashMap<>(0);
        }
        HashMap<String, Object> result = new HashMap<>(2);
        Date date = new Date();
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

        List<String> cpuCommandRes = cpuUsageCollect.get(cpuCommand);
        if (cpuCommandRes != null && cpuCommandRes.size() > 0) {
            String[] cpuInfo = cpuCommandRes.get(0).split(" ");
            MonHardwareCpuInfoDtl monHardwareCpuInfoDtl = new MonHardwareCpuInfoDtl();
            monHardwareCpuInfoDtl.setUsCpuRate(cpuInfo[0]);
            monHardwareCpuInfoDtl.setSyCpuRate(cpuInfo[1]);
            monHardwareCpuInfoDtl.setWaCpuRate(cpuInfo[2]);
            monHardwareCpuInfoDtl.setIdCpuRate(cpuInfo[3]);
            monHardwareCpuInfoDtl.setDataDt(date);
            monHardwareCpuInfoDtl.setServiceIp(ip);
            if(StringUtils.isNotEmpty(serverName)){
                monHardwareCpuInfoDtl.setServiceNm(serverName);
            }
            result.put(cpuDtlKey, monHardwareCpuInfoDtl);
        }
        MonHardwareCpuInfoTol monHardwareCpuInfoTol = new MonHardwareCpuInfoTol();
        monHardwareCpuInfoTol.setCpuNum(cpuNumCommandRes);
        monHardwareCpuInfoTol.setCpuType(cpuModelInfo);
        monHardwareCpuInfoTol.setCpuThread(cpuTotalThreadsNumCommandRes);
        monHardwareCpuInfoTol.setDataDt(date);
        monHardwareCpuInfoTol.setServiceNm(serverName);
        result.put(cpuTolKey, monHardwareCpuInfoTol);
        return result;
    }

    /**
     * 获取内存使用情况
     *
     * @return
     */
    public MonHardwareMemInfoDtl getMemUsage() throws JSchException {
        Map<String, String> map = new HashMap<>(9);
        jschUtil.connect();
        List<String> memoResult = jschUtil.execCmd(memCommand);
        List<String> serverNameResult = jschUtil.execCmd(serverHostNameCommand);
        return memoAnalyis(memoResult,serverNameResult);
    }

    /**
     * 内存结果分析
     * @param memoResult
     * @return
     */
    public MonHardwareMemInfoDtl memoAnalyis(List<String> memoResult,List<String> serverNameResult){

        if (memoResult == null
                || memoResult.size() != 2
                || serverNameResult == null
                || StringUtils.isEmpty(serverNameResult.get(0))) {
            return new MonHardwareMemInfoDtl();
        }

        String memoInfo = memoResult.get(0);
        String swapInfo = memoResult.get(1);
        String serverName = serverNameResult.get(0);

        if (StringUtils.isEmpty(memoInfo) || StringUtils.isEmpty(swapInfo)) {
            return new MonHardwareMemInfoDtl();
        }

        String[] memoInfoArray = memoInfo.split(" ");
        String[] swapInfoArray = swapInfo.split(" ");
        int memoBookLen=6;
        int swapBookLen = 3;
        if (memoInfoArray.length != memoBookLen || swapInfoArray.length != swapBookLen) {
            return new MonHardwareMemInfoDtl();
        }
        double memTotal = Double.parseDouble(memoInfoArray[0]) / 1024 / 1024;
        double memFree = Double.parseDouble(memoInfoArray[2]) / 1024 / 1024;
        double memUsed = Double.parseDouble(memoInfoArray[1]) / 1024 / 1024;
        double memoShared = Double.parseDouble(memoInfoArray[3]) / 1024 / 1024;
        double swapTotal = Double.parseDouble(swapInfoArray[0]) / 1024 / 1024;
        double swapUsed = Double.parseDouble(swapInfoArray[1]) / 1024 / 1024;
        double swapFree = Double.parseDouble(swapInfoArray[2]) / 1024 / 1024;
        double memUsedRate = memUsed / memTotal * 100;
        double cachedAndBuffers = (Double.parseDouble(memoInfoArray[4])
                + Double.parseDouble(memoInfoArray[5])) / 1024 / 1024;
        MonHardwareMemInfoDtl monHardwareMemInfoDtl = new MonHardwareMemInfoDtl();
        monHardwareMemInfoDtl.setMemTotal(df.format(memTotal));
        monHardwareMemInfoDtl.setMemUseTotal(df.format(memUsed));
        monHardwareMemInfoDtl.setFreeMemTotal(df.format(memFree));
        monHardwareMemInfoDtl.setMemUsedRate(df.format(memUsedRate));
        monHardwareMemInfoDtl.setSharedMemTotal(df.format(memoShared));
        monHardwareMemInfoDtl.setSwapMemTotal(df.format(swapTotal));
        monHardwareMemInfoDtl.setSwapUseMemTotal(df.format(swapUsed));
        monHardwareMemInfoDtl.setSwapFreeMemTotal(df.format(swapFree));
        monHardwareMemInfoDtl.setBufferCacheUseMemTotal(df.format(cachedAndBuffers));
        monHardwareMemInfoDtl.setServiceNm(serverName);
        return monHardwareMemInfoDtl;
    }

    public HashMap<String,String> memoInitAnalyisResult(DecimalFormat df){
        HashMap<String, String> analyisResult = new HashMap<>();
        analyisResult.put("memTotal", df.format(0));
        analyisResult.put("memUsed", df.format(0));
        analyisResult.put("memFree", df.format(0));
        analyisResult.put("memUsedRate", df.format(0));
        analyisResult.put("memoShared", df.format(0));
        analyisResult.put("swapTotal", df.format(0));
        analyisResult.put("swapUsed", df.format(0));
        analyisResult.put("swapFree", df.format(0));
        analyisResult.put("cachedAndBuffers", df.format(0));
        return analyisResult;
    }

    /**
     * 获取io信息(此方法会使用5s时间去Linux收集数据求平均值)
     *
     * @return
     */
    public  List<MonHardwareIoInfo> getIoUsage() throws JSchException {
        jschUtil.connect();
        List<String> remoteQueryRes = jschUtil.execCmd(ioCommand);
        List<String> serverNameResult = jschUtil.execCmd(serverHostNameCommand);
        Map<String, List<Map<String, String>>> parseIoUsageMsg = parseIoUsageMsg(remoteQueryRes);
        List<Map<String, String>> ioUsageCalculation = ioUsageCalculation(parseIoUsageMsg);
        ArrayList<MonHardwareIoInfo> monHardwareIoInfoArrayList = new ArrayList<>();
        Date date = new Date();
        ioUsageCalculation.forEach(map->{
            MonHardwareIoInfo monHardwareIoInfo = new MonHardwareIoInfo();
            //设备名
            monHardwareIoInfo.setDiskNm(map.get("device"));
            /*//每秒读次数
            splitArrayCollect.put("readTimesPerSecond", splitQuery[1]);*/
            /*//每秒写次数
            splitArrayCollect.put("writeTimesPerSecond", splitQuery[2]);*/
            //每秒读数据量
            monHardwareIoInfo.setDiskRead(map.get("readKbPerSecond"));
            //每秒写数据量
            monHardwareIoInfo.setDiskWrite(map.get("writeKbPerSecond"));
            //磁盘响应时间
            monHardwareIoInfo.setDiskAvgRespond(map.get("await"));
            //磁盘使用率
            monHardwareIoInfo.setDiskAvgRespond(map.get("util"));
            if(serverNameResult !=null
                    && serverNameResult.size()!=0
                    && StringUtils.isNotEmpty(serverNameResult.get(0))){
                monHardwareIoInfo.setServiceNm(serverNameResult.get(0));
            }
            monHardwareIoInfo.setDataDt(date);
            Double diskTranSecond = Double.parseDouble(map.get("readKbPerSecond")) + Double.parseDouble(map.get("writeKbPerSecond"));
            monHardwareIoInfo.setDiskTrans(df.format(diskTranSecond));
            monHardwareIoInfo.setServiceIp(jschUtil.getHost());
            monHardwareIoInfoArrayList.add(monHardwareIoInfo);
        });

        return monHardwareIoInfoArrayList;
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
     * 获取磁盘使用情况_汇总
     *
     * @return
     */
    public MonHardwareDiskInfoTol getDiskUsageTol() throws JSchException {
        jschUtil.connect();
        List<String> result = jschUtil.execCmd(diskCommandTol);
        List<String> serverNameResult = jschUtil.execCmd(serverHostNameCommand);
        MonHardwareDiskInfoTol monHardwareDiskInfoTol = new MonHardwareDiskInfoTol();
        if (result != null && result.size() > 0) {
            String diskInfo = result.get(0);
            String[] diskInfoArray = diskInfo.split(" ");
            monHardwareDiskInfoTol.setDiskTotalSize(diskInfoArray[0]);
            monHardwareDiskInfoTol.setDiskUsedSize(diskInfoArray[1]);
            monHardwareDiskInfoTol.setDiskAvailSize(diskInfoArray[2]);
            monHardwareDiskInfoTol.setDiskUsedRate(diskInfoArray[3]);
            monHardwareDiskInfoTol.setDataDt(new Date());
            monHardwareDiskInfoTol.setServiceIp(jschUtil.getHost());
            if (serverNameResult !=null
                && serverNameResult.size()>0
                && StringUtils.isNotEmpty(serverNameResult.get(0))) {
                monHardwareDiskInfoTol.setServiceNm(serverNameResult.get(0));
            }
        }
        return monHardwareDiskInfoTol;
    }

    /**
     * 获取磁盘使用情况_明细
     *
     * @return 磁盘明细对象集合
     */
    public List<MonHardwareDiskInfoDtl> getDiskUsageDtl() throws JSchException {
        jschUtil.connect();
        List<String> result = jschUtil.execCmd(diskCommandDtl);
        List<String> serverNameResult = jschUtil.execCmd(serverHostNameCommand);
        ArrayList<MonHardwareDiskInfoDtl> monHardwareDiskInfoDtlList = new ArrayList<>();
        if(result == null || result.size()<=0){
            return monHardwareDiskInfoDtlList;
        }
        Date date = new Date();
        result.forEach(item->{
            if(StringUtils.isEmpty(item)){return;}
            String[] diskInfoArray = item.split(" ");
            if(diskInfoArray.length!=5){return;}
            MonHardwareDiskInfoDtl monHardwareDiskInfoDtl = new MonHardwareDiskInfoDtl();
            monHardwareDiskInfoDtl.setFilesystemNm(diskInfoArray[0]);
            monHardwareDiskInfoDtl.setDiskTotalSize(diskInfoArray[1]);
            monHardwareDiskInfoDtl.setDiskUsedSize(diskInfoArray[2]);
            monHardwareDiskInfoDtl.setDiskAvailSize(diskInfoArray[3]);
            monHardwareDiskInfoDtl.setDiskUsedRate(diskInfoArray[4]);
            if(serverNameResult!=null
                    && serverNameResult.size()>0
                    && StringUtils.isNotEmpty(serverNameResult.get(0))) {
                monHardwareDiskInfoDtl.setServiceNm(serverNameResult.get(0));
            }
            monHardwareDiskInfoDtl.setServiceIp(jschUtil.getHost());
            monHardwareDiskInfoDtl.setDataDt(date);
            monHardwareDiskInfoDtlList.add(monHardwareDiskInfoDtl);
        });
        return monHardwareDiskInfoDtlList;
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
   /* public static void main(String[] args) throws JSchException {
        RemoteComputerMonitorUtil remoteComputerMonitorUtil
                = new RemoteComputerMonitorUtil("root", "admin", "localhost", 49160, "eth0");
        try {

            Map<String, String> cpuUsage = remoteComputerMonitorUtil.getServerInfo();
            System.out.println(cpuUsage);
        } finally {
            remoteComputerMonitorUtil.close();
        }

    }*/
}
