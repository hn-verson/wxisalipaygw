package com.nykj.wxisalipaygw.util;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.internal.util.StringUtils;
import com.alipay.api.request.AlipayMobilePublicGisGetRequest;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.request.AlipayUserUserinfoShareRequest;
import com.alipay.api.response.AlipayMobilePublicGisGetResponse;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserUserinfoShareResponse;
import com.nykj.wxisalipaygw.constants.AlipayServiceEnvConstants;
import com.nykj.wxisalipaygw.entity.alipay.UnitLink;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import java.util.Calendar;
import java.util.Map;

import org.apache.log4j.Logger;

public class AlipayUtil {

    private static final Logger LOGGER = Logger.getLogger(AlipayUtil.class);

    public static String getAlipayOpenId(JSONObject alipayBizBody) {
        try {
            if (alipayBizBody == null) {
                LOGGER.info("业务体参数为空,执行终止");
                return null;
            }

            UnitLink unitLink = (UnitLink) alipayBizBody.get("unit_link");
            if (unitLink == null) {
                LOGGER.info("医院服务窗配置信息为空,执行终止");
                return null;
            }

            String authCode = alipayBizBody.getString("auth_code");
            String scope = alipayBizBody.getString("scope");

            if (StringUtils.isEmpty(authCode) || StringUtils.isEmpty(scope)) {
                LOGGER.info("参数认证失败,auth_code:" + authCode + ",scope:" + scope + ",执行终止");
                return null;
            }

            AlipayClient alipayClient = AlipayUtil.getAlipayClient(unitLink);

            AlipaySystemOauthTokenRequest oauthTokenRequest = new AlipaySystemOauthTokenRequest();
            oauthTokenRequest.setCode(authCode);
            oauthTokenRequest.setGrantType(AlipayServiceEnvConstants.GRANT_TYPE);
            AlipaySystemOauthTokenResponse oauthTokenResponse = alipayClient.execute(oauthTokenRequest);

            // 成功获得authToken
            if (null != oauthTokenResponse && oauthTokenResponse.isSuccess()) {
                return oauthTokenResponse.getUserId();
            }
        } catch (Exception e) {
            LOGGER.info("获取用户openId异常");
        }
        return null;
    }

    /**
     * 获得API调用客户端
     *
     * @return
     */
    public static AlipayClient getAlipayClient(UnitLink unitLink){
        AlipayClient alipayClient = new DefaultAlipayClient(unitLink.getSrv_url(), unitLink.getApp_id(),
                unitLink.getApp_secret(), AlipayServiceEnvConstants.FORMAT, AlipayServiceEnvConstants.CHARSET,unitLink.getSrv_token());
        return alipayClient;
    }

    /**
     * 获得服务窗用户信息
     *
     * @return
     */
    public static AlipayUserUserinfoShareResponse getAlipayUserInfo(JSONObject alipayBizBody) {
        AlipayUserUserinfoShareRequest userinfoShareRequest = new AlipayUserUserinfoShareRequest();
        AlipayUserUserinfoShareResponse userinfoShareResponse = null;
        
        try {
            AlipaySystemOauthTokenRequest oauthTokenRequest = new AlipaySystemOauthTokenRequest();
            oauthTokenRequest.setCode(alipayBizBody.getString("auth_code"));
            UnitLink unitLink = (UnitLink) alipayBizBody.get("unit_link");
            oauthTokenRequest.setGrantType(AlipayServiceEnvConstants.GRANT_TYPE);
            AlipayClient alipayClient = AlipayUtil.getAlipayClient(unitLink);
            if (alipayClient != null) {
                AlipaySystemOauthTokenResponse oauthTokenResponse = alipayClient.execute(oauthTokenRequest);
                
                //用户accessToken获取用户信息
                if (null != oauthTokenResponse && oauthTokenResponse.isSuccess()) {
                    userinfoShareResponse = alipayClient.execute(userinfoShareRequest, oauthTokenResponse.getAccessToken());
                    if (null == userinfoShareResponse || !userinfoShareResponse.isSuccess()) {
                        return null;
                    }
                } 
            } else {
                LOGGER.debug("AlipayUtil 生成AlipayClient失败，医院id为空");
            }
        } catch (Exception e) {
            LOGGER.error("获取用户信息异常: " + e);
        }
        
        return userinfoShareResponse;
    }

    /**
     * 获取用户地理位置
     */
    public static String getUserGisInfo(JSONObject alipayBizBody) {
        try {
            if (alipayBizBody == null) {
                LOGGER.info("服务窗业务体为空，终止执行");
            }
            
            AlipayMobilePublicGisGetRequest request = new AlipayMobilePublicGisGetRequest();
            String unitId = alipayBizBody.getString("unit_id");
            String openId = alipayBizBody.getString("FromAlipayUserId");
            UnitLink unitLink = (UnitLink) alipayBizBody.get("unit_link");

            request.setBizContent("{'userId':'" + openId + "'}");
            AlipayMobilePublicGisGetResponse response = null;
            AlipayClient alipayClient = AlipayUtil.getAlipayClient(unitLink);

            if (alipayClient == null) {
                LOGGER.info("获取医院【" + unitId + "】的服务窗客户端失败");
                return null;
            }

            response = alipayClient.execute(request);
            if (response != null && response.isSuccess()) {
                return response.getLongitude() + "," + response.getLatitude();
            }
        } catch (Exception e) {
            LOGGER.error("获取用户地理位置失败:" + e);
        }
        return null;
    }

    /**
     * 构造基础的响应消息
     *
     * @return
     */
    public static String buildBaseAckMsg(JSONObject alipayBizBody) throws Exception{
        StringBuilder sb = new StringBuilder();
        String userId = alipayBizBody.getString("FromAlipayUserId");
        UnitLink unitLink = (UnitLink)alipayBizBody.get("unit_link");
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
     *
     * @param fromUserId
     * @return
     */
    public static String buildSingleTextMsg(String fromUserId,String content) {

        StringBuilder sb = new StringBuilder();

        //构建json格式单发纯文本消息体
        sb.append("{'msgType':'text','text':{'"+content+"':'这是纯文本消息'}, 'toUserId':'" + fromUserId
                + "'}");

        return sb.toString();
    }

    /**
     * 验签
     *
     * @param
     * @return
     */
    public static void verifySign(Map<String, String> params,JSONObject alipayBizBody) throws AlipayApiException {
        JSONObject unitLinkJsonObj = (JSONObject)alipayBizBody.get("unit_link");
        UnitLink unitLink = (UnitLink) JSONObject.toBean(unitLinkJsonObj,UnitLink.class);

        if(unitLink == null){
            throw new AlipayApiException("未获取医院服务窗配置，验签失败");
        }

        //医院对应服务窗配置信息从业务体中获取
        /*if (!AlipaySignature.rsaCheckV2(params, unitLink.getSrv_token(),
                AlipayServiceEnvConstants.SIGN_CHARSET)) {

            throw new AlipayApiException("验签失败");
        }*/
        if (!AlipaySignature.rsaCheckV2(params, AlipayServiceEnvConstants.ALIPAY_PUBLIC_KEY,
                AlipayServiceEnvConstants.SIGN_CHARSET)) {

            throw new AlipayApiException("验签失败");
        }

    }
}