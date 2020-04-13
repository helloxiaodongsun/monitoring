package com.pactera.monitoring.dao.ds1;

import com.pactera.monitoring.entity.MonHardwareDiskInfoDtl;
import com.pactera.monitoring.entity.SearchBaseEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 84483
 */
public interface MonHardwareDiskInfoDtlDao {
    int insert(MonHardwareDiskInfoDtl record);

    int insertSelective(MonHardwareDiskInfoDtl record);

    /**
     * 批量插入硬盘明细信息
     *
     * @param monHardwareDiskInfoDtlList 硬盘明细集合
     * @return 1
     */
    int insertSelectiveBatch(@Param("monHardwareDiskInfoDtlList") List<MonHardwareDiskInfoDtl> monHardwareDiskInfoDtlList);

    /**
     * 根据条件查询磁盘明细信息
     *
     * @param searchBaseEntity 条件实体类
     * @return 磁盘明细信息
     */
    List<MonHardwareDiskInfoDtl> selectByCondition(@Param("searchBaseEntity") SearchBaseEntity searchBaseEntity);

}
