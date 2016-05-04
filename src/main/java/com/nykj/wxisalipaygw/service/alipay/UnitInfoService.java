package com.nykj.wxisalipaygw.service.alipay;

import com.nykj.wxisalipaygw.model.alipay.UnitInfoMapper;
import com.nykj.wxisalipaygw.model.alipay.UnitLinkMapper;
import com.nykj.wxisalipaygw.entity.alipay.UnitLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Verson on 2016/4/27.
 */
@Service
public class UnitInfoService {
    @Autowired
    private UnitInfoMapper unitInfoMapper;

    @Autowired
    private UnitLinkMapper unitLinkMapper;

    public String findUnitNameByUnitId(String unitId){
        return unitInfoMapper.findUnitNameByUnitId(unitId);
    }

    public UnitLink findUnitLinkByUnitId(String unitId){
        return unitLinkMapper.findUnitLinkByUnitId(unitId);
    }

}