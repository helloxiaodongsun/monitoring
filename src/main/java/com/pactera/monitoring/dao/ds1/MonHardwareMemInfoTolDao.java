package com.pactera.monitoring.dao.ds1;

import com.pactera.monitoring.entity.MonHardwareMemInfoTol;

public interface MonHardwareMemInfoTolDao {
    int insert(MonHardwareMemInfoTol record);

    int insertSelective(MonHardwareMemInfoTol record);
}
