package com.nykj.wxisalipaygw.entity.alipay;

/**
 * Created by Verson on 2016/5/6.
 */
public class AlipayInstCardInfo {
    /** 买家支付宝用户号 **/
    private String buyer_user_id;
    /** 买家支付宝账号 **/
    private String buyer_logon_id;
    /** 签约协议号 **/
    private String agreement_no;
    /** 签约结果 **/
    private String sign_status;
    /** 签约时间 **/
    private String gmt_sign;
    /** 医疗证号 **/
    private String medical_card_id;
    /** 医保卡号 **/
    private String medical_card_no;
    /** 卡颁发机构编号 **/
    private String card_org_no;
    /** 卡颁发机构名称 **/
    private String card_org_name;
    /** 城市编码 **/
    private String city;

    public String getBuyer_user_id() {
        return buyer_user_id;
    }

    public void setBuyer_user_id(String buyer_user_id) {
        this.buyer_user_id = buyer_user_id;
    }

    public String getBuyer_logon_id() {
        return buyer_logon_id;
    }

    public void setBuyer_logon_id(String buyer_logon_id) {
        this.buyer_logon_id = buyer_logon_id;
    }

    public String getAgreement_no() {
        return agreement_no;
    }

    public void setAgreement_no(String agreement_no) {
        this.agreement_no = agreement_no;
    }

    public String getSign_status() {
        return sign_status;
    }

    public void setSign_status(String sign_status) {
        this.sign_status = sign_status;
    }

    public String getGmt_sign() {
        return gmt_sign;
    }

    public void setGmt_sign(String gmt_sign) {
        this.gmt_sign = gmt_sign;
    }

    public String getMedical_card_id() {
        return medical_card_id;
    }

    public void setMedical_card_id(String medical_card_id) {
        this.medical_card_id = medical_card_id;
    }

    public String getMedical_card_no() {
        return medical_card_no;
    }

    public void setMedical_card_no(String medical_card_no) {
        this.medical_card_no = medical_card_no;
    }

    public String getCard_org_no() {
        return card_org_no;
    }

    public void setCard_org_no(String card_org_no) {
        this.card_org_no = card_org_no;
    }

    public String getCard_org_name() {
        return card_org_name;
    }

    public void setCard_org_name(String card_org_name) {
        this.card_org_name = card_org_name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
