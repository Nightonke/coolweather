package com.coolweather.app.activity;

import com.coolweather.app.R;
import com.coolweather.app.service.AutoUpdateService;
import com.coolweather.app.util.HttpCallbackListener;
import com.coolweather.app.util.HttpUtil;

import com.coolweather.app.util.Utility;

import android.R.drawable;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.opengl.Visibility;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TableLayout;
import android.widget.TextView;

public class WeatherActivity extends Activity implements OnClickListener {

	private LinearLayout weatherInfoLayout;
	private TextView cityNameTextView;
	private TextView publishTextView;
	private TextView weatherDespTextView;
	private TextView temp1TextView;
	private TextView temp2TextView;
//	private TextView currentDateTextView;
	
	private RelativeLayout simpleInfomationLayout;
	private RelativeLayout moreInfomationLayout;
	
	private Button switchCityButton;
	private Button refreshWeatherButton;
	private boolean moreInfomationShown;
	
	private ImageView imageView;
	
	private float x1 = 0, y1 = 0, x2 = 0, y2 = 0;
	
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
		
		moreInfomationLayout = (RelativeLayout)findViewById(
				R.id.more_infomation_layout);
		moreInfomationLayout.setVisibility(View.INVISIBLE);
		
		simpleInfomationLayout = (RelativeLayout)findViewById(
				R.id.simple_infomation_layout);
		
		moreInfomationShown = false;
		
		imageView = (ImageView)findViewById(R.id.image);
		imageView.bringToFront();
		AlphaAnimation alphaAnimation = new AlphaAnimation(0.5F, 0.5F);
		alphaAnimation.setDuration(0);
		alphaAnimation.setFillAfter(true);
		imageView.startAnimation(alphaAnimation);
		
//		currentDateTextView = (TextView)findViewById(R.id.current_date);
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
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			x1 = event.getX();
			y1 = event.getY();
		}
		if (event.getAction() == MotionEvent.ACTION_UP) {
			
			x2 = event.getX();
			y2 = event.getY();
			
			if (y1 - y2 > 50) {
				Log.d("direction", "up");
				if (!moreInfomationShown) {
					moreInfomationLayout.startAnimation(AnimationUtils.loadAnimation(this, R.anim.up_in));
				}
				moreInfomationShown = true;
				moreInfomationLayout.setVisibility(View.VISIBLE);
			} else if (y2 - y1 > 50) {
				Log.d("direction", "down");
				if (moreInfomationShown) {
					Animation animation = AnimationUtils.loadAnimation(this, R.anim.down_out);
					animation.setAnimationListener(new AnimationListener() {
						
						@Override
						public void onAnimationStart(Animation animation) {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void onAnimationRepeat(Animation animation) {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void onAnimationEnd(Animation animation) {
							// TODO Auto-generated method stub
							moreInfomationLayout.setVisibility(View.INVISIBLE);
						}
					});
					moreInfomationLayout.startAnimation(animation);
				}
				moreInfomationShown = false;
			}
		}
		return super.onTouchEvent(event);
	}
	
	private void queryWeather(String countyCodeString) {
		Log.d("weather", countyCodeString);
		String address = "http://apis.baidu.com/heweather/weather/free?cityid=CN"
					+ countyCodeString;
		queryFromServer(address);
	}
	
	private void queryFromServer(final String address) {

		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onFinish(final String response) {
				// TODO Auto-generated method stub
				
				Utility.handleWeatherResponse(WeatherActivity.this, response);
				
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
		cityNameTextView.setText(preferences.getString("basic_city", ""));
		temp1TextView.setText(preferences.getString("daily_forecast_tmp_min_0", "") + "°C");
		temp2TextView.setText(preferences.getString("daily_forecast_tmp_max_0", "") + "°C");
		String weather_desp = "";
		if (preferences.getString("daily_forecast_cond_txt_d_0", "").equals(
				preferences.getString("daily_forecast_cond_txt_n_0", ""))) {
			weather_desp = preferences.getString("daily_forecast_cond_txt_d_0", "");
		} else {
			weather_desp = preferences.getString("daily_forecast_cond_txt_d_0", "") 					
					+ " 转 "
					+ preferences.getString("daily_forecast_cond_txt_n_0", "");
		}
		weatherDespTextView.setText(weather_desp);
		publishTextView.setText("今天" 
				+ preferences.getString("basic_loc", "") + "发布");
//		currentDateTextView.setText(preferences.getString("current_date", ""));
		weatherInfoLayout.setVisibility(View.VISIBLE);
		cityNameTextView.setVisibility(View.VISIBLE);
		
		if ("100".equals(preferences.getString("now_cond_code", ""))) {
			imageView.setImageResource(R.drawable.p100);
		} else if ("101".equals(preferences.getString("now_cond_code", ""))) {
			imageView.setImageResource(R.drawable.p101);
		} else if ("102".equals(preferences.getString("now_cond_code", ""))) {
			imageView.setImageResource(R.drawable.p102);
		} else if ("103".equals(preferences.getString("now_cond_code", ""))) {
			imageView.setImageResource(R.drawable.p103);
		} else if ("104".equals(preferences.getString("now_cond_code", ""))) {
			imageView.setImageResource(R.drawable.p104);
		} else if ("200".equals(preferences.getString("now_cond_code", ""))) {
			imageView.setImageResource(R.drawable.p200);
		} else if ("201".equals(preferences.getString("now_cond_code", ""))) {
			imageView.setImageResource(R.drawable.p201);
		} else if ("202".equals(preferences.getString("now_cond_code", ""))) {
			imageView.setImageResource(R.drawable.p202);
		} else if ("203".equals(preferences.getString("now_cond_code", ""))) {
			imageView.setImageResource(R.drawable.p203);
		} else if ("204".equals(preferences.getString("now_cond_code", ""))) {
			imageView.setImageResource(R.drawable.p204);
		} else if ("205".equals(preferences.getString("now_cond_code", ""))) {
			imageView.setImageResource(R.drawable.p205);
		} else if ("206".equals(preferences.getString("now_cond_code", ""))) {
			imageView.setImageResource(R.drawable.p206);
		} else if ("207".equals(preferences.getString("now_cond_code", ""))) {
			imageView.setImageResource(R.drawable.p207);
		} else if ("208".equals(preferences.getString("now_cond_code", ""))) {
			imageView.setImageResource(R.drawable.p208);
		} else if ("209".equals(preferences.getString("now_cond_code", ""))) {
			imageView.setImageResource(R.drawable.p209);
		} else if ("210".equals(preferences.getString("now_cond_code", ""))) {
			imageView.setImageResource(R.drawable.p210);
		} else if ("211".equals(preferences.getString("now_cond_code", ""))) {
			imageView.setImageResource(R.drawable.p211);
		} else if ("212".equals(preferences.getString("now_cond_code", ""))) {
			imageView.setImageResource(R.drawable.p212);
		} else if ("213".equals(preferences.getString("now_cond_code", ""))) {
			imageView.setImageResource(R.drawable.p213);
		} else if ("300".equals(preferences.getString("now_cond_code", ""))) {
			imageView.setImageResource(R.drawable.p300);
		} else if ("301".equals(preferences.getString("now_cond_code", ""))) {
			imageView.setImageResource(R.drawable.p301);
		} else if ("302".equals(preferences.getString("now_cond_code", ""))) {
			imageView.setImageResource(R.drawable.p302);
		} else if ("303".equals(preferences.getString("now_cond_code", ""))) {
			imageView.setImageResource(R.drawable.p303);
		} else if ("304".equals(preferences.getString("now_cond_code", ""))) {
			imageView.setImageResource(R.drawable.p304);
		} else if ("305".equals(preferences.getString("now_cond_code", ""))) {
			imageView.setImageResource(R.drawable.p305);
		} else if ("306".equals(preferences.getString("now_cond_code", ""))) {
			imageView.setImageResource(R.drawable.p306);
		} else if ("307".equals(preferences.getString("now_cond_code", ""))) {
			imageView.setImageResource(R.drawable.p307);
		} else if ("308".equals(preferences.getString("now_cond_code", ""))) {
			imageView.setImageResource(R.drawable.p308);
		} else if ("309".equals(preferences.getString("now_cond_code", ""))) {
			imageView.setImageResource(R.drawable.p309);
		} else if ("310".equals(preferences.getString("now_cond_code", ""))) {
			imageView.setImageResource(R.drawable.p310);
		} else if ("311".equals(preferences.getString("now_cond_code", ""))) {
			imageView.setImageResource(R.drawable.p311);
		} else if ("312".equals(preferences.getString("now_cond_code", ""))) {
			imageView.setImageResource(R.drawable.p312);
		} else if ("313".equals(preferences.getString("now_cond_code", ""))) {
			imageView.setImageResource(R.drawable.p313);
		} else if ("400".equals(preferences.getString("now_cond_code", ""))) {
			imageView.setImageResource(R.drawable.p400);
		} else if ("401".equals(preferences.getString("now_cond_code", ""))) {
			imageView.setImageResource(R.drawable.p401);
		} else if ("402".equals(preferences.getString("now_cond_code", ""))) {
			imageView.setImageResource(R.drawable.p402);
		} else if ("403".equals(preferences.getString("now_cond_code", ""))) {
			imageView.setImageResource(R.drawable.p403);
		} else if ("404".equals(preferences.getString("now_cond_code", ""))) {
			imageView.setImageResource(R.drawable.p404);
		} else if ("405".equals(preferences.getString("now_cond_code", ""))) {
			imageView.setImageResource(R.drawable.p405);
		} else if ("406".equals(preferences.getString("now_cond_code", ""))) {
			imageView.setImageResource(R.drawable.p406);
		} else if ("407".equals(preferences.getString("now_cond_code", ""))) {
			imageView.setImageResource(R.drawable.p407);
		} else if ("500".equals(preferences.getString("now_cond_code", ""))) {
			imageView.setImageResource(R.drawable.p500);
		} else if ("501".equals(preferences.getString("now_cond_code", ""))) {
			imageView.setImageResource(R.drawable.p501);
		} else if ("502".equals(preferences.getString("now_cond_code", ""))) {
			imageView.setImageResource(R.drawable.p502);
		} else if ("503".equals(preferences.getString("now_cond_code", ""))) {
			imageView.setImageResource(R.drawable.p503);
		} else if ("504".equals(preferences.getString("now_cond_code", ""))) {
			imageView.setImageResource(R.drawable.p504);
		} else if ("507".equals(preferences.getString("now_cond_code", ""))) {
			imageView.setImageResource(R.drawable.p507);
		} else if ("508".equals(preferences.getString("now_cond_code", ""))) {
			imageView.setImageResource(R.drawable.p508);
		} else if ("900".equals(preferences.getString("now_cond_code", ""))) {
			imageView.setImageResource(R.drawable.p900);
		} else if ("901".equals(preferences.getString("now_cond_code", ""))) {
			imageView.setImageResource(R.drawable.p901);
		} else {
			AlphaAnimation alphaAnimation = new AlphaAnimation(0F, 0F);
			alphaAnimation.setDuration(0);
			alphaAnimation.setFillAfter(true);
			imageView.startAnimation(alphaAnimation);
		}
		
		
		// Intent intent = new Intent(this, AutoUpdateService.class);
		// startService(intent);
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
			String countyCodeString = preferences.getString("city_id", "");
			if (!TextUtils.isEmpty(countyCodeString)) {
				queryWeather(countyCodeString);
			}
			break;
		default:
			break;
		}
	}
	
}
