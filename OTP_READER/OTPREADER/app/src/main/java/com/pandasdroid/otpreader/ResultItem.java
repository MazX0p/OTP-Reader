package com.pandasdroid.otpreader;

import com.google.gson.annotations.SerializedName;

public class ResultItem{

	@SerializedName("Id")
	private String id;

	@SerializedName("username")
	private String username;

	public String getId(){
		return id;
	}

	public String getUsername(){
		return username;
	}
}