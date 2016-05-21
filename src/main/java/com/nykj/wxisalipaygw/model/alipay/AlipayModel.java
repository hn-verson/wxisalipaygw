package com.nykj.wxisalipaygw.model.alipay;

import com.alipay.api.*;
import com.alipay.api.internal.util.AlipayHashMap;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.internal.util.RequestParametersHolder;
import com.alipay.api.internal.util.WebUtils;
import com.alipay.api.request.AlipayMobilePublicMessageCustomSendRequest;
import com.alipay.api.response.AlipayMobilePublicMessageCustomSendResponse;
import com.nykj.wxisalipaygw.constants.AlipayEnvConstants;
import com.nykj.wxisalipaygw.constants.GlobalConstants;
import com.nykj.wxisalipaygw.entity.alipay.AlipayMedicalCard;
import com.nykj.wxisalipaygw.entity.alipay.UnitLink;
import com.nykj.wxisalipaygw.entity.alipay.request.AlipayMedicalInstCardBindRequest;
import com.nykj.wxisalipaygw.entity.alipay.request.AlipayMedicalInstCardQueryRequest;
import com.nykj.wxisalipaygw.util.DateUtil;
import com.nykj.wxisalipaygw.util.HttpUtil;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Verson on 2016/4/29.
 */
@Repository
public class AlipayModel {
    private static final Logger LOGGER = Logger.getLogger(AlipayModel.class);

    @Value("#{config['alipay.api.medical.card.pay']}")
    private String medicalCardPayApi;

    /**
     * 获得API调用客户端
     *
     * @return
     */
    public AlipayClient getAlipayClient(UnitLink unitLink){
        AlipayClient alipayClient = new DefaultAlipayClient(unitLink.getAlipay_gateway(), unitLink.getApp_id(),
                unitLink.getPrivate_key(), AlipayEnvConstants.FORMAT, AlipayEnvConstants.CHARSET,unitLink.getAlipay_public_key());
        return alipayClient;
    }
    /**
     * 构造基础的响应消息
     * @return
     */
    public String buildBaseAckMsg(JSONObject alipayBizBody) throws Exception{
        StringBuilder sb = new StringBuilder();
        JSONObject bizContentJson = (JSONObject)alipayBizBody.get("biz_content");
        String userId = bizContentJson.getString("FromAlipayUserId");
        JSONObject unitLinkJson = (JSONObject) alipayBizBody.get("unit_link");
        UnitLink unitLink = (UnitLink)JSONObject.toBean(unitLinkJson,UnitLink.class);
        sb.append("<XML>");
        sb.append("<ToUserId><![CDATA[" + userId + "]]></ToUserId>");
        sb.append("<AppId><![CDATA[" + unitLink.getApp_id() + "]]></AppId>");
        sb.append("<CreateTime>" + Calendar.getInstance().getTimeInMillis() + "</CreateTime>");
        sb.append("<MsgType><![CDATA[ack]]></MsgType>");
        sb.append("</XML>");
        return sb.toString();
    }

    /**
     * 构造单发纯文本消息
     * @param fromUserId
     * @return
     */
    public String buildSingleTextMsg(String fromUserId,String content) {

        JSONObject msgJson = new JSONObject();
        msgJson.put("toUserId",fromUserId);
        msgJson.put("msgType","text");
        JSONObject contentJson = new JSONObject();
        contentJson.put("content",content);
        msgJson.put("text",contentJson);
        return msgJson.toString();

    }

    /**
     * 构建响应成功字符串
     * @return
     */
    public String buildSucessResponse(JSONObject alipayBizBody) throws Exception {
        JSONObject unitLinkJson = (JSONObject) alipayBizBody.get("unit_link");
        UnitLink unitLink = (UnitLink)JSONObject.toBean(unitLinkJson,UnitLink.class);

        //固定响应格式，必须按此格式返回
        StringBuilder builder = new StringBuilder();
        builder.append("<success>").append(Boolean.TRUE.toString()).append("</success>");
        builder.append("<biz_content>").append(unitLink.getPublic_key())
                .append("</biz_content>");
        return builder.toString();
    }

    /**
     * 组装单文本消息发送线程体
     * @param alipayBizBody
     * @return
     */
    public Runnable buildSingleTextMsgSendThread(final JSONObject alipayBizBody) throws Exception{
        return new Runnable() {
            @Override
            public void run() {
                JSONObject unitLinkJson = (JSONObject)alipayBizBody.get("unit_link");
                UnitLink unitLink = (UnitLink)JSONObject.toBean(unitLinkJson,UnitLink.class);
                AlipayClient alipayClient = getAlipayClient(unitLink);

                AlipayMobilePublicMessageCustomSendRequest request = new AlipayMobilePublicMessageCustomSendRequest();
                JSONObject bizContentJson = (JSONObject)alipayBizBody.get("biz_content");
                String openId = bizContentJson.getString("FromAlipayUserId");
                String replyContent = alipayBizBody.has("reply_content") ? alipayBizBody.getString("reply_content") : "";
                request.setBizContent(buildSingleTextMsg(openId,replyContent));

                try {

                    // 调用单发接口发送纯文本消息
                    AlipayMobilePublicMessageCustomSendResponse response = alipayClient.execute(request);

                    if (null != response && response.isSuccess()) {
                        LOGGER.info("消息发送成功 : response = " + response.getBody());
                    } else {
                        LOGGER.info("消息发送失败 code=" + response.getCode() + "msg=" + response.getMsg());
                    }

                } catch (AlipayApiException e) {
                    LOGGER.error("消息发送失败:" + e);
                }

            }

        };
    }

    /**
     * 构造医保卡绑定请求url
     * @param alipayBizBody
     * @return
     */
    public String buildMedicalInstCardBindUrl(JSONObject alipayBizBody) throws Exception{
        AlipayMedicalInstCardBindRequest request = new AlipayMedicalInstCardBindRequest();
        request.setBuyerId(alipayBizBody.getString("open_id"));
        request.setReturnUrl(alipayBizBody.getString("return_url"));

        JSONObject unitLinkJson = (JSONObject)alipayBizBody.get("unit_link");
        UnitLink unitLink = (UnitLink)JSONObject.toBean(unitLinkJson,UnitLink.class);

        RequestParametersHolder requestHolder = buildAlipayRequestHolder(request,unitLink,alipayBizBody);

        StringBuilder baseUrl = new StringBuilder(unitLink.getAlipay_gateway());

        return buildAlipayUrl(baseUrl,requestHolder,GlobalConstants.CHARSET_UTF8);
    }

    /**
     * 构建医保卡查询请求url
     * @param alipayBizBody
     * @return
     * @throws Exception
     */
    public AlipayMedicalCard queryMedicalInstCard(JSONObject alipayBizBody) throws Exception{
        AlipayMedicalInstCardQueryRequest request = new AlipayMedicalInstCardQueryRequest();
        request.setBuyerId(alipayBizBody.getString("open_id"));
        request.setCardOrgNo(alipayBizBody.getString("card_org_no"));

        JSONObject unitLinkJson = (JSONObject)alipayBizBody.get("unit_link");
        UnitLink unitLink = (UnitLink)JSONObject.toBean(unitLinkJson,UnitLink.class);

        RequestParametersHolder requestHolder = buildAlipayRequestHolder(request,unitLink,alipayBizBody);

        StringBuilder baseUrl = new StringBuilder(unitLink.getAlipay_gateway());
        String bizUrl = buildAlipayUrl(baseUrl,requestHolder,AlipayConstants.CHARSET_UTF8);

        //调用alipay系统接口，获取社保卡信息
        String resultContent = HttpUtil.httpGet(bizUrl,null,null);
        JSONObject resultContentJson = JSONObject.fromObject(resultContent);
        JSONObject bizContentJson = resultContentJson.has("alipay_commerce_medical_card_query_response") ? (JSONObject) resultContentJson.get("alipay_commerce_medical_card_query_response") : null;

        if(bizContentJson == null){
            throw new Exception("查询医保卡信息异常,调用地址:" + bizUrl);
        }

        if(!bizContentJson.has("code") || !"10000".equals(bizContentJson.getString("code"))){
            throw new Exception("查询医保卡信息失败:" + bizContentJson.getString("msg"));
        }

        AlipayMedicalCard alipayMedicalCard = new AlipayMedicalCard();
        alipayMedicalCard.setBuyerLogonId(bizContentJson.getString("buyer_logon_id"));
        alipayMedicalCard.setBuyerUserId(bizContentJson.getString("buyer_user_id"));
        alipayMedicalCard.setCardOrgName(bizContentJson.getString("card_org_name"));
        alipayMedicalCard.setCardOrgNo(bizContentJson.getString("card_org_no"));
        alipayMedicalCard.setCity(bizContentJson.getString("city"));
        alipayMedicalCard.setExtendParams(bizContentJson.getString("extend_params"));
        alipayMedicalCard.setGmtSign(bizContentJson.has("gmt_sign") ? bizContentJson.getString("gmt_sign") : null);
        alipayMedicalCard.setMedicalCardId(bizContentJson.getString("medical_card_id"));
        alipayMedicalCard.setMedicalCardNo(bizContentJson.getString("medical_card_no"));
        alipayMedicalCard.setMedicalCardType(bizContentJson.getString("medical_card_type"));
        alipayMedicalCard.setSignStatus(bizContentJson.getString("sign_status"));

        return alipayMedicalCard;
    }

    /**
     * 构造requestHolder
     * @param request
     * @param alipayBizBody
     * @return
     */
    public RequestParametersHolder buildAlipayRequestHolder(AlipayRequest request,UnitLink unitLink,JSONObject alipayBizBody) throws Exception{
        RequestParametersHolder requestHolder = new RequestParametersHolder();
        AlipayHashMap appParams = new AlipayHashMap(request.getTextParams());
        requestHolder.setApplicationParams(appParams);

        String charset = AlipayConstants.CHARSET_UTF8;
        String signType = AlipayEnvConstants.SIGN_TYPE;

        AlipayHashMap protocalMustParams = new AlipayHashMap();
        protocalMustParams.put(AlipayConstants.METHOD, request.getApiMethodName());
        protocalMustParams.put(AlipayConstants.VERSION, request.getApiVersion());
        protocalMustParams.put(AlipayConstants.APP_ID, unitLink.getApp_id());
        protocalMustParams.put(AlipayConstants.SIGN_TYPE, AlipayEnvConstants.SIGN_TYPE);
        protocalMustParams.put(AlipayConstants.TERMINAL_TYPE, request.getTerminalType());
        protocalMustParams.put(AlipayConstants.TERMINAL_INFO, request.getTerminalInfo());
        protocalMustParams.put(AlipayConstants.NOTIFY_URL, request.getNotifyUrl());
        protocalMustParams.put(AlipayConstants.CHARSET, charset);
        protocalMustParams.put(AlipayConstants.TIMESTAMP, DateUtil.formatDate(new Date(), GlobalConstants.DATE_TIME_FORMAT));
        requestHolder.setProtocalMustParams(protocalMustParams);

        AlipayHashMap protocalOptParams = new AlipayHashMap();
        protocalOptParams.put(AlipayConstants.FORMAT, AlipayEnvConstants.FORMAT);
        if(alipayBizBody.has("access_token")){
            protocalOptParams.put(AlipayConstants.ACCESS_TOKEN, alipayBizBody.getString("access_token"));
        }
        protocalOptParams.put(AlipayConstants.ALIPAY_SDK, AlipayConstants.SDK_VERSION);
        protocalOptParams.put(AlipayConstants.PROD_CODE, request.getProdCode());
        requestHolder.setProtocalOptParams(protocalOptParams);

        if (AlipayConstants.SIGN_TYPE_RSA.equals(signType)) {

            String signContent = AlipaySignature.getSignatureContent(requestHolder);

            protocalMustParams.put(AlipayConstants.SIGN,
                    AlipaySignature.rsaSign(signContent, unitLink.getPrivate_key(), charset));

        } else {
            protocalMustParams.put(AlipayConstants.SIGN, "");
        }

        return requestHolder;
    }

    /**
     * 构建支付宝请求地址
     * @param baseUrl
     * @return
     */
    public String buildAlipayUrl(StringBuilder baseUrl,RequestParametersHolder requestHolder,String charset) throws Exception{
        try {
            String sysMustQuery = WebUtils.buildQuery(requestHolder.getProtocalMustParams(),
                    charset);
            String sysOptQuery = WebUtils.buildQuery(requestHolder.getProtocalOptParams(), charset);

            String applicationParams = WebUtils.buildQuery(requestHolder.getApplicationParams(),
                    charset);

            baseUrl.append("?");
            baseUrl.append(sysMustQuery);
            if (sysOptQuery != null & sysOptQuery.length() > 0) {
                baseUrl.append("&");
                baseUrl.append(sysOptQuery);
            }

            if (applicationParams != null & applicationParams.length() > 0) {
                baseUrl.append("&");
                baseUrl.append(applicationParams);
            }
        } catch (IOException e) {
            throw new AlipayApiException(e);
        }
        return baseUrl.toString();
    }

    /**
     * 社保卡支付
     * @param paramsJson
     * @return
     */
    public String handlerMedicalCardPay(JSONObject paramsJson) throws Exception{
        return HttpUtil.httpPost(medicalCardPayApi,paramsJson,GlobalConstants.REQUEST_CONTENT_TYPE);
    }

    /**
     * 转义推送用户消息
     * @param oldUrl
     * @param projectBasePath
     * @param unitId
     * @param unitName
     * @param openId
     * @return
     */
    public String changeUrl(String oldUrl, String projectBasePath, String unitId, String unitName, String openId) {
        String newUrl = "";
        if(oldUrl!=null){
            //替换所有的 医院id：{unit_id}
            if(oldUrl.indexOf("{unit_id}") != -1) {
                oldUrl = oldUrl.replaceAll("\\{unit_id\\}", unitId);
            }
            //替换所有的 医院名称：{unit_name}
            if(oldUrl.indexOf("{unit_name}") != -1) {
                oldUrl = oldUrl.replaceAll("\\{unit_name\\}", unitName);
            }
            //替换所有的 openid：{open_id}
            if(oldUrl.indexOf("{open_id}") != -1) {
                oldUrl = oldUrl.replaceAll("\\{open_id\\}", openId);
            }
            //替换所有的 我的一卡通URL：{my_cards_url}
            if(oldUrl.indexOf("{my_cards_url}") != -1) {
                oldUrl = oldUrl.replaceAll("\\{my_cards_url\\}", projectBasePath+"/act/member/main.do?unit_id="+unitId+"&open_id="+openId);
            }
            //替换所有的 我的挂号：{my_orders_url}
            if(oldUrl.indexOf("{my_orders_url}") != -1) {
                oldUrl = oldUrl.replaceAll("\\{my_orders_url\\}", projectBasePath+"/act/order/orderList.do?unitId="+unitId+"&unit_id="+unitId+"&open_id="+openId);
            }
            //替换所有的 当天挂号：{make_curorder_url}
            if(oldUrl.indexOf("{make_curorder_url}") != -1) {
                oldUrl = oldUrl.replaceAll("\\{make_curorder_url\\}", projectBasePath+"/cur/dep/main.do?unit_id="+unitId+"&open_id="+openId);
            }
            //替换所有的 预约挂号：{make_order_url}
            if(oldUrl.indexOf("{make_order_url}") != -1) {
                oldUrl = oldUrl.replaceAll("\\{make_order_url\\}", projectBasePath+"/dep/main.do?unit_id="+unitId+"&open_id="+openId);
            }
            //替换所有的 在线取号：{get_number_url}
            if(oldUrl.indexOf("{get_number_url}") != -1) {
                oldUrl = oldUrl.replaceAll("\\{get_number_url\\}", projectBasePath+"/act/weixin_jzqh.do?unit_id="+unitId+"&open_id="+openId);
            }
            //替换所有的 手机交费：{my_pays_url}
            if(oldUrl.indexOf("{my_pays_url}") != -1) {
                oldUrl = oldUrl.replaceAll("\\{my_pays_url\\}", projectBasePath+"/act/weixin_mzjf.do?unit_id="+unitId+"&open_id="+openId);
            }
            //替换所有的 检验检查新：{checks_new_url}
            if(oldUrl.indexOf("{checks_new_url}") != -1) {
                oldUrl = oldUrl.replaceAll("\\{checks_new_url\\}", projectBasePath+"/newrep/main.do?unit_id="+unitId+"&open_id="+openId);
            }
            //替换所有的 检验检查旧：{checks_old_url}
            if(oldUrl.indexOf("{checks_old_url}") != -1) {
                oldUrl = oldUrl.replaceAll("\\{checks_old_url\\}", projectBasePath+"/act/responseReport.do?unit_id="+unitId+"&open_id="+openId);
            }
            //替换所有的 我的住院：{my_hospital_url}
            if(oldUrl.indexOf("{my_hospital_url}") != -1) {
                oldUrl = oldUrl.replaceAll("\\{my_hospital_url\\}", projectBasePath+"/hos/index.do?unit_id="+unitId+"&open_id="+openId);
            }
            //替换所有的 {domain}
            if(oldUrl.indexOf("{domain}") != -1) {
                String[] tempArr = oldUrl.split("href=\"");
                for (int i = 0; i < tempArr.length; i++) {
                    String temp = tempArr[i];
                    if(i==0){
                        newUrl += temp ;
                    }else{
                        if(temp.indexOf("{domain}") != -1){
                            String[] tempArr1 = temp.split("\"");
                            String temp1 = "";
                            for (int j = 0; j < tempArr1.length; j++) {
                                if(j==0){
                                    temp1 += tempArr1[j].replace("{domain}", projectBasePath);
                                }else{
                                    temp1 += "\""+tempArr1[j];
                                }
                            }
                            temp = temp1;
                        }
                        newUrl += "href=\""+temp ;
                    }
                }
            }else {
                newUrl = oldUrl;
            }
        }
        return newUrl;
    }
}