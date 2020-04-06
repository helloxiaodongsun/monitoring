package com.pactera.monitoring.dao.ds1;

import com.pactera.monitoring.entity.MonHardwareCpuInfoTol;

public interface MonHardwareCpuInfoTolDao {
    int insert(MonHardwareCpuInfoTol record);

    int insertSelective(MonHardwareCpuInfoTol record);
}
