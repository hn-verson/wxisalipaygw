package com.nykj.wxisalipaygw.mock.model;

import com.nykj.wxisalipaygw.constants.GlobalConstants;
import com.nykj.wxisalipaygw.util.HttpUtil;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

/**
 * Created by Verson on 2016/4/29.
 */
@Repository
public class AlipayModelMock {
    private static final Logger LOGGER = Logger.getLogger(AlipayModelMock.class);

    @Value("#{config['alipay.api.medical.card.pay']}")
    private String medicalCardPayApi;

    /**
     * 社保卡支付
     * @param paramsJson
     * @return
     */
    public String handlerMedicalCardPay(JSONObject paramsJson) throws Exception{
        //伪造请求体
        /*paramsJson = (paramsJson == null ? new net.sf.json.JSONObject() : paramsJson);
        paramsJson.clear();
        paramsJson.put("bill_no","545445516");
        paramsJson.put("buyer_id","1665164251");
        paramsJson.put("extend_params","{'bb','22'}");
        paramsJson.put("gmt_out_create","2016-01-01 12:12:12");
        paramsJson.put("industry","HOSPITAL");
        paramsJson.put("is_insurance","1");
        paramsJson.put("org_name","龙华人民医院");
        paramsJson.put("org_no","15531");
        paramsJson.put("out_trade_no","12321321");
        paramsJson.put("scene","REGISTRATION");
        paramsJson.put("seller_id","161561651");
        paramsJson.put("serial_no","545454545");
        paramsJson.put("subject","处方单");
        paramsJson.put("timeout_express","15m");
        paramsJson.put("total_amount","10.00");
        paramsJson.put("return_url","http://xxx/xx.do");
        paramsJson.put("unit_id","22");
        paramsJson.put("his_pay_no","222");
        paramsJson.put("timestamp","1561616165");*/
        return HttpUtil.httpPost(medicalCardPayApi,paramsJson,GlobalConstants.REQUEST_CONTENT_TYPE);
    }
}