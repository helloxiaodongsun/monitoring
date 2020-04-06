package com.pactera.monitoring.dao.ds1;

import com.pactera.monitoring.entity.MonHardwareDiskInfoTol;

public interface MonHardwareDiskInfoTolDao {
    int insert(MonHardwareDiskInfoTol record);

    int insertSelective(MonHardwareDiskInfoTol record);
}
