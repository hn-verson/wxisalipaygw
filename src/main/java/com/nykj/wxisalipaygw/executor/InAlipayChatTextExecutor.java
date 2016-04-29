/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package com.nykj.wxisalipaygw.executor;

import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayMobilePublicMessageCustomSendRequest;
import com.alipay.api.response.AlipayMobilePublicMessageCustomSendResponse;
import com.nykj.wxisalipaygw.entity.alipay.UnitLink;
import com.nykj.wxisalipaygw.util.AlipayUtil;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 聊天执行器(纯文本消息)
 * 
 * @author baoxing.gbx
 * @version $Id: InAlipayChatExecutor.java, v 0.1 Jul 28, 2014 5:17:04 PM baoxing.gbx Exp $
 */
public class InAlipayChatTextExecutor implements ActionExecutor {

    private static final Logger LOGGER = Logger.getLogger(InAlipayChatTextExecutor.class);

    /** 线程池 */
    private static ExecutorService executors = Executors.newSingleThreadExecutor();

    /** 业务参数 */
    private JSONObject alipayBizBody;

    /** 响应参数 **/
    private JSONObject alipayResponseBody;

    public InAlipayChatTextExecutor(JSONObject alipayBizBody,JSONObject alipayResponseBody) {
        this.alipayBizBody = alipayBizBody;
        this.alipayResponseBody = alipayResponseBody;
    }

    /**
     * 
     * @see ActionExecutor#execute()
     */
    @Override
    public String execute() throws Exception {

        //1. 首先同步构建ACK响应
        String syncResponseMsg = AlipayUtil.buildBaseAckMsg(alipayBizBody);

        final String fromUserId = alipayBizBody.getString("FromAlipayUserId");

        final UnitLink unitLink = (UnitLink)alipayBizBody.get("unit_link");

        //2. 异步发送消息
        executors.execute(new Runnable() {
            @Override
            public void run() {
                try {

                    // 2.1 构建一个业务响应消息
                    String requestMsg = AlipayUtil.buildSingleTextMsg(fromUserId,alipayResponseBody.getString("content"));

                    AlipayClient alipayClient = AlipayUtil.getAlipayClient(unitLink);
                    AlipayMobilePublicMessageCustomSendRequest request = new AlipayMobilePublicMessageCustomSendRequest();
                    request.setBizContent(requestMsg);

                    // 2.2 使用SDK接口类发送响应
                    AlipayMobilePublicMessageCustomSendResponse response = alipayClient
                        .execute(request);

                    // 2.3 商户根据响应结果处理结果
                    if (null != response && response.isSuccess()) {
                        LOGGER.info("异步发送成功，结果为：" + response.getBody());
                    } else {
                        LOGGER.info("异步发送失败 code=" + response.getCode() + "msg："
                                           + response.getMsg());
                    }
                } catch (Exception e) {
                    LOGGER.info("异步发送失败");
                }
            }
        });

        // 3.返回同步的ACK响应
        return syncResponseMsg;
    }

}