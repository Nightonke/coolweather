package com.coolweather.app.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.PrivateCredentialPermission;

import org.apache.http.util.EncodingUtils;

import com.coolweather.app.R;
import com.coolweather.app.model.City;
import com.coolweather.app.model.CoolWeatherDB;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;
import com.coolweather.app.util.HttpCallbackListener;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.Utility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.util.LogWriter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChooseAreaActivity extends Activity {

	public static final int LEVEL_PROVINCE = 0;
	public static final int LEVEL_CITY = 1;
	public static final int LEVEL_COUNTY = 2;
	
	private ProgressDialog progressDialog;
	private TextView titleTextView;
	private ListView listView;
	private ArrayAdapter<String> adapter;
	private CoolWeatherDB coolWeatherDB;
	private List<String> dataList = new ArrayList<String>();
	
	private List<Province> provinceList;
	private List<City> cityList;
	private List<County> countyList;
	
	private Province selectedProvince;
	private City selectedCity;
	private int currentLevel;
	
	private String idTableLocationString = "idTable.txt";
	
	private boolean isFromWeatherActivity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		isFromWeatherActivity = getIntent().getBooleanExtra(
				"from_weather_activity", false);
		SharedPreferences preferences = 
				PreferenceManager.getDefaultSharedPreferences(this);
		if (preferences.getBoolean("city_selected", false)
				&& !isFromWeatherActivity) {
			Intent intent = new Intent(this, WeatherActivity.class);
			startActivity(intent);
			finish();
			return;
		}
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_area);
		
		listView = (ListView)findViewById(R.id.list_view);
		titleTextView = (TextView)findViewById(R.id.title_text);
		adapter = new ArrayAdapter<String>(
				this, android.R.layout.simple_list_item_1, dataList);
		listView.setAdapter(adapter);
		try {
			coolWeatherDB = CoolWeatherDB.getInstance(this);
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long arg3) {
				// TODO Auto-generated method stub
				if (currentLevel == LEVEL_PROVINCE) {
					selectedProvince = provinceList.get(position);
					queryCities();
				} else if (currentLevel == LEVEL_CITY) {
					selectedCity = cityList.get(position);
					queryCounties();
				} else if (currentLevel == LEVEL_COUNTY) {
					String countyCodeString = 
							countyList.get(position).getCountyCode();
					Intent intent = new Intent(
							ChooseAreaActivity.this, WeatherActivity.class);
					intent.putExtra("county_code", countyCodeString);
					startActivity(intent);
					finish();
				}
			}
			
		});
		queryProvinces();
	}
	
	private void queryProvinces() {
		provinceList = coolWeatherDB.loadProvinces();
		if (provinceList.size() > 1) {
			dataList.clear();
			for (Province province : provinceList) {
				dataList.add(province.getProvinceName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleTextView.setText("中国");
			currentLevel = LEVEL_PROVINCE;
		} else {
			// queryFromServer(null, "province");
			importData();
		}
	}
	
	private void queryCities() {
		cityList = coolWeatherDB.loadCities(selectedProvince.getProvinceCode());
		if (cityList.size() > 0) {
			dataList.clear();
			for (City city : cityList) {
				dataList.add(city.getCityName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleTextView.setText(selectedProvince.getProvinceName());
			currentLevel = LEVEL_CITY;
		} else {
			// queryFromServer(selectedProvince.getProvinceCode(), "city");
			importData();
		}
	}
	
	private void queryCounties() {
		countyList = coolWeatherDB.loadCounties(selectedCity.getCityCode());
		if (countyList.size() > 0) {
			dataList.clear();
			for (County county : countyList) {
				dataList.add(county.getCountyName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleTextView.setText(selectedCity.getCityName());
			currentLevel = LEVEL_COUNTY;
		} else {
			// queryFromServer(selectedCity.getCityCode(), "county");
			importData();
		}
	}
	
	private void importData() {
		try { 
			InputStreamReader inputReader = new InputStreamReader(
					getResources().getAssets().open(idTableLocationString),
					"UTF-8"); 
		    BufferedReader bufReader = new BufferedReader(inputReader);
		    String line="";   
		    
		    boolean firstTime = true;
		    
		    while((line = bufReader.readLine()) != null) {
		    	
		    	String[] arrayStrings;
		    	arrayStrings = line.split("\\|");
		    	
		    	if (arrayStrings.length != 9) {
		    		Log.d("idTable", "split function error!");
		    	}
		    	
		    	String areaIdString = arrayStrings[0];
		    	String nameEnString = arrayStrings[1];
		    	String nameCnString = arrayStrings[2];
		    	String districtEnString = arrayStrings[3];
		    	String districtCnString = arrayStrings[4];
		    	String provEnString = arrayStrings[5];
		    	String provCnString = arrayStrings[6];
		    	String nationEnString = arrayStrings[7];
		    	String nationCnString = arrayStrings[8];
		    	
		    	String provinceCodeString = areaIdString.substring(0, 5);
		    	String cityCodeString = areaIdString.substring(0, 7);
		    	String countyCodeString = areaIdString.substring(0, 9);
		    	
//		    	Log.d("idTable", line);
//		    	Log.d("idTable", areaIdString);
//		    	Log.d("idTable", nameEnString);
//		    	Log.d("idTable", nameCnString);
//		    	Log.d("idTable", districtEnString);
//		    	Log.d("idTable", districtCnString);
//		    	Log.d("idTable", provEnString);
//		    	Log.d("idTable", provCnString);
//		    	Log.d("idTable", nationEnString);
//		    	Log.d("idTable", nationCnString);
		    	Log.d("idTable", provinceCodeString);
//		    	Log.d("idTable", cityCodeString);
//		    	Log.d("idTable", countyCodeString);
		    	
		    	String string1010 = "1010";
		    	
		    	if (firstTime) {
		    		provinceCodeString = "10101";
		    		cityCodeString = "1010102";
		    		countyCodeString = "101010200";
			    	Log.d("idTable", line);
			    	Log.d("idTable", areaIdString);
			    	Log.d("idTable", nameEnString);
			    	Log.d("idTable", nameCnString);
			    	Log.d("idTable", districtEnString);
			    	Log.d("idTable", districtCnString);
			    	Log.d("idTable", provEnString);
			    	Log.d("idTable", provCnString);
			    	Log.d("idTable", nationEnString);
			    	Log.d("idTable", nationCnString);
			    	Log.d("idTable", provinceCodeString);
			    	Log.d("idTable", cityCodeString);
			    	Log.d("idTable", countyCodeString);
			    	
			    	firstTime = false;
		    	}
		    	
		    	Province province = new Province();
		    	province.setProvinceCode(provinceCodeString);
		    	province.setProvinceName(provCnString);
		    	coolWeatherDB.saveProvince(province);
		    	
		    	City city = new City();
		    	city.setProvincedId(provinceCodeString);
		    	city.setCityCode(cityCodeString);
		    	city.setCityName(districtCnString);
		    	coolWeatherDB.saveCity(city);
		    	
		    	County county = new County();
		    	county.setCityId(cityCodeString);
		    	county.setCountyCode(countyCodeString);
		    	county.setCountyName(nameCnString);
		    	coolWeatherDB.saveCounty(county);
		    		    	
		    }
		} catch (Exception e) { 
		    e.printStackTrace(); 
		}
	}
	
	private void queryFromServer(final String code, final String type) {
		String addressString;
		if (!TextUtils.isEmpty(code)) {
			addressString = "http://www.weather.com.cn/data/list3/city" 
					+ code + ".xml";
		} else {
			addressString = "http://www.weather.com.cn/data/list3/city.xml";
		}
		showProgressDialog();
		HttpUtil.sendHttpRequest(addressString, new HttpCallbackListener() {
			
			@Override
			public void onFinish(String response) {
				// TODO Auto-generated method stub
				boolean result = false;
				if ("province".equals(type)) {
					result = Utility.handleProvincesResponse(
							coolWeatherDB, response);
				} else if ("city".equals(type)) {
					result = Utility.handleCitiesResponse(
							coolWeatherDB, response, selectedProvince.getProvinceCode());
				} else if ("county".equals(type)) {
					result = Utility.handleCountiesResponse(
							coolWeatherDB, response, selectedCity.getCityCode());
				}
				if (result) {
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							closeProgressDialog();
							if ("province".equals(type)) {
								queryProvinces();
							} else if ("city".equals(type)) {
								queryCities();
							} else if ("county".equals(type)) {
								queryCounties();
							}
						}
					});
				}
			}
			
			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						closeProgressDialog();
						Toast.makeText(ChooseAreaActivity.this, 
								"加载失败", 
								Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
	}
	
	private void showProgressDialog() {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("正在加载……");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}
	
	private void closeProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}
	
	@Override
	public void onBackPressed() {
		if (currentLevel == LEVEL_COUNTY) {
			queryCities();
		} else if (currentLevel == LEVEL_CITY) {
			queryProvinces();
		} else {
			if (isFromWeatherActivity) {
				Intent intent = new Intent(this, WeatherActivity.class);
				startActivity(intent);
			}
			finish();
		}
	}
	
	
	
}
