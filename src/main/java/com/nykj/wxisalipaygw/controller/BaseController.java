package com.nykj.wxisalipaygw.controller;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Verson on 2016/4/29.
 */
public class BaseController {
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
}