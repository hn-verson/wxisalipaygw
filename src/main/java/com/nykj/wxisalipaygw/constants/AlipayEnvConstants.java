package com.nykj.wxisalipaygw.constants;

/**
 * Created by Verson on 2016/4/29.
 */
public class AlipayEnvConstants {
    /**签名编码-视支付宝服务窗要求*/
    public static final String SIGN_CHARSET      = "GBK";

    /**字符编码-传递给支付宝的数据编码*/
    public static final String CHARSET           = "GBK";

    /**签名类型-视支付宝服务窗要求*/
    public static final String SIGN_TYPE         = "RSA";

    /**授权码方式获取访问令牌*/
    public static final String GRANT_TYPE_AUTHORIZATION_CODE = "authorization_code";

    /**刷新令牌方式获取访问令牌**/
    public static final String GRANT_TYPE_REFRESH_TOKEN = "refresh_token";

    /** 文本格式 **/
    public static final String FORMAT            = "json";

    /**开发者网关验证服务*/
    public static final String ALIPAY_CHECK_SERVICE         = "alipay.service.check";

    /**支付宝消息通知服务*/
    public static final String ALIPAY_PUBLIC_MESSAGE_NOTIFY = "alipay.mobile.public.message.notify";

    /** 支付宝社保卡绑定服务 **/
    public static final String ALIPAY_MEDICAL_INST_CARD_BIND = "alipay.commerce.medical.instcard.bind";

    /** 支付宝社保卡查询服务 **/
    public static final String ALIPAY_MEDICAL_INST_CARD_QUERY = "alipay.commerce.medical.card.query";

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

    /** 渠道标识 **/
    public static int CHANNEL_ID = 2;

    /** 接口请求成功标记 **/
    public final static int API_ACESS_SUCCESS_FLAG = 0;

}