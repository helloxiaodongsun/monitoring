package com.pactera.monitoring.utils.ssh;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSchUtil {

    private static final Logger log = LoggerFactory.getLogger(JSchUtil.class);

    // 设置编码格式
    private String charset = "UTF-8";

    // 用户名
    private String user;

    // 登录密码
    private String passwd;

    // 主机IP
    private String host;

    //端口
    private int port = 22;
    private JSch jsch;
    private Session session;

    /**
     * @param user
     * @param passwd
     * @param host
     * @param port
     */
    public JSchUtil(String user, String passwd, String host, int port) {
        this.user = user;
        this.passwd = passwd;
        this.host = host;
        this.port = port;
    }

    /**
     * @param user
     * @param passwd
     * @param host
     */
    public JSchUtil(String user, String passwd, String host) {
        this.user = user;
        this.passwd = passwd;
        this.host = host;
    }

    /**
     * 连接到指定的IP
     *
     * @throws JSchException
     */
    public void connect() throws JSchException {
        if (session == null || session.isConnected()) {
            jsch = new JSch();
            session = jsch.getSession(user, host, port);
            session.setPassword(passwd);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect(5000);
        }
    }

    /**
     * 关闭连接
     */
    public void disconnect() {
        if (session != null && session.isConnected()) {
            session.disconnect();
        }
    }

    /**
     * 执行一条命令
     */
    public List<String> execCmd(String command) {
        BufferedReader reader = null;
        Channel channel = null;
        InputStream in = null;
        ArrayList<String> result = new ArrayList<>();
        try {
            channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);
            channel.setInputStream(null);
            ((ChannelExec) channel).setErrStream(System.err);
            channel.connect();
            in = channel.getInputStream();
            reader = new BufferedReader(new InputStreamReader(in,
                    Charset.forName(charset)));
            String buf = null;
            while ((buf = reader.readLine()) != null) {
                result.add(buf);
            }
        } catch (JSchException | IOException e) {
            log.error(e.getMessage(), e);
        } finally {

            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
            if (channel != null) {
                channel.disconnect();
            }
        }
        return result;
    }

    /**
     * 执行一条命令,异常抛出，手动关闭channel，针对多条命令优化
     */
    public Map<String, List<String>> execCmdMultipleCommands(String[] commandArray) {
        if (commandArray == null || commandArray.length <= 0) {
            return new HashMap<>(0);
        }
        HashMap<String, List<String>> resultCollect = new HashMap<>();
        for (String command : commandArray) {
            if(command == null || "".equals(command)){continue;}
            List<String> commandList = execCmd(command);
            resultCollect.put(command, commandList);
        }
        return resultCollect;
    }
    /**
     * 上传文件
     */
   /* public void uploadFile(String local, String remote) throws Exception {
        ChannelSftp channel = null;
        InputStream inputStream = null;
        try {
            channel = (ChannelSftp) session.openChannel("sftp");
            channel.connect(5000);
            inputStream = new FileInputStream(new File(local));
            channel.setInputStream(inputStream);
            channel.put(inputStream, remote, new ProgressMonitor());
        } catch (Exception e) {
            throw e;
        } finally {
            if (channel != null) {
                channel.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }
*/
    /**
     * 下载文件
     */
   /* public void downloadFile(String remote, String local) throws Exception {
        ChannelSftp channel = null;
        OutputStream outputStream = null;
        try {
            channel = (ChannelSftp) session.openChannel("sftp");
            channel.connect(5000);
            outputStream = new FileOutputStream(new File(local));
            channel.get(remote, outputStream, new ProgressMonitor());
            outputStream.flush();
        } catch (Exception e) {
            throw e;
        } finally {
            if (channel != null) {
                channel.disconnect();
            }
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }
}*/

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
