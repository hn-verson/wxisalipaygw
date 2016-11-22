package com.nykj.wxisalipaygw.entity;

import java.util.Date;

public class ChannelScene {
	
	private String id;				//主键ID
	private int channel_id;			//渠道标识(公众号、服务窗)
	private String scene_code;		//场景值
	private String scene_name;		//说明
	private String qrCode_url;		//二维码url
	private String unit_id;			//医院ID
	private String creator;			//创建者
	private Date create_time;		//创建时间
	private String modifor;			//修改者
	private Date modify_time;		//修改时间
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public int getChannel_id() {
		return channel_id;
	}

	public void setChannel_id(int channel_id) {
		this.channel_id = channel_id;
	}

	public String getScene_code() {
		return scene_code;
	}

	public void setScene_code(String scene_code) {
		this.scene_code = scene_code;
	}

	public String getScene_name() {
		return scene_name;
	}

	public void setScene_name(String scene_name) {
		this.scene_name = scene_name;
	}

	public String getQrCode_url() {
		return qrCode_url;
	}

	public void setQrCode_url(String qrCode_url) {
		this.qrCode_url = qrCode_url;
	}

	public String getUnit_id() {
		return unit_id;
	}

	public void setUnit_id(String unit_id) {
		this.unit_id = unit_id;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public String getModifor() {
		return modifor;
	}

	public void setModifor(String modifor) {
		this.modifor = modifor;
	}

	public Date getModify_time() {
		return modify_time;
	}

	public void setModify_time(Date modify_time) {
		this.modify_time = modify_time;
	}

	@Override
	public String toString() {
		return "WeixinChannelScene [id=" + id + "channel_id=" + channel_id + ", scene_code=" + scene_code
				+ ", scene_name=" + scene_name + ", qrCode_url=" + qrCode_url
				+ ", unit_id=" + unit_id + ", creator=" + creator
				+ ", create_time=" + create_time + ", modifor=" + modifor
				+ ", modify_time=" + modify_time + "]";
	}

}