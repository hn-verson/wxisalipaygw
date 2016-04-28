package com.nykj.wxisalipaygw.dao.alipay;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by Verson on 2016/4/27.
 */
@Repository
public interface UnitInfoMapper {
    String findUnitNameByUnitId(@Param("unit_id") String unitId);
}
