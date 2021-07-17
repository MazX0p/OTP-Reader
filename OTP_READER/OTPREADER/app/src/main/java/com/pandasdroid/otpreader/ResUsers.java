package com.pandasdroid.otpreader;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResUsers{

	@SerializedName("result")
	private List<ResultItem> result;

	public List<ResultItem> getResult(){
		return result;
	}
}