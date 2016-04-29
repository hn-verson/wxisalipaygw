/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package com.nykj.wxisalipaygw.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.nykj.wxisalipaygw.constants.AlipayServiceEnvConstants;
import com.nykj.wxisalipaygw.dispatcher.Dispatcher;
import com.nykj.wxisalipaygw.entity.alipay.UnitLink;
import com.nykj.wxisalipaygw.executor.ActionExecutor;
import com.nykj.wxisalipaygw.util.AlipayUtil;
import com.nykj.wxisalipaygw.util.RequestUtil;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
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
@Scope("prototype")
@RequestMapping("/alipay")
public class AlipayGateway {

    private static final long serialVersionUID = 1210436705188940602L;

    private final static Logger LOGGER = Logger.getLogger(AlipayGateway.class);

    @Autowired
    private RequestUtil requestUtil;

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
        Map<String,String> params = null;
        JSONObject alipayBizBody = null;
        JSONObject alipayResponseBody = null;
        UnitLink unitLink = null;
        String responseMsg = null;
        try {
            //将原生参数解析成Map
            params = requestUtil.getRequestParams(request);
            LOGGER.info("支付宝请求业务体:"+ params);

            //1. 根据请求信息组装业务体
            alipayBizBody = requestUtil.buildAlipayBizBody(request);

            //2. 验证签名
            AlipayUtil.verifySign(params,alipayBizBody);

            //3. 获取业务执行器
            alipayResponseBody = new JSONObject();
            ActionExecutor executor = Dispatcher.getExecutor(alipayBizBody,alipayResponseBody);

            //4. 执行业务逻辑
            responseMsg = executor.execute();

        } catch (AlipayApiException e) {
            LOGGER.error("应用网关执行异常:" + e);
        } catch (Exception e) {
            LOGGER.error("应用网关执行异常:" + e);
        } finally {
            //5. 响应结果加签及返回
            try {
                JSONObject unitLinkJsonObj = (JSONObject) alipayBizBody.get("unit_link");
                unitLink = (UnitLink)JSONObject.toBean(unitLinkJsonObj,UnitLink.class);
                //对响应内容加签
                /*responseMsg = AlipaySignature.encryptAndSign(responseMsg,
                        unitLink.getSrv_token(),
                        unitLink.getApp_secret(), AlipayServiceEnvConstants.CHARSET,
                        false, true);*/
                responseMsg = AlipaySignature.encryptAndSign(responseMsg,
                        AlipayServiceEnvConstants.ALIPAY_PUBLIC_KEY,
                        AlipayServiceEnvConstants.PRIVATE_KEY, AlipayServiceEnvConstants.CHARSET,
                        false, true);
                LOGGER.info("响应结果加签内容:" + responseMsg);
            } catch (Exception e) {
                LOGGER.error("响应结果加签失败:" + e);
            }
        }
        return responseMsg;
    }

}