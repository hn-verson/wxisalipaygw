package com.nykj.wxisalipaygw.controller;

import com.nykj.wxisalipaygw.constants.StatusCode;
import com.nykj.wxisalipaygw.constants.Wxish5Constants;
import com.nykj.wxisalipaygw.entity.alipay.AlipayMedicalCard;
import com.nykj.wxisalipaygw.entity.alipay.AlipayUserInfo;
import com.nykj.wxisalipaygw.entity.alipay.UnitLink;
import com.nykj.wxisalipaygw.exception.BaseException;
import com.nykj.wxisalipaygw.service.alipay.AlipayService;
import com.nykj.wxisalipaygw.service.alipay.HisService;
import com.nykj.wxisalipaygw.service.alipay.UnitInfoService;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by Verson on 2016/5/4.
 */
@Controller
@Scope("prototype")
@RequestMapping("/gateway")
public class Wxish5Gateway extends BaseController {
    private final static Logger LOGGER = Logger.getLogger(Wxish5Gateway.class);

    @Autowired
    private AlipayService alipayService;

    @Autowired
    private HisService hisService;

    @Autowired
    private UnitInfoService unitInfoService;

    @RequestMapping(value = "/wxish5", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String handler(HttpServletRequest request){
        Map<String,String> paramMap = getRequestParams(request);
        JSONObject verifyResultJson = null;
        JSONObject responseMsgJson = null;
        JSONObject bizBodyJson = null;

        try{
            LOGGER.info("接收WXISH5请求:" + paramMap.toString());

            verifyResultJson = paramsVerify(paramMap);
            if(verifyResultJson.has("code") && verifyResultJson.getInt("code") == StatusCode.SUCCESS){
                return verifyResultJson.toString();
            }

            bizBodyJson = buildBizBody(paramMap);
            String projectBasePath = getProjectBasePath(request);
            bizBodyJson.put("project_base_path",projectBasePath);

            responseMsgJson = new JSONObject();
            String service = bizBodyJson.getString("service");

            //支付宝用户信息服务
            if(Wxish5Constants.ALIPAY_USER_INFO_SERVICE.equals(service)){
                AlipayUserInfo alipayUserInfo = alipayService.getAlipayUserInfo(bizBodyJson);
                responseMsgJson.put("data",JSONObject.fromObject(alipayUserInfo));

                //支付宝用户地理位置信息服务
            }else if(Wxish5Constants.ALIPAY_USER_GIS_SERVICE.equals(service)){
                String location = alipayService.getUserGisInfo(bizBodyJson);
                responseMsgJson.put("data",location);

                //支付宝社保卡绑定服务
            }else if(Wxish5Constants.ALIPAY_INST_CARD_BIND_SERVICE.equals(service)){
                String medicalInstCardBindUrl = alipayService.buildMedicalInstCardBindUrl(bizBodyJson);
                JSONObject resMedicalInstCardBindUrl = new JSONObject();
                resMedicalInstCardBindUrl.put("url",medicalInstCardBindUrl);
                resMedicalInstCardBindUrl.put("refresh_token",bizBodyJson.getString("refresh_token"));
                responseMsgJson.put("data",resMedicalInstCardBindUrl.toString());

                //支付宝社保卡查询服务
            }else if(Wxish5Constants.ALIPAY_INST_CARD_QUERY_SERVICE.equals(service)){
                AlipayMedicalCard alipayMedicalCard = alipayService.queryMedicalInstCard(bizBodyJson);
                responseMsgJson.put("data",JSONObject.fromObject(alipayMedicalCard));

                //门诊待缴费记录列表查询
            }else if(Wxish5Constants.HIS_MZ_FEE_LIST_QUERY_SERVICE.equals(service)){
                String mzFeeListInfo = hisService.mzFeeListQuery(bizBodyJson);
                responseMsgJson.put("data",mzFeeListInfo);

                //门诊待缴费记录明细查询
            }else if(Wxish5Constants.HIS_MZ_FEE_DETAIL_QUERY_SERVICE.equals(service)){
                String mzFeeDetailInfo = hisService.mzFeeDetailQuery(bizBodyJson);
                responseMsgJson.put("data",mzFeeDetailInfo);

                //门诊缴费清单状态查询
            }else if(Wxish5Constants.HIS_MZ_FEE_STATE_QUERY_SERVICE.equals(service)){
                String mzFeeStateInfo = hisService.mzFeeStateQuery(bizBodyJson);
                responseMsgJson.put("data",mzFeeStateInfo);

                //社保支付
            }else if(Wxish5Constants.ALIPAY_MEDICAL_CARD_PAY_SERVICE.equals(service)){
                alipayService.handlerMedicalCardPay(bizBodyJson);

            }else{
                responseMsgJson.put("code",StatusCode.SUCCESS);
                responseMsgJson.put("message","未知的服务参数【" + service + "】");
                responseMsgJson.put("time",System.currentTimeMillis());
                return responseMsgJson.toString();
            }
            responseMsgJson.put("code",StatusCode.SUCCESS);
            responseMsgJson.put("message","成功");
            responseMsgJson.put("time",System.currentTimeMillis());
            return responseMsgJson.toString();
        }catch (Exception e){
            LOGGER.error("WXISH5网关处理异常:" + e);
            if(e instanceof BaseException){
                responseMsgJson.put("code",((BaseException) e).getCode());
                responseMsgJson.put("message","处理异常:" + e.getMessage());
            }else{
                responseMsgJson.put("code", StatusCode.INTERNEL_SERVER_EXCEPTION);
                responseMsgJson.put("message","内部服务异常");
            }
            responseMsgJson.put("time",System.currentTimeMillis());
            return responseMsgJson.toString();
        }finally {
            LOGGER.info("返回WXISH5请求:" + responseMsgJson.toString());
        }

    }

    /**
     * 参数验证
     * @param paramMap
     * @return
     */
    public JSONObject paramsVerify(Map<String,String> paramMap) throws Exception{
        JSONObject verifyResultJson = new JSONObject();
        String service = paramMap.get("service");
        String data = paramMap.get("data");
        String time = paramMap.get("time");
        if(StringUtils.isEmpty(service)){
            verifyResultJson.put("code",StatusCode.SUCCESS);
            verifyResultJson.put("message","服务参数为空");
            return verifyResultJson;
        }
        if(StringUtils.isEmpty(data)){
            verifyResultJson.put("code",StatusCode.SUCCESS);
            verifyResultJson.put("message","业务参数为空");
            return verifyResultJson;
        }
        if(StringUtils.isEmpty(time)){
            verifyResultJson.put("code",StatusCode.SUCCESS);
            verifyResultJson.put("message","请求时间为空");
            return verifyResultJson;
        }
        JSONObject dataJson = JSONObject.fromObject(data);
        if(!isExistsParam(dataJson,"unit_id",verifyResultJson,"医院参数为空"))
            return verifyResultJson;
        if(!isExistsParam(dataJson,"channel",verifyResultJson,"渠道参数为空"))
            return verifyResultJson;
        if(Wxish5Constants.ALIPAY_USER_INFO_SERVICE.equals(service)){
            if(!isExistsParam(dataJson,"auth_code",verifyResultJson,"用户认证码为空"))
                return verifyResultJson;
        }else if(Wxish5Constants.ALIPAY_USER_GIS_SERVICE.equals(service)){
            if(!isExistsParam(dataJson,"open_id",verifyResultJson,"用户标识为空"))
                return verifyResultJson;
        }else if(Wxish5Constants.ALIPAY_INST_CARD_BIND_SERVICE.equals(service)){
            if(!isExistsParam(dataJson,"open_id",verifyResultJson,"用户标识为空"))
                return verifyResultJson;
            if(!isExistsParam(dataJson,"refresh_token",verifyResultJson,"刷新令牌为空"))
                return verifyResultJson;
            if(!isExistsParam(dataJson,"return_url",verifyResultJson,"支付宝跳转页面为空"))
                return verifyResultJson;
        }else if(Wxish5Constants.ALIPAY_INST_CARD_QUERY_SERVICE.equals(service)){
            if(!isExistsParam(dataJson,"open_id",verifyResultJson,"用户标识为空"))
                return verifyResultJson;
            if(!isExistsParam(dataJson,"card_org_no",verifyResultJson,"卡颁发机构为空"))
                return verifyResultJson;
        }else if(Wxish5Constants.HIS_MZ_FEE_LIST_QUERY_SERVICE.equals(service)){
            if(!isExistsParam(dataJson,"card_no",verifyResultJson,"就诊卡号为空"))
                return verifyResultJson;
            if(!isExistsParam(dataJson,"begin_time",verifyResultJson,"开始时间为空"))
                return verifyResultJson;
            if(!isExistsParam(dataJson,"end_time",verifyResultJson,"结束时间为空"))
                return verifyResultJson;
        }else if(Wxish5Constants.HIS_MZ_FEE_DETAIL_QUERY_SERVICE.equals(service)){
            if(!isExistsParam(dataJson,"card_no",verifyResultJson,"就诊卡号为空"))
                return verifyResultJson;
            if(!isExistsParam(dataJson,"begin_time",verifyResultJson,"开始时间为空"))
                return verifyResultJson;
            if(!isExistsParam(dataJson,"end_time",verifyResultJson,"结束时间为空"))
                return verifyResultJson;
        }else if(Wxish5Constants.HIS_MZ_FEE_STATE_QUERY_SERVICE.equals(service)){
            //TODO 待HIS接口确定后填充
        }else if(Wxish5Constants.ALIPAY_MEDICAL_CARD_PAY_SERVICE.equals(service)){
            if(!isExistsParam(dataJson,"return_url",verifyResultJson,"页面跳转地址为空"))
                return verifyResultJson;
            if(!isExistsParam(dataJson,"notify_url",verifyResultJson,"结果通知地址为空"))
                return verifyResultJson;
            if(!isExistsParam(dataJson,"refresh_token",verifyResultJson,"刷新令牌为空"))
                return verifyResultJson;
            if(!isExistsParam(dataJson,"scene",verifyResultJson,"支付场景为空"))
                return verifyResultJson;
            if(!isExistsParam(dataJson,"industry",verifyResultJson,"支付行业为空"))
                return verifyResultJson;
            if(!isExistsParam(dataJson,"buyer_id",verifyResultJson,"买家支付宝用户号为空"))
                return verifyResultJson;
            if(!isExistsParam(dataJson,"seller_id",verifyResultJson,"卖家支付宝用户ID为空"))
                return verifyResultJson;
            if(!isExistsParam(dataJson,"total_amount",verifyResultJson,"订单总金额为空"))
                return verifyResultJson;
            if(!isExistsParam(dataJson,"subject",verifyResultJson,"订单标题为空"))
                return verifyResultJson;
            if(!isExistsParam(dataJson,"timeout_express",verifyResultJson,"支付超时时间表达式为空"))
                return verifyResultJson;
            if(!isExistsParam(dataJson,"org_no",verifyResultJson,"医疗机构编码为空"))
                return verifyResultJson;
            if(!isExistsParam(dataJson,"org_name",verifyResultJson,"医疗机构名称为空"))
                return verifyResultJson;
            if(!isExistsParam(dataJson,"serial_no",verifyResultJson,"业务流水号为空"))
                return verifyResultJson;
            if(!isExistsParam(dataJson,"gmt_out_create",verifyResultJson,"外部下单时间为空"))
                return verifyResultJson;
            if(!isExistsParam(dataJson,"is_insurance",verifyResultJson,"是否允许医保为空"))
                return verifyResultJson;
        }else{
            verifyResultJson.put("code",StatusCode.SUCCESS);
            verifyResultJson.put("message","未标识的服务:" + service);
            return verifyResultJson;
        }
        return verifyResultJson;
    }

    /**
     * 验证参数是否存在
     * @param dataJson
     * @param paramName
     * @param omitDesc
     * @return
     */
    public boolean isExistsParam(JSONObject dataJson,String paramName,JSONObject verifyResultJson,String omitDesc) throws Exception{
        if(dataJson == null || verifyResultJson == null){
            return false;
        }
        if(!dataJson.has(paramName)){
            verifyResultJson.put("code",StatusCode.SUCCESS);
            verifyResultJson.put("message",omitDesc);
            return false;
        }
        return true;
    }

    /**
     * 构建业务体
     * @param paramMap
     * @return
     */
    public JSONObject buildBizBody(Map<String,String> paramMap) throws Exception{
        JSONObject bizBodyJson = new JSONObject();
        JSONObject dataJson = JSONObject.fromObject(paramMap.get("data"));

        String unitId = dataJson.getString("unit_id");
        String service = paramMap.get("service");
        bizBodyJson.put("service",service);
        bizBodyJson.put("unit_id",unitId);
        bizBodyJson.put("channel",dataJson.getString("channel"));
        bizBodyJson.put("time",paramMap.get("time"));

        //将医院服务窗配置信息整合至业务消息体
        UnitLink unitLink = unitInfoService.findUnitLinkByUnitId(unitId);
        bizBodyJson.put("unit_link",unitLink);

        //组装服务参数
        if(Wxish5Constants.ALIPAY_USER_INFO_SERVICE.equals(service)){
            bizBodyJson.put("auth_code",dataJson.getString("auth_code"));
        }else if(Wxish5Constants.ALIPAY_USER_GIS_SERVICE.equals(service)){
            bizBodyJson.put("open_id",dataJson.getString("open_id"));
        }else if(Wxish5Constants.ALIPAY_INST_CARD_BIND_SERVICE.equals(service)){
            bizBodyJson.put("open_id",dataJson.getString("open_id"));
            bizBodyJson.put("refresh_token",dataJson.getString("refresh_token"));
            bizBodyJson.put("return_url",dataJson.getString("return_url"));
        }else if(Wxish5Constants.ALIPAY_INST_CARD_QUERY_SERVICE.equals(service)){
            bizBodyJson.put("open_id",dataJson.getString("open_id"));
            bizBodyJson.put("refresh_token",dataJson.getString("refresh_token"));
            bizBodyJson.put("card_org_no",dataJson.getString("card_org_no"));
        }else if(Wxish5Constants.HIS_MZ_FEE_LIST_QUERY_SERVICE.equals(service)){
            bizBodyJson.put("card_no",dataJson.getString("card_no"));
            bizBodyJson.put("begin_time",dataJson.getString("begin_time"));
            bizBodyJson.put("end_time",dataJson.getString("end_time"));
            if(dataJson.has("branch_code"))
                bizBodyJson.put("branch_code",dataJson.getString("branch_code"));

        }else if(Wxish5Constants.HIS_MZ_FEE_DETAIL_QUERY_SERVICE.equals(service)){
            bizBodyJson.put("card_no",dataJson.getString("card_no"));
            bizBodyJson.put("begin_time",dataJson.getString("begin_time"));
            bizBodyJson.put("end_time",dataJson.getString("end_time"));
            if(dataJson.has("branch_code"))
                bizBodyJson.put("branch_code",dataJson.getString("branch_code"));
        }else if(Wxish5Constants.HIS_MZ_FEE_STATE_QUERY_SERVICE.equals(service)){
            //TODO 待HIS接口确认后填充
        }else if(Wxish5Constants.ALIPAY_MEDICAL_CARD_PAY_SERVICE.equals(service)){

            //MUST PARAMS
            bizBodyJson.put("return_url",dataJson.getString("return_url"));
            bizBodyJson.put("notify_url",dataJson.getString("notify_url"));
            bizBodyJson.put("refresh_token",dataJson.getString("refresh_token"));
            bizBodyJson.put("scene",dataJson.getString("scene"));
            bizBodyJson.put("industry",dataJson.getString("industry"));
            bizBodyJson.put("buyer_id",dataJson.getString("buyer_id"));
            bizBodyJson.put("seller_id",dataJson.getString("seller_id"));
            bizBodyJson.put("total_amount",dataJson.getString("total_amount"));
            bizBodyJson.put("subject",dataJson.getString("subject"));
            bizBodyJson.put("timeout_express",dataJson.getString("timeout_express"));
            bizBodyJson.put("org_no",dataJson.getString("org_no"));
            bizBodyJson.put("org_name",dataJson.getString("org_name"));
            bizBodyJson.put("serial_no",dataJson.getString("serial_no"));
            bizBodyJson.put("gmt_out_create",dataJson.getString("gmt_out_create"));
            bizBodyJson.put("is_insurance",dataJson.getString("is_insurance"));

            //OPTION PARAMS
            if(dataJson.has("body"))
                bizBodyJson.put("body",dataJson.getString("body"));
            if(dataJson.has("extend_params"))
                bizBodyJson.put("extend_params",dataJson.getString("extend_params"));
            if(dataJson.has("bill_no"))
                bizBodyJson.put("bill_no",dataJson.getString("bill_no"));
            if(dataJson.has("patient_card_type"))
                bizBodyJson.put("patient_card_type",dataJson.getString("patient_card_type"));
            if(dataJson.has("patient_card_no"))
                bizBodyJson.put("patient_card_no",dataJson.getString("patient_card_no"));
            if(dataJson.has("patient_name"))
                bizBodyJson.put("patient_name",dataJson.getString("patient_name"));
            if(dataJson.has("patient_mobile"))
                bizBodyJson.put("patient_mobile",dataJson.getString("patient_mobile"));

        }

        return bizBodyJson;
    }

}