package com.nykj.wxisalipaygw.entity;

import java.util.Date;

/**
 * 当用户关注和扫描我们生成的二维码时，将支付宝推送过来的事件参数存储起来
 * @ClassName: ChannelStatistics
 * @author verson
 * @date 2015年10月15日 下午2:40:56
 */
public class ChannelStatistics {
	
	private String id;				//主键ID
	private String channel;			//渠道标识(公众号，服务窗)
	private String ticket;			//二维码ticket
	private String fromUserName;	//用户在公众号里的openId
	private String event;			//事件类型，该表只存subscribe，SCAN
	private String eventKey;		//事件KEY值，当even的值为subscribe的时候，那么evenKey的值以qrscene_为前缀，后面为二维码的参数值；如果even的值为SCAN,那么就没有前缀，直接存的是二维码的参数值
	private String createTime;		//时间数字
	private String toUserName;		//微信号ID
	private String msgType;			//消息类型，event
	private String scene_id;		//场景表数据ID
	private Date create_time;		//插入时间
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public String getFromUserName() {
		return fromUserName;
	}
	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	public String getEventKey() {
		return eventKey;
	}
	public void setEventKey(String eventKey) {
		this.eventKey = eventKey;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getToUserName() {
		return toUserName;
	}
	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}
	public String getMsgType() {
		return msgType;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	public String getScene_id() {
		return scene_id;
	}
	public void setScene_id(String scene_id) {
		this.scene_id = scene_id;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	@Override
	public String toString() {
		return "WeixinChannelCount [id=" + id + ", ticket=" + ticket
				+ ", fromUserName=" + fromUserName + ", event=" + event
				+ ", eventKey=" + eventKey + ", createTime=" + createTime
				+ ", toUserName=" + toUserName + ", msgType=" + msgType
				+ ", scene_id=" + scene_id + ", create_time=" + create_time
				+ "]";
	}

}