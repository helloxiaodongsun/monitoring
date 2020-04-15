package com.pactera.monitoring.utils.ssh;

import com.jcraft.jsch.JSchException;
import com.pactera.monitoring.entity.MonHardwareMemInfoDtl;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 内存明细信息查询服务器
 *
 * @author 84483
 */
public class MemoDtlInformationFromServer extends AbstractAssessRemoteServer<MonHardwareMemInfoDtl> {
    /**
     * 内存明细信息查询
     */
    private String memCommand = "free|grep -v 'buffers/cache'|awk 'BEGIN{FS=\" \";OFS=\";\"} {NF=NF;print $0}'";
    /**
     * 服务名
     */
    private String serverHostNameCommand = "hostname";

    public MemoDtlInformationFromServer(String user, String passwd, String host, int port) {
        super(user, passwd, host, port);
    }

    public MemoDtlInformationFromServer(String user, String passwd, String host) {
        super(user, passwd, host);
    }

    /**
     * @param date 数据日期
     * @return 实体对象
     */
    @Override
    public MonHardwareMemInfoDtl getObject(Date date) throws JSchException, IOException {
        jSchUtil.connect();
        List<String> memoResult = jSchUtil.execCmd(memCommand);
        List<String> serverNameResult = jSchUtil.execCmd(serverHostNameCommand);
        return memoAnalyis(memoResult, serverNameResult, jSchUtil.getHost(), date);
    }

    /**
     * 内存结果分析
     *
     * @param memoResult 内存信息查询结果
     * @return 内存对象
     */
    public MonHardwareMemInfoDtl memoAnalyis(List<String> memoResult,
                                             List<String> serverNameResult,
                                             String ip,
                                             Date date) {

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
        date = date == null ? new Date() : date;
        if (StringUtils.isEmpty(memoInfo) || StringUtils.isEmpty(swapInfo)) {
            return new MonHardwareMemInfoDtl();
        }

        String[] memoInfoArray = memoInfo.split(";");
        String[] swapInfoArray = swapInfo.split(";");
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
        monHardwareMemInfoDtl.setDataDt(date);
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
}
