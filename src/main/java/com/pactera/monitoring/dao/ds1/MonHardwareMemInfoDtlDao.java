package com.pactera.monitoring.dao.ds1;

import com.pactera.monitoring.entity.MonHardwareMemInfoDtl;
import com.pactera.monitoring.entity.SearchBaseEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**内存信息明细dao
 * @author 84483
 */
public interface MonHardwareMemInfoDtlDao {
    int insert(MonHardwareMemInfoDtl record);

    int insertSelective(MonHardwareMemInfoDtl record);

    /**
     * 根据条件查询服务器内存明细信息
     *
     * @param searchBaseEntity 查询条件实体类
     * @return 内存明细信息
     */
    List<MonHardwareMemInfoDtl> selectByCondition(@Param("searchBaseEntity") SearchBaseEntity searchBaseEntity);
}
