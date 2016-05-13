package com.nykj.wxisalipaygw.controller;

import com.nykj.wxisalipaygw.constants.Wxish5Constants;
import com.nykj.wxisalipaygw.entity.alipay.AlipayMedicalCard;
import com.nykj.wxisalipaygw.entity.alipay.AlipayUserInfo;
import com.nykj.wxisalipaygw.entity.alipay.UnitLink;
import com.nykj.wxisalipaygw.service.alipay.AlipayService;
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
            if(verifyResultJson.has("code") && verifyResultJson.getInt("code") == Wxish5Constants.API_ACESS_FAILER_FLAG){
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
            }else{
                responseMsgJson.put("code",Wxish5Constants.API_ACESS_FAILER_FLAG);
                responseMsgJson.put("message","未知的服务参数【" + service + "】");
                responseMsgJson.put("time",System.currentTimeMillis());
                return responseMsgJson.toString();
            }
            responseMsgJson.put("code",Wxish5Constants.API_ACESS_SUCCESS_FLAG);
            responseMsgJson.put("message","成功");
            responseMsgJson.put("time",System.currentTimeMillis());
            return responseMsgJson.toString();
        }catch (Exception e){
            LOGGER.error("网关处理异常:" + e);
            responseMsgJson.put("code",Wxish5Constants.API_ACESS_FAILER_FLAG);
            responseMsgJson.put("message","处理异常:" + e);
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
            verifyResultJson.put("code",1);
            verifyResultJson.put("message","服务参数为空");
            return verifyResultJson;
        }
        if(StringUtils.isEmpty(data)){
            verifyResultJson.put("code",1);
            verifyResultJson.put("message","业务参数为空");
            return verifyResultJson;
        }
        if(StringUtils.isEmpty(time)){
            verifyResultJson.put("code",1);
            verifyResultJson.put("message","请求时间为空");
            return verifyResultJson;
        }
        JSONObject dataJson = JSONObject.fromObject(data);
        String unitId = dataJson.getString("unit_id");
        if(StringUtils.isEmpty(unitId)){
            verifyResultJson.put("code",1);
            verifyResultJson.put("message","医院参数为空");
            return verifyResultJson;
        }
        String channel = dataJson.getString("channel");
        if(StringUtils.isEmpty(channel)){
            verifyResultJson.put("code",1);
            verifyResultJson.put("message","渠道参数为空");
            return verifyResultJson;
        }
        if(Wxish5Constants.ALIPAY_USER_INFO_SERVICE.equals(service)){
            String authCode = dataJson.getString("auth_code");
            if(StringUtils.isEmpty(authCode)){
                verifyResultJson.put("code",1);
                verifyResultJson.put("message","用户认证码为空");
                return verifyResultJson;
            }
        }else if(Wxish5Constants.ALIPAY_USER_GIS_SERVICE.equals(service)
                || Wxish5Constants.ALIPAY_INST_CARD_BIND_SERVICE.equals(service)
                || Wxish5Constants.ALIPAY_INST_CARD_QUERY_SERVICE.equals(service)){
            String openId = dataJson.getString("open_id");
            if(StringUtils.isEmpty(openId)){
                verifyResultJson.put("code",1);
                verifyResultJson.put("message","用户标识为空");
                return verifyResultJson;
            }
            if(Wxish5Constants.ALIPAY_INST_CARD_BIND_SERVICE.equals(service)){
                String refreshToken = dataJson.getString("refresh_token");
                if(StringUtils.isEmpty(refreshToken)){
                    verifyResultJson.put("code",1);
                    verifyResultJson.put("message","刷新令牌为空");
                    return verifyResultJson;
                }
                String returnUrl = dataJson.getString("return_url");
                if(StringUtils.isEmpty(returnUrl)){
                    verifyResultJson.put("code",1);
                    verifyResultJson.put("message","支付宝跳转页面为空");
                    return verifyResultJson;
                }
            }else if(Wxish5Constants.ALIPAY_INST_CARD_QUERY_SERVICE.equals(service)){
                String cardOrgNo = dataJson.getString("card_org_no");
                if(StringUtils.isEmpty(cardOrgNo)){
                    verifyResultJson.put("code",1);
                    verifyResultJson.put("message","卡颁发机构为空");
                    return verifyResultJson;
                }
            }
        }else{
            verifyResultJson.put("code",1);
            verifyResultJson.put("message","未标识的服务:" + service);
            return verifyResultJson;
        }
        return verifyResultJson;
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
        }

        return bizBodyJson;
    }

}