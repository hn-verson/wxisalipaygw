package com.nykj.wxisalipaygw.controller.alipay;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alipay.api.response.AlipayUserUserinfoShareResponse;
import com.nykj.wxisalipaygw.entity.alipay.BaseRequest;
import com.nykj.wxisalipaygw.entity.alipay.BaseResponse;
import com.nykj.wxisalipaygw.service.alipay.UserInfoService;
import com.nykj.wxisalipaygw.util.UtilDate;

@Controller
@RequestMapping("/alipay")
public class UserInfoController {

    private static final Logger LOGGER = Logger.getLogger(UserInfoController.class);
    
    @Autowired
    private UserInfoService userInfoService;
    
    @RequestMapping(value = "/openId", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String getAlipayOpenId(HttpServletRequest request) {
        BaseRequest baseRequest = new BaseRequest();
        BaseResponse baseResponse = new BaseResponse();
        try {
            if (validRequest(request)) {
                baseRequest = JSON.parseObject(request.getParameter("params"), new TypeReference<BaseRequest>() {});
                String unitId = baseRequest.getData().get("unitId").toString();
                String authCode = baseRequest.getData().get("authCode").toString();

                String openId = userInfoService.getAlipayOpenId(unitId, authCode);
                Map<String, Object> data = new HashMap<String, Object>();
                data.put("openId", openId);
                
                baseResponse.setMessage("转换成功");
                baseResponse.setData(data);
                baseResponse.setTime(UtilDate.getDateFormatter());
            } else {
                baseResponse.setMessage("参数错误");
                baseResponse.setTime(UtilDate.getDateFormatter());
            }
        } catch (Exception e) {
            LOGGER.error("支付宝转换openId异常：" + e);
        }
        return JSON.toJSONString(baseResponse);
    }
    
    @RequestMapping(value = "/userInfo", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String getAlipayUserInfo(HttpServletRequest request) {
        BaseRequest baseRequest = new BaseRequest();
        BaseResponse baseResponse = new BaseResponse();
        try {
            if (validRequest(request)) {
                baseRequest = JSON.parseObject(request.getParameter("params"), new TypeReference<BaseRequest>() {});
                String unitId = baseRequest.getData().get("unitId").toString();
                String authCode = baseRequest.getData().get("authCode").toString();
                
                AlipayUserUserinfoShareResponse userinfoShareResponse = userInfoService.getAlipayUserInfo(unitId, authCode);
                baseResponse.setMessage("获取成功");
                baseResponse.setData(userinfoShareResponse);
                baseResponse.setTime(UtilDate.getDateFormatter());
            } else {
                baseResponse.setMessage("参数错误");
                baseResponse.setTime(UtilDate.getDateFormatter());
            }
        } catch (Exception e) {
            LOGGER.error("支付宝获取用户信息异常：" + e);
        }
        return JSON.toJSONString(baseResponse);
    }
    
    private boolean validRequest(HttpServletRequest request) {
        boolean flag = false;
        BaseRequest baseRequest = null;
        String params = request.getParameter("params");
        
        if (params != null && !"".equals(params)) {
            baseRequest = JSON.parseObject(request.getParameter("params"), new TypeReference<BaseRequest>(){});
            if (baseRequest != null) {
                if (baseRequest.getData().get("unitId") != null && baseRequest.getData().get("authCode") != null) {
                    flag = true;
                }
            }
        }
        
        return flag;
    }
}
