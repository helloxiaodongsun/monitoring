package com.pactera.monitoring.utils.ssh;

import com.jcraft.jsch.JSchException;
import com.pactera.monitoring.entity.MonHardwareIoInfo;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * io信息查询服务器
 *
 * @author 84483
 */
public class IoInformationFromServer extends AbstractAssessRemoteServer<MonHardwareIoInfo> {
    /**
     * io信息
     */
    private String ioCommand = "iostat -dx |awk '!/^$|Linux/ {print $0}'";
    /**
     * 服务名
     */
    private String serverHostNameCommand = "hostname";
    /**
     * 查看磁盘使用率
     */
    private String diskUseRateCommand = "df |awk 'NR !=1 {print $2 \";\" $3}'";

    public IoInformationFromServer(String user, String passwd, String host, int port) {
        super(user, passwd, host, port);
    }

    public IoInformationFromServer(String user, String passwd, String host) {
        super(user, passwd, host);
    }

    /**
     * @param date 数据日期
     * @return 实体对象
     */
    @Override
    public MonHardwareIoInfo getObject(Date date) throws JSchException, IOException {
        jSchUtil.connect();
        List<String> remoteQueryRes = jSchUtil.execCmd(ioCommand);
        List<String> serverNameResult = jSchUtil.execCmd(serverHostNameCommand);
        List<String> diskUseList = jSchUtil.execCmd(diskUseRateCommand);
        String diskUsedRate = diskUseCalculat(diskUseList);
        if (remoteQueryRes == null || remoteQueryRes.size() <= 0) {
            return new MonHardwareIoInfo();
        }
        List<List<String>> remoteQueryResTrim = remoteResTrimAndSplit(remoteQueryRes);
        Map<String, Map<String, String>> parseIoUsageMsg = parseIoUsageMsg(remoteQueryResTrim);
        Map<String, Double> ioUsageCalculation = ioUsageCalculation(parseIoUsageMsg);
        date = date == null ? new Date() : date;
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
        monHardwareIoInfo.setServiceIp(jSchUtil.getHost());
        return monHardwareIoInfo;
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
}
