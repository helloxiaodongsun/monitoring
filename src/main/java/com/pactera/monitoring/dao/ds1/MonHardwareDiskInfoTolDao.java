package com.pactera.monitoring.dao.ds1;

import com.pactera.monitoring.entity.MonHardwareDiskInfoTol;

/**硬盘汇总dao
 * @author 84483
 */
public interface MonHardwareDiskInfoTolDao {
    int insert(MonHardwareDiskInfoTol record);

    int insertSelective(MonHardwareDiskInfoTol record);
}
