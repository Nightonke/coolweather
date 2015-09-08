package com.coolweather.app.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.coolweather.app.model.City;
import com.coolweather.app.model.CoolWeatherDB;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;

public class Utility {

	public synchronized static boolean handleProvincesResponse(
			CoolWeatherDB coolWeatherDB, String response) {
		if (!TextUtils.isEmpty(response)) {
			String[] allProvinceStrings = response.split(",");
			if (allProvinceStrings != null && allProvinceStrings.length > 0) {
				for (String p : allProvinceStrings) {
					String[] array = p.split("\\|");
					Province province = new Province();
					province.setProvinceCode(array[0]);
					province.setProvinceName(array[1]);
					coolWeatherDB.saveProvince(province);
				}
				return true;
			}
		}
		return false;
	}
	
	public synchronized static boolean handleCitiesResponse(
			CoolWeatherDB coolWeatherDB, String response, String provinceId) {
		if (!TextUtils.isEmpty(response)) {
			String[] allCitiesStrings = response.split(",");
			if (allCitiesStrings != null && allCitiesStrings.length > 0) {
				for (String c : allCitiesStrings) {
					String[] array = c.split("\\|");
					City city  = new City();
					city.setCityCode(array[0]);
					city.setCityName(array[1]);
					city.setProvincedId(provinceId);
					coolWeatherDB.saveCity(city);
				}
				return true;
			}
		}
		return false;
	}
	
	public synchronized static boolean handleCountiesResponse(
			CoolWeatherDB coolWeatherDB, String response, String cityId) {
		if (!TextUtils.isEmpty(response)) {
			String[] allCountiesStrings = response.split(",");
			if (allCountiesStrings != null && allCountiesStrings.length > 0) {
				for (String c : allCountiesStrings) {
					String[] array = c.split("\\|");
					County county  = new County();
					county.setCountyCode(array[0]);
					county.setCountyName(array[1]);
					county.setCityId(cityId);
					coolWeatherDB.saveCounty(county);
				}
				return true;
			}
		}
		return false;
	}
	
	public static void handleWeatherResponse(Context context, String response) {
		try {
			JSONObject jsonObject = new JSONObject(response);
			JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
			String cityNameString = weatherInfo.getString("city");
			String weatherCodeString = weatherInfo.getString("cityid");
			String temp1 = weatherInfo.getString("temp1");
			String temp2 = weatherInfo.getString("temp2");
			String weatherDespString = weatherInfo.getString("weather");
			String publishTimeString = weatherInfo.getString("ptime");
			saveWeatherInfo(context, cityNameString, weatherCodeString, temp1, 
					temp2, weatherDespString, publishTimeString);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public static void saveWeatherInfo(Context context, String cityName, 
			String weatherCode, String temp1, String temp2, String weatherDesp,
			String publishTime) {
		SimpleDateFormat sdfDateFormat = new SimpleDateFormat("yyyyƒÍM‘¬d»’", 
				Locale.CHINA);
		SharedPreferences.Editor editor = PreferenceManager
				.getDefaultSharedPreferences(context).edit();
		editor.putBoolean("city_selected", true);
		editor.putString("city_name", cityName);
		editor.putString("weather_code", weatherCode);
		editor.putString("temp1", temp1);
		editor.putString("temp2", temp2);
		editor.putString("weather_desp", weatherDesp);
		editor.putString("publish_teim", publishTime);
		editor.putString("current_date", sdfDateFormat.format(new Date()));
		editor.commit();
	}
	
}
