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
	
	private TextView day_1_maxTextView;
	private TextView day_2_maxTextView;
	private TextView day_3_maxTextView;
	private TextView day_4_maxTextView;
	private TextView day_5_maxTextView;
	private TextView day_6_maxTextView;
	private TextView day_7_maxTextView;
	
	private TextView day_1_minTextView;
	private TextView day_2_minTextView;
	private TextView day_3_minTextView;
	private TextView day_4_minTextView;
	private TextView day_5_minTextView;
	private TextView day_6_minTextView;
	private TextView day_7_minTextView;
	
	private ImageView imageView_1;
	private ImageView imageView_2;
	private ImageView imageView_3;
	private ImageView imageView_4;
	private ImageView imageView_5;
	private ImageView imageView_6;
	private ImageView imageView_7;
	
	private TextView day_4_dateTextView;
	private TextView day_5_dateTextView;
	private TextView day_6_dateTextView;
	private TextView day_7_dateTextView;
	
	private TextView flTextView;
	private TextView humTextView;
	private TextView presTextView;
	private TextView pcpnTextView;
	private TextView visTextView;
	private TextView scTextView;
	private TextView degTextView;
	private TextView dirTextView;
	private TextView spdTextView;
	
	private TextView qltyTextView;
	private TextView pm25TextView;
	private TextView aqiTextView;
	private TextView pm10TextView;
	
	private TextView comf_brfTextView;
	private TextView comf_txtTextView;
	private TextView cw_brfTextView;
	private TextView cw_txtTextView;
	private TextView drsg_brfTextView;
	private TextView drsg_txtTextView;
	private TextView flu_brfTextView;
	private TextView flu_txtTextView;
	private TextView sport_brfTextView;
	private TextView sport_txtTextView;
	private TextView trav_brfTextView;
	private TextView trav_txtTextView;
	private TextView uv_brfTextView;
	private TextView uv_txtTextView;
	
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
		
		
		moreInfomationShown = false;
		
		imageView = (ImageView)findViewById(R.id.weather_image);
		imageView.bringToFront();
		AlphaAnimation alphaAnimation = new AlphaAnimation(0.5F, 0.5F);
		alphaAnimation.setDuration(0);
		alphaAnimation.setFillAfter(true);
		imageView.startAnimation(alphaAnimation);
		
		day_1_maxTextView = (TextView)findViewById(R.id.day_1_max);
		day_2_maxTextView = (TextView)findViewById(R.id.day_2_max);
		day_3_maxTextView = (TextView)findViewById(R.id.day_3_max);
		day_4_maxTextView = (TextView)findViewById(R.id.day_4_max);
		day_5_maxTextView = (TextView)findViewById(R.id.day_5_max);
		day_6_maxTextView = (TextView)findViewById(R.id.day_6_max);
		day_7_maxTextView = (TextView)findViewById(R.id.day_7_max);
		
		day_1_minTextView = (TextView)findViewById(R.id.day_1_min);
		day_2_minTextView = (TextView)findViewById(R.id.day_2_min);
		day_3_minTextView = (TextView)findViewById(R.id.day_3_min);
		day_4_minTextView = (TextView)findViewById(R.id.day_4_min);
		day_5_minTextView = (TextView)findViewById(R.id.day_5_min);
		day_6_minTextView = (TextView)findViewById(R.id.day_6_min);
		day_7_minTextView = (TextView)findViewById(R.id.day_7_min);
		
		imageView_1 = (ImageView)findViewById(R.id.day_1_weather_image);
		imageView_2 = (ImageView)findViewById(R.id.day_2_weather_image);
		imageView_3 = (ImageView)findViewById(R.id.day_3_weather_image);
		imageView_4 = (ImageView)findViewById(R.id.day_4_weather_image);
		imageView_5 = (ImageView)findViewById(R.id.day_5_weather_image);
		imageView_6 = (ImageView)findViewById(R.id.day_6_weather_image);
		imageView_7 = (ImageView)findViewById(R.id.day_7_weather_image);
		
		day_4_dateTextView = (TextView)findViewById(R.id.day_4_date);
		day_5_dateTextView = (TextView)findViewById(R.id.day_5_date);
		day_6_dateTextView = (TextView)findViewById(R.id.day_6_date);
		day_7_dateTextView = (TextView)findViewById(R.id.day_7_date);
		
		flTextView = (TextView)findViewById(R.id.fl);
		humTextView = (TextView)findViewById(R.id.hum);
		presTextView = (TextView)findViewById(R.id.pres);
		pcpnTextView = (TextView)findViewById(R.id.pcpn);
		visTextView = (TextView)findViewById(R.id.vis);
		scTextView = (TextView)findViewById(R.id.sc);
		degTextView = (TextView)findViewById(R.id.deg);
		dirTextView = (TextView)findViewById(R.id.dir);
		spdTextView = (TextView)findViewById(R.id.spd);
		
		qltyTextView = (TextView)findViewById(R.id.qlty);
		pm25TextView = (TextView)findViewById(R.id.pm25);
		aqiTextView = (TextView)findViewById(R.id.aqi);
		pm10TextView = (TextView)findViewById(R.id.pm10);
		
		comf_brfTextView = (TextView)findViewById(R.id.comf_brf);
		comf_txtTextView = (TextView)findViewById(R.id.comf_txt);
		cw_brfTextView = (TextView)findViewById(R.id.cw_brf);
		cw_txtTextView = (TextView)findViewById(R.id.cw_txt);
		drsg_brfTextView = (TextView)findViewById(R.id.drsg_brf);
		drsg_txtTextView = (TextView)findViewById(R.id.drsg_txt);
		flu_brfTextView = (TextView)findViewById(R.id.flu_brf);
		flu_txtTextView = (TextView)findViewById(R.id.flu_txt);
		sport_brfTextView = (TextView)findViewById(R.id.sport_brf);
		sport_txtTextView = (TextView)findViewById(R.id.sport_txt);
		trav_brfTextView = (TextView)findViewById(R.id.trav_brf);
		trav_txtTextView = (TextView)findViewById(R.id.trav_txt);
		uv_brfTextView = (TextView)findViewById(R.id.uv_brf);
		uv_txtTextView = (TextView)findViewById(R.id.uv_txt);
		
		
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
		publishTextView.setText(preferences.getString("basic_loc", "") + " 发布");
//		currentDateTextView.setText(preferences.getString("current_date", ""));
		weatherInfoLayout.setVisibility(View.VISIBLE);
		cityNameTextView.setVisibility(View.VISIBLE);
		
		// 7 days max temp
		day_1_maxTextView.setText(preferences.getString("daily_forecast_tmp_max_0", "") + "°C");
		day_2_maxTextView.setText(preferences.getString("daily_forecast_tmp_max_1", "") + "°C");
		day_3_maxTextView.setText(preferences.getString("daily_forecast_tmp_max_2", "") + "°C");
		day_4_maxTextView.setText(preferences.getString("daily_forecast_tmp_max_3", "") + "°C");
		day_5_maxTextView.setText(preferences.getString("daily_forecast_tmp_max_4", "") + "°C");
		day_6_maxTextView.setText(preferences.getString("daily_forecast_tmp_max_5", "") + "°C");
		day_7_maxTextView.setText(preferences.getString("daily_forecast_tmp_max_6", "") + "°C");
		
		// 7 days min temp
		day_1_minTextView.setText(preferences.getString("daily_forecast_tmp_min_0", "") + "°C");
		day_2_minTextView.setText(preferences.getString("daily_forecast_tmp_min_1", "") + "°C");
		day_3_minTextView.setText(preferences.getString("daily_forecast_tmp_min_2", "") + "°C");
		day_4_minTextView.setText(preferences.getString("daily_forecast_tmp_min_3", "") + "°C");
		day_5_minTextView.setText(preferences.getString("daily_forecast_tmp_min_4", "") + "°C");
		day_6_minTextView.setText(preferences.getString("daily_forecast_tmp_min_5", "") + "°C");
		day_7_minTextView.setText(preferences.getString("daily_forecast_tmp_min_6", "") + "°C");
		
		setPicture(imageView, preferences.getString("now_cond_code", ""));
		
		// 7 days weather picture
		setPicture(imageView_1, preferences.getString("now_cond_code", ""));
		setPicture(imageView_2, preferences.getString("daily_forecast_cond_code_d_1", ""));
		setPicture(imageView_3, preferences.getString("daily_forecast_cond_code_d_2", ""));
		setPicture(imageView_4, preferences.getString("daily_forecast_cond_code_d_3", ""));
		setPicture(imageView_5, preferences.getString("daily_forecast_cond_code_d_4", ""));
		setPicture(imageView_6, preferences.getString("daily_forecast_cond_code_d_5", ""));
		setPicture(imageView_7, preferences.getString("daily_forecast_cond_code_d_6", ""));
		
		// 4 days date
		String day_4_dateString = preferences.getString("daily_forecast_date_3", "");
		if (!"".equals(day_4_dateString)) {
			day_4_dateString = day_4_dateString.substring(5, 10);
		}
		day_4_dateTextView.setText(day_4_dateString);
		String day_5_dateString = preferences.getString("daily_forecast_date_4", "");
		if (!"".equals(day_5_dateString)) {
			day_5_dateString = day_5_dateString.substring(5, 10);
		}
		day_5_dateTextView.setText(day_5_dateString);
		String day_6_dateString = preferences.getString("daily_forecast_date_5", "");
		if (!"".equals(day_6_dateString)) {
			day_6_dateString = day_6_dateString.substring(5, 10);
		}
		day_6_dateTextView.setText(day_6_dateString);
		String day_7_dateString = preferences.getString("daily_forecast_date_6", "");
		if (!"".equals(day_7_dateString)) {
			day_7_dateString = day_7_dateString.substring(5, 10);
		}
		day_7_dateTextView.setText(day_7_dateString);
		
		// set 9 infomations
		flTextView.setText(preferences.getString("now_fl", ""));
		humTextView.setText(preferences.getString("now_hum", ""));
		presTextView.setText(preferences.getString("now_pres", ""));
		pcpnTextView.setText(preferences.getString("now_pcpn", ""));
		visTextView.setText(preferences.getString("now_vis", ""));
		scTextView.setText(preferences.getString("now_wind_sc", ""));
		degTextView.setText(preferences.getString("now_wind_deg", ""));
		dirTextView.setText(preferences.getString("now_wind_dir", ""));
		spdTextView.setText(preferences.getString("now_wind_spd", ""));
		
		// set air quantity
		qltyTextView.setText(preferences.getString("api_city_qlty", ""));
		pm25TextView.setText(preferences.getString("api_city_pm25", ""));
		aqiTextView.setText(preferences.getString("api_city_aqi", ""));
		pm10TextView.setText(preferences.getString("api_city_pm10", ""));
		
		// set suggestion
		comf_brfTextView.setText(preferences.getString("suggestion_comf_brf", ""));
		comf_txtTextView.setText(preferences.getString("suggestion_comf_txt", ""));
		cw_brfTextView.setText(preferences.getString("suggestion_cw_brf", ""));
		cw_txtTextView.setText(preferences.getString("suggestion_cw_txt", ""));
		drsg_brfTextView.setText(preferences.getString("suggestion_drsg_brf", ""));
		drsg_txtTextView.setText(preferences.getString("suggestion_drsg_txt", ""));
		flu_brfTextView.setText(preferences.getString("suggestion_flu_brf", ""));
		flu_txtTextView.setText(preferences.getString("suggestion_flu_txt", ""));
		sport_brfTextView.setText(preferences.getString("suggestion_sport_brf", ""));
		sport_txtTextView.setText(preferences.getString("suggestion_sport_txt", ""));
		trav_brfTextView.setText(preferences.getString("suggestion_trav_brf", ""));
		trav_txtTextView.setText(preferences.getString("suggestion_trav_txt", ""));
		uv_brfTextView.setText(preferences.getString("suggestion_uv_brf", ""));
		uv_txtTextView.setText(preferences.getString("suggestion_uv_txt", ""));
		
		// Intent intent = new Intent(this, AutoUpdateService.class);
		// startService(intent);
	}

	private void setPicture(ImageView imageView, String weather_code) {
		if ("100".equals(weather_code)) {
			imageView.setImageResource(R.drawable.p100);
		} else if ("101".equals(weather_code)) {
			imageView.setImageResource(R.drawable.p101);
		} else if ("102".equals(weather_code)) {
			imageView.setImageResource(R.drawable.p102);
		} else if ("103".equals(weather_code)) {
			imageView.setImageResource(R.drawable.p103);
		} else if ("104".equals(weather_code)) {
			imageView.setImageResource(R.drawable.p104);
		} else if ("200".equals(weather_code)) {
			imageView.setImageResource(R.drawable.p200);
		} else if ("201".equals(weather_code)) {
			imageView.setImageResource(R.drawable.p201);
		} else if ("202".equals(weather_code)) {
			imageView.setImageResource(R.drawable.p202);
		} else if ("203".equals(weather_code)) {
			imageView.setImageResource(R.drawable.p203);
		} else if ("204".equals(weather_code)) {
			imageView.setImageResource(R.drawable.p204);
		} else if ("205".equals(weather_code)) {
			imageView.setImageResource(R.drawable.p205);
		} else if ("206".equals(weather_code)) {
			imageView.setImageResource(R.drawable.p206);
		} else if ("207".equals(weather_code)) {
			imageView.setImageResource(R.drawable.p207);
		} else if ("208".equals(weather_code)) {
			imageView.setImageResource(R.drawable.p208);
		} else if ("209".equals(weather_code)) {
			imageView.setImageResource(R.drawable.p209);
		} else if ("210".equals(weather_code)) {
			imageView.setImageResource(R.drawable.p210);
		} else if ("211".equals(weather_code)) {
			imageView.setImageResource(R.drawable.p211);
		} else if ("212".equals(weather_code)) {
			imageView.setImageResource(R.drawable.p212);
		} else if ("213".equals(weather_code)) {
			imageView.setImageResource(R.drawable.p213);
		} else if ("300".equals(weather_code)) {
			imageView.setImageResource(R.drawable.p300);
		} else if ("301".equals(weather_code)) {
			imageView.setImageResource(R.drawable.p301);
		} else if ("302".equals(weather_code)) {
			imageView.setImageResource(R.drawable.p302);
		} else if ("303".equals(weather_code)) {
			imageView.setImageResource(R.drawable.p303);
		} else if ("304".equals(weather_code)) {
			imageView.setImageResource(R.drawable.p304);
		} else if ("305".equals(weather_code)) {
			imageView.setImageResource(R.drawable.p305);
		} else if ("306".equals(weather_code)) {
			imageView.setImageResource(R.drawable.p306);
		} else if ("307".equals(weather_code)) {
			imageView.setImageResource(R.drawable.p307);
		} else if ("308".equals(weather_code)) {
			imageView.setImageResource(R.drawable.p308);
		} else if ("309".equals(weather_code)) {
			imageView.setImageResource(R.drawable.p309);
		} else if ("310".equals(weather_code)) {
			imageView.setImageResource(R.drawable.p310);
		} else if ("311".equals(weather_code)) {
			imageView.setImageResource(R.drawable.p311);
		} else if ("312".equals(weather_code)) {
			imageView.setImageResource(R.drawable.p312);
		} else if ("313".equals(weather_code)) {
			imageView.setImageResource(R.drawable.p313);
		} else if ("400".equals(weather_code)) {
			imageView.setImageResource(R.drawable.p400);
		} else if ("401".equals(weather_code)) {
			imageView.setImageResource(R.drawable.p401);
		} else if ("402".equals(weather_code)) {
			imageView.setImageResource(R.drawable.p402);
		} else if ("403".equals(weather_code)) {
			imageView.setImageResource(R.drawable.p403);
		} else if ("404".equals(weather_code)) {
			imageView.setImageResource(R.drawable.p404);
		} else if ("405".equals(weather_code)) {
			imageView.setImageResource(R.drawable.p405);
		} else if ("406".equals(weather_code)) {
			imageView.setImageResource(R.drawable.p406);
		} else if ("407".equals(weather_code)) {
			imageView.setImageResource(R.drawable.p407);
		} else if ("500".equals(weather_code)) {
			imageView.setImageResource(R.drawable.p500);
		} else if ("501".equals(weather_code)) {
			imageView.setImageResource(R.drawable.p501);
		} else if ("502".equals(weather_code)) {
			imageView.setImageResource(R.drawable.p502);
		} else if ("503".equals(weather_code)) {
			imageView.setImageResource(R.drawable.p503);
		} else if ("504".equals(weather_code)) {
			imageView.setImageResource(R.drawable.p504);
		} else if ("507".equals(weather_code)) {
			imageView.setImageResource(R.drawable.p507);
		} else if ("508".equals(weather_code)) {
			imageView.setImageResource(R.drawable.p508);
		} else if ("900".equals(weather_code)) {
			imageView.setImageResource(R.drawable.p900);
		} else if ("901".equals(weather_code)) {
			imageView.setImageResource(R.drawable.p901);
		} else {
			AlphaAnimation alphaAnimation = new AlphaAnimation(0F, 0F);
			alphaAnimation.setDuration(0);
			alphaAnimation.setFillAfter(true);
			imageView.startAnimation(alphaAnimation);
		}
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
