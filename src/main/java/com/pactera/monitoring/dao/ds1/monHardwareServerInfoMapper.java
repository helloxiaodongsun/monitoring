package com.pactera.monitoring.dao.ds1;

import com.pactera.monitoring.entity.monHardwareServerInfo;

public interface monHardwareServerInfoMapper {
    int insert(monHardwareServerInfo record);

    int insertSelective(monHardwareServerInfo record);
}