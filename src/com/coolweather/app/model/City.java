package com.coolweather.app.model;

public class City {

	private int id;
	private String cityNameString;
	private String cityCodeString;
	private int provinceId;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getCityName() {
		return cityNameString;
	}
	
	public void setCityName(String cityNameString) {
		this.cityNameString = cityNameString;
	}
	
	public String getCityCode() {
		return cityCodeString;
	}
	
	public void setCityCode(String cityCodeString) {
		this.cityCodeString = cityCodeString;
	}
	
	public int getProvinceId() {
		return provinceId;
	}
	
	public void setProvincedId(int provinceId) {
		this.provinceId = provinceId;
	}
	
}
