package com.nykj.wxisalipaygw.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.request.AlipayUserUserinfoShareRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserUserinfoShareResponse;
import com.nykj.wxisalipaygw.constants.AlipayServiceEnvConstants;
import com.nykj.wxisalipaygw.factory.AlipayAPIClientFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by cq on 4/21/16.
 */
@Controller
public class Order {

    @RequestMapping(value = "/order", method = RequestMethod.GET)
    @ResponseBody
    public String doGet(
            @RequestParam(value = "source", defaultValue = "") String source,
            @RequestParam(value = "auth_code", defaultValue = "") String authCode
    ) {
        try {
            AlipaySystemOauthTokenRequest oauthTokenRequest = new AlipaySystemOauthTokenRequest();
            oauthTokenRequest.setCode(authCode);
            oauthTokenRequest.setGrantType(AlipayServiceEnvConstants.GRANT_TYPE);
            AlipayClient alipayClient = AlipayAPIClientFactory.getAlipayClient();
            AlipaySystemOauthTokenResponse oauthTokenResponse = alipayClient
                    .execute(oauthTokenRequest);
            if (null == oauthTokenResponse || !oauthTokenResponse.isSuccess()) {
                return "get openid failed!";
            }
            String openId = oauthTokenResponse.getAlipayUserId();
            AlipayUserUserinfoShareRequest userinfoShareRequest = new AlipayUserUserinfoShareRequest();
            AlipayUserUserinfoShareResponse userinfoShareResponse = alipayClient.execute(
                    userinfoShareRequest, oauthTokenResponse.getAccessToken());
            if (null == userinfoShareRequest || !userinfoShareResponse.isSuccess()) {
                return "get user info failed!";
            }
            System.out.println(userinfoShareResponse.getBody());
            return "success!";
        } catch (AlipayApiException e) {
            return "exception!";
        }
    }
}
