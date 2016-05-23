package com.nykj.wxisalipaygw.service.alipay;

import com.alipay.api.AlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.internal.util.StringUtils;
import com.alipay.api.request.AlipayMobilePublicGisGetRequest;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.request.AlipayUserUserinfoShareRequest;
import com.alipay.api.response.AlipayMobilePublicGisGetResponse;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserUserinfoShareResponse;
import com.nykj.wxisalipaygw.constants.AlipayEnvConstants;
import com.nykj.wxisalipaygw.constants.GlobalConstants;
import com.nykj.wxisalipaygw.constants.StatusCode;
import com.nykj.wxisalipaygw.entity.alipay.AlipayMedicalCard;
import com.nykj.wxisalipaygw.entity.alipay.AlipayUserInfo;
import com.nykj.wxisalipaygw.entity.alipay.UnitLink;
import com.nykj.wxisalipaygw.exception.ApiCallException;
import com.nykj.wxisalipaygw.exception.ArgumentException;
import com.nykj.wxisalipaygw.exception.DatabaseAccessException;
import com.nykj.wxisalipaygw.model.alipay.AlipayModel;
import com.nykj.wxisalipaygw.service.ChannelService;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Verson on 2016/4/29.
 */
@Service
@Scope("prototype")
public class AlipayService {
    private static final Logger LOGGER = Logger.getLogger(AlipayService.class);

    @Autowired
    private AlipayModel alipayModel;
    @Autowired
    private UnitInfoService unitInfoService;
    @Autowired
    private UserQuestionService userQuestionService;
    @Autowired
    private ChannelService channelService;

    private static HashMap<String,String> unitNameMap = new HashMap<String, String>();

    private static ExecutorService executors = Executors.newFixedThreadPool(GlobalConstants.THREAD_POOL_MAX_SIZE);

    /**
     * 处理服务窗回调业务
     * @param alipayBizBody
     * @return 响应体
     */
    public String handlerCallBack(JSONObject alipayBizBody) throws Exception{
        if(alipayBizBody == null){
            throw new NullPointerException("业务体为空,业务处理终止");
        }

        JSONObject bizContentJson = (JSONObject) alipayBizBody.get("biz_content");
        if (!bizContentJson.has("MsgType")) {
            throw new ArgumentException(StatusCode.ARGUMENT_EXCEPTION,"无法取得消息类型");
        }

        String msgType = bizContentJson.getString("MsgType");
        JSONObject textJson = bizContentJson.has("Text") ? (JSONObject)bizContentJson.get("Text") : null;
        String content = (textJson != null && textJson.has("Content")) ? textJson.getString("Content") : null;
        String openId = bizContentJson.has("FromAlipayUserId") ? bizContentJson.getString("FromAlipayUserId") : null;

        String unitId = alipayBizBody.has("unit_id") ? alipayBizBody.getString("unit_id") : null;
        String unitName = null;

        // 2.根据消息类型(msgType)进行执行器的分类转发
        // 2.1 纯文本聊天类型
        if ("text".equals(msgType)) {
            if(StringUtils.isEmpty(content)){
                return alipayModel.buildBaseAckMsg(alipayBizBody);
            }
            if(content.length() > 300){
                content = content.substring(0, 300);
            }

            LOGGER.info("支付宝用户【" + openId + "】在服务窗进行提问【"+content+"】");

            //持久化患者提问信息至数据库[注意渠道标识的添加]
            userQuestionService.insertUserQuestion(unitId,content);

            unitName = getUnitNameByUnitId(unitId);

            //根据医院ID和提问内容得到回复内容
            String replyContent = userQuestionService.getAutoReplyContentByUnitIdAndContent(unitId,content);
            if(!StringUtils.isEmpty(replyContent)){
                replyContent = alipayModel.changeUrl(replyContent, GlobalConstants.WXISH5_PROJECT_BASE_PATH, unitId, unitName, openId);
                //将回复内容组装至业务体中
                alipayBizBody.put("reply_content",replyContent);
                //获取消息发送线程实例
                Runnable r = alipayModel.buildSingleTextMsgSendThread(alipayBizBody);
                //异步消息发送
                executors.execute(r);
            }

            return alipayModel.buildBaseAckMsg(alipayBizBody);

            // 2.2 事件类型
        } else if ("event".equals(msgType)) {
            String eventType = bizContentJson.getString("EventType");
            if(AlipayEnvConstants.FOLLOW_EVENT.equals(eventType)){
                unitName = getUnitNameByUnitId(unitId);

                //统计用户关注事件
                channelService.insertChannelStatistics(alipayBizBody);

                //根据医院ID和提问内容得到回复内容
                String replyContent = userQuestionService.getAutoReplyContent(unitId, AlipayEnvConstants.AUTO_REPLY_TRIGGER_TYPE_FOLLOW);
                if(!StringUtils.isEmpty(replyContent)){
                    //转义回复内容
                    replyContent = alipayModel.changeUrl(replyContent, GlobalConstants.WXISH5_PROJECT_BASE_PATH, unitId, unitName, openId);
                    alipayBizBody.put("reply_content",replyContent);

                    //获取消息发送线程实例
                    Runnable r = alipayModel.buildSingleTextMsgSendThread(alipayBizBody);
                    //异步消息发送
                    executors.execute(r);

                    return alipayModel.buildBaseAckMsg(alipayBizBody);
                }

            }else if(AlipayEnvConstants.ENTER_EVENT.equals(eventType)){
                //统计用户扫码事件
                channelService.insertChannelStatistics(alipayBizBody);
            }else if(AlipayEnvConstants.VERIFYGW_EVENT.equals(eventType)){
                //网关认证事件
                return alipayModel.buildSucessResponse(alipayBizBody);
            }
            return alipayModel.buildBaseAckMsg(alipayBizBody);
        }

        // 2.3 后续支付宝新增的其他类型，默认返回ack应答
        return alipayModel.buildBaseAckMsg(alipayBizBody);

    }

    /**
     * 获得服务窗用户信息
     *
     * @return
     */
    public AlipayUserInfo getAlipayUserInfo(JSONObject alipayBizBody) throws Exception{
        AlipayUserInfo alipayUserInfo = null;
        AlipayUserUserinfoShareRequest userinfoShareRequest = new AlipayUserUserinfoShareRequest();
        AlipayUserUserinfoShareResponse userinfoShareResponse = null;
        try {
            AlipaySystemOauthTokenRequest oauthTokenRequest = new AlipaySystemOauthTokenRequest();
            String authCode = alipayBizBody.getString("auth_code");
            oauthTokenRequest.setCode(authCode);
            JSONObject unitLinkJson = (JSONObject)alipayBizBody.get("unit_link");
            UnitLink unitLink = (UnitLink) JSONObject.toBean(unitLinkJson,UnitLink.class);
            oauthTokenRequest.setGrantType(AlipayEnvConstants.GRANT_TYPE_AUTHORIZATION_CODE);
            AlipayClient alipayClient = alipayModel.getAlipayClient(unitLink);
            if(alipayClient == null){
                LOGGER.info("获取医院【"+unitLink.getUnit_id()+"】的服务窗客户端失败");
                throw new DatabaseAccessException(StatusCode.DATABASE_ACCESS_EXCEPTION,"无法获取医院【"+unitLink.getUnit_id()+"】配置");
            }

            AlipaySystemOauthTokenResponse oauthTokenResponse = alipayClient.execute(oauthTokenRequest);

            //成功获得authToken
            if (null != oauthTokenResponse && oauthTokenResponse.isSuccess()) {
                String refreshToken = oauthTokenResponse.getRefreshToken();
                userinfoShareResponse = alipayClient.execute(userinfoShareRequest, oauthTokenResponse.getAccessToken());
                //成功获得用户信息
                if(userinfoShareResponse == null || userinfoShareResponse.isSuccess()){
                    throw new ApiCallException(StatusCode.API_CALL_EXCEPTION,"获取支付宝用户信息失败");
                }
                alipayUserInfo = new AlipayUserInfo();
                alipayUserInfo.setAvatar(userinfoShareResponse.getAvatar());
                alipayUserInfo.setNickName(userinfoShareResponse.getNickName());
                alipayUserInfo.setProvince(userinfoShareResponse.getProvince());
                alipayUserInfo.setCity(userinfoShareResponse.getCity());
                alipayUserInfo.setGender(userinfoShareResponse.getGender());
                alipayUserInfo.setUserTypeValue(userinfoShareResponse.getUserTypeValue());
                alipayUserInfo.setOpenId(userinfoShareResponse.getAlipayUserId());
                alipayUserInfo.setAuthCode(authCode);
                alipayUserInfo.setRefreshToken(refreshToken);
            }
        } catch (Exception e) {
            LOGGER.error("获取用户信息异常:" + e);
            throw e;
        }

        return alipayUserInfo;
    }

    /**
     * 获取用户地理位置信息
     * @param alipayBizBody
     * @return
     * @throws Exception
     */
    public String getUserGisInfo(JSONObject alipayBizBody) throws Exception{
        JSONObject gisJson = new JSONObject();
        JSONObject unitLinkJson = (JSONObject)alipayBizBody.get("unit_link");
        String openId = alipayBizBody.getString("open_id");
        UnitLink unitLink = (UnitLink) JSONObject.toBean(unitLinkJson,UnitLink.class);
        AlipayClient alipayClient = alipayModel.getAlipayClient(unitLink);

        AlipayMobilePublicGisGetRequest gisGetRequest = new AlipayMobilePublicGisGetRequest();
        JSONObject gisReqJson = new JSONObject();
        gisReqJson.put("userId",openId);
        gisGetRequest.setBizContent(gisReqJson.toString());
        AlipayMobilePublicGisGetResponse gisGetResponse = null ;
        gisGetResponse = alipayClient.execute(gisGetRequest);
        if(gisGetResponse == null || !gisGetResponse.isSuccess()){
            throw new ApiCallException(StatusCode.API_CALL_EXCEPTION,"获取用户【"+openId+"】的地理位置失败");
        }
        gisJson.put("longitude",gisGetResponse.getLongitude());
        gisJson.put("latitude",gisGetResponse.getLatitude());

        return gisJson.toString();
    }

    /**
     * 构建绑定社保卡请求url
     * @param alipayBizBody
     * @return
     * @throws Exception
     */
    public String buildMedicalInstCardBindUrl(JSONObject alipayBizBody) throws Exception{

        exchangeAccessToken(alipayBizBody);
        return alipayModel.buildMedicalInstCardBindUrl(alipayBizBody);

    }

    /**
     * 构建查询社保卡请求url
     * @param alipayBizBody
     * @return
     * @throws Exception
     */
    public AlipayMedicalCard queryMedicalInstCard(JSONObject alipayBizBody) throws Exception{
        return alipayModel.queryMedicalInstCard(alipayBizBody);
    }

    /**
     * 医保卡支付
     * @param alipayBizBody
     * @return
     */
    public String handlerMedicalCardPay(JSONObject alipayBizBody) throws Exception{
        exchangeAccessToken(alipayBizBody);
        //业务参数清洗
        JSONObject bizParamJson = JSONObject.fromObject(alipayBizBody.toString());
        if(bizParamJson.has("unit_id"))
            bizParamJson.discard("unit_id");
        if(bizParamJson.has("service"))
            bizParamJson.discard("service");
        if(bizParamJson.has("channel"))
            bizParamJson.discard("channel");
        if(bizParamJson.has("time")){
            bizParamJson.put("timestamp",bizParamJson.getString("time"));
            bizParamJson.discard("time");
        }
        if(bizParamJson.has("access_token")){
            bizParamJson.put("access_token",bizParamJson.getString("access_token"));
            bizParamJson.discard("refresh_token");
        }
        String resContent = alipayModel.handlerMedicalCardPay(bizParamJson);
        JSONObject resContentJson = JSONObject.fromObject(resContent);
        if(resContentJson.has("code") && AlipayEnvConstants.API_ACESS_SUCCESS_FLAG == resContentJson.getInt("code")){
            throw new ApiCallException(StatusCode.API_CALL_EXCEPTION,resContentJson.getString("message"));
        }
        return resContent;
    }

    /**
     * 获取访问令牌
     * @param alipayBizBody
     * @return
     * @throws Exception
     */
    public void exchangeAccessToken(JSONObject alipayBizBody) throws Exception{
        if(!alipayBizBody.has("auth_code") && !alipayBizBody.has("refresh_token")){
            throw new ArgumentException(StatusCode.ARGUMENT_EXCEPTION,"缺失授权码或刷新令牌");
        }
        String authCode = alipayBizBody.has("auth_code") ? alipayBizBody.getString("auth_code") : null;
        String refreshToken = alipayBizBody.has("refresh_token") ? alipayBizBody.getString("refresh_token") : null;
        JSONObject unitLinkJson = (JSONObject) alipayBizBody.get("unit_link");
        UnitLink unitLink = (UnitLink) JSONObject.toBean(unitLinkJson,UnitLink.class);

        AlipayClient alipayClient = alipayModel.getAlipayClient(unitLink);
        AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
        if(!StringUtils.isEmpty(authCode)){
            request.setGrantType(AlipayEnvConstants.GRANT_TYPE_AUTHORIZATION_CODE);
            request.setCode(authCode);
        }else{
            request.setGrantType(AlipayEnvConstants.GRANT_TYPE_REFRESH_TOKEN);
            request.setRefreshToken(refreshToken);
        }
        AlipaySystemOauthTokenResponse response = alipayClient.execute(request);
        if(request == null || !response.isSuccess()){
            throw new ApiCallException(StatusCode.API_CALL_EXCEPTION,"获取访问令牌失败");
        }

        //需要重置业务体refresh_token和access_token值
        alipayBizBody.put("refresh_token",response.getRefreshToken());
        alipayBizBody.put("access_token",response.getAccessToken());
    }

    /**
     * 验签
     * @param alipayBizBody
     * @throws Exception
     */
    public void verifySign(JSONObject alipayBizBody) throws Exception {
        JSONObject paramsJson = (JSONObject) alipayBizBody.get("params");
        Map<String,String> paramsMap = (HashMap<String,String>)JSONObject.toBean(paramsJson,HashMap.class);
        JSONObject unitLinkJsonObj = (JSONObject)alipayBizBody.get("unit_link");
        UnitLink unitLink = (UnitLink) JSONObject.toBean(unitLinkJsonObj,UnitLink.class);

        if(unitLink == null){
            throw new DatabaseAccessException(StatusCode.API_CALL_EXCEPTION,"未获取医院服务窗配置，验签失败");
        }

        //医院对应服务窗配置信息从业务体中获取
        if (!AlipaySignature.rsaCheckV2(paramsMap, unitLink.getAlipay_public_key(),
                AlipayEnvConstants.SIGN_CHARSET)) {

            throw new ApiCallException(StatusCode.API_CALL_EXCEPTION,"验签失败");
        }
    }

    /**
     * 加签
     * @param alipayBizBody
     * @return
     * @throws Exception
     */
    public String encryptAndSign(JSONObject alipayBizBody) throws Exception{
        JSONObject unitLinkJson = (JSONObject) alipayBizBody.get("unit_link");
        String responseMsg = alipayBizBody.getString("response_msg");
        UnitLink unitLink = (UnitLink)JSONObject.toBean(unitLinkJson,UnitLink.class);
        return AlipaySignature.encryptAndSign(responseMsg,
                unitLink.getAlipay_public_key(),
                unitLink.getPrivate_key(),
                AlipayEnvConstants.CHARSET,
                false, true);
    }

    /**
     * 获取医院名称
     * @param unitId
     * @return
     * @throws Exception
     */
    public String getUnitNameByUnitId(String unitId) throws Exception{
        String unitName = unitNameMap.get(unitId);
        if(unitName == null){
            //查询医院名称
            unitName = unitInfoService.findUnitNameByUnitId(unitId);
            unitNameMap.put(unitId,unitName);
        }
        return unitName;
    }

}