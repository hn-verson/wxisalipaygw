package com.nykj.wxisalipaygw.entity.alipay;

/**
 * Created by Verson on 2016/5/13.
 */
public class AlipayMedicalCard {
    /* 买家支付宝账号 */
    private String buyerLogonId;
    /* 买家支付宝用户号 */
    private String buyerUserId;
    /* 卡颁发机构名称 */
    private String cardOrgName;
    /* 卡颁发机构编号 */
    private String cardOrgNo;
    /* 城市编码 */
    private String city;
    /* 扩展参数 */
    private String extendParams;
    /* 签约时间 */
    private String gmtSign;
    /* 医疗证号 */
    private String medicalCardId;
    /* 医保卡号 */
    private String medicalCardNo;
    /* 卡类型 */
    private String medicalCardType;
    /* 签约结果 */
    private String signStatus;

    public String getBuyerLogonId() {
        return buyerLogonId;
    }

    public void setBuyerLogonId(String buyerLogonId) {
        this.buyerLogonId = buyerLogonId;
    }

    public String getBuyerUserId() {
        return buyerUserId;
    }

    public void setBuyerUserId(String buyerUserId) {
        this.buyerUserId = buyerUserId;
    }

    public String getCardOrgName() {
        return cardOrgName;
    }

    public void setCardOrgName(String cardOrgName) {
        this.cardOrgName = cardOrgName;
    }

    public String getCardOrgNo() {
        return cardOrgNo;
    }

    public void setCardOrgNo(String cardOrgNo) {
        this.cardOrgNo = cardOrgNo;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getExtendParams() {
        return extendParams;
    }

    public void setExtendParams(String extendParams) {
        this.extendParams = extendParams;
    }

    public String getGmtSign() {
        return gmtSign;
    }

    public void setGmtSign(String gmtSign) {
        this.gmtSign = gmtSign;
    }

    public String getMedicalCardId() {
        return medicalCardId;
    }

    public void setMedicalCardId(String medicalCardId) {
        this.medicalCardId = medicalCardId;
    }

    public String getMedicalCardNo() {
        return medicalCardNo;
    }

    public void setMedicalCardNo(String medicalCardNo) {
        this.medicalCardNo = medicalCardNo;
    }

    public String getMedicalCardType() {
        return medicalCardType;
    }

    public void setMedicalCardType(String medicalCardType) {
        this.medicalCardType = medicalCardType;
    }

    public String getSignStatus() {
        return signStatus;
    }

    public void setSignStatus(String signStatus) {
        this.signStatus = signStatus;
    }
}