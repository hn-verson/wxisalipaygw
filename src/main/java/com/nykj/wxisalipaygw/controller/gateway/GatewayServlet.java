/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package com.nykj.wxisalipaygw.controller.gateway;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.nykj.wxisalipaygw.constants.AlipayServiceEnvConstants;
import com.nykj.wxisalipaygw.dispatcher.Dispatcher;
import com.nykj.wxisalipaygw.executor.ActionExecutor;
import com.nykj.wxisalipaygw.util.LogUtil;
import com.nykj.wxisalipaygw.util.RequestUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 开发者网关，支付宝所有主动和开发者的交互会经过此网关进入开发者系统
 *
 * @author taixu.zqq
 * @version $Id: GatewayServlet.java, v 0.1 2014年7月22日 下午5:59:55 taixu.zqq Exp $
 */
@Controller
public class GatewayServlet {

    /**
     *
     */
    private static final long serialVersionUID = 1210436705188940602L;

    /**
     * @see HttpServlet#doGet(HttpServletRequest, HttpServletResponse)
     */
    @RequestMapping(value = "/gateway", produces = "text/xml;charset=GBK", method = RequestMethod.GET)
    @ResponseBody
    public String doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
            IOException {
        return this.doPost(req, resp);
    }

    /**
     * 网关处理
     *
     * @see HttpServlet#doPost(HttpServletRequest, HttpServletResponse)
     */
    @RequestMapping(value = "/gateway", produces = "text/xml;charset=GBK", method = RequestMethod.POST)
    @ResponseBody
    public String doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException,
            IOException {
        //支付宝响应消息  
        String responseMsg = "";

        //1. 解析请求参数
        Map<String, String> params = RequestUtil.getRequestParams(request);

        //打印本次请求日志，开发者自行决定是否需要
        LogUtil.log("支付宝请求串", params.toString());

        try {
            //2. 验证签名
            this.verifySign(params);

            //3. 获取业务执行器   根据请求中的 service, msgType, eventType, actionParam 确定执行器
            ActionExecutor executor = Dispatcher.getExecutor(params);

            //4. 执行业务逻辑
            responseMsg = executor.execute();

        } catch (AlipayApiException alipayApiException) {
            //开发者可以根据异常自行进行处理
            alipayApiException.printStackTrace();

        } catch (Exception exception) {
            //开发者可以根据异常自行进行处理
            exception.printStackTrace();

        } finally {
            //5. 响应结果加签及返回
            try {
                //对响应内容加签
                responseMsg = AlipaySignature.encryptAndSign(responseMsg,
                        AlipayServiceEnvConstants.ALIPAY_PUBLIC_KEY,
                        AlipayServiceEnvConstants.PRIVATE_KEY, AlipayServiceEnvConstants.CHARSET,
                        false, true);

                //开发者自行决定是否要记录，视自己需求
                LogUtil.log("开发者响应串", responseMsg);

            } catch (AlipayApiException alipayApiException) {
                //开发者可以根据异常自行进行处理
                alipayApiException.printStackTrace();
            }
        }
//        return "true";
        return responseMsg;
    }

    /**
     * 验签
     *
     * @param
     * @return
     */
    private void verifySign(Map<String, String> params) throws AlipayApiException {

        if (!AlipaySignature.rsaCheckV2(params, AlipayServiceEnvConstants.ALIPAY_PUBLIC_KEY,
                AlipayServiceEnvConstants.SIGN_CHARSET)) {

            throw new AlipayApiException("verify sign fail.");
        }
    }

}
