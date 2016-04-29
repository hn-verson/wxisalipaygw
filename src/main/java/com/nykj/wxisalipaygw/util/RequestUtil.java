/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package com.nykj.wxisalipaygw.util;

import com.alipay.api.internal.util.StringUtils;
import com.nykj.wxisalipaygw.dao.alipay.UnitLinkMapper;
import com.nykj.wxisalipaygw.entity.alipay.UnitLink;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 解析HttpServletRequest参数
 * 
 * @author taixu.zqq
 * @version $Id: RequestUtil.java, v 0.1 2014年7月23日 上午10:48:10 taixu.zqq Exp $
 */
@Component
@Scope("singleton")
public class RequestUtil {

    @Autowired
    private UnitLinkMapper unitLinkMapper;

    /**
     * 获取所有request请求参数key-value
     * 
     * @param request
     * @return
     */
    public Map<String, String> getRequestParams(HttpServletRequest request){
        
        Map<String, String> params = new HashMap<String, String>();
        if(null != request){
            Set<String> paramsKey = request.getParameterMap().keySet();
            for(String key : paramsKey){
                params.put(key, request.getParameter(key));
            }
        }
        return params;
    }

    /**
     * 获取项目应用目录
     * @param request
     * @return
     */
    public String getProjectBasePath(HttpServletRequest request){
        if(request == null){
            return null;
        }
        return request.getScheme() + "://" + request.getServerName() + request.getContextPath();
    }

    /**
     * 构建支付宝回调请求体，方便简化下层逻辑的处理
     * @param request
     * @return
     */
    public JSONObject buildAlipayBizBody(HttpServletRequest request) throws Exception{
        if(request == null){
            throw new NullPointerException("请求体【request】为空，无法构建业务体");
        }
        Map<String,String> params = getRequestParams(request);

        //获取服务信息
        String service = params.get("service");
        if (StringUtils.isEmpty(service)) {
            throw new Exception("无法取得服务名");
        }

        //获取渠道[微信,支付宝]信息
        String channel = params.get("channel");
        if (StringUtils.isEmpty(channel)) {
            throw new Exception("无法取得渠道标识");
        }

        //获取医院信息
        String unitId = params.get("unit_id");
        if (StringUtils.isEmpty(service)) {
            throw new Exception("无法取得医院ID");
        }

        //获取内容信息
        String bizContent = params.get("biz_content");
        if (StringUtils.isEmpty(bizContent)) {
            throw new Exception("无法取得业务内容信息");
        }

        //获取工程根目录
        String projectBasePath = getProjectBasePath(request);
        if (StringUtils.isEmpty(projectBasePath)) {
            throw new Exception("无法应用访问目录");
        }

        JSONObject alipayBizBody = (JSONObject) new XMLSerializer().read(bizContent);
        alipayBizBody.put("service",service);
        alipayBizBody.put("unit_id",unitId);
        alipayBizBody.put("channel",channel);
        alipayBizBody.put("project_base_path",projectBasePath);

        //将医院服务窗配置信息整合至业务消息体
        UnitLink unitLink = unitLinkMapper.findUnitLinkByUnitId(unitId);
        alipayBizBody.put("unit_link",unitLink);

        return alipayBizBody;
    }

}