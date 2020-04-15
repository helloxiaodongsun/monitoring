package com.pactera.monitoring.utils.ssh;

import com.jcraft.jsch.JSchException;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author 84483
 */
public abstract class AbstractAssessRemoteServer<T> {
    DecimalFormat df = new DecimalFormat("0.000");
    JSchUtil jSchUtil = null;
    public AbstractAssessRemoteServer(String user, String passwd, String host, int port){
        jSchUtil=new JSchUtil(user, passwd, host, port);
    }
    public AbstractAssessRemoteServer(String user, String passwd, String host){
        jSchUtil=new JSchUtil(user, passwd, host, 22);
    }


    /**
     * @param date 数据日期
     * @return 实体对象集合
     */
    public List<T> getCollection(Date date) throws JSchException, IOException {
        return null;
    }

    /**
     * @param date 数据日期
     * @return 实体对象
     */
    public T getObject(Date date) throws JSchException, IOException {
        return null;
    }

    /**
     * 关闭链接
     */
    public void close() {
        if(jSchUtil !=null){
            jSchUtil.disconnect();
        }
    }
    /**
     * 返回的表头跟预设数据对比，计算出预设数据在返回结果中的索引值
     *
     * @param tableHeader 表头数据
     * @return 需要的字段
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

    /**
     * 消除字符串上的%
     * @param source
     * @return
     */
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
}

