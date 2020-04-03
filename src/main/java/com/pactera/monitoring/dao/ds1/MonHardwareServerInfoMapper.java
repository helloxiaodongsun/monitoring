package com.pactera.monitoring.dao.ds1;

import com.pactera.monitoring.entity.MonHardwareServerInfo;

public interface MonHardwareServerInfoMapper {
    int insert(MonHardwareServerInfo record);

    int insertSelective(MonHardwareServerInfo record);
}
