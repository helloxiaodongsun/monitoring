package com.pactera.monitoring.utils.ssh;

import com.jcraft.jsch.JSchException;
import com.pactera.monitoring.entity.MonHardwareServerInfo;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * 服务器信息查询服务器
 *
 * @author 84483
 */
public class ServerInformationFromServer extends AbstractAssessRemoteServer<MonHardwareServerInfo> {
    /**
     * 服务器内核名称
     */
    private String serverCoreCommand = "uname -r ";
    /**
     * 判断服务器是ubantu 还是centos 、redhat
     */
    private String determineServerType = "find /etc -name redhat-release |wc -l";
    /**
     * centos 、redhat查看发行版本号
     */
    private String serverInfoCommandCentos = "cat /etc/redhat-release";

    /**
     * ubantu查看发行版本号
     */
    private String serverInfoCommandUbntu = "cat /etc/issue.net";

    /**
     * 服务名
     */
    private String serverHostNameCommand = "hostname";

    public ServerInformationFromServer(String user, String passwd, String host, int port) {
        super(user, passwd, host, port);
    }

    public ServerInformationFromServer(String user, String passwd, String host) {
        super(user, passwd, host);
    }


    /**
     * @param date 数据日期
     * @return 实体对象
     */
    @Override
    public MonHardwareServerInfo getObject(Date date) throws JSchException, IOException {
        MonHardwareServerInfo monHardwareServerInfo = new MonHardwareServerInfo();
        jSchUtil.connect();
        List<String> sercerCoreResult = jSchUtil.execCmd(serverCoreCommand);
        List<String> determinServerTypeList = jSchUtil.execCmd(determineServerType);
        List<String> serverInfoResult;
        if (determinServerTypeList == null
                || determinServerTypeList.size() <= 0
                || "1".equals(determinServerTypeList.get(0))) {
            //服务器是centos、redhat
            serverInfoResult = jSchUtil.execCmd(serverInfoCommandCentos);
        } else {
            //服务器是ubantu
            serverInfoResult = jSchUtil.execCmd(serverInfoCommandUbntu);
        }
        List<String> serverHostNameResult = jSchUtil.execCmd(serverHostNameCommand);
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
        monHardwareServerInfo.setDataDt(date);
        return monHardwareServerInfo;
    }
}
