package com.pandasdroid.otpreader.pojomessages;

public class DataMessages {

	private String sender;
	private String id;
	private String msgTime;
	private String message;
	private String user;

	public DataMessages(String sender,String id,String msgTime,String message,String user){
		this.sender = sender;
		this.id = id;
		this.msgTime = msgTime;
		this.message = message;
		this.user = user;
	}

	public String getSender(){
		return sender;
	}

	public String getId(){
		return id;
	}

	public String getMsgTime(){
		return msgTime;
	}

	public String getMessage(){
		return message;
	}

	public String getUser(){
		return user;
	}
}