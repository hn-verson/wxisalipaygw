

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





    public static final String PARTNER           = "2088121475832528";


    /** 服务窗appId  */

    //TODO !!!! 注：该appId必须设为开发者自己的服务窗id  这里只是个测试id

    public static final String APP_ID            = "2016042101320485";


    //开发者请使用openssl生成的密钥替换此处  请看文档：https://fuwu.alipay.com/platform/doc.htm#2-1接入指南

    //TODO !!!! 注：该私钥为测试账号私钥  开发者必须设置自己的私钥 , 否则会存在安全隐患

    public static final String PRIVATE_KEY       = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKQLYbxT4GVYQpmCk+4s6edsW5pDbwgjn5UwG8KJDF0wP/McYnoPYb8AD/zyidRZ/7GR3fEcF6jVDGPtyVKGnX5fvuvNgDZDAT8at9C35MKzHJpYnl/B8nP0rQUxFnqrkdoHmTMqx3WBWlGI1jnmT0Or46Wm4PkS0yWBt4RwVYAnAgMBAAECgYBD7HKLiC9hxynMZkTaWb6AFF3ZghJ3TEjHfSQBaL212ESiZR5ha/0NGPmFwudPBfTZkYxUSU2CmrSGq+6wPQz4nLUdqprhipHKVcWSqmDtQdiiktgRZB+HvV8jzNf2K+82PMUsAMeTP8Cq42qGmhQ/40d8DUFmGpcjWdUV1VSQAQJBANiY4QmDhMft1aZFb2CZy24ODCWZCqm+1bn0VAEXkFIKqQjKLTPaEr/RT+gUMRFpWr2ZIeN1hJQnm6SP3yK6MCUCQQDB4xOF3mPYLS1O3N3ep7lbV0S7hjK3IucDaUjsacFBoNTOjNPLkqF6tK33f9On9yyiJWORVKGDhrpOvFP/UedbAkEAl03sXe3+0n7k8XqtvCiTQDUV7SnEliFWQclY6U5hpb4GqLo6627aDB8Wl8OLJ2OWXL3dO3y7eZdVlZZfRG4V9QJAP10gwj090CZjikRwVhXmY82PbuGarXNAoELGTEz7laEkKySnCv6uu1VVDW+/EkObWJpzgclwx3U+agDuSqDPxQJBAMSqldi5ykMI9QvPVbwxK82Gpni3/zeRmqeLRhBBtGtYpDLCK8csKjzH+uFEa248nPzVDeptVbARvm16A3Lq3mY=";


    //TODO !!!! 注：该公钥为测试账号公钥  开发者必须设置自己的公钥 ,否则会存在安全隐患

    public static final String PUBLIC_KEY        = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCkC2G8U+BlWEKZgpPuLOnnbFuaQ28II5+VMBvCiQxdMD/zHGJ6D2G/AA/88onUWf+xkd3xHBeo1Qxj7clShp1+X77rzYA2QwE/GrfQt+TCsxyaWJ5fwfJz9K0FMRZ6q5HaB5kzKsd1gVpRiNY55k9Dq+OlpuD5EtMlgbeEcFWAJwIDAQAB";


    /**支付宝网关*/

    public static final String ALIPAY_GATEWAY    = "https://openapi.alipay.com/gateway.do";


    /**授权访问令牌的授权类型*/

    public static final String GRANT_TYPE        = "authorization_code";

    /** 文本格式 **/
    public static final String FORMAT            = "json";

}