package com.pandasdroid.otpreader.pojomessages;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class ResultItem {

	@SerializedName("id")
	@Expose
	private String id;
	@SerializedName("message")
	@Expose
	private String message;
	@SerializedName("sender")
	@Expose
	private String sender;
	@SerializedName("user")
	@Expose
	private String user;
	@SerializedName("msg_time")
	@Expose
	private String msgTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getMsgTime() {
		return msgTime;
	}

	public void setMsgTime(String msgTime) {
		this.msgTime = msgTime;
	}

}