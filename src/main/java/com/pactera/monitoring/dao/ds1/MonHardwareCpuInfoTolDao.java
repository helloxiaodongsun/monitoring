package com.pactera.monitoring.dao.ds1;

import com.pactera.monitoring.entity.MonHardwareCpuInfoTol;
/**cpu汇总dao
 * @author 84483
 */
public interface MonHardwareCpuInfoTolDao {
    int insert(MonHardwareCpuInfoTol record);

    int insertSelective(MonHardwareCpuInfoTol record);
}
