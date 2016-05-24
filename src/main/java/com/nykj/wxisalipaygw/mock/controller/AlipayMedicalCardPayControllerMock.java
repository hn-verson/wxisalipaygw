package com.nykj.wxisalipaygw.mock.controller;

import com.nykj.wxisalipaygw.constants.Wxish5Constants;
import com.nykj.wxisalipaygw.controller.BaseController;
import com.nykj.wxisalipaygw.mock.service.AlipayServiceMock;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by Verson on 2016/5/24.
 */
@Controller
@Scope("prototype")
@RequestMapping("/gateway/wxish5")
public class AlipayMedicalCardPayControllerMock extends BaseController {
    private final static Logger LOGGER = Logger.getLogger(AlipayMedicalCardPayControllerMock.class);

    @Autowired
    private AlipayServiceMock alipayServiceMock;

    @RequestMapping(value = "/alipayMedicalCardPay/mock", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String alipayMedicalCardPay(HttpServletRequest request){
        String responseMsg = null;
        try {
            Map<String,String> paramMap = getRequestParams(request);
            LOGGER.info("接收WXISH5请求:" + paramMap.toString());

            String[] mustParams = Wxish5Constants.REQUEST_MUST_BUSI_PARAMS.get(Wxish5Constants.ALIPAY_MEDICAL_CARD_PAY_SERVICE);
            String[] optParams = Wxish5Constants.REQUEST_OPT_BUSI_PARAMS.get(Wxish5Constants.ALIPAY_MEDICAL_CARD_PAY_SERVICE);

            //请求参数验证[可选参数无需验证]
            //paramsVerify(paramMap,mustParams);

            //业务数据组装
            //JSONObject bizBodyJson = buildBizBody(paramMap,mustParams,optParams);

            //TODO 处理业务,由于支付接口并未在真实场景中联调，需后续跟进
            responseMsg = alipayServiceMock.handlerMedicalCardPay(JSONObject.fromObject(paramMap.get("data")));

            //响应数据组装
            responseMsg = wrapSuccessResponseMsg(null);
        } catch (Exception e) {
            LOGGER.error("WXISH5网关处理异常:" + e);
        }
        return responseMsg;
    }
}