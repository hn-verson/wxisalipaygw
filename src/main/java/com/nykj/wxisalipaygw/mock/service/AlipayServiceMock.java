package com.nykj.wxisalipaygw.mock.service;

import com.nykj.wxisalipaygw.mock.model.AlipayModelMock;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * Created by Verson on 2016/4/29.
 */
@Service
@Scope("prototype")
public class AlipayServiceMock {
    private static final Logger LOGGER = Logger.getLogger(AlipayServiceMock.class);

    @Autowired
    private AlipayModelMock alipayModelMock;

    /**
     * 医保卡支付
     * @param alipayBizBody
     * @return
     */
    public String handlerMedicalCardPay(JSONObject alipayBizBody) throws Exception{
        return alipayModelMock.handlerMedicalCardPay(alipayBizBody);
    }

}