package com.pactera.monitoring.utils.ssh;

import com.jcraft.jsch.JSchException;
import com.pactera.monitoring.entity.MonHardwareDiskInfoTol;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**硬盘汇总信息查询服务器
 * @author 84483
 */
public class DiskTolInformationFromServer extends AbstractAssessRemoteServer<MonHardwareDiskInfoTol>{
    /**
     * 查看硬盘汇总
     */
    private String diskCommandTol = "df --total|awk '/total/ {print $2,$3,$4,$5}'";
    /**
     * 服务名
     */
    private String serverHostNameCommand = "hostname";
    public DiskTolInformationFromServer(String user, String passwd, String host, int port) {
        super(user, passwd, host, port);
    }

    public DiskTolInformationFromServer(String user, String passwd, String host) {
        super(user, passwd, host);
    }

    /**
     * @param date 数据日期
     * @return 实体对象
     */
    @Override
    public MonHardwareDiskInfoTol getObject(Date date) throws JSchException, IOException {
        jSchUtil.connect();
        List<String> result = jSchUtil.execCmd(diskCommandTol);
        List<String> serverNameResult = jSchUtil.execCmd(serverHostNameCommand);
        MonHardwareDiskInfoTol monHardwareDiskInfoTol = new MonHardwareDiskInfoTol();
        date = date == null ? new Date() : date;
        if (result != null && result.size() > 0) {
            String diskInfo = result.get(0);
            String[] diskInfoArray = diskInfo.split(" ");
            monHardwareDiskInfoTol.setDiskTotalSize(diskInfoArray[0]);
            monHardwareDiskInfoTol.setDiskUsedSize(diskInfoArray[1]);
            monHardwareDiskInfoTol.setDiskAvailSize(diskInfoArray[2]);
            monHardwareDiskInfoTol.setDiskUsedRate(eliminatePercentSign(diskInfoArray[3]));
            monHardwareDiskInfoTol.setDataDt(date);
            monHardwareDiskInfoTol.setServiceIp(jSchUtil.getHost());
            if (serverNameResult != null
                    && serverNameResult.size() > 0
                    && StringUtils.isNotEmpty(serverNameResult.get(0))) {
                monHardwareDiskInfoTol.setServiceNm(serverNameResult.get(0));
            }
        }
        return monHardwareDiskInfoTol;
    }
}
