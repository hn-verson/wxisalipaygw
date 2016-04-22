

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

    

    

    public static final String PARTNER           = "2088302104513991";


    /** 服务窗appId  */

    //TODO !!!! 注：该appId必须设为开发者自己的服务窗id  这里只是个测试id

    public static final String APP_ID            = "2016042101320449";


    //开发者请使用openssl生成的密钥替换此处  请看文档：https://fuwu.alipay.com/platform/doc.htm#2-1接入指南

    //TODO !!!! 注：该私钥为测试账号私钥  开发者必须设置自己的私钥 , 否则会存在安全隐患 

//    public static final String PRIVATE_KEY       = "MIICXQIBAAKBgQDFIUM4MWCHZ08volqtO/1QRYhvZsg1qjXONo38/UMuuvOyRCG75JSSBdG/vwuwpjxd2s54xQIfioNy1TVCXPcfOw5oNBbD58IjKRYt0UXQGfLuOAc5mdDL3K+XGU/OwIlYRZTxR2zu1NwNHH2udWb+51f42zUTGvo0tpumfmVqUwIDAQABAoGBAJdhJrSFPoJcdDO5MnbrpWg6LT6XKFIFppH31OudNTUyfae8qEcYjnrgGyVrOTfrQFo2h+24JBxCixTo46BVsbKhuQ8ociI5UIWyRaazh4FYgrvD2cJwoZ6Qnqkpi5DZddul6cUJIK62cx4OHdnQrwdMfu39CPkdd2M5DqqG8nApAkEA6ElVfg8O1CF5So3gljP+fqxvqbXxtHsFFUmeZka52IgLow36oUfu3WX1WptgnBENIn+wzKEvG2+/sSAtWrqClwJBANlBIk87GF4O1khW/GbRzYl94zGnqDyP4uWrF1NVxwShLg36KwXEMKLF7S9v/DbgXqhvLUu7CEJgwfYI01BVmaUCQQCOMF3B5YvJNJhfl8uThk5R0lIkC7jiJq/s4PyyVLbsohKlhCgYU8UXB5OSHCyePB3TMWDTUhNY8yXfUfgRYrwPAkAA8CoQw4WQamn8d/uxCfjkPhnsDfhJAqoNKdb48ibg2z1SrCDhbn5zRX2HtekV55QhEBHXAODBQJ+1y0X2U9hNAkAB4SG/BWgh1+qfrw4I6DrKhYEIz8YyfBpbKl6MljG8c1Ui/dHOenMfNKeyXSHmW9QgI2B5eJJuBykNwEs7JxZ6";
    public static final String PRIVATE_KEY       = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMUhQzgxYIdnTy+iWq07/VBFiG9myDWqNc42jfz9Qy6687JEIbvklJIF0b+/C7CmPF3aznjFAh+Kg3LVNUJc9x87Dmg0FsPnwiMpFi3RRdAZ8u44BzmZ0Mvcr5cZT87AiVhFlPFHbO7U3A0cfa51Zv7nV/jbNRMa+jS2m6Z+ZWpTAgMBAAECgYEAl2EmtIU+glx0M7kyduulaDotPpcoUgWmkffU6501NTJ9p7yoRxiOeuAbJWs5N+tAWjaH7bgkHEKLFOjjoFWxsqG5DyhyIjlQhbJFprOHgViCu8PZwnChnpCeqSmLkNl126XpxQkgrrZzHg4d2dCvB0x+7f0I+R13YzkOqobycCkCQQDoSVV+Dw7UIXlKjeCWM/5+rG+ptfG0ewUVSZ5mRrnYiAujDfqhR+7dZfVam2CcEQ0if7DMoS8bb7+xIC1auoKXAkEA2UEiTzsYXg7WSFb8ZtHNiX3jMaeoPI/i5asXU1XHBKEuDforBcQwosXtL2/8NuBeqG8tS7sIQmDB9gjTUFWZpQJBAI4wXcHli8k0mF+Xy5OGTlHSUiQLuOImr+zg/LJUtuyiEqWEKBhTxRcHk5IcLJ48HdMxYNNSE1jzJd9R+BFivA8CQADwKhDDhZBqafx3+7EJ+OQ+GewN+EkCqg0p1vjyJuDbPVKsIOFufnNFfYe16RXnlCEQEdcA4MFAn7XLRfZT2E0CQAHhIb8FaCHX6p+vDgjoOsqFgQjPxjJ8GlsqXoyWMbxzVSL90c56cx80p7JdIeZb1CAjYHl4km4HKQ3ASzsnFno=";


    //TODO !!!! 注：该公钥为测试账号公钥  开发者必须设置自己的公钥 ,否则会存在安全隐患

    public static final String PUBLIC_KEY        = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDFIUM4MWCHZ08volqtO/1QRYhvZsg1qjXONo38/UMuuvOyRCG75JSSBdG/vwuwpjxd2s54xQIfioNy1TVCXPcfOw5oNBbD58IjKRYt0UXQGfLuOAc5mdDL3K+XGU/OwIlYRZTxR2zu1NwNHH2udWb+51f42zUTGvo0tpumfmVqUwIDAQAB";


    /**支付宝网关*/

    public static final String ALIPAY_GATEWAY    = "https://openapi.alipay.com/gateway.do";


    /**授权访问令牌的授权类型*/

    public static final String GRANT_TYPE        = "authorization_code";

}