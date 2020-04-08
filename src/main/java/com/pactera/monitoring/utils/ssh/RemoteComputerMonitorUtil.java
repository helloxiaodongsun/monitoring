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
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * @author 84483
 */
public class RemoteComputerMonitorUtil {


    private static Logger logger = LoggerFactory.getLogger(RemoteComputerMonitorUtil.class);
    private String networkAdapter;
    //private String cpuCommand = "vmstat|awk 'NR==3''{print $13, $14, $16, $15}'";
    private String cpuCommand = "iostat -c|awk -F ' ' 'NR==4 {print $1 \";\" $2 \";\" $3 \";\" $4 \";\" $6}'";
    //查询物理cpu数目
    private String cpuNumCommand = "grep 'physical id' /proc/cpuinfo|sort -u|wc -l";
    //查询cpu核心数
    private String cpuCoreNumCommand = "grep 'cpu cores' /proc/cpuinfo|uniq|awk -F '[: ]' '{print $NF}'";
    //查询cpu总线程
    private String cpuTotalThreadsNumCommand = "grep 'processor' /proc/cpuinfo | sort -u | wc -l";
    //cpu型号
    private String cpuModel = "grep 'model name' /proc/cpuinfo|awk -F ':' '{if(NR==1) print $NF}'";

    //private String memCommand = "cat /proc/meminfo |grep 'MemTotal\\|MemFree'|awk '{print $2}'";
    private String memCommandUbantu = "free |awk '/Mem/||/Swap/ {print $2,$3,$4,$5,$6,$7}'";
    private String memCommandCentos = "free |awk '/Mem/||/Swap/ {print $2,$3,$4,$5,$6,$7}'";
    //private String diskCommand = "df -h|grep -v Filesystem";
    //查看硬盘汇总
    private String diskCommandTol = "df --total|awk '/total/ {print $2,$3,$4,$5}'";
    //查看硬盘明细
    private String diskCommandDtl = "df |awk 'NR>1 {print $1,$2,$3,$4,$5}'";
    //查看磁盘使用率
    private String diskUseRateCommand = "df |awk 'NR !=1 {print $2 \";\" $3}'";
    private String networkCommand = "cat /proc/net/dev|grep networkAdapter|awk '{print $2, $10}'";
    //ubantu查看发行版本号
    private String serverInfoCommandUbntu = "cat /etc/issue.net";
    //centos 、redhat查看发行版本号
    private String serverInfoCommandCentos = "cat /etc/redhat-release";
    //判断服务器是ubantu 还是centos 、redhat
    private String determineServerType = "find /etc -name redhat-release |wc -l";
    //服务名
    private String serverHostNameCommand = "hostname";
    private String serverCoreCommand = "uname -r ";
    private String ioCommand = "iostat -dx |awk '!/^$|Linux/ {print $0}'";

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

    /**
     * 获得服务器信息
     *
     * @return
     * @throws JSchException
     */
    public MonHardwareServerInfo getServerInfo() throws JSchException {
        MonHardwareServerInfo monHardwareServerInfo = new MonHardwareServerInfo();
        try {
            jschUtil.connect();
            List<String> sercerCoreResult = jschUtil.execCmd(serverCoreCommand);
            List<String> determinServerTypeList = jschUtil.execCmd(determineServerType);
            List<String> serverInfoResult ;
            if(determinServerTypeList==null
                    || determinServerTypeList.size()<=0
                    || "1".equals(determinServerTypeList.get(0))){
                //服务器是centos、redhat
                serverInfoResult = jschUtil.execCmd(serverInfoCommandCentos);
            }else {
                //服务器是ubantu
                serverInfoResult = jschUtil.execCmd(serverInfoCommandUbntu);
            }
            List<String> serverHostNameResult = jschUtil.execCmd(serverHostNameCommand);
            if (sercerCoreResult != null && sercerCoreResult.size() > 0) {
                monHardwareServerInfo.setServiceCoreVersion(sercerCoreResult.get(0));
            }
            if (serverInfoResult != null && serverInfoResult.size() > 0) {
                monHardwareServerInfo.setServiceVersion(serverInfoResult.get(0));
            }
            monHardwareServerInfo.setServiceActive("1");
            if (serverHostNameResult != null && serverHostNameResult.size() > 0) {
                monHardwareServerInfo.setServiceNm(serverHostNameResult.get(0));
            }
        } finally {
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
        jschUtil.connect();
        String[] cpuUsageCommandArray = {cpuCommand, serverHostNameCommand};
        Map<String, List<String>> cpuUsageCollect = jschUtil.execCmdMultipleCommands(cpuUsageCommandArray);
        return (MonHardwareCpuInfoDtl) cpuUsageAnalyis(cpuUsageCollect, jschUtil.getHost()).get(cpuDtlKey);
    }

    /**
     * 获取CPU使用情况(汇总)
     *
     * @return
     */
    public MonHardwareCpuInfoTol getCpuUsageTol() throws JSchException {
        jschUtil.connect();
        String[] cpuUsageCommandArray = {cpuCommand, cpuCoreNumCommand,
                cpuModel, cpuNumCommand, cpuTotalThreadsNumCommand, serverHostNameCommand};
        Map<String, List<String>> cpuUsageCollect = jschUtil.execCmdMultipleCommands(cpuUsageCommandArray);
        return (MonHardwareCpuInfoTol) cpuUsageAnalyis(cpuUsageCollect, jschUtil.getHost()).get(cpuTolKey);
    }

    /**
     * 获取全量cpu信息
     *
     * @return
     * @throws JSchException
     */
    public Map<String, Object> getCpuUsageInfo() throws JSchException {
        jschUtil.connect();
        String[] cpuUsageCommandArray = {cpuCommand,
                cpuModel, cpuNumCommand, cpuTotalThreadsNumCommand, serverHostNameCommand};
        Map<String, List<String>> cpuUsageCollect = jschUtil.execCmdMultipleCommands(cpuUsageCommandArray);
        return cpuUsageAnalyis(cpuUsageCollect, jschUtil.getHost());
    }

    /**
     * cpu结果集格式化
     *
     * @param cpuUsageCollect
     * @return
     */
    public Map<String, Object> cpuUsageAnalyis(Map<String, List<String>> cpuUsageCollect, String ip) {
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
            String[] cpuInfo = cpuCommandRes.get(0).split(";");
            MonHardwareCpuInfoDtl monHardwareCpuInfoDtl = new MonHardwareCpuInfoDtl();
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
        List<String> memoResult = jschUtil.execCmd(memCommandUbantu);
        List<String> serverNameResult = jschUtil.execCmd(serverHostNameCommand);
        return memoAnalyis(memoResult, serverNameResult);
    }

    /**
     * 内存结果分析
     *
     * @param memoResult
     * @return
     */
    public MonHardwareMemInfoDtl memoAnalyis(List<String> memoResult, List<String> serverNameResult) {

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
        int memoBookLen = 6;
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

    public HashMap<String, String> memoInitAnalyisResult(DecimalFormat df) {
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
    public MonHardwareIoInfo getIoUsage() throws JSchException {
        jschUtil.connect();
        List<String> remoteQueryRes = jschUtil.execCmd(ioCommand);
        List<String> serverNameResult = jschUtil.execCmd(serverHostNameCommand);
        List<String> diskUseList = jschUtil.execCmd(diskUseRateCommand);
        String diskUsedRate = diskUseCalculat(diskUseList);
        if(remoteQueryRes == null || remoteQueryRes.size()<=0){
            return new MonHardwareIoInfo();
        }
        List<List<String>> remoteQueryResTrim = remoteResTrimAndSplit(remoteQueryRes);
        Map<String, Map<String, String>> parseIoUsageMsg = parseIoUsageMsg(remoteQueryResTrim);
        Map<String, Double> ioUsageCalculation = ioUsageCalculation(parseIoUsageMsg);
        Date date = new Date();
        MonHardwareIoInfo monHardwareIoInfo = new MonHardwareIoInfo();
        //设备名
        // monHardwareIoInfo.setDiskNm(map.get("device"));
            /*//每秒读次数
            splitArrayCollect.put("readTimesPerSecond", splitQuery[1]);*/
            /*//每秒写次数
            splitArrayCollect.put("writeTimesPerSecond", splitQuery[2]);*/
        //每秒读数据量
        monHardwareIoInfo.setDiskRead(df.format(ioUsageCalculation.get("rkB/s")));
        //每秒写数据量
        monHardwareIoInfo.setDiskWrite(df.format(ioUsageCalculation.get("wkB/s")));
        //磁盘响应时间
        monHardwareIoInfo.setDiskAvgRespond(df.format(ioUsageCalculation.get("await")));
        //磁盘使用率
        monHardwareIoInfo.setDiskUseRate(diskUsedRate);
        if (serverNameResult != null
                && serverNameResult.size() != 0
                && StringUtils.isNotEmpty(serverNameResult.get(0))) {
            monHardwareIoInfo.setServiceNm(serverNameResult.get(0));
        }
        monHardwareIoInfo.setDataDt(date);
        Double diskTranSecond = ioUsageCalculation.get("rkB/s") + ioUsageCalculation.get("wkB/s");
        monHardwareIoInfo.setDiskTrans(df.format(diskTranSecond));
        monHardwareIoInfo.setServiceIp(jschUtil.getHost());
        return monHardwareIoInfo;
    }

    /**
     * 返回的表头跟预设数据对比，计算出预设数据在返回结果中的索引值
     *
     * @param tableHeader
     * @return
     */
    public Map<String, Integer> ioFieldIndexCaculat(List<String> tableHeader) {
        HashMap<String, Integer> map = new HashMap<>();
        List<Map<String, List<String>>> ioSearchField = ioSearchField();
        ioSearchField.forEach(item -> {
            boolean flag = false;
            Set<String> itemKey = item.keySet();
            for (String key : itemKey) {
                List<String> itemValueList = item.get(key);
                for (String itemValue : itemValueList) {
                    for (int i = 0; i < tableHeader.size(); i++) {
                        String splitItem = tableHeader.get(i);
                        if (itemValue.equals(splitItem)) {
                            map.put(key, i);
                            flag = true;
                            break;
                        }

                    }
                    if (flag) {
                        break;
                    }
                }
                if (flag) {
                    break;
                }
            }
        });
        return map;
    }

    //初始化io查询字段集合
    public List<Map<String, List<String>>> ioSearchField() {
        ArrayList<Map<String, List<String>>> result = new ArrayList<>();
        List<String> device = Arrays.asList("Device", "Device:");
        HashMap<String, List<String>> deviceMap = new HashMap<>();
        deviceMap.put("Device", device);
        List<String> readTimesPerSecond = Collections.singletonList("r/s");
        HashMap<String, List<String>> readTimesPerSecondMap = new HashMap<>();
        readTimesPerSecondMap.put("r/s", readTimesPerSecond);
        List<String> writeTimesPerSecond = Collections.singletonList("w/s");
        HashMap<String, List<String>> writeTimesPerSecondMap = new HashMap<>();
        writeTimesPerSecondMap.put("w/s", writeTimesPerSecond);
        List<String> readKbPerSecond = Collections.singletonList("rkB/s");
        HashMap<String, List<String>> readKbPerSecondMap = new HashMap<>();
        readKbPerSecondMap.put("rkB/s", readKbPerSecond);
        List<String> writeKbPerSecond = Collections.singletonList("wkB/s");
        HashMap<String, List<String>> writeKbPerSecondMap = new HashMap<>();
        writeKbPerSecondMap.put("wkB/s", writeKbPerSecond);
        List<String> rAwait = Collections.singletonList("r_await");
        HashMap<String, List<String>> rAwaitMap = new HashMap<>();
        rAwaitMap.put("r_await", rAwait);
        List<String> wAwait = Collections.singletonList("w_await");
        HashMap<String, List<String>> wAwaitMap = new HashMap<>();
        wAwaitMap.put("w_await", wAwait);
        List<String> await = Collections.singletonList("await");
        HashMap<String, List<String>> awaitMap = new HashMap<>();
        awaitMap.put("await", await);
        result.add(deviceMap);
        result.add(readTimesPerSecondMap);
        result.add(writeTimesPerSecondMap);
        result.add(readKbPerSecondMap);
        result.add(writeKbPerSecondMap);
        result.add(rAwaitMap);
        result.add(wAwaitMap);
        result.add(awaitMap);
        return result;
    }
    //针对前台返回的数据以空格切割后，去除空处理
    public List<List<String>> remoteResTrimAndSplit(List<String> remoteQueryRes){
        ArrayList<List<String>> result = new ArrayList<>();
        remoteQueryRes.forEach(item->{
            if(StringUtils.isNotEmpty(item)){
                String[] split = item.split(" ");
                ArrayList<String> itemList = new ArrayList<>();
                for (String itemValue : split) {
                    if(StringUtils.isNotEmpty(itemValue)){
                        itemList.add(itemValue);
                    }
                }
                result.add(itemList);
            }
        });
        return result;
    }
    /**
     * 解析从服务器拿到的io数据
     *
     * @param remoteQueryRes
     * @return
     */
    public Map<String, Map<String, String>> parseIoUsageMsg(List<List<String>> remoteQueryRes) {

        if (remoteQueryRes == null || remoteQueryRes.size() <= 1) {
            return new HashMap<>(0);
        }
        HashMap<String, Map<String, String>> queryResColl = new HashMap<>();
        Map<String, Integer> fieldIndex = ioFieldIndexCaculat(remoteQueryRes.get(0));
        Integer deviceIndex = fieldIndex.get("Device");
        Integer readTimesPerSecondIndex = fieldIndex.get("r/s");
        Integer writeTimePerSecondIndex = fieldIndex.get("w/s");
        Integer readKbPerSecondIndex = fieldIndex.get("rkB/s");
        Integer writeKbPerSecondIndex = fieldIndex.get("wkB/s");
        Integer rAwaitIndex = fieldIndex.get("r_await");
        Integer wAaitIndex = fieldIndex.get("w_await");
        Integer awaitIndex = fieldIndex.get("await");
        if (deviceIndex == null
                || readTimesPerSecondIndex == null
                || writeTimePerSecondIndex == null
                || readKbPerSecondIndex == null
                || writeKbPerSecondIndex == null
        ) {
            return queryResColl;
        }
        boolean readWriteWaitCheck = rAwaitIndex == null || wAaitIndex == null;
        if (awaitIndex == null && readWriteWaitCheck) {
            return queryResColl;
        }
        for (int i = 1; i < remoteQueryRes.size(); i++) {
            List<String> itemList = remoteQueryRes.get(i);
            if (itemList == null
                    || itemList.size()==0) {
                continue;
            }

            HashMap<String, String> splitArrayCollect = new HashMap<>(8);
            //设备名
            splitArrayCollect.put("Device", itemList.get(deviceIndex));
            //每秒读次数
            splitArrayCollect.put("r/s", itemList.get(readTimesPerSecondIndex));
            //每秒写次数
            splitArrayCollect.put("w/s", itemList.get(writeTimePerSecondIndex));
            //每秒读数据量
            splitArrayCollect.put("rkB/s", itemList.get(readKbPerSecondIndex));
            //每秒写数据量
            splitArrayCollect.put("wkB/s", itemList.get(writeKbPerSecondIndex));
            //磁盘响应时间
            if (awaitIndex != null) {
                splitArrayCollect.put("await", itemList.get(awaitIndex));
            }
            if (rAwaitIndex != null) {
                splitArrayCollect.put("r_await", itemList.get(rAwaitIndex));
            }
            if (wAaitIndex != null) {
                splitArrayCollect.put("w_await", itemList.get(wAaitIndex));
            }
            queryResColl.put(itemList.get(deviceIndex), splitArrayCollect);
        }
        return queryResColl;
    }

    /**
     * 计算获得的数据的平均值
     *
     * @param ioUsageParse
     * @return
     */
    public Map<String, Double> ioUsageCalculation(Map<String, Map<String, String>> ioUsageParse) {
        if (ioUsageParse == null || ioUsageParse.size() <= 0) {
            return new HashMap<>();
        }
        HashMap<String, Double> dataTrans = new HashMap<>();
        AtomicInteger length = new AtomicInteger();
        //求和
        ioUsageParse.forEach((key, valueCal) -> {
            if (length.get() == 0) {
                length.set(valueCal.size());
            }
            valueCal.forEach((itemKey, itemValue) -> {
                boolean strResult = itemValue.matches("-?[0-9]+.*[0-9]*");
                if (strResult) {
                    double value = Double.parseDouble(itemValue);
                    dataTrans.merge(itemKey, value, Double::sum);
                }
            });
        });

        Double await = dataTrans.get("await");
        if (await == null) {
            Double readTimesPerSecond = dataTrans.get("r/s");
            Double writeTimesPerSecond = dataTrans.get("w/s");
            Double rAwait = dataTrans.get("r_await");
            Double wAwait = dataTrans.get("w_await");
            dataTrans.put("await", ioWaitCalculat(readTimesPerSecond, writeTimesPerSecond, rAwait, wAwait));
        } else {
            dataTrans.put("await", await / ioUsageParse.size());
        }
        return dataTrans;
    }

    //计算硬盘响应时间
    public double ioWaitCalculat(double readTimes, double writeTimes, double readWait, double writeWait) {
        double readTimesInit = Double.compare(readTimes,0.00d)==0 ? 1 : readTimes;
        double writeTimesInit = Double.compare(writeTimes,0.00d)==0  ? 1 : writeTimes;
        double operateTime = readWait * readTimesInit + writeWait * writeTimesInit;
        double operateTimes = readTimes + writeTimes;
        operateTimes =Double.compare(operateTimes,0.00d)  == 0 ? 1 : operateTimes;
        return operateTime / operateTimes;
    }

    //硬盘使用率结果集分析
    public String diskUseCalculat(List<String> diskUseList) {
        if (diskUseList == null || diskUseList.size() <= 0) {
            return "0";
        }
        //结果集切割分隔符后的预计长度
        int splitCount = 2;
        ArrayList<Double> blocksTotalList = new ArrayList<>();
        ArrayList<Double> usedTotalList = new ArrayList<>();
        diskUseList.forEach(item -> {
            if (StringUtils.isEmpty(item)) {
                return;
            }
            String[] split = item.split(";");
            if (split.length != splitCount) {
                return;
            }
            String blockItemStr = split[0];
            String usedItemStr = split[1];
            if (StringUtils.isEmpty(blockItemStr)) {
                blockItemStr = "0";
            }
            if (StringUtils.isEmpty(usedItemStr)) {
                usedItemStr = "0";
            }
            Double blockItem = Double.parseDouble(blockItemStr);
            Double usedItem = Double.parseDouble(usedItemStr);
            blocksTotalList.add(blockItem);
            usedTotalList.add(usedItem);
        });
        Double blocksSum = 0.0;
        Double usedSum = 0.0;
        for (Double blocks : blocksTotalList) {
            blocksSum += blocks;
        }
        for (Double used : usedTotalList) {
            usedSum += used;
        }
        if (blocksSum.compareTo(0.0) < 1) {
            return "0";
        }
        double diskUsedRate = usedSum / blocksSum * 100;
        return df.format(diskUsedRate);
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
            monHardwareDiskInfoTol.setDiskUsedRate(eliminatePercentSign(diskInfoArray[3]));
            monHardwareDiskInfoTol.setDataDt(new Date());
            monHardwareDiskInfoTol.setServiceIp(jschUtil.getHost());
            if (serverNameResult != null
                    && serverNameResult.size() > 0
                    && StringUtils.isNotEmpty(serverNameResult.get(0))) {
                monHardwareDiskInfoTol.setServiceNm(serverNameResult.get(0));
            }
        }
        return monHardwareDiskInfoTol;
    }
    //消除字符串上的%
    public String eliminatePercentSign(String source){
        if(StringUtils.isEmpty(source)){
            return "";
        }
        int index = source.indexOf("%");
        if(index<0){
            return source;
        }
        return source.substring(0, index);
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
        if (result == null || result.size() <= 0) {
            return monHardwareDiskInfoDtlList;
        }
        Date date = new Date();
        result.forEach(item -> {
            if (StringUtils.isEmpty(item)) {
                return;
            }
            String[] diskInfoArray = item.split(" ");
            if (diskInfoArray.length != 5) {
                return;
            }
            MonHardwareDiskInfoDtl monHardwareDiskInfoDtl = new MonHardwareDiskInfoDtl();
            monHardwareDiskInfoDtl.setFilesystemNm(diskInfoArray[0]);
            monHardwareDiskInfoDtl.setDiskTotalSize(diskInfoArray[1]);
            monHardwareDiskInfoDtl.setDiskUsedSize(diskInfoArray[2]);
            monHardwareDiskInfoDtl.setDiskAvailSize(diskInfoArray[3]);
            monHardwareDiskInfoDtl.setDiskUsedRate(eliminatePercentSign(diskInfoArray[4]));
            if (serverNameResult != null
                    && serverNameResult.size() > 0
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
