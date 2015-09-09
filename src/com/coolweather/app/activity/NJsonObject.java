package com.coolweather.app.activity;

import org.json.JSONException;
import org.json.JSONObject;

public class NJsonObject extends JSONObject{

	@Override
	public String getString(String name) throws JSONException {
		// TODO Auto-generated method stub
		if (super.isNull(name)) {
			return "";
		}
		return super.getString(name);
	}
	
}
