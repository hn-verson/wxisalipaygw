

/**

 * Alipay.com Inc.

 * Copyright (c) 2004-2014 All Rights Reserved.

 */

package com.nykj.wxisalipaygw.constants;


/**

 * 支付宝服务窗环境常量（demo中常量只是参考，需要修改成自己的常量值）

 *

 * @author taixu.zqq

 * @version $Id: AlipayServiceConstants.java, v 0.1 2014年7月24日 下午4:33:49 taixu.zqq Exp $

 */

public class AlipayServiceEnvConstants {


    /**支付宝公钥-从支付宝服务窗获取*/

    public static final String ALIPAY_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB";


    /**签名编码-视支付宝服务窗要求*/

    public static final String SIGN_CHARSET      = "GBK";


    /**字符编码-传递给支付宝的数据编码*/

    public static final String CHARSET           = "GBK";


    /**签名类型-视支付宝服务窗要求*/

    public static final String SIGN_TYPE         = "RSA";





    public static final String PARTNER           = "2088221705823174";


    /** 服务窗appId  */

    //TODO !!!! 注：该appId必须设为开发者自己的服务窗id  这里只是个测试id

    public static final String APP_ID            = "2016042101320522";


    //开发者请使用openssl生成的密钥替换此处  请看文档：https://fuwu.alipay.com/platform/doc.htm#2-1接入指南

    //TODO !!!! 注：该私钥为测试账号私钥  开发者必须设置自己的私钥 , 否则会存在安全隐患 

    public static final String PRIVATE_KEY       = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAPlrc9z148KwYPKcUur/1eVOizOBYjhN6x7BBPLN5w5TJIvBRHSvIWpJe8xA/a+Zu2jBCkib/GSYBf6gCPGVJ+NTqsgeiCl3B0UAIzADVtUNeEW9XFtwYCaLY8ce0PlhryvkHytq4EwgOhjeahlZ5ESS2JLKhPrBrPCnUF3K/T99AgMBAAECgYEAto1V9RCnjhSIMKoH6mYryLSvH5iAKhOllhHSLGBVz988rWlcUEbqHx//kySySLctOuOnQjOhDINUR+QcB62SzdbGtQ3suxgB2JaZIzLRyiRQgjZYpZJUBclxw/nOvw0abRlX5iT2WzyoVXOuU+qyRpe/UhrotBF3YGX9xfIdFcECQQD9Px4fapB3m+c7LixZULCDTTJQy8BtOTyJrJ3uPTYEn8q0WDuQXDTdWJ1f6bcbrEbWjAgXyTXbqi3c75tQqKENAkEA/CGu9OK/FX+36217crS+M7+AHOfQBQGQrGfnd4NFxiNFAiwj0LxlKlfyf6D34xq4s4ffwLTuu/19GRZQbT0cMQJBANOXOe7b1arqCvAKaymQ3npGNKUHKFbZ/Le3Dxlv8UM5016NXe8G63CJSqxJlSE89s7pnxVom2RWsw0m8WkaC70CQQCBVX373HyMuLFNsSGMG68duHQtX7RTfRQLfFk98zbNhrYVgTL2Fk4GxIp+ccH223Q3k9Fxdt6nzEZpogqrnpdhAkBn9p/MVeMgt4RwrV88K/GJ876VEXnSJoVixCT+s4HP9qXoTxH1yP0PamFiiWhEXbQ95R/CZkNPYj+0yuv7c7PG";


    //TODO !!!! 注：该公钥为测试账号公钥  开发者必须设置自己的公钥 ,否则会存在安全隐患

    public static final String PUBLIC_KEY        = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQD5a3Pc9ePCsGDynFLq/9XlToszgWI4TesewQTyzecOUySLwUR0ryFqSXvMQP2vmbtowQpIm/xkmAX+oAjxlSfjU6rIHogpdwdFACMwA1bVDXhFvVxbcGAmi2PHHtD5Ya8r5B8rauBMIDoY3moZWeREktiSyoT6wazwp1Bdyv0/fQIDAQAB";


    /**支付宝网关*/

    public static final String ALIPAY_GATEWAY    = "https://openapi.alipay.com/gateway.do";


    /**授权访问令牌的授权类型*/

    public static final String GRANT_TYPE        = "authorization_code";

}