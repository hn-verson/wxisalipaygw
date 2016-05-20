package com.nykj.wxisalipaygw.mock.controller;

import com.nykj.wxisalipaygw.constants.Wxish5Constants;
import com.nykj.wxisalipaygw.controller.BaseController;
import com.nykj.wxisalipaygw.mock.service.AlipayServiceMock;
import com.nykj.wxisalipaygw.mock.service.HisServiceMock;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by Verson on 2016/5/4.
 */
@Controller
@Scope("prototype")
@RequestMapping("/gateway")
public class Wxish5GatewayMock extends BaseController {
    private final static Logger LOGGER = Logger.getLogger(Wxish5GatewayMock.class);

    @Autowired
    private HisServiceMock hisServiceMock;

    @Autowired
    private AlipayServiceMock alipayServiceMock;

    /*@RequestMapping(value = "/wxish5/mock", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String feeList(HttpServletRequest request){
        String service = request.getParameter("service");
        if(StringUtils.isEmpty(service)){
            return "{code:-1,message:\"service参数为空\"}";
        }
        String resContent = null;
        try{
            if(Wxish5Constants.HIS_MZ_FEE_LIST_QUERY_SERVICE.equals(service))
                resContent = hisServiceMock.mzFeeListQuery(null);
            else if(Wxish5Constants.HIS_MZ_FEE_DETAIL_QUERY_SERVICE.equals(service))
                resContent = hisServiceMock.mzFeeDetailQuery(null);
            else if(Wxish5Constants.ALIPAY_MEDICAL_CARD_PAY_SERVICE.equals(service))
                resContent = alipayServiceMock.handlerMedicalCardPay(null);
            else
                return "{code:-1,message:\"未知的服务\"}";
        }catch (Exception e){
            //do something
        }
        return resContent;
    }*/

    @RequestMapping(value = "/wxish5/mock/mzfeedetail", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String feeDetail(HttpServletRequest request){
        String resContent = null;
        try{
            resContent = hisServiceMock.mzFeeDetailQuery(null);
        }catch (Exception e){
            //do something
        }
        return resContent;
    }

    @RequestMapping(value = "/wxish5/mock/medicalpay", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String medicalPay(HttpServletRequest request){
        String resContent = null;
        try{
            resContent = alipayServiceMock.handlerMedicalCardPay(null);
        }catch (Exception e){
            //do something
        }
        return resContent;
    }

}