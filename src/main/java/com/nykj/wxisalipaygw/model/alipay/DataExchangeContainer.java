package com.nykj.wxisalipaygw.model.alipay;

import com.nykj.wxisalipaygw.entity.alipay.AlipayInstCardInfo;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Verson on 2016/5/6.
 */
public class DataExchangeContainer {
    /** 社保卡绑定回调数据容器 **/
    public static Map<String,AlipayInstCardInfo> alipayInstCardInfoMap;
    static {
        alipayInstCardInfoMap = new HashMap<String, AlipayInstCardInfo>();
    }
}
