package com.pactera.monitoring.dao.ds1;

import com.pactera.monitoring.entity.MonHardwareCpuInfoDtl;
import com.pactera.monitoring.entity.SearchBaseEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**cpu明细dao
 * @author 84483
 */
public interface MonHardwareCpuInfoDtlDao {
    int insert(MonHardwareCpuInfoDtl record);

    int insertSelective(MonHardwareCpuInfoDtl record);

    /**
     * 根据条件查询cpu明细信息
     *
     * @param searchBaseEntity 搜索实体类
     * @return cpu明细实体类
     */
    List<MonHardwareCpuInfoDtl> selectByCondition(@Param("searchBaseEntity") SearchBaseEntity searchBaseEntity);
}
