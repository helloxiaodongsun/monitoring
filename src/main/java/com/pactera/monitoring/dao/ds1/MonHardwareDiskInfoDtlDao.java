package com.pactera.monitoring.dao.ds1;

import com.pactera.monitoring.entity.MonHardwareDiskInfoDtl;

public interface MonHardwareDiskInfoDtlDao {
    int insert(MonHardwareDiskInfoDtl record);

    int insertSelective(MonHardwareDiskInfoDtl record);
}
