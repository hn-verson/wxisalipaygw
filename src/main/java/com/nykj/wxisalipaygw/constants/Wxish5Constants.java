package com.nykj.wxisalipaygw.constants;

import java.util.HashMap;

/**
 * Created by Verson on 2016/5/4.
 */
public class Wxish5Constants {

    /** 支付宝用户信息服务 **/
    public final static Integer ALIPAY_USER_INFO_SERVICE = 1;
    /** 支付宝用户地理位置服务 **/
    public final static Integer ALIPAY_USER_GIS_SERVICE = 2;
    /** 社保卡绑定服务 **/
    public final static Integer ALIPAY_INST_CARD_BIND_SERVICE = 3;
    /** 社保卡查询服务 **/
    public final static Integer ALIPAY_INST_CARD_QUERY_SERVICE = 4;
    /** 门诊待缴费记录列表查询 **/
    public final static Integer HIS_MZ_FEE_LIST_QUERY_SERVICE = 5;
    /** 门诊待缴费记录明细查询 **/
    public final static Integer HIS_MZ_FEE_DETAIL_QUERY_SERVICE = 6;
    /** 门诊缴费订单状态查询 **/
    public final static Integer HIS_MZ_FEE_STATE_QUERY_SERVICE = 7;
    /** 社保支付服务 **/
    public final static Integer ALIPAY_MEDICAL_CARD_PAY_SERVICE = 8;

    /** 请求公共参数定义 **/
    public final static String[] REQUEST_COMMON_PARAMS = {"token","time","data"};
    /** 请求必填业务参数定义(data的子属性) **/
    public final static HashMap<Integer,String[]> REQUEST_MUST_BUSI_PARAMS = new HashMap<Integer, String[]>(){
        {
            put(ALIPAY_USER_INFO_SERVICE,new String[]{"auth_code"});
            put(ALIPAY_USER_GIS_SERVICE,new String[]{"open_id"});
            put(ALIPAY_INST_CARD_BIND_SERVICE,new String[]{"open_id","refresh_token","return_url"});
            put(ALIPAY_INST_CARD_QUERY_SERVICE,new String[]{"open_id","card_org_no"});

            //TODO 待HIS接口确认
            put(HIS_MZ_FEE_LIST_QUERY_SERVICE,new String[]{});
            //TODO 待HIS接口确认
            put(HIS_MZ_FEE_DETAIL_QUERY_SERVICE,new String[]{});
            //TODO 待HIS接口确认
            put(HIS_MZ_FEE_STATE_QUERY_SERVICE,new String[]{});
            //TODO 待支付宝接口确认
            put(ALIPAY_MEDICAL_CARD_PAY_SERVICE,new String[]{});
        }
    };
    /** 请求可选业务参数定义(data的子属性) **/
    public final static HashMap<Integer,String[]> REQUEST_OPT_BUSI_PARAMS = new HashMap<Integer, String[]>(){
        {
            put(ALIPAY_USER_INFO_SERVICE,null);
            put(ALIPAY_USER_GIS_SERVICE,null);
            put(ALIPAY_INST_CARD_BIND_SERVICE,null);
            put(ALIPAY_INST_CARD_QUERY_SERVICE,null);

            //TODO 待HIS接口确认
            put(HIS_MZ_FEE_LIST_QUERY_SERVICE,new String[]{});
            //TODO 待HIS接口确认
            put(HIS_MZ_FEE_DETAIL_QUERY_SERVICE,new String[]{});
            //TODO 待HIS接口确认
            put(HIS_MZ_FEE_STATE_QUERY_SERVICE,new String[]{});
            //TODO 待支付宝接口确认
            put(ALIPAY_MEDICAL_CARD_PAY_SERVICE,new String[]{});
        }
    };

    /** 业务处理公共参数定义 **/
    public final static String[] BUSI_COMMON_PARAMS = {"unit_id","channel"};

}