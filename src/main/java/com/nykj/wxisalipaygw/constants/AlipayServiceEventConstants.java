/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package com.nykj.wxisalipaygw.constants;

/**
 * 支付宝服务窗事件常量
 * 
 * @author taixu.zqq
 * @version $Id: AlipayServiceEvent.java, v 0.1 2014年7月24日 下午1:40:04 taixu.zqq Exp $
 */
public class AlipayServiceEventConstants {

    /**网关验证事件*/
    public static final String VERIFYGW_EVENT = "verifygw";

    /**服务窗关注事件*/
    public static final String FOLLOW_EVENT   = "follow";

    /**服务窗取消关注事件*/
    public static final String UNFOLLOW_EVENT = "unfollow";

    /**服务窗菜单点击事件*/
    public static final String CLICK_EVENT    = "click";

    /**服务窗进入事件*/
    public static final String ENTER_EVENT = "enter";

    /** 智能客服：触发类型  默认  1 */
    public static int AUTO_REPLY_TRIGGER_TYPE_DEFAULT = 1;
    /** 智能客服：触发类型  关键字  2 */
    public static int AUTO_REPLY_TRIGGER_TYPE_KEY = 2;
    /** 智能客服：触发类型  常见问题编号  3 */
    public static int AUTO_REPLY_TRIGGER_TYPE_QUESTIONNUM = 3;
    /** 智能客服：触发类型  用户关注  4 */
    public static int AUTO_REPLY_TRIGGER_TYPE_FOLLOW = 4;
}
