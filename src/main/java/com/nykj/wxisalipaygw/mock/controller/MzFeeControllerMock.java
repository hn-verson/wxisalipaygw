package com.nykj.wxisalipaygw.mock.controller;

import com.nykj.wxisalipaygw.constants.Wxish5Constants;
import com.nykj.wxisalipaygw.controller.BaseController;
import com.nykj.wxisalipaygw.mock.service.HisServiceMock;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by Verson on 2016/5/24.
 */
public class MzFeeControllerMock extends BaseController {
    private final static Logger LOGGER = Logger.getLogger(MzFeeControllerMock.class);

    @Autowired
    private HisServiceMock hisServiceMock;

    @RequestMapping(value = "/fetchMzFeeList/mock", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String fetchMzFeeList(HttpServletRequest request){
        String responseMsg = null;
        try {
            Map<String,String> paramMap = getRequestParams(request);
            LOGGER.info("接收WXISH5请求:" + paramMap.toString());

            String[] mustParams = Wxish5Constants.REQUEST_MUST_BUSI_PARAMS.get(Wxish5Constants.HIS_MZ_FEE_LIST_QUERY_SERVICE);
            String[] optParams = Wxish5Constants.REQUEST_OPT_BUSI_PARAMS.get(Wxish5Constants.HIS_MZ_FEE_LIST_QUERY_SERVICE);

            //mock阶段流程测试，无需验证参数
            //paramsVerify(paramMap,mustParams);

            //业务数据组装
            JSONObject bizBodyJson = buildBizBody(paramMap,mustParams,optParams);

            //处理业务
            responseMsg = hisServiceMock.mzFeeListQuery(bizBodyJson);

            //响应数据组装
            //responseMsg = wrapSuccessResponseMsg(mzFeeListInfo);
        } catch (Exception e) {
            LOGGER.error("WXISH5网关处理异常:" + e);
        }
        return responseMsg;
    }

    @RequestMapping(value = "/fetchMzFeeDetail/mock", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String fetchMzFeeDetail(HttpServletRequest request){
        String responseMsg = null;
        try {
            Map<String,String> paramMap = getRequestParams(request);
            LOGGER.info("接收WXISH5请求:" + paramMap.toString());

            String[] mustParams = Wxish5Constants.REQUEST_MUST_BUSI_PARAMS.get(Wxish5Constants.HIS_MZ_FEE_DETAIL_QUERY_SERVICE);
            String[] optParams = Wxish5Constants.REQUEST_OPT_BUSI_PARAMS.get(Wxish5Constants.HIS_MZ_FEE_DETAIL_QUERY_SERVICE);

            //请求参数验证[可选参数无需验证]
            //paramsVerify(paramMap,mustParams);

            //业务数据组装
            //JSONObject bizBodyJson = buildBizBody(paramMap,mustParams,optParams);

            //处理业务
            responseMsg = hisServiceMock.mzFeeDetailQuery(null);

            //响应数据组装
            //responseMsg = wrapSuccessResponseMsg(mzFeeDetailInfo);
        } catch (Exception e) {
            LOGGER.error("WXISH5网关处理异常:" + e);
        }
        return responseMsg;
    }

    @RequestMapping(value = "/fetchMzFeeState/mock", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String fetchMzFeeState(HttpServletRequest request){
        String responseMsg = null;
        try {
            Map<String,String> paramMap = getRequestParams(request);
            LOGGER.info("接收WXISH5请求:" + paramMap.toString());

            String[] mustParams = Wxish5Constants.REQUEST_MUST_BUSI_PARAMS.get(Wxish5Constants.HIS_MZ_FEE_STATE_QUERY_SERVICE);
            String[] optParams = Wxish5Constants.REQUEST_OPT_BUSI_PARAMS.get(Wxish5Constants.HIS_MZ_FEE_STATE_QUERY_SERVICE);

            //请求参数验证[可选参数无需验证]
            //paramsVerify(paramMap,mustParams);

            //业务数据组装
            //JSONObject bizBodyJson = buildBizBody(paramMap,mustParams,optParams);

            //处理业务
            responseMsg = hisServiceMock.mzFeeStateQuery(null);

            //响应数据组装
            //responseMsg = wrapSuccessResponseMsg(mzFeeStateInfo);
        } catch (Exception e) {
            LOGGER.error("WXISH5网关处理异常:" + e);
        }
        return responseMsg;
    }
}