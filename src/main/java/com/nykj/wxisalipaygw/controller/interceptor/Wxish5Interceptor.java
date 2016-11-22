package com.nykj.wxisalipaygw.controller.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.nykj.wxisalipaygw.constants.GlobalConstants;
import com.nykj.wxisalipaygw.constants.ParamNameConstants;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class Wxish5Interceptor implements HandlerInterceptor{
    private final static Logger LOGGER = Logger.getLogger(Wxish5Interceptor.class);

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
        request.setCharacterEncoding(GlobalConstants.CHARSET_UTF8);
        String token = request.getParameter(ParamNameConstants.TOKEN);
        boolean flag = !StringUtils.isEmpty(token) && token.equals(alipayToken);
        if(!flag){
            LOGGER.info("无效的令牌:" + token);
        }
        return flag;
    }

}