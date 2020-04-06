package com.pactera.monitoring.dao.ds1;

import com.pactera.monitoring.entity.MonHardwareIoInfo;

public interface MonHardwareIoInfoDao {
    int insert(MonHardwareIoInfo record);

    int insertSelective(MonHardwareIoInfo record);
}
