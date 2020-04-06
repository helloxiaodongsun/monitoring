package com.pactera.monitoring.dao.ds1;

import com.pactera.monitoring.entity.MonHardwareCpuInfoDtl;

public interface MonHardwareCpuInfoDtlDao {
    int insert(MonHardwareCpuInfoDtl record);

    int insertSelective(MonHardwareCpuInfoDtl record);
}
