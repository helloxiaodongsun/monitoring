package com.pactera.monitoring.dao.ds1;

import com.pactera.monitoring.entity.MonHardwareCpuinfoDtl;

public interface MonHardwareCpuinfoDtlMapper {
    int insert(MonHardwareCpuinfoDtl record);

    int insertSelective(MonHardwareCpuinfoDtl record);
}
