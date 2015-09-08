package com.coolweather.app.db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLDataException;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CoolWeatherOpenHelper extends SQLiteOpenHelper {
	
	private static String DB_PATH = "";
	private static String DB_NAME = "cool_weather";
	private final Context mContext;
	private SQLiteDatabase database;
	
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
		if (android.os.Build.VERSION.SDK_INT >= 17) {
			DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
		} else {
			DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
		}
		mContext = context;
	}
	
	public void createDatabase() throws IOException {
		boolean dataBaseExist = checkDatabase();
		if (!dataBaseExist) {
			Log.d("database", "database doesn't exist");
			this.getReadableDatabase();
			this.close();
			try {
				copyDatabase();
				Log.d("database", "copy database");
			} catch (IOException e) {
				e.printStackTrace();
				throw new Error("Error in copying database");
			}
		} else {
			Log.d("database", "database exist");
		}
	}
	
	private boolean checkDatabase() {
		File dbFile = new File(DB_PATH + DB_NAME);
		return dbFile.exists();
	}
	
	private void copyDatabase() throws IOException {
		InputStream inputStream = mContext.getAssets().open(DB_NAME);
		String outFileNameString = DB_PATH + DB_NAME;
		OutputStream outputStream = new FileOutputStream(outFileNameString);
		byte[] buffer = new byte[1024];
		int length;
		while ((length = inputStream.read(buffer)) > 0) {
			outputStream.write(buffer, 0, length);
		}
		outputStream.flush();
		outputStream.close();
		inputStream.close();
	}
	
	public boolean openDatabase() throws SQLException {
		String pathString = DB_PATH + DB_NAME;
		database = SQLiteDatabase.openDatabase(
				pathString, null, SQLiteDatabase.CREATE_IF_NECESSARY);
		return database != null;
	}
	
	@Override
	public synchronized void close() {
		if (database != null) {
			database.close();
		}
		super.close();
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
