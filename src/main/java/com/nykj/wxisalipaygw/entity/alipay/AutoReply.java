package com.nykj.wxisalipaygw.entity.alipay;

public class AutoReply
{
	private String id;
	private String unit_id;
	private String trigger_word;
	private int trigger_type;
	private String reply_content;
	private int is_active;
	
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
	public String getTrigger_word() {
		return trigger_word;
	}
	public void setTrigger_word(String trigger_word) {
		this.trigger_word = trigger_word;
	}
	public int getTrigger_type() {
		return trigger_type;
	}
	public void setTrigger_type(int trigger_type) {
		this.trigger_type = trigger_type;
	}
	public String getReply_content() {
		return reply_content;
	}
	public void setReply_content(String reply_content) {
		this.reply_content = reply_content;
	}
	public int getIs_active() {
		return is_active;
	}
	public void setIs_active(int is_active) {
		this.is_active = is_active;
	}
}