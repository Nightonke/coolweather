package com.coolweather.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class CoolWeatherOpenHelper extends SQLiteOpenHelper {
	
	public static final String CREATE_PROVINCE_STRING = 
			"create table Province (" +
			"id integer primary key autoincrement, " +
			"province_name text, " +
			"province_code text)";
	
	public static final String CREATE_CITY_STRING = 
			"create table City (" +
			"id integer primary key autoincrement, " +
			"city_name text, " +
			"city_code text, " +
			"province_id text)";
	
	public static final String CREATE_COUNTY_STRING = 
			"create table County (" +
			"id integer primary key autoincrement, " +
			"county_name text, " +
			"county_code text, " +
			"city_id text)";

	public CoolWeatherOpenHelper(Context context, String name, 
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_PROVINCE_STRING);
		db.execSQL("insert into Province (province_name, province_code) " +
				"values (?, ?)", 
				new String[] {"北京", "10101"});
		db.execSQL(CREATE_CITY_STRING);
		db.execSQL("insert into City (city_name, city_code, province_id) " +
				"values (?, ?, ?)", 
				new String[] {"北京", "1010101", "10101"});
		db.execSQL(CREATE_COUNTY_STRING);
		db.execSQL("insert into County (county_name, county_code, city_id) " +
				"values (?, ?, ?)",
				new String[] {"北京", "101010100", "1010101"});
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
}
