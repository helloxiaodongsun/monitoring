package com.pactera.monitoring.dao.ds1;

import com.pactera.monitoring.entity.MonHardwareIoInfo;

public interface MonHardwareIoInfoMapper {
    int insert(MonHardwareIoInfo record);

    int insertSelective(MonHardwareIoInfo record);
}
