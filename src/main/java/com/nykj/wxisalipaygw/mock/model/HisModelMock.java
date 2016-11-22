package com.nykj.wxisalipaygw.mock.model;

import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

/**
 * Created by Verson on 2016/5/13.
 */
@Repository
public class HisModelMock {
    private static final Logger LOGGER = Logger.getLogger(HisModelMock.class);

    @Value("#{config['his.api.mz.fee.list.query']}")
    private String mzFeeListApi;

    @Value("#{config['his.api.mz.fee.detail.query']}")
    private String mzFeeDetailApi;

    /**
     * 门诊待缴费记录列表查询
     * @param paramsJson
     * @return
     */
    public String mzFeeListQuery(JSONObject paramsJson) throws Exception{
        if(paramsJson != null && paramsJson.has("pay_state") && "1".equals(paramsJson.getString("pay_state")))
            return "{\"code\":\"0\",\"message\":\"查询成功\",\"time\":\"111111\",\"data\":[{\"consult_id\":\"NY157500507\",\"consult_date\":\"2015-07-13 15:43:23\",\"consult_dep\":\"皮肤科门诊\",\"consult_doctor\":\"管理员\",\"guide_info\":\"请到门诊1号大楼一楼西药房取药；排队号为：1259\",\"pay_amt\":20,\"his_pay_no\":\"21078742\",\"pay_type\":\"检验单\",\"pay_state\":\"1\",\"order_encode_id\":null}]}";
        return "{\"code\":\"0\",\"message\":\"查询成功\",\"time\":\"111111\",\"data\":[{\"consult_id\":\"NY157500507\",\"consult_date\":\"2015-07-13 15:43:23\",\"consult_dep\":\"皮肤科门诊\",\"consult_doctor\":\"管理员\",\"guide_info\":\"请到门诊1号大楼一楼西药房取药；排队号为：1259\",\"pay_amt\":20,\"his_pay_no\":\"31078742\",\"pay_type\":\"检验单\",\"pay_state\":\"0\",\"order_encode_id\":null}]}";
    }

    /**
     * 门诊待缴费记录明细查询
     * @param paramsJson
     * @return
     */
    public String mzFeeDetailQuery(JSONObject paramsJson) throws Exception{
        if(paramsJson != null && paramsJson.has("his_pay_no") && "21078742".equals(paramsJson.getString("his_pay_no")))
            return "{\"code\":\"0\",\"message\":\"操作成功\",\"time\":\"111111\",\"pay_summary\":{\"pay_amt_total\":20,\"self_pay_amt_total\":0,\"social_pay_amt_total\":0,\"his_pay_no\":\"21078742\",\"his_invoice_no\":\"\"},\"pay_info\":[{\"card_no\":\"8001935848\",\"cf_group_no\":\"\",\"cf_mxs\":\"1\",\"create_time\":\"2015-07-13 15:43:23\",\"dep_id\":\"\",\"dep_name\":\"皮肤科门诊\",\"details\":[{\"PAY_INFO_ID\":\"\",\"amt\":1,\"detail_item_code\":\"\",\"his_pay_no\":\"21078742\",\"his_pay_no_sn\":\"\",\"id\":\"354D8145-C37D-4789-AF37-F5022CAFF88A\",\"item_code_his\":\"949135\",\"item_code_yb\":\"250101015\",\"item_name\":\"血常规（五分类）\",\"pay_amt\":20,\"pres_no\":\"\",\"price\":20,\"self_pay_amt\":0,\"social_item_code\":\"H\",\"social_pay_amt\":0,\"std\":\"\",\"sur_charge_flag\":\"\",\"unit\":\"次\",\"unit_id\":\"\"}],\"doc_id\":\"0\",\"doc_name\":\"管理员\",\"guide_info\":\"请到门诊1号大楼一楼西药房取药；排队号为：1259\",\"healthy_no\":\"NY157500507\",\"his_lsh\":\"\",\"his_pay_no\":\"21078742\",\"id\":\"703D3E7D-8A6C-4DCD-A76C-314BF6A34411\",\"item_name\":\"检验单\",\"mz_type\":\"1\",\"online_unit_branch_id\":\"\",\"order_encode_id\":\"\",\"order_no\":\"\",\"pay_amt\":20,\"pay_method\":\"weixin\",\"pay_method_list\":[],\"pay_state\":\"1\",\"pay_time\":\"2016-05-17 10:00:12\",\"pay_type\":\"3\",\"pay_url\":\"\",\"phone\":\"\",\"pres_no\":\"\",\"ps_flag\":\"\",\"sb_djh\":\"\",\"sb_lsh\":\"\",\"self_pay_amt\":0,\"social_pay_amt\":0,\"social_record_id\":\"\",\"sur_charge_flag\":\"\",\"tb_type\":\"0\",\"unit_branch_id\":\"\",\"unit_branch_name\":\"\",\"unit_id\":\"111\",\"unit_name\":\"\",\"web_pay_no\":\"1000840665201507140406289817\",\"yuyue_id\":\"\",\"zd\":\"\",\"zdsm\":\"\"}]}";
        return "{\"code\":\"0\",\"message\":\"操作成功\",\"time\":\"111111\",\"pay_summary\":{\"pay_amt_total\":20,\"self_pay_amt_total\":0,\"social_pay_amt_total\":0,\"his_pay_no\":\"21078742\",\"his_invoice_no\":\"\"},\"pay_info\":[{\"card_no\":\"8001935848\",\"cf_group_no\":\"\",\"cf_mxs\":\"1\",\"create_time\":\"2015-07-13 15:43:23\",\"dep_id\":\"\",\"dep_name\":\"皮肤科门诊\",\"details\":[{\"PAY_INFO_ID\":\"\",\"amt\":1,\"detail_item_code\":\"\",\"his_pay_no\":\"31078742\",\"his_pay_no_sn\":\"\",\"id\":\"354D8145-C37D-4789-AF37-F5022CAFF88A\",\"item_code_his\":\"949135\",\"item_code_yb\":\"250101015\",\"item_name\":\"血常规（五分类）\",\"pay_amt\":20,\"pres_no\":\"\",\"price\":20,\"self_pay_amt\":0,\"social_item_code\":\"H\",\"social_pay_amt\":0,\"std\":\"\",\"sur_charge_flag\":\"\",\"unit\":\"次\",\"unit_id\":\"\"}],\"doc_id\":\"0\",\"doc_name\":\"管理员\",\"guide_info\":\"请到门诊1号大楼一楼西药房取药；排队号为：1259\",\"healthy_no\":\"NY157500507\",\"his_lsh\":\"\",\"his_pay_no\":\"31078742\",\"id\":\"703D3E7D-8A6C-4DCD-A76C-314BF6A34411\",\"item_name\":\"检验单\",\"mz_type\":\"1\",\"online_unit_branch_id\":\"\",\"order_encode_id\":\"\",\"order_no\":\"\",\"pay_amt\":20,\"pay_method\":\"weixin\",\"pay_method_list\":[],\"pay_state\":\"0\",\"pay_time\":\"2016-05-17 10:00:12\",\"pay_type\":\"3\",\"pay_url\":\"\",\"phone\":\"\",\"pres_no\":\"\",\"ps_flag\":\"\",\"sb_djh\":\"\",\"sb_lsh\":\"\",\"self_pay_amt\":0,\"social_pay_amt\":0,\"social_record_id\":\"\",\"sur_charge_flag\":\"\",\"tb_type\":\"0\",\"unit_branch_id\":\"\",\"unit_branch_name\":\"\",\"unit_id\":\"111\",\"unit_name\":\"\",\"web_pay_no\":\"1000840665201507140406289817\",\"yuyue_id\":\"\",\"zd\":\"\",\"zdsm\":\"\"}]}";
    }

    /**
     * 门诊待缴费记录明细查询
     * @param paramsJson
     * @return
     */
    public String mzFeeStateQuery(JSONObject paramsJson) throws Exception{
        return "{\"code\":0,\"message\":\"操作成功\",\"data\":{\"pay_state\":\"1\"}}";
    }
}