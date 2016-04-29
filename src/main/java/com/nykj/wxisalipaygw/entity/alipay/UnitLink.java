package com.nykj.wxisalipaygw.entity.alipay;

import com.nykj.wxisalipaygw.entity.BaseEntity;

public class UnitLink extends BaseEntity {
	
	private String id;
	private String unit_id;
	private String weixin_id;
	private String app_id;
	private String app_name;
	private String app_secret;
	private String srv_url;
	private String srv_token;
	private String srv_aeskey;
	private String srv_mode;
	private String is_active;
	private String is_delete;
	private String create_time;
	private String creator;
	private String modify_time;
	private String modifor;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUnit_id() {
		return unit_id;
	}
	public void setUnit_id(String unit_id) {
		this.unit_id = unit_id;
	}
	public String getWeixin_id() {
		return weixin_id;
	}
	public void setWeixin_id(String weixin_id) {
		this.weixin_id = weixin_id;
	}
	public String getApp_id() {
		return app_id;
	}
	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}
	public String getApp_name() {
		return app_name;
	}
	public void setApp_name(String app_name) {
		this.app_name = app_name;
	}
	public String getApp_secret() {
		return app_secret;
	}
	public void setApp_secret(String app_secret) {
		this.app_secret = app_secret;
	}
	public String getSrv_url() {
		return srv_url;
	}
	public void setSrv_url(String srv_url) {
		this.srv_url = srv_url;
	}
	public String getSrv_token() {
		return srv_token;
	}
	public void setSrv_token(String srv_token) {
		this.srv_token = srv_token;
	}
	public String getSrv_aeskey() {
		return srv_aeskey;
	}
	public void setSrv_aeskey(String srv_aeskey) {
		this.srv_aeskey = srv_aeskey;
	}
	public String getSrv_mode() {
		return srv_mode;
	}
	public void setSrv_mode(String srv_mode) {
		this.srv_mode = srv_mode;
	}
	public String getIs_active() {
		return is_active;
	}
	public void setIs_active(String is_active) {
		this.is_active = is_active;
	}
	public String getIs_delete() {
		return is_delete;
	}
	public void setIs_delete(String is_delete) {
		this.is_delete = is_delete;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getModify_time() {
		return modify_time;
	}
	public void setModify_time(String modify_time) {
		this.modify_time = modify_time;
	}
	public String getModifor() {
		return modifor;
	}
	public void setModifor(String modifor) {
		this.modifor = modifor;
	}

}