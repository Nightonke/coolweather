package com.coolweather.app.model;

public class Province {
	
	private int id;
	private String provinceNameString;
	private String provinceCodeString;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getProvinceName() {
		return provinceNameString;
	}
	
	public void setProvinceName(String provinceNameString) {
		this.provinceNameString = provinceNameString;
	}
	
	public String getProvinceCode() {
		return provinceCodeString;
	}
	
	public void setProvinceCode(String provinceCodeString) {
		this.provinceCodeString = provinceCodeString;
	}
	
}
