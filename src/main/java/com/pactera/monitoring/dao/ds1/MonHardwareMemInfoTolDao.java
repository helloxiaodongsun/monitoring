package com.pactera.monitoring.dao.ds1;

import com.pactera.monitoring.entity.MonHardwareMemInfoTol;

/**内存信息汇总dao
 * @author 84483
 */
public interface MonHardwareMemInfoTolDao {
    int insert(MonHardwareMemInfoTol record);

    int insertSelective(MonHardwareMemInfoTol record);
}
