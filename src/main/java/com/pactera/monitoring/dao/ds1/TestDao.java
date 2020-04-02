package com.pactera.monitoring.dao.ds1;

import com.pactera.monitoring.entity.Test;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface TestDao {
    Test findByName(@Param("name") String n);
}
