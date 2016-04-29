/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package com.nykj.wxisalipaygw.executor;

import com.nykj.wxisalipaygw.util.AlipayUtil;
import net.sf.json.JSONObject;

/**
 * 默认执行器(该执行器仅发送ack响应)
 * 
 * @author baoxing.gbx
 * @version $Id: InAlipayDefaultExecutor.java, v 0.1 Jul 30, 2014 10:22:11 AM baoxing.gbx Exp $
 */
public class InAlipayDefaultExecutor implements ActionExecutor {

    /** 业务参数 */
    private JSONObject alipayBizBody;

    public InAlipayDefaultExecutor(JSONObject alipayBizBody) {
        this.alipayBizBody = alipayBizBody;
    }

    /**
     * 
     * @see ActionExecutor#execute()
     */
    @Override
    public String execute() throws Exception {
        return AlipayUtil.buildBaseAckMsg(alipayBizBody);
    }
}