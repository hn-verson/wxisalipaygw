package com.nykj.wxisalipaygw.controller;

import org.apache.log4j.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Verson on 2016/4/29.
 */
public class BaseController {
    private final static Logger LOGGER = Logger.getLogger(BaseController.class);
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

    /**
     * 响应客户端
     * @param response
     * @param content
     */
    public void printClient(HttpServletResponse response,String content){
        response.setContentType("application/json;charset=UTF-8");
        try{
            response.getWriter().print(content);
        }catch (IOException ioe){
            LOGGER.error("响应客户端失败:" + ioe);
        }
    }
}