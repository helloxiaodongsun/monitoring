package com.pactera.monitoring.dao.ds1;

import com.pactera.monitoring.entity.monHardwareIoInfo;

public interface monHardwareIoInfoMapper {
    int insert(monHardwareIoInfo record);

    int insertSelective(monHardwareIoInfo record);
}