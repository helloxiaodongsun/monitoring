package com.pactera.monitoring.dao.ds1;

import com.pactera.monitoring.entity.MonHardwareMeminfoDtl;

public interface MonHardwareMeminfoDtlMapper {
    int insert(MonHardwareMeminfoDtl record);

    int insertSelective(MonHardwareMeminfoDtl record);
}
