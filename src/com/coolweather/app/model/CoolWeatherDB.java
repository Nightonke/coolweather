package com.coolweather.app.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.coolweather.app.db.CoolWeatherOpenHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

public class CoolWeatherDB {

	public static final String DB_NAME_STRING = "cool_weather";
	
	public static final int VERSION = 1;
	
	private static CoolWeatherDB coolWeatherDB;
	private SQLiteDatabase db;
	
	
	private CoolWeatherDB(Context context) throws IOException {
		Log.d("database", "in CoolWeatherDB constructor");
		CoolWeatherOpenHelper dbHelper = new CoolWeatherOpenHelper(
				context, DB_NAME_STRING, null, VERSION);
		dbHelper.createDatabase();
		db = dbHelper.getReadableDatabase();
	}
	
	public synchronized static CoolWeatherDB getInstance(Context context) 
			throws IOException {
		if (coolWeatherDB == null) {
			coolWeatherDB = new CoolWeatherDB(context);
		}
		return coolWeatherDB;
	}
	
	public void saveProvince(Province province) {
		if (province != null) {
			
			Cursor cursor = db.query(
					"Province", 
					null, 
					"province_code = ?", 
					new String[] {province.getProvinceCode()}, 
					null,
					null, 
					null);
			
			if (cursor.getCount() == 0) {
				ContentValues values = new ContentValues();
				values.put("province_name", province.getProvinceName());
				values.put("province_code", province.getProvinceCode());
				db.insert("Province", null, values);
			}

		}
	}
	
	public List<Province> loadProvinces() {
		List<Province> list = new ArrayList<Province>();
		Cursor cursor = db
				.query("Province", null, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				Province province = new Province();
				province.setId(cursor.getInt(cursor.getColumnIndex("id")));
				province.setProvinceName(cursor.getString(
						cursor.getColumnIndex("province_name")));
				province.setProvinceCode(cursor.getString(
						cursor.getColumnIndex("province_code")));
				list.add(province);
			} while (cursor.moveToNext());
			if (cursor != null) {
				cursor.close();
			}
		}
		return list;
	}
	
	public void saveCity(City city) {
		if (city != null) {
			
			Cursor cursor = db.query(
					"City", 
					null, 
					"city_code = ?", 
					new String[] {city.getCityCode()}, 
					null,
					null, 
					null);
			
			if (cursor.getCount() == 0) {
				ContentValues values = new ContentValues();
				values.put("city_name", city.getCityName());
				values.put("city_code", city.getCityCode());
				values.put("province_id", city.getProvinceId());
				db.insert("City", null, values);
			}

		}
	}
	
	public List<City> loadCities(String provinceId) {
		List<City> list = new ArrayList<City>();
		Cursor cursor = db
				.query("City", 
						null, 
						"province_id = ?", 
						new String[] {provinceId}, 
						null, 
						null, 
						null);
		if (cursor.moveToFirst()) {
			do {
				City city = new City();
				city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setCityName(cursor.getString(
						cursor.getColumnIndex("city_name")));
				city.setCityCode(cursor.getString(
						cursor.getColumnIndex("city_code")));
				list.add(city);
			} while (cursor.moveToNext());
			if (cursor != null) {
				cursor.close();
			}
		}
		return list;
	}
	
	public void saveCounty(County county) {
		if (county != null) {
			
			Cursor cursor = db.query(
					"County", 
					null, 
					"county_code = ?", 
					new String[] {county.getCountyCode()}, 
					null,
					null, 
					null);
			
			if (cursor.getCount() == 0) {
				ContentValues values = new ContentValues();
				values.put("county_name", county.getCountyName());
				values.put("county_code", county.getCountyCode());
				values.put("city_id", county.getCityId());
				db.insert("County", null, values);
			}
			
		}
	}
	
	public List<County> loadCounties(String cityId) {
		List<County> list = new ArrayList<County>();

		Cursor cursor = db
				.query("County", 
						null, 
						"city_id = ?", 
						new String[] {cityId}, 
						null, 
						null, 
						null);

		if (cursor.moveToFirst()) {
			do {
				County county = new County();
				county.setId(cursor.getInt(cursor.getColumnIndex("id")));
				county.setCountyName(cursor.getString(
						cursor.getColumnIndex("county_name")));
				county.setCountyCode(cursor.getString(
						cursor.getColumnIndex("county_code")));
				list.add(county);
			} while (cursor.moveToNext());
			if (cursor != null) {
				cursor.close();
			}
		}
		return list;
	}
	
	private void importData(InputStream inputStream) {
		
	}
}
