/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package com.nykj.wxisalipaygw.dispatcher;

import com.alipay.api.internal.util.StringUtils;
import com.nykj.wxisalipaygw.constants.AlipayServiceEventConstants;
import com.nykj.wxisalipaygw.executor.*;
import com.nykj.wxisalipaygw.service.ChannelService;
import com.nykj.wxisalipaygw.service.alipay.UnitInfoService;
import com.nykj.wxisalipaygw.service.alipay.UserQuestionService;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import java.util.HashMap;

/**
 * 业务动作分发器
 * 
 * @author taixu.zqq
 * @version $Id: Dispatcher.java, v 0.1 2014年7月24日 下午4:59:58 taixu.zqq Exp $
 */
@Controller
public class Dispatcher {
    private static final Logger LOGGER = Logger.getLogger(Dispatcher.class);
    private static HashMap<String,String> unitNameMap = new HashMap<String, String>();

    @Autowired
    private static UserQuestionService userQuestionService;

    @Autowired
    private static UnitInfoService unitInfoService;

    @Autowired
    private static ChannelService channelService;

    /**
     * 根据业务参数获取业务执行器
     * 
     * @param alipayBizBody
     * @return
     * @throws Exception
     */
    public static ActionExecutor getExecutor(JSONObject alipayBizBody,JSONObject alipayResponseBody) throws Exception {

        if(alipayBizBody == null){
            throw new Exception("业务体为空,业务处理终止");
        }
        if(alipayResponseBody == null){
            throw new Exception("响应体为空,业务处理终止");
        }

        //获取消息类型信息
        String msgType = alipayBizBody.getString("MsgType");
        if (StringUtils.isEmpty(msgType)) {
            throw new Exception("无法取得消息类型");
        }

        String content = alipayBizBody.getString("Text");
        String openId = alipayBizBody.getString("FromAlipayUserId");
        String unitId = alipayBizBody.getString("unit_id");
        String projectBasePath = alipayBizBody.getString("project_base_path");
        String unitName = null;

        // 2.根据消息类型(msgType)进行执行器的分类转发
        //  2.1 纯文本聊天类型
        if ("text".equals(msgType)) {
            if(StringUtils.isEmpty(content)){
                return new InAlipayDefaultExecutor(alipayBizBody);
            }
            if(content.length() > 300){
                content = content.substring(0, 300);
            }
            alipayResponseBody.put("content",content);

            LOGGER.info("支付宝用户【" + openId + "】在服务窗进行提问【"+content+"】");

            //TODO 持久化患者提问信息至数据库[注意渠道标识的添加]
            userQuestionService.insertUserQuestion(unitId,content);

            unitName = getUnitNameByUnitId(unitId);

            //TODO 根据医院ID和提问内容得到回复内容
            String replyContent = userQuestionService.getAutoReplyContentByUnitIdAndContent(unitId,content);
            if(!StringUtils.isEmpty(replyContent)){
                //TODO 转义回复内容
                replyContent = changeUrl(replyContent, projectBasePath, unitId, unitName, openId);
            }

            //TODO 定义消息回复执行器
            return new InAlipayChatTextExecutor(alipayBizBody,alipayResponseBody);

            // 2.2 事件类型
        } else if ("event".equals(msgType)) {
            //TODO 上报用户的地理位置[redis缓存]

            String eventType = alipayBizBody.getString("EventType");
            if("follow".equals(eventType)){
                //TODO 关注事件逻辑处理
                unitName = getUnitNameByUnitId(unitId);

                //TODO 统计用户关注事件
                channelService.insertChannelStatistics(unitId,alipayBizBody);

                //TODO 根据医院ID和提问内容得到回复内容
                String replyContent = userQuestionService.getAutoReplyContent(unitId,AlipayServiceEventConstants.AUTO_REPLY_TRIGGER_TYPE_FOLLOW);
                if(!StringUtils.isEmpty(replyContent)){
                    //TODO 转义回复内容
                    replyContent = changeUrl(replyContent, alipayBizBody.getString("projectBasePath"), unitId, unitName, openId);
                    alipayResponseBody.put("content",replyContent);
                    //TODO 定义消息回复执行器
                    return new InAlipayChatTextExecutor(alipayBizBody,alipayResponseBody);
                }

            }else if("enter".equals(eventType)){
                //TODO 统计用户扫码事件
                channelService.insertChannelStatistics(unitId,alipayBizBody);
            }
            return new InAlipayDefaultExecutor(alipayBizBody);
        } else {

            // 2.3 后续支付宝还会新增其他类型，因此默认返回ack应答
            return new InAlipayDefaultExecutor(alipayBizBody);
        }

    }

    //获取医院名称
    public static String getUnitNameByUnitId(String unitId){
        String unitName = unitNameMap.get(unitId);
        if(unitName == null){
            //TODO 查询医院名称
            unitName = unitInfoService.findUnitNameByUnitId(unitId);
            unitNameMap.put(unitId,unitName);
        }
        return unitName;
    }

    //转义推送用户消息
    private static String changeUrl(String oldUrl, String projectBasePath, String unitId, String unitName, String openId) {
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