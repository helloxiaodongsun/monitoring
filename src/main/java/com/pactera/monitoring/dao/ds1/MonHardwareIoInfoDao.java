package com.pactera.monitoring.dao.ds1;

import com.pactera.monitoring.entity.MonHardwareIoInfo;
import com.pactera.monitoring.entity.SearchBaseEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**io信息dao
 * @author 84483
 */
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

    /**
     * 生成io图表所需数据
     * @param searchBaseEntity
     * @return
     */
    List<MonHardwareIoInfo> ioChartData(@Param("searchBaseEntity") SearchBaseEntity searchBaseEntity);
}
