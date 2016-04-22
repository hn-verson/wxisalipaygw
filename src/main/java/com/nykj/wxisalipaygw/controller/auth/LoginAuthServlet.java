/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package com.nykj.wxisalipaygw.controller.auth;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.request.AlipayUserUserinfoShareRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserUserinfoShareResponse;
import com.nykj.wxisalipaygw.constants.AlipayServiceEnvConstants;
import com.nykj.wxisalipaygw.factory.AlipayAPIClientFactory;
import com.nykj.wxisalipaygw.util.RequestUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 用户信息共享（获取用户信息）
 * 
 * @author taixu.zqq
 * @version $Id: LoginAuthServlet.java, v 0.1 2014年7月25日 下午5:13:03 taixu.zqq Exp $
 */
@Controller
public class LoginAuthServlet {

    /**  */
    private static final long serialVersionUID = -6807693807426739985L;

    /**
     * 业务处理
     * @see HttpServlet#doGet(HttpServletRequest, HttpServletResponse)
     */
    @RequestMapping(value = "/loginAuth", produces = "text/html")
    public void doGet(HttpServletRequest request, HttpServletResponse response)
                                                                                  throws ServletException,
                                                                                  IOException {
        //1. 解析请求参数
        Map<String, String> params = RequestUtil.getRequestParams(request);
        //2. 获得authCode
        String authCode = params.get("auth_code");

        try {
            //3. 利用authCode获得authToken
            AlipaySystemOauthTokenRequest oauthTokenRequest = new AlipaySystemOauthTokenRequest();
            oauthTokenRequest.setCode(authCode);
            oauthTokenRequest.setGrantType(AlipayServiceEnvConstants.GRANT_TYPE);
            AlipayClient alipayClient = AlipayAPIClientFactory.getAlipayClient();
            AlipaySystemOauthTokenResponse oauthTokenResponse = alipayClient
                .execute(oauthTokenRequest);

            //成功获得authToken
            if (null != oauthTokenResponse && oauthTokenResponse.isSuccess()) {

                //4. 利用authToken获取用户信息
                AlipayUserUserinfoShareRequest userinfoShareRequest = new AlipayUserUserinfoShareRequest();
                AlipayUserUserinfoShareResponse userinfoShareResponse = alipayClient.execute(
                    userinfoShareRequest, oauthTokenResponse.getAccessToken());

                //成功获得用户信息
                if (null != userinfoShareResponse && userinfoShareResponse.isSuccess()) {
                    //这里仅是简单打印， 请开发者按实际情况自行进行处理
                    System.out.println("获取用户信息成功：" + userinfoShareResponse.getBody());

                } else {
                    //这里仅是简单打印， 请开发者按实际情况自行进行处理
                    System.out.println("获取用户信息失败");

                }
            } else {
                //这里仅是简单打印， 请开发者按实际情况自行进行处理
                System.out.println("authCode换取authToken失败");
            }
        } catch (AlipayApiException alipayApiException) {
            //自行处理异常
            alipayApiException.printStackTrace();
        }
    }

}
