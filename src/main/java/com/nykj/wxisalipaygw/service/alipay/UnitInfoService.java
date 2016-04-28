package com.nykj.wxisalipaygw.service.alipay;

import com.nykj.wxisalipaygw.dao.alipay.UnitInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Verson on 2016/4/27.
 */
@Service
public class UnitInfoService {
    @Autowired
    private UnitInfoMapper unitInfoMapper;

    public String findUnitNameByUnitId(String unitId){
        return unitInfoMapper.findUnitNameByUnitId(unitId);
    }
}
