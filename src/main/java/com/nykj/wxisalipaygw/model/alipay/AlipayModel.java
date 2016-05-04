package com.nykj.wxisalipaygw.model.alipay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayMobilePublicMessageCustomSendRequest;
import com.alipay.api.response.AlipayMobilePublicMessageCustomSendResponse;
import com.nykj.wxisalipaygw.constants.AlipayConstants;
import com.nykj.wxisalipaygw.entity.alipay.UnitLink;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import java.util.Calendar;

/**
 * Created by Verson on 2016/4/29.
 */
@Repository
public class AlipayModel {
    private static final Logger LOGGER = Logger.getLogger(AlipayModel.class);
    /**
     * 获得API调用客户端
     *
     * @return
     */
    public AlipayClient getAlipayClient(UnitLink unitLink){
        AlipayClient alipayClient = new DefaultAlipayClient(unitLink.getSrv_url(), unitLink.getApp_id(),
                unitLink.getApp_secret(), AlipayConstants.FORMAT, AlipayConstants.CHARSET,unitLink.getSrv_token());
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
        builder.append("<biz_content>").append(unitLink.getSrv_aeskey())
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

    //转义推送用户消息
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