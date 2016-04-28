/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package com.nykj.wxisalipaygw.dispatcher;

import com.alipay.api.internal.util.StringUtils;
import com.nykj.wxisalipaygw.common.MyException;
import com.nykj.wxisalipaygw.constants.AlipayServiceEventConstants;
import com.nykj.wxisalipaygw.constants.AlipayServiceNameConstants;
import com.nykj.wxisalipaygw.entity.alipay.UserQuestion;
import com.nykj.wxisalipaygw.executor.*;
import com.nykj.wxisalipaygw.service.alipay.UnitInfoService;
import com.nykj.wxisalipaygw.service.alipay.UserQuestionService;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

    /**
     * 根据业务参数获取业务执行器
     * 
     * @param params
     * @return
     * @throws MyException
     */
    public static ActionExecutor getExecutor(Map<String, String> params) throws Exception {
        //获取服务信息
        String service = params.get("service");
        if (StringUtils.isEmpty(service)) {
            throw new Exception("无法取得服务名");
        }

        //获取医院信息
        String unitId = params.get("unit_id");
        if (StringUtils.isEmpty(service)) {
            throw new Exception("无法取得医院id");
        }

        //获取内容信息
        String bizContent = params.get("biz_content");
        if (StringUtils.isEmpty(bizContent)) {
            throw new Exception("无法取得业务内容信息");
        }

        //将XML转化成json对象
        JSONObject bizContentJson = (JSONObject) new XMLSerializer().read(bizContent);

        //获取消息类型信息
        String msgType = bizContentJson.getString("MsgType");
        if (StringUtils.isEmpty(msgType)) {
            throw new Exception("无法取得消息类型");
        }

        String content = bizContentJson.getString("Text");
        String openId = bizContentJson.getString("FromAlipayUserId");
        String unitName = null;

        // 2.根据消息类型(msgType)进行执行器的分类转发
        //  2.1 纯文本聊天类型
        if ("text".equals(msgType)) {
            if(StringUtils.isEmpty(content)){
                return new InAlipayDefaultExecutor(bizContentJson);
            }
            if(content.length() > 300){
                content = content.substring(0, 300);
            }
            System.out.println("支付宝用户【" + openId + "】在服务窗进行提问【"+content+"】");

            //TODO 持久化患者提问信息至数据库[注意渠道标识的添加]
            UserQuestion userQuestion = new UserQuestion();
            userQuestion.setUnitId(unitId);
            userQuestion.setUserQuestion(content);
            userQuestion.setCreateTime(new Date());
            userQuestionService.insertUserQuestion(userQuestion);

            unitName = getUnitNameByUnitId(unitId);

            //TODO 根据医院ID和提问内容得到回复内容
            String replyContent = userQuestionService.getAutoReplyContentByUnitIdAndContent(unitId,content);
            if(!StringUtils.isEmpty(replyContent)){
                //TODO 转义回复内容
                replyContent = changeUrl(replyContent, params.get("projectBasePath"), unitId, unitName, openId);
            }

            //TODO 定义消息回复执行器
            return new InAlipayChatTextExecutor(bizContentJson,content);

            // 2.2 事件类型
        } else if ("event".equals(msgType)) {
            String eventType = params.get("EventType");
            if("follow".equals(eventType)){
                //TODO 关注事件逻辑处理
                unitName = getUnitNameByUnitId(unitId);

                //TODO 根据医院ID和提问内容得到回复内容
                String replyContent = userQuestionService.getAutoReplyContent(unitId,AlipayServiceEventConstants.AUTO_REPLY_TRIGGER_TYPE_FOLLOW);
                if(!StringUtils.isEmpty(replyContent)){
                    //TODO 转义回复内容
                }
                //TODO 定义消息回复执行器
                //TODO 统计用户关注事件

            }else if("enter".equals(eventType)){
                //TODO 统计用户扫码事件

            }
            //TODO 上报用户的地理位置

            return getEventExecutor(service, bizContentJson);

        } else {

            // 2.3 后续支付宝还会新增其他类型，因此默认返回ack应答
            return new InAlipayDefaultExecutor(bizContentJson);
        }

    }

    /**
     * 根据事件类型细化查找对应执行器
     * 
     * @param service
     * @param bizContentJson
     * @return
     * @throws MyException
     */
    private static ActionExecutor getEventExecutor(String service, JSONObject bizContentJson)
                                                                                             throws MyException {
        // 1. 获取事件类型
        String eventType = bizContentJson.getString("EventType");

        if (StringUtils.isEmpty(eventType)) {
            throw new MyException("无法取得事件类型");
        }

        // 2.根据事件类型再次区分服务类型
        // 2.1 激活验证开发者模式
        if (AlipayServiceNameConstants.ALIPAY_CHECK_SERVICE.equals(service)
            && eventType.equals(AlipayServiceEventConstants.VERIFYGW_EVENT)) {

            return new InAlipayVerifyExecutor();

            // 2.2 其他消息通知类 
        } else if (AlipayServiceNameConstants.ALIPAY_PUBLIC_MESSAGE_NOTIFY.equals(service)) {

            return getMsgNotifyExecutor(eventType, bizContentJson);

            // 2.3 对于后续支付宝可能新增的类型，统一默认返回AKC响应
        } else {
            return new InAlipayDefaultExecutor(bizContentJson);
        }
    }

    /**
     * 根据事件类型(eventType)进行执行器的分类转发
     * 
     * @param eventType
     * @param bizContentJson
     * @return
     * @throws MyException
     */
    private static ActionExecutor getMsgNotifyExecutor(String eventType, JSONObject bizContentJson)
                                                                                                   throws MyException {
        if (eventType.equals(AlipayServiceEventConstants.FOLLOW_EVENT)) {

            // 服务窗关注事件
            return new InAlipayFollowExecutor(bizContentJson);

        } else if (eventType.equals(AlipayServiceEventConstants.UNFOLLOW_EVENT)) {

            //  服务窗取消关注事件
            return new InAlipayUnFollowExecutor(bizContentJson);

            //根据actionParam进行执行器的转发
        } else if (eventType.equals(AlipayServiceEventConstants.CLICK_EVENT)) {

            // 点击事件
            return getClickEventExecutor(bizContentJson);

        } else if (eventType.equals(AlipayServiceEventConstants.ENTER_EVENT)) {

            // 进入事件
            return getEnterEventTypeExecutor(bizContentJson);

        } else {

            // 对于后续支付宝可能新增的类型，统一默认返回AKC响应
            return new InAlipayDefaultExecutor(bizContentJson);
        }

    }

    /**
     * 进入事件执行器
     * 
     * @param bizContentJson
     * @return
     */
    private static ActionExecutor getEnterEventTypeExecutor(JSONObject bizContentJson) {
        try {

            JSONObject param = JSONObject.fromObject(bizContentJson.get("ActionParam"));
            JSONObject scene = JSONObject.fromObject(param.get("scene"));

            if (!StringUtils.isEmpty(scene.getString("sceneId"))) {

                //自定义场景参数进入服务窗事件
                return new InAlipayDIYQRCodeEnterExecutor(bizContentJson);
            } else {

                //普通进入服务窗事件
                return new InAlipayEnterExecutor(bizContentJson);
            }
        } catch (Exception exception) {
            //无法解析sceneId的情况作为普通进入服务窗事件
            return new InAlipayEnterExecutor(bizContentJson);
        }
    }

    /**
     * 点击事件执行器
     * 
     * @param bizContentJson
     * @return
     */
    private static ActionExecutor getClickEventExecutor(JSONObject bizContentJson) {

        String actionParam = bizContentJson.getString("ActionParam");

        if ("authentication".equals(actionParam)) {

            //申请开发者会员绑定事件:  actionParam支付宝服务窗固定值
            //TODO 开发者自行实现，开发者若不需要绑定开发者会员，可无需实现
            return null;
        } else if ("delete".equals(actionParam)) {

            //删除开发者会员绑定事件：actionParam支付宝服务窗固定值
            //TODO 开发者自行实现，开发者若不需要绑定开发者会员，可无需实现
            return null;

            // 除上述支付宝保留key外，其他key为开发者配置菜单时自定义
        } else {

            //服务窗菜单点击事件：actionParam开发者可以自定义，除不能与authentication和delete重复及特殊字符外,没有其他要求
            //"async_image_text"即为开发者自定义的异步发送图文消息的菜单key，这里只是个样例而已
            if ("async_image_text".equals(actionParam)) {

                // 根据配置的菜单具体含义，开发者进行业务应答，这里只是个样例
                return new InAlipayAsyncMsgSendExecutor(bizContentJson);

                // 其他菜单key请开发者自行补充,并执行开发相应响应
            } else if ("xxx".equals(actionParam)) {

                // TODO 开发者根据key自行开发相应响应，这里只是个样例
                return null;
            } else {
                // TODO 开发者根据key自行开发相应响应，这里只是个样例
                return null;
            }
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