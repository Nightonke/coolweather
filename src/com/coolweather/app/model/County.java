package com.coolweather.app.model;

public class County {

	private int id;
	private String countyNameString;
	private String countyCodeString;
	private int cityId;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getCountyName() {
		return countyNameString;
	}
	
	public void setCountyName(String countyNameString) {
		this.countyNameString = countyNameString;
	}
	
	public String getCountyCode() {
		return countyCodeString;
	}
	
	public void setCountyCode(String countyCodeString) {
		this.countyCodeString = countyCodeString;
	}
	
	public int getCityId() {
		return cityId;
	}
	
	public void setCityId(int cityId) {
		this.cityId = cityId;
	}
	
}
