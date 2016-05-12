package com.nykj.wxisalipaygw.entity.alipay.request;

import com.alipay.api.AlipayRequest;
import com.alipay.api.internal.util.AlipayHashMap;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import java.util.Map;

/**
 * Created by Verson on 2016/5/11.
 */
public class AlipayMedicalInstCardQueryRequest implements
        AlipayRequest<AlipaySystemOauthTokenResponse> {

    private AlipayHashMap udfParams;
    private String apiVersion = "1.0";
    private String buyerId;
    private String cardOrgNo;
    private String terminalType;
    private String terminalInfo;
    private String prodCode;
    private String notifyUrl;

    public Map<String, String> getTextParams() {
        AlipayHashMap txtParams = new AlipayHashMap();
        txtParams.put("buyer_id", this.buyerId);
        txtParams.put("card_org_ no", this.cardOrgNo);
        if (udfParams != null) {
            txtParams.putAll(this.udfParams);
        }
        return txtParams;
    }

    public AlipayHashMap getUdfParams() {
        return udfParams;
    }

    public void setUdfParams(AlipayHashMap udfParams) {
        this.udfParams = udfParams;
    }

    @Override
    public String getApiMethodName() {
        return "alipay.commerce.medical.card.query";
    }

    @Override
    public String getApiVersion() {
        return apiVersion;
    }

    @Override
    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getCardOrgNo() {
        return cardOrgNo;
    }

    public void setCardOrgNo(String cardOrgNo) {
        this.cardOrgNo = cardOrgNo;
    }

    @Override
    public String getTerminalType() {
        return terminalType;
    }

    @Override
    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    @Override
    public String getTerminalInfo() {
        return terminalInfo;
    }

    @Override
    public void setTerminalInfo(String terminalInfo) {
        this.terminalInfo = terminalInfo;
    }

    @Override
    public String getProdCode() {
        return prodCode;
    }

    @Override
    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }

    @Override
    public String getNotifyUrl() {
        return notifyUrl;
    }

    @Override
    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public Class<AlipaySystemOauthTokenResponse> getResponseClass() {
        return AlipaySystemOauthTokenResponse.class;
    }

}