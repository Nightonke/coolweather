package com.coolweather.app.activity;

import com.coolweather.app.R;
import com.coolweather.app.util.HttpCallbackListener;
import com.coolweather.app.util.HttpUtil;

import com.coolweather.app.util.Utility;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WeatherActivity extends Activity implements OnClickListener {

	private LinearLayout weatherInfoLayout;
	private TextView cityNameTextView;
	private TextView publishTextView;
	private TextView weatherDespTextView;
	private TextView temp1TextView;
	private TextView temp2TextView;
	private TextView currentDateTextView;
	
	private Button switchCityButton;
	private Button refreshWeatherButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		weatherInfoLayout = (LinearLayout)findViewById(
				R.id.weather_info_layout);
		cityNameTextView = (TextView)findViewById(R.id.city_name);
		publishTextView = (TextView)findViewById(R.id.public_text);
		weatherDespTextView = (TextView)findViewById(R.id.weather_desp);
		temp1TextView = (TextView)findViewById(R.id.temp1);
		temp2TextView = (TextView)findViewById(R.id.temp2);
		currentDateTextView = (TextView)findViewById(R.id.current_date);
		String countyCodeString = getIntent().getStringExtra("county_code");
		if (!TextUtils.isEmpty(countyCodeString)) {
			publishTextView.setText("同步中...");
			weatherInfoLayout.setVisibility(View.INVISIBLE);
			cityNameTextView.setVisibility(View.INVISIBLE);
			queryWeather(countyCodeString);
		} else {
			showWeather();
		}
		
		switchCityButton = (Button)findViewById(R.id.switch_city);
		refreshWeatherButton = (Button)findViewById(R.id.refresh_weather);
		switchCityButton.setOnClickListener(this);
		refreshWeatherButton.setOnClickListener(this);
		
	}
	
	private void queryWeather(String countyCodeString) {
		Log.d("weather", countyCodeString);
		String address = "http://www.weather.com.cn/data/cityinfo/"
					+ countyCodeString + ".html";
		queryFromServer(address);
	}
	
	private void queryFromServer(final String address) {
		
		Log.d("weather", "here");
		
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onFinish(final String response) {
				// TODO Auto-generated method stub

				Log.d("weather", "here");
				
				Utility.handleWeatherResponse(WeatherActivity.this, response);
				
				Log.d("weather", response);
				
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Log.d("weather", response);
						showWeather();
					}
				});

			}
			
			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						publishTextView.setText("同步失败");
					}
				});
			}
		});
	}
	
	private void showWeather() {
		SharedPreferences preferences = 
				PreferenceManager.getDefaultSharedPreferences(this);
		cityNameTextView.setText(preferences.getString("city_name", ""));
		temp1TextView.setText(preferences.getString("temp1", ""));
		temp2TextView.setText(preferences.getString("temp2", ""));
		weatherDespTextView.setText(preferences.getString("weather_desp", ""));
		publishTextView.setText("今天" 
				+ preferences.getString("publish_time", "") + "发布");
		currentDateTextView.setText(preferences.getString("current_date", ""));
		weatherInfoLayout.setVisibility(View.VISIBLE);
		cityNameTextView.setVisibility(View.VISIBLE);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.switch_city:
			Intent intent = new Intent(this, ChooseAreaActivity.class);
			intent.putExtra("from_weather_activity", true);
			startActivity(intent);
			finish();
			break;
		case R.id.refresh_weather:
			publishTextView.setText("同步中...");
			SharedPreferences preferences = 
					PreferenceManager.getDefaultSharedPreferences(this);
			String countyCodeString = preferences.getString("weather_code", "");
			if (!TextUtils.isEmpty(countyCodeString)) {
				queryWeather(countyCodeString);
			}
			break;
		default:
			break;
		}
	}
	
}
