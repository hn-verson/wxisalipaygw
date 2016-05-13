package com.nykj.wxisalipaygw.entity.alipay;

/**
 * Created by Verson on 2016/5/13.
 */
public class AlipayMedicalCard {
    private String buyerLogonId;
    private String buyerUserId;
    private String cardOrgName;
    private String cardOrgNo;
    private String city;
    private String extendParams;
    private String medicalCardId;
    private String medicalCardNo;
    private String medicalCardType;
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