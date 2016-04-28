package com.nykj.wxisalipaygw.service.alipay;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alipay.api.response.AlipayUserUserinfoShareResponse;
import com.nykj.wxisalipaygw.util.AlipayUtil;

@Service
public class UserInfoService {

    private static final Logger LOGGER = Logger.getLogger(UserInfoService.class);
    
    public String getAlipayOpenId(String unitId, String authCode) {
        LOGGER.info("收到支付宝转换openId请求：unitId=" + unitId + " authCode=" + authCode);
        
        String openId = AlipayUtil.getAlipayOpenId(unitId, authCode);
        if (openId != null && !"".equals(openId)) {
            LOGGER.debug("openId转换成功！");
            return openId;
        } else {
            LOGGER.debug("openId转换失败：unitId=" + unitId + " authCode=" + authCode);
            return null;
        }
    }
    
    public AlipayUserUserinfoShareResponse getAlipayUserInfo(String unitId, String authCode) {
        LOGGER.info("收到支付宝获取userInfo请求：unitId=" + unitId + " authCode=" + authCode);
        
        AlipayUserUserinfoShareResponse userinfoShareResponse = AlipayUtil.getAlipayUserInfo(unitId, authCode);
        if (userinfoShareResponse != null) {
            LOGGER.debug("userInfo获取成功！");
            return userinfoShareResponse;
        } else {
            LOGGER.debug("userInfo获取失败：unitId=" + unitId + " authCode=" + authCode);
            return null;
        }
    }
}
