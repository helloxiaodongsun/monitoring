package com.pactera.monitoring.utils.ssh;

import com.jcraft.jsch.JSchException;
import com.pactera.monitoring.entity.MonHardwareDiskInfoDtl;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**硬盘明细信息查询服务器
 * @author 84483
 */
public class DiskDtlInformationFromServer extends AbstractAssessRemoteServer<MonHardwareDiskInfoDtl>{
    /**
     * 查看硬盘明细
     */
    private String diskCommandDtl = "df|awk 'BEGIN{FS=\" \";OFS=\";\"} {NF=NF;print $0}'";
    /**
     * 服务名
     */
    private String serverHostNameCommand = "hostname";
    public DiskDtlInformationFromServer(String user, String passwd, String host, int port) {
        super(user, passwd, host, port);
    }

    public DiskDtlInformationFromServer(String user, String passwd, String host) {
        super(user, passwd, host);
    }

    /**
     * @param date 数据日期
     * @return 实体对象集合
     */
    @Override
    public List<MonHardwareDiskInfoDtl> getCollection(Date date) throws JSchException, IOException {
        jSchUtil.connect();
        List<String> result = jSchUtil.execCmd(diskCommandDtl);
        List<String> serverNameResult = jSchUtil.execCmd(serverHostNameCommand);
        ArrayList<MonHardwareDiskInfoDtl> monHardwareDiskInfoDtlList = new ArrayList<>();
        if (result == null || result.size() <= 0) {
            return monHardwareDiskInfoDtlList;
        }
        serverNameResult = serverNameResult == null
                || serverNameResult.size() == 0
                || StringUtils.isEmpty(serverNameResult.get(0))
                ? Collections.singletonList("未知") : serverNameResult;

        return cpuDetailedResultSetAnalysis(result, serverNameResult, jSchUtil.getHost());
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
}
