package com.nykj.wxisalipaygw.controller.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.nykj.wxisalipaygw.entity.alipay.BaseRequest;

public class SecurityInterceptor implements HandlerInterceptor{
    
    @Value("#{config['config.alipay.token']}")
    private String alipayToken;

    @Override
    public void afterCompletion(HttpServletRequest arg0,
            HttpServletResponse arg1, Object arg2, Exception arg3)
            throws Exception {
        
    }

    @Override
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
            Object arg2, ModelAndView arg3) throws Exception {
        
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) throws Exception {
        request.setCharacterEncoding("UTF-8");
        String params = request.getParameter("params");
        boolean flag = false;
        
        if (params != null && !"".equals(params)) {
            BaseRequest baseRequest = JSON.parseObject(request.getParameter("params"), new TypeReference<BaseRequest>(){});
            if (baseRequest != null) {
                if (baseRequest.getToken() != null && baseRequest.getToken().equalsIgnoreCase(alipayToken)) {
                    flag = true;
                }
            }
        }
        
        return flag;
    }

}
