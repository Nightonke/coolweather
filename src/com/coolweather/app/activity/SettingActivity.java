package com.coolweather.app.activity;

import net.simonvt.menudrawer.MenuDrawer;

import com.coolweather.app.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingActivity extends Activity implements OnClickListener {

	private Button backButton;
	private RadioGroup radioGroup;
	private RelativeLayout settingTitleRelativeLayout;
	private FrameLayout settingFrameLayout;
	
	private Context mContext;
	
	private SharedPreferences preferences;
	
	private String weatherTitleBackgroundColorRedString = "#E16B8C";
	private String weatherBackgroundColorRedString = "#F17C67";
	private String WeatherScrollviewBackgroundColorRedString = "#AFEEA9A9";
	
	private String weatherTitleBackgroundColorYellowString = "#E9CD4C";
	private String weatherBackgroundColorYellowString = "#EFBB24";
	private String WeatherScrollviewBackgroundColorYellowString = "#7FFBE251";
	
	private String weatherTitleBackgroundColorBlueString = "#58B2DC";
	private String weatherBackgroundColorBlueString = "#58B2FF";
	private String WeatherScrollviewBackgroundColorBlueString = "#AF27A5E9";
	
	private String weatherTitleBackgroundColorGreenString = "#A8D8B9";
	private String weatherBackgroundColorGreenString = "#91B493";
	private String WeatherScrollviewBackgroundColorGreenString = "#AF86A697";
	
	private String defaultWeatherTitleBackgroundColorString = "#58B2DC";
	private String defaultWeatherBackgroundColorString = "#58B2DC";
	
	private MenuDrawer menuDrawer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.setting_layout);
		
		menuDrawer = MenuDrawer.attach(this);
		menuDrawer.setContentView(R.layout.setting_layout);
		menuDrawer.setMenuView(R.layout.menu_left);
		
		radioGroup = (RadioGroup)findViewById(R.id.radio_group);
		
		backButton = (Button)findViewById(R.id.back);
		backButton.setOnClickListener(this);
		
		settingTitleRelativeLayout = 
				(RelativeLayout)findViewById(R.id.setting_title);
		settingFrameLayout = 
				(FrameLayout)findViewById(R.id.setting_framelayout);
		
		mContext = this;
		
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		
		settingTitleRelativeLayout.setBackgroundColor(Color.parseColor(
				preferences.getString(
						"weatherTitleBackgroundColor", 
						defaultWeatherTitleBackgroundColorString)));
		settingFrameLayout.setBackgroundColor(Color.parseColor(
				preferences.getString(
						"weatherBackgroundColor", 
						defaultWeatherBackgroundColorString)));
		
		if ("red".equals(preferences.getString("refreshColorStyle", 
				"blue"))) {
			radioGroup.check(R.id.radio_red);
		} else if ("yellow".equals(preferences.getString("refreshColorStyle", 
				"blue"))) {
			radioGroup.check(R.id.radio_yellow);
		} else if ("blue".equals(preferences.getString("refreshColorStyle", 
				"blue"))) {
			radioGroup.check(R.id.radio_blue);
		} else if ("green".equals(preferences.getString("refreshColorStyle", 
				"blue"))) {
			radioGroup.check(R.id.radio_green);
		}
		
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				SharedPreferences.Editor editor = PreferenceManager
						.getDefaultSharedPreferences(mContext).edit();
				if (checkedId == R.id.radio_red) {
		            editor.putString("weatherTitleBackgroundColor", 
		            		weatherTitleBackgroundColorRedString);
		            editor.putString("weatherBackgroundColor", 
		            		weatherBackgroundColorRedString);
		            editor.putString("weatherScrollviewBackgroundColor", 
		            		WeatherScrollviewBackgroundColorRedString);
		            editor.putString("refreshColorStyle", "red");
				} else if (checkedId == R.id.radio_yellow) {
					editor.putString("weatherTitleBackgroundColor", 
		            		weatherTitleBackgroundColorYellowString);
		            editor.putString("weatherBackgroundColor", 
		            		weatherBackgroundColorYellowString);
		            editor.putString("weatherScrollviewBackgroundColor", 
		            		WeatherScrollviewBackgroundColorYellowString);
		            editor.putString("refreshColorStyle", "yellow");
				} else if (checkedId == R.id.radio_blue) {
					editor.putString("weatherTitleBackgroundColor", 
		            		weatherTitleBackgroundColorBlueString);
		            editor.putString("weatherBackgroundColor", 
		            		weatherBackgroundColorBlueString);
		            editor.putString("weatherScrollviewBackgroundColor", 
		            		WeatherScrollviewBackgroundColorBlueString);
		            editor.putString("refreshColorStyle", "blue");
				} else if (checkedId == R.id.radio_green) {
					editor.putString("weatherTitleBackgroundColor", 
		            		weatherTitleBackgroundColorGreenString);
		            editor.putString("weatherBackgroundColor", 
		            		weatherBackgroundColorGreenString);
		            editor.putString("weatherScrollviewBackgroundColor", 
		            		WeatherScrollviewBackgroundColorGreenString);
		            editor.putString("refreshColorStyle", "green");
				}
	            
	            editor.commit();
	            
	            settingTitleRelativeLayout.setBackgroundColor(Color.parseColor(
	    				preferences.getString(
	    						"weatherTitleBackgroundColor", 
	    						defaultWeatherTitleBackgroundColorString)));
	    		settingFrameLayout.setBackgroundColor(Color.parseColor(
	    				preferences.getString(
	    						"weatherBackgroundColor", 
	    						defaultWeatherBackgroundColorString)));
			}
		});
		
	}
	
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, WeatherActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back:
			Intent intent = new Intent(this, WeatherActivity.class);
			startActivity(intent);
			finish();
			break;
		default:
			break;
		}
	}
}
