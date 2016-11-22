package com.nykj.wxisalipaygw.model.alipay;

import com.nykj.wxisalipaygw.entity.alipay.UnitLink;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by Verson on 2016/4/28.
 */
@Repository
public interface UnitLinkMapper {
    UnitLink findUnitLinkByUnitId(@Param("unit_id") String unitId);
}
