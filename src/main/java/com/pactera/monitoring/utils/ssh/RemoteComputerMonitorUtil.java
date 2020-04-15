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
import java.util.stream.Collectors;


/**
 * @author 84483
 */
public class RemoteComputerMonitorUtil {


    private static Logger logger = LoggerFactory.getLogger(RemoteComputerMonitorUtil.class);
    private String networkAdapter;
    //private String cpuCommand = "vmstat|awk 'NR==3''{print $13, $14, $16, $15}'";
    private String cpuCommand = "iostat -c 1 2|grep -v '^$'|awk 'NR==5  {print $1 \";\" $2 \";\" $3 \";\" $4 \";\" $6}'";
    //查询物理cpu数目
    private String cpuNumCommand = "grep 'physical id' /proc/cpuinfo|sort -u|wc -l";
    //查询cpu核心数
    private String cpuCoreNumCommand = "grep 'cpu cores' /proc/cpuinfo|uniq|awk -F '[: ]' '{print $NF}'";
    //查询cpu总线程
    private String cpuTotalThreadsNumCommand = "grep 'processor' /proc/cpuinfo | sort -u | wc -l";
    //cpu型号
    private String cpuModel = "grep 'model name' /proc/cpuinfo|awk -F ':' '{if(NR==1) print $NF}'";

    //private String memCommand = "cat /proc/meminfo |grep 'MemTotal\\|MemFree'|awk '{print $2}'";
    private String memCommand = "free|grep -v 'buffers/cache'|awk 'BEGIN{FS=\" \";OFS=\";\"} {NF=NF;print $0}'";
    //private String diskCommand = "df -h|grep -v Filesystem";
    //查看硬盘汇总
    private String diskCommandTol = "df --total|awk '/total/ {print $2,$3,$4,$5}'";
    //查看硬盘明细
    private String diskCommandDtl = "df|awk 'BEGIN{FS=\" \";OFS=\";\"} {NF=NF;print $0}'";
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
            List<String> serverInfoResult;
            if (determinServerTypeList == null
                    || determinServerTypeList.size() <= 0
                    || "1".equals(determinServerTypeList.get(0))) {
                //服务器是centos、redhat
                serverInfoResult = jschUtil.execCmd(serverInfoCommandCentos);
            } else {
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
        monHardwareCpuInfoTol.setServiceIp(ip);
        result.put(cpuTolKey, monHardwareCpuInfoTol);
        return result;
    }

    /**
     * 获取内存使用情况
     *
     * @return
     */
    public MonHardwareMemInfoDtl getMemUsage() throws JSchException {
        jschUtil.connect();
        List<String> memoResult = jschUtil.execCmd(memCommand);
        List<String> serverNameResult = jschUtil.execCmd(serverHostNameCommand);
        return memoAnalyis(memoResult, serverNameResult, jschUtil.getHost());
    }

    /**
     * 内存结果分析
     *
     * @param memoResult
     * @return
     */
    public MonHardwareMemInfoDtl memoAnalyis(List<String> memoResult, List<String> serverNameResult, String ip) {

        if (memoResult == null
                || memoResult.size() != 3
                || serverNameResult == null
                || StringUtils.isEmpty(serverNameResult.get(0))) {
            return new MonHardwareMemInfoDtl();
        }
        String headerStr = memoResult.get(0);
        if (StringUtils.isEmpty(headerStr)) {
            return new MonHardwareMemInfoDtl();
        }
        String[] split = headerStr.split(";");
        Map<String, Integer> filedIndex = ioFieldIndexCaculat(Arrays.asList(split), memoFields());
        String memoInfo = memoResult.get(1);
        String swapInfo = memoResult.get(2);
        String serverName = serverNameResult.get(0);

        if (StringUtils.isEmpty(memoInfo) || StringUtils.isEmpty(swapInfo)) {
            return new MonHardwareMemInfoDtl();
        }

        String[] memoInfoArray = memoInfo.split(";");
        String[] swapInfoArray = swapInfo.split(";");
     /*   int memoBookLen = 6;
        int swapBookLen = 3;
        if (memoInfoArray.length != memoBookLen || swapInfoArray.length != swapBookLen) {
            return new MonHardwareMemInfoDtl();
        }*/
        double memTotal = Double.parseDouble(memoInfoArray[filedIndex.get("total") + 1]) / 1024 / 1024;
        double memFree = Double.parseDouble(memoInfoArray[filedIndex.get("free") + 1]) / 1024 / 1024;
        double memUsed = Double.parseDouble(memoInfoArray[filedIndex.get("used") + 1]) / 1024 / 1024;
        double memoShared = Double.parseDouble(memoInfoArray[filedIndex.get("shared") + 1]) / 1024 / 1024;
        double swapTotal = Double.parseDouble(swapInfoArray[filedIndex.get("total") + 1]) / 1024 / 1024;
        double swapUsed = Double.parseDouble(swapInfoArray[filedIndex.get("used") + 1]) / 1024 / 1024;
        double swapFree = Double.parseDouble(swapInfoArray[filedIndex.get("free") + 1]) / 1024 / 1024;
        double memUsedRate = memUsed / memTotal * 100;
        Integer buffersIndex = filedIndex.get("buffers");
        Integer cachedIndex = filedIndex.get("cached");
        Integer buffCacheIndex = filedIndex.get("buffCacheMap");
        boolean bufferCheck = buffersIndex != null;
        boolean cacheCheck = cachedIndex != null;
        boolean buffCacheIndexFlag = buffCacheIndex != null;
        double cachedAndBuffers;
        if (buffCacheIndexFlag) {
            cachedAndBuffers = Double.parseDouble(memoInfoArray[filedIndex.get("buffCacheMap") + 1]) / 1024 / 1024;
        } else if (bufferCheck && cacheCheck) {
            cachedAndBuffers = (Double.parseDouble(memoInfoArray[4])
                    + Double.parseDouble(memoInfoArray[5])) / 1024 / 1024;
        } else {
            cachedAndBuffers = 0.00d;
        }
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
        monHardwareMemInfoDtl.setServiceIp(ip);
        return monHardwareMemInfoDtl;
    }

    //生成memo的各个字段
    public Map<String, List<String>> memoFields() {
        HashMap<String, List<String>> memoMap = new HashMap<>(1);
        memoMap.put("total", Collections.singletonList("total"));
        memoMap.put("used", Collections.singletonList("used"));
        memoMap.put("free", Collections.singletonList("free"));
        memoMap.put("shared", Collections.singletonList("shared"));
        memoMap.put("buffCacheMap", Collections.singletonList("buff/cache"));
        memoMap.put("available", Collections.singletonList("available"));
        memoMap.put("buffers", Collections.singletonList("buffers"));
        memoMap.put("cached", Collections.singletonList("cached"));
        return memoMap;
    }


    /**
     * 获取io信息
     *
     * @return
     */
    public MonHardwareIoInfo getIoUsage() throws JSchException {
        jschUtil.connect();
        List<String> remoteQueryRes = jschUtil.execCmd(ioCommand);
        List<String> serverNameResult = jschUtil.execCmd(serverHostNameCommand);
        List<String> diskUseList = jschUtil.execCmd(diskUseRateCommand);
        String diskUsedRate = diskUseCalculat(diskUseList);
        if (remoteQueryRes == null || remoteQueryRes.size() <= 0) {
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
    public Map<String, Integer> ioFieldIndexCaculat(List<String> tableHeader, Map<String, List<String>> searchField) {
        Set<String> keySet = searchField.keySet();
        return keySet.stream().collect(Collectors.collectingAndThen(Collectors.toMap(
                key -> key,
                value -> searchField.get(value).stream().filter(tableHeader::contains).mapToInt(tableHeader::indexOf).max().orElse(-1),
                (value1, value2) -> value1 == -1 ? value2 : value1
        ), result -> {
            HashMap<String, Integer> filterMap = new HashMap<>(16);
            result.forEach((key, value) -> {
                if (value != -1) {
                    filterMap.put(key, value);
                }
            });
            return filterMap;
        }));
    }

    //初始化io查询字段集合
    public Map<String, List<String>> ioSearchField() {
        HashMap<String, List<String>> deviceMap = new HashMap<>(1);
        deviceMap.put("Device", Arrays.asList("Device", "Device:"));
        deviceMap.put("r/s", Collections.singletonList("r/s"));
        deviceMap.put("w/s", Collections.singletonList("w/s"));
        deviceMap.put("rkB/s", Collections.singletonList("rkB/s"));
        deviceMap.put("wkB/s", Collections.singletonList("wkB/s"));
        deviceMap.put("r_await", Collections.singletonList("r_await"));
        deviceMap.put("w_await", Collections.singletonList("w_await"));
        deviceMap.put("await", Collections.singletonList("await"));
        return deviceMap;
    }

    //针对前台返回的数据以空格切割后，去除空处理
    public List<List<String>> remoteResTrimAndSplit(List<String> remoteQueryRes) {
        ArrayList<List<String>> result = new ArrayList<>();
        remoteQueryRes.forEach(item -> {
            if (StringUtils.isNotEmpty(item)) {
                String[] split = item.split(" ");
                ArrayList<String> itemList = new ArrayList<>();
                for (String itemValue : split) {
                    if (StringUtils.isNotEmpty(itemValue)) {
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
        Map<String, Integer> fieldIndex = ioFieldIndexCaculat(remoteQueryRes.get(0), ioSearchField());
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
                    || itemList.size() == 0) {
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
        double readTimesInit = Double.compare(readTimes, 0.00d) == 0 ? 1 : readTimes;
        double writeTimesInit = Double.compare(writeTimes, 0.00d) == 0 ? 1 : writeTimes;
        double operateTime = readWait * readTimesInit + writeWait * writeTimesInit;
        double operateTimes = readTimes + writeTimes;
        operateTimes = Double.compare(operateTimes, 0.00d) == 0 ? 1 : operateTimes;
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
    public String eliminatePercentSign(String source) {
        if (StringUtils.isEmpty(source)) {
            return "";
        }
        int index = source.indexOf("%");
        if (index < 0) {
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
        serverNameResult = serverNameResult == null
                || serverNameResult.size() == 0
                || StringUtils.isEmpty(serverNameResult.get(0))
                ? Collections.singletonList("未知") : serverNameResult;

        return cpuDetailedResultSetAnalysis(result, serverNameResult, jschUtil.getHost());
    }

    /**
     * cpu明细结果集分析
     *
     * @param searchResult     cpu查询结果集
     * @param serverNameResult 服务器信息查询结果集
     * @return cpu明细实体类集合
     */
    public List<MonHardwareDiskInfoDtl> cpuDetailedResultSetAnalysis(List<String> searchResult,
                                                                     List<String> serverNameResult,
                                                                     String ip) {
        String tableHeader = searchResult.get(0);
        Date currentTime = new Date();
        Map<String, Integer> requiredFieldIndex = ioFieldIndexCaculat(Arrays.asList(tableHeader.split(";")),
                cpuDetailRequiredFieldGeneration());
        return searchResult.stream().skip(1)
                .map(queryResultString -> queryResultString.split(";"))
                .map(queryResultArray
                        -> new MonHardwareDiskInfoDtl(serverNameResult.get(0),
                        ip,
                        currentTime,
                        queryResultArray[requiredFieldIndex.get("filesSystem")],
                        queryResultArray[requiredFieldIndex.get("totalSize")],
                        queryResultArray[requiredFieldIndex.get("usedSize")],
                        eliminatePercentSign(queryResultArray[requiredFieldIndex.get("usedRate")]),
                        queryResultArray[requiredFieldIndex.get("availSize")],
                        queryResultArray[requiredFieldIndex.get("mountedOn")])).collect(Collectors.toList());

    }

    /**
     * cpu明细必须字段生成
     */
    public Map<String, List<String>> cpuDetailRequiredFieldGeneration() {
        HashMap<String, List<String>> resultMap = new HashMap<>(6);
        resultMap.put("filesSystem", Collections.singletonList("Filesystem"));
        resultMap.put("totalSize", Collections.singletonList("1K-blocks"));
        resultMap.put("usedSize", Collections.singletonList("Used"));
        resultMap.put("availSize", Collections.singletonList("Available"));
        resultMap.put("usedRate", Collections.singletonList("Use%"));
        resultMap.put("mountedOn", Collections.singletonList("Mounted"));
        return resultMap;
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
