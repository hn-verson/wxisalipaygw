package com.nykj.wxisalipaygw.entity.alipay.request;

import com.alipay.api.AlipayRequest;
import com.alipay.api.internal.util.AlipayHashMap;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import java.util.Map;

/**
 * Created by Verson on 2016/5/11.
 */
public class AlipayMedicalInstCardBindRequest implements
        AlipayRequest<AlipaySystemOauthTokenResponse> {

    private AlipayHashMap udfParams;
    private String apiVersion = "1.0";
    private String returnUrl;
    private String buyerId;
    private String terminalType;
    private String terminalInfo;
    private String prodCode;
    private String notifyUrl;

    public Map<String, String> getTextParams() {
        AlipayHashMap txtParams = new AlipayHashMap();
        txtParams.put("return_url", this.returnUrl);
        txtParams.put("sign_flag", "true");
        txtParams.put("buyer_id", this.buyerId);
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
        return "alipay.commerce.medical.instcard.bind";
    }

    @Override
    public String getApiVersion() {
        return apiVersion;
    }

    @Override
    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
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