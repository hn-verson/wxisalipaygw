package com.nykj.wxisalipaygw.entity.alipay;

/**
 * Created by Verson on 2016/5/4.
 */
public class AlipayUserInfo {
    /*用户头像*/
    private String avatar;
    /*用户昵称*/
    private String nickName;
    /*省份*/
    private String province;
    /*城市*/
    private String city;
    /*性别*/
    private String gender;
    /*用户类型*/
    private String userTypeValue;
    /*用户标识*/
    private String openId;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUserTypeValue() {
        return userTypeValue;
    }

    public void setUserTypeValue(String userTypeValue) {
        this.userTypeValue = userTypeValue;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

}