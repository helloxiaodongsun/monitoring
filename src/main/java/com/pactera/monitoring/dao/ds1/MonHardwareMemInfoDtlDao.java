package com.pactera.monitoring.dao.ds1;

import com.pactera.monitoring.entity.MonHardwareMemInfoDtl;

public interface MonHardwareMemInfoDtlDao {
    int insert(MonHardwareMemInfoDtl record);

    int insertSelective(MonHardwareMemInfoDtl record);
}
