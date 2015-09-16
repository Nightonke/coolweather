package com.coolweather.app.activity;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;

import net.simonvt.menudrawer.MenuDrawer;

import com.coolweather.app.R;
import com.coolweather.app.activity.MenuAdapter.MenuListener;
import com.coolweather.app.service.AutoUpdateService;
import com.coolweather.app.util.HttpCallbackListener;
import com.coolweather.app.util.HttpUtil;

import com.coolweather.app.util.Utility;

import android.R.drawable;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import android.support.v4.widget.SwipeRefreshLayout;

public class WeatherActivity extends Activity implements 
	OnClickListener, 
	SwipeRefreshLayout.OnRefreshListener, 
	MenuAdapter.MenuListener {

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
	
	private SwipeRefreshLayout swipeRefreshLayout;
	
	private Button switchCityButton;
	private Button refreshWeatherButton;
	private boolean moreInfomationShown;
	
	private ImageView imageView;
	
	private RelativeLayout weatherTitleRelativeLayout;
	private FrameLayout weatherFrameLayout;
	private ScrollView weatherScrollView;
	private ImageView centerLogoImageView;
	
	private String defaultWeatherTitleBackgroundColorString = "#58B2DC";
	private String defaultWeatherBackgroundColorString = "#58B2DC";
	private String defaultWeatherScrollviewBackgroundColorString = "#AF27A5E9";
	private String defaultRefreshColorStyleString = "blue";
	
	private float x1 = 0, y1 = 0, x2 = 0, y2 = 0;
	
	private SharedPreferences preferences;
	
	private MenuDrawer menuDrawer;
	
	protected MenuAdapter mAdapter;
    protected ListView mList;

    private int mActivePosition = 0;
    
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
	
	private Button changeCityButton;
	
	private Button redStyleButton;
	private Button yellowStyleButton;
	private Button blueStyleButton;
	private Button greenStyleButton;
	
	private LinearLayout menuLeftLayout;
	
	// temperature graph
	LineChartView lineChartView;
	private LineChartData data;
    private int numberOfLines = 2;
    private int maxNumberOfLines = 2;
    private int numberOfPoints = 7;

    int[][] randomNumbersTab = new int[maxNumberOfLines][numberOfPoints];

    private boolean hasAxes = true;
    private boolean hasAxesNames = true;
    private boolean hasLines = true;
    private boolean hasPoints = true;
    private ValueShape shape = ValueShape.CIRCLE;
    private boolean isFilled = false;
    private boolean hasLabels = true;
    private boolean isCubic = false;
    private boolean hasLabelForSelected = false;
    private boolean pointsHaveDifferentColor;
    
    private String day_4_dateString;
    private String day_5_dateString;
    private String day_6_dateString;
    private String day_7_dateString;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		
		
		
		menuDrawer = MenuDrawer.attach(this);
		menuDrawer.setContentView(R.layout.weather_layout);

        menuDrawer.setMenuView(R.layout.menu_left);
        
        menuDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_FULLSCREEN);
        menuDrawer.setSlideDrawable(R.drawable.ic_drawer);
        menuDrawer.setDrawerIndicatorEnabled(true);
		
		initMenuDrawer();
		
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
		
//		day_1_maxTextView = (TextView)findViewById(R.id.day_1_max);
//		day_2_maxTextView = (TextView)findViewById(R.id.day_2_max);
//		day_3_maxTextView = (TextView)findViewById(R.id.day_3_max);
//		day_4_maxTextView = (TextView)findViewById(R.id.day_4_max);
//		day_5_maxTextView = (TextView)findViewById(R.id.day_5_max);
//		day_6_maxTextView = (TextView)findViewById(R.id.day_6_max);
//		day_7_maxTextView = (TextView)findViewById(R.id.day_7_max);
//		
//		day_1_minTextView = (TextView)findViewById(R.id.day_1_min);
//		day_2_minTextView = (TextView)findViewById(R.id.day_2_min);
//		day_3_minTextView = (TextView)findViewById(R.id.day_3_min);
//		day_4_minTextView = (TextView)findViewById(R.id.day_4_min);
//		day_5_minTextView = (TextView)findViewById(R.id.day_5_min);
//		day_6_minTextView = (TextView)findViewById(R.id.day_6_min);
//		day_7_minTextView = (TextView)findViewById(R.id.day_7_min);
		
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
		
		swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
		swipeRefreshLayout.setOnRefreshListener(this);
		
		
		preferences = 
				PreferenceManager.getDefaultSharedPreferences(this);
		
		weatherTitleRelativeLayout = 
				(RelativeLayout)findViewById(R.id.weather_title_background);
		weatherFrameLayout = 
				(FrameLayout)findViewById(R.id.weather_framelayout);
		weatherScrollView = 
				(ScrollView)findViewById(R.id.weather_scrollview);
		centerLogoImageView = 
				(ImageView)findViewById(R.id.center_logo);
		menuLeftLayout = (LinearLayout)findViewById(R.id.menu_left_linearlayout);
		
		makeColor();
		
		String countyCodeString = getIntent().getStringExtra("county_code");
		if (!TextUtils.isEmpty(countyCodeString)) {
			publishTextView.setText("同步中...");
			weatherInfoLayout.setVisibility(View.INVISIBLE);
			cityNameTextView.setVisibility(View.INVISIBLE);
			queryWeather(countyCodeString);
		} else {
			showWeather();
		}
		
		changeCityButton = (Button)findViewById(R.id.change_city);
		changeCityButton.setOnClickListener(this);
		redStyleButton = (Button)findViewById(R.id.red_style);
		redStyleButton.setOnClickListener(this);
		yellowStyleButton = (Button)findViewById(R.id.yellow_style);
		yellowStyleButton.setOnClickListener(this);
		blueStyleButton = (Button)findViewById(R.id.blue_style);
		blueStyleButton.setOnClickListener(this);
		greenStyleButton = (Button)findViewById(R.id.green_style);
		greenStyleButton.setOnClickListener(this);
		
		
		initTemperatureGraph();
	}
	
	private void initTemperatureGraph() {
		
		lineChartView = (LineChartView)findViewById(R.id.temperature_chart);
		
		initTemperatureGraphData();
		
		Log.d("weather", "in init");
		
        List<Line> lines = new ArrayList<Line>();
        for (int i = 0; i < numberOfLines; ++i) {

            List<PointValue> values = new ArrayList<PointValue>();
            for (int j = 0; j < numberOfPoints; ++j) {
                values.add(new PointValue(j, randomNumbersTab[i][j]).setLabel(
                		String.valueOf(randomNumbersTab[i][j]) + "°C"));
            }

            Line line = new Line(values);
            line.setColor(ChartUtils.COLORS[i]);
            line.setShape(shape);
            line.setCubic(isCubic);
            line.setFilled(isFilled);
            line.setHasLabels(hasLabels);
            line.setHasLabelsOnlyForSelected(hasLabelForSelected);
            line.setHasLines(hasLines);
            line.setHasPoints(hasPoints);
            
            
            if (pointsHaveDifferentColor){
                line.setPointColor(ChartUtils.COLORS[(i + 1) % ChartUtils.COLORS.length]);
            }
            lines.add(line);
        }
        
        data = new LineChartData(lines);

        if (hasAxes) {
        	
        	day_4_dateString = preferences.getString("daily_forecast_date_3", "");
    		if (!"".equals(day_4_dateString)) {
    			day_4_dateString = day_4_dateString.substring(5, 10);
    		}
    		day_4_dateTextView.setText(day_4_dateString);
    		day_5_dateString = preferences.getString("daily_forecast_date_4", "");
    		if (!"".equals(day_5_dateString)) {
    			day_5_dateString = day_5_dateString.substring(5, 10);
    		}
    		day_5_dateTextView.setText(day_5_dateString);
    		day_6_dateString = preferences.getString("daily_forecast_date_5", "");
    		if (!"".equals(day_6_dateString)) {
    			day_6_dateString = day_6_dateString.substring(5, 10);
    		}
    		day_6_dateTextView.setText(day_6_dateString);
    		day_7_dateString = preferences.getString("daily_forecast_date_6", "");
    		if (!"".equals(day_7_dateString)) {
    			day_7_dateString = day_7_dateString.substring(5, 10);
    		}
    		day_7_dateTextView.setText(day_7_dateString);

            List<AxisValue> axisLabelXList = new ArrayList<AxisValue>();
            AxisValue axisValue1 = new AxisValue(0).setLabel("今天");
            AxisValue axisValue2 = new AxisValue(1).setLabel("明天");
            AxisValue axisValue3 = new AxisValue(2).setLabel("后天");
            AxisValue axisValue4 = new AxisValue(3).setLabel(day_4_dateString);
            AxisValue axisValue5 = new AxisValue(4).setLabel(day_5_dateString);
            AxisValue axisValue6 = new AxisValue(5).setLabel(day_6_dateString);
            AxisValue axisValue7 = new AxisValue(6).setLabel(day_7_dateString);
            axisLabelXList.add(axisValue1);
            axisLabelXList.add(axisValue2);
            axisLabelXList.add(axisValue3);
            axisLabelXList.add(axisValue4);
            axisLabelXList.add(axisValue5);
            axisLabelXList.add(axisValue6);
            axisLabelXList.add(axisValue7);
            
            Axis axisX = new Axis(axisLabelXList).setHasLines(true);
            axisX.setLineColor(Color.parseColor("#FFFFFF"));
            axisX.setTextColor(Color.parseColor("#FFFFFF"));

            Axis axisY = new Axis();
            
            if (hasAxesNames) {
                axisX.setName("");
                axisY.setName("Temperature");
            }
            data.setAxisXBottom(axisX);
            data.setAxisYLeft(null);
        } else {
            data.setAxisXBottom(null);
            data.setAxisYLeft(null);
        }

//        lineChartView.setZoomEnabled(false);
//        lineChartView.setScrollEnabled(false);
        
        data.setBaseValue(Float.NEGATIVE_INFINITY);
        lineChartView.setLineChartData(data);
	}
	
	private void initTemperatureGraphData() {
		randomNumbersTab[0][0] = Integer.parseInt(
				preferences.getString("daily_forecast_tmp_max_0", "0"));
		Log.d("weather", preferences.getString("daily_forecast_tmp_max_0", "0"));
        randomNumbersTab[0][1] = Integer.parseInt(
				preferences.getString("daily_forecast_tmp_max_1", "0"));
        randomNumbersTab[0][2] = Integer.parseInt(
				preferences.getString("daily_forecast_tmp_max_2", "0"));
        randomNumbersTab[0][3] = Integer.parseInt(
				preferences.getString("daily_forecast_tmp_max_3", "0"));
        randomNumbersTab[0][4] = Integer.parseInt(
				preferences.getString("daily_forecast_tmp_max_4", "0"));
        randomNumbersTab[0][5] = Integer.parseInt(
				preferences.getString("daily_forecast_tmp_max_5", "0"));
        randomNumbersTab[0][6] = Integer.parseInt(
				preferences.getString("daily_forecast_tmp_max_6", "0"));
        randomNumbersTab[1][0] = Integer.parseInt(
				preferences.getString("daily_forecast_tmp_min_0", "0"));
        randomNumbersTab[1][1] = Integer.parseInt(
				preferences.getString("daily_forecast_tmp_min_1", "0"));
        randomNumbersTab[1][2] = Integer.parseInt(
				preferences.getString("daily_forecast_tmp_min_2", "0"));
        randomNumbersTab[1][3] = Integer.parseInt(
				preferences.getString("daily_forecast_tmp_min_3", "0"));
        randomNumbersTab[1][4] = Integer.parseInt(
				preferences.getString("daily_forecast_tmp_min_4", "0"));
        randomNumbersTab[1][5] = Integer.parseInt(
				preferences.getString("daily_forecast_tmp_min_5", "0"));
        randomNumbersTab[1][6] = Integer.parseInt(
				preferences.getString("daily_forecast_tmp_min_6", "0"));
	}
	
	private void initMenuDrawer() {
		
	}
	
	private void makeColor() {
		weatherTitleRelativeLayout.setBackgroundColor(Color.parseColor(
				preferences.getString(
						"weatherTitleBackgroundColor", 
						defaultWeatherTitleBackgroundColorString)));
		weatherFrameLayout.setBackgroundColor(Color.parseColor(
				preferences.getString(
						"weatherBackgroundColor", 
						defaultWeatherScrollviewBackgroundColorString)));
		weatherScrollView.setBackgroundColor(Color.parseColor(
				preferences.getString(
						"weatherScrollviewBackgroundColor", 
						defaultWeatherScrollviewBackgroundColorString)));
		menuLeftLayout.setBackgroundColor(Color.parseColor(
				preferences.getString(
						"weatherTitleBackgroundColor", 
						defaultWeatherTitleBackgroundColorString)));
		if ("red".equals(preferences.getString(
				"refreshColorStyle", 
				defaultRefreshColorStyleString))) {
			swipeRefreshLayout.setColorSchemeResources(
					android.R.color.holo_red_dark,
					android.R.color.holo_red_light,
					android.R.color.holo_red_dark,
					android.R.color.holo_red_light);
			centerLogoImageView.setImageResource(R.drawable.cloud_red);
		} else if ("yellow".equals(preferences.getString(
				"refreshColorStyle", 
				defaultRefreshColorStyleString))) {
			swipeRefreshLayout.setColorSchemeResources(
					android.R.color.holo_orange_dark,
					android.R.color.holo_orange_light,
					android.R.color.holo_orange_dark,
					android.R.color.holo_orange_light);
			centerLogoImageView.setImageResource(R.drawable.cloud_yellow);
		} else if ("blue".equals(preferences.getString(
				"refreshColorStyle", 
				defaultRefreshColorStyleString))) {
			swipeRefreshLayout.setColorSchemeResources(
					android.R.color.holo_blue_dark,
					android.R.color.holo_blue_bright,
					android.R.color.holo_blue_dark,
					android.R.color.holo_blue_bright);
			centerLogoImageView.setImageResource(R.drawable.cloud_blue);
		} else if ("green".equals(preferences.getString(
				"refreshColorStyle", 
				defaultRefreshColorStyleString))) {
			swipeRefreshLayout.setColorSchemeResources(
					android.R.color.holo_green_dark,
					android.R.color.holo_green_light,
					android.R.color.holo_green_dark,
					android.R.color.holo_green_light);
			centerLogoImageView.setImageResource(R.drawable.cloud_green);
		} else {
			swipeRefreshLayout.setColorSchemeResources(
					android.R.color.holo_blue_light,
					android.R.color.holo_blue_bright,
					android.R.color.holo_blue_light,
					android.R.color.holo_blue_bright);
			centerLogoImageView.setImageResource(R.drawable.cloud_blue);
		}
	}
	
	private void queryWeather(String countyCodeString) {
		Log.d("weather", countyCodeString);
		String address = "https://api.heweather.com/x3/weather?cityid=CN"
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
		Log.d("DEBUG", "in show function");
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
		
//		// 7 days max temp
//		day_1_maxTextView.setText(preferences.getString("daily_forecast_tmp_max_0", "") + "°C");
//		day_2_maxTextView.setText(preferences.getString("daily_forecast_tmp_max_1", "") + "°C");
//		day_3_maxTextView.setText(preferences.getString("daily_forecast_tmp_max_2", "") + "°C");
//		day_4_maxTextView.setText(preferences.getString("daily_forecast_tmp_max_3", "") + "°C");
//		day_5_maxTextView.setText(preferences.getString("daily_forecast_tmp_max_4", "") + "°C");
//		day_6_maxTextView.setText(preferences.getString("daily_forecast_tmp_max_5", "") + "°C");
//		day_7_maxTextView.setText(preferences.getString("daily_forecast_tmp_max_6", "") + "°C");
//		
//		// 7 days min temp
//		day_1_minTextView.setText(preferences.getString("daily_forecast_tmp_min_0", "") + "°C");
//		day_2_minTextView.setText(preferences.getString("daily_forecast_tmp_min_1", "") + "°C");
//		day_3_minTextView.setText(preferences.getString("daily_forecast_tmp_min_2", "") + "°C");
//		day_4_minTextView.setText(preferences.getString("daily_forecast_tmp_min_3", "") + "°C");
//		day_5_minTextView.setText(preferences.getString("daily_forecast_tmp_min_4", "") + "°C");
//		day_6_minTextView.setText(preferences.getString("daily_forecast_tmp_min_5", "") + "°C");
//		day_7_minTextView.setText(preferences.getString("daily_forecast_tmp_min_6", "") + "°C");
		
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
		day_4_dateString = preferences.getString("daily_forecast_date_3", "");
		if (!"".equals(day_4_dateString)) {
			day_4_dateString = day_4_dateString.substring(5, 10);
		}
		day_4_dateTextView.setText(day_4_dateString);
		day_5_dateString = preferences.getString("daily_forecast_date_4", "");
		if (!"".equals(day_5_dateString)) {
			day_5_dateString = day_5_dateString.substring(5, 10);
		}
		day_5_dateTextView.setText(day_5_dateString);
		day_6_dateString = preferences.getString("daily_forecast_date_5", "");
		if (!"".equals(day_6_dateString)) {
			day_6_dateString = day_6_dateString.substring(5, 10);
		}
		day_6_dateTextView.setText(day_6_dateString);
		day_7_dateString = preferences.getString("daily_forecast_date_6", "");
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
		initTemperatureGraph();
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
		SharedPreferences.Editor editor = PreferenceManager
				.getDefaultSharedPreferences((Context)this).edit();
		switch (v.getId()) {
		case R.id.change_city:
			Intent intent = new Intent(this, ChooseAreaActivity.class);
			intent.putExtra("from_weather_activity", true);
			startActivity(intent);
			finish();
			break;
		case R.id.red_style:
			editor.putString("weatherTitleBackgroundColor", 
            		weatherTitleBackgroundColorRedString);
            editor.putString("weatherBackgroundColor", 
            		weatherBackgroundColorRedString);
            editor.putString("weatherScrollviewBackgroundColor", 
            		WeatherScrollviewBackgroundColorRedString);
            editor.putString("refreshColorStyle", "red");
            break;
		case R.id.yellow_style:
			Log.d("Menu", "yellow");
			editor.putString("weatherTitleBackgroundColor", 
            		weatherTitleBackgroundColorYellowString);
            editor.putString("weatherBackgroundColor", 
            		weatherBackgroundColorYellowString);
            editor.putString("weatherScrollviewBackgroundColor", 
            		WeatherScrollviewBackgroundColorYellowString);
            editor.putString("refreshColorStyle", "yellow");
			break;
		case R.id.blue_style:
			Log.d("Menu", "blue");
			editor.putString("weatherTitleBackgroundColor", 
            		weatherTitleBackgroundColorBlueString);
            editor.putString("weatherBackgroundColor", 
            		weatherBackgroundColorBlueString);
            editor.putString("weatherScrollviewBackgroundColor", 
            		WeatherScrollviewBackgroundColorBlueString);
            editor.putString("refreshColorStyle", "blue");
			break;
		case R.id.green_style:
			Log.d("Menu", "green");
			editor.putString("weatherTitleBackgroundColor", 
            		weatherTitleBackgroundColorGreenString);
            editor.putString("weatherBackgroundColor", 
            		weatherBackgroundColorGreenString);
            editor.putString("weatherScrollviewBackgroundColor", 
            		WeatherScrollviewBackgroundColorGreenString);
            editor.putString("refreshColorStyle", "green");
			break;
		default:
			break;
		}
		editor.commit();
    	makeColor();
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		publishTextView.setText("同步中...");
		SharedPreferences preferences = 
				PreferenceManager.getDefaultSharedPreferences(this);
		String countyCodeString = preferences.getString("city_id", "");
		if (!TextUtils.isEmpty(countyCodeString)) {
			Log.d("DEBUG", "yoyoyo1");
			queryWeather(countyCodeString);
		}
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				swipeRefreshLayout.setRefreshing(false);
				Log.d("DEBUG", "yoyoyo");
				
			}
		}, 5000);
	}

	@Override
	public void onActiveViewChanged(View v) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
    public void onBackPressed() {
        final int drawerState = menuDrawer.getDrawerState();
        if (drawerState == MenuDrawer.STATE_OPEN || drawerState == MenuDrawer.STATE_OPENING) {
        	menuDrawer.closeMenu();
            return;
        }

        super.onBackPressed();
    }
	
}
