package com.pandasdroid.otpreader.pojomessages;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResMessages {

	@SerializedName("result")
	private List<ResultItem> result;

	public List<ResultItem> getResult(){
		return result;
	}
}
