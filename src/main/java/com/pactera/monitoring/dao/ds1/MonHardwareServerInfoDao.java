package com.pactera.monitoring.dao.ds1;

import com.pactera.monitoring.entity.MonHardwareServerInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MonHardwareServerInfoDao {
    int insert(MonHardwareServerInfo record);

    int insertSelective(MonHardwareServerInfo record);

    /**
     * 根据ip查询服务器信息
     * @param ip
     * @return
     */
    MonHardwareServerInfo findByIp(@Param("ip") String ip);
}
