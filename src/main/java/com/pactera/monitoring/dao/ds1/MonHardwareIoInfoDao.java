package com.pactera.monitoring.dao.ds1;

import com.pactera.monitoring.entity.MonHardwareIoInfo;
import com.pactera.monitoring.entity.SearchBaseEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MonHardwareIoInfoDao {
    int insert(MonHardwareIoInfo record);

    int insertSelective(MonHardwareIoInfo record);

    /**
     * 根据条件查询服务器IO明细信息
     *
     * @param searchBaseEntity 查询条件实体类
     * @return IO明细信息
     */
    List<MonHardwareIoInfo> selectByCondition(@Param("searchBaseEntity") SearchBaseEntity searchBaseEntity);
}
