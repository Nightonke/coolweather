package com.coolweather.app.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.coolweather.app.activity.NJsonObject;
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
		Log.d("JSON", response);
		try {
			
			SharedPreferences.Editor editor = PreferenceManager
					.getDefaultSharedPreferences(context).edit();
			
			JSONObject jsonObject = new JSONObject(response);
			JSONArray weatherInfoArray = jsonObject.getJSONArray("HeWeather data service 3.0");
			JSONObject weatherInfo = weatherInfoArray.getJSONObject(0);
			
			if (!weatherInfo.isNull("basic")) {
				// 城市名
				JSONObject basic = weatherInfo.getJSONObject("basic");
				String basic_city = basic.getString("city");
				Log.d("JSON", "basic_city: " + basic_city);
				editor.putString("basic_city", basic_city);
				// 国家名
				String basic_cnty = basic.getString("cnty");
				Log.d("JSON", "basic_cnty: " + basic_cnty);
				editor.putString("basic_cnty", basic_cnty);
				// 城市ID
				String basic_id = basic.getString("id");
				Log.d("JSON", "basic_id: " + basic_id);
				editor.putString("basic_id", basic_id);
				// 纬度
				String basic_lat = basic.getString("lat");
				Log.d("JSON", "basic_lat: " + basic_lat);
				editor.putString("basic_lat", basic_lat);
				// 经度
				String basic_lon = basic.getString("lon");
				Log.d("JSON", "basic_lon: " + basic_lon);
				editor.putString("basic_lon", basic_lon);
				// 更新时间
				JSONObject update = basic.getJSONObject("update");
				// 更新的当地时间
				String basic_loc = update.getString("loc");
				Log.d("JSON", "basic_loc: " + basic_loc);
				editor.putString("basic_loc", basic_loc);
				// 更新的UTC时间
				String basic_utc = update.getString("utc");
				Log.d("JSON", "basic_utc: " + basic_utc);
				editor.putString("basic_utc", basic_utc);
				
				editor.putString("city_id", (basic_id.subSequence(2, 
						basic_id.length() - 1)).toString());
			}

			if (!weatherInfo.isNull("aqi")) {
				JSONObject aqi = weatherInfo.getJSONObject("aqi");
				JSONObject city = aqi.getJSONObject("city");
				
				if (!city.isNull("aqi")) {
					// 空气质量指数
					String api_city_aqi = city.getString("aqi");
					Log.d("JSON", "api_city_aqi: " + api_city_aqi);
					editor.putString("api_city_aqi", api_city_aqi);
				}
				
				if (!city.isNull("pm10")) {
					// PM2.5指数
					String api_city_pm10 = city.getString("pm10");
					Log.d("JSON", "api_city_pm10: " + api_city_pm10);
					editor.putString("api_city_pm10", api_city_pm10);
				}
				
				if (!city.isNull("pm25")) {
					// PM10指数
					String api_city_pm25 = city.getString("pm25");
					Log.d("JSON", "api_city_pm25: " + api_city_pm25);
					editor.putString("api_city_pm25", api_city_pm25);
				}
				
				if (!city.isNull("qlty")) {
					// 空气质量类别
					String api_city_qlty = city.getString("qlty");
					Log.d("JSON", "api_city_qlty: " + api_city_qlty);
					editor.putString("api_city_qlty", api_city_qlty);
				}

			}
			
			if (!weatherInfo.isNull("daily_forecast")) {
				JSONArray daily_forecast = weatherInfo.
						getJSONArray("daily_forecast");
				for (int i = 0; i < daily_forecast.length(); i++) {
					JSONObject a_daily_forecast = 
							daily_forecast.getJSONObject(i);
					
					if (!a_daily_forecast.isNull("astro")) {
						JSONObject astro = a_daily_forecast.getJSONObject("astro");
						if (!astro.isNull("sr")) {
							// 日出时间
							String daily_forecast_astro_sr = astro.getString("sr");
							Log.d("JSON", "daily_forecast_astro_sr_" 
									+ String.valueOf(i) 
									+ ": " 
									+ daily_forecast_astro_sr);
							editor.putString("daily_forecast_astro_sr_" 
									+ String.valueOf(i),
									daily_forecast_astro_sr);
						}
						
						if (!astro.isNull("ss")) {
							// 日落时间
							String daily_forecast_astro_ss = astro.getString("ss");
							Log.d("JSON", "daily_forecast_astro_ss_" 
									+ String.valueOf(i) 
									+ ": " 
									+ daily_forecast_astro_ss);
							editor.putString("daily_forecast_astro_ss_" 
									+ String.valueOf(i),
									daily_forecast_astro_ss);
						}
					}
					
					if (!a_daily_forecast.isNull("cond")) {
						JSONObject cond = a_daily_forecast.getJSONObject("cond");
						if (!cond.isNull("code_d")) {
							// 白天天气代码
							String daily_forecast_cond_code_d = cond.getString("code_d");
							Log.d("JSON", "daily_forecast_cond_code_d_" 
									+ String.valueOf(i) 
									+ ": " 
									+ daily_forecast_cond_code_d);
							editor.putString("daily_forecast_cond_code_d_" 
									+ String.valueOf(i),
									daily_forecast_cond_code_d);
						}
						
						if (!cond.isNull("code_n")) {
							// 夜间天气代码
							String daily_forecast_cond_code_n = cond.getString("code_n");
							Log.d("JSON", "daily_forecast_cond_code_n_" 
									+ String.valueOf(i) 
									+ ": " 
									+ daily_forecast_cond_code_n);
							editor.putString("daily_forecast_cond_code_n_" 
									+ String.valueOf(i),
									daily_forecast_cond_code_n);
						}
						
						if (!cond.isNull("txt_d")) {
							// 白天天气
							String daily_forecast_cond_txt_d = cond.getString("txt_d");
							Log.d("JSON", "daily_forecast_cond_txt_d_" 
									+ String.valueOf(i) 
									+ ": " 
									+ daily_forecast_cond_txt_d);
							editor.putString("daily_forecast_cond_txt_d_" 
									+ String.valueOf(i),
									daily_forecast_cond_txt_d);
						}
						
						if (!cond.isNull("txt_n")) {
							// 夜间天气
							String daily_forecast_cond_txt_n = cond.getString("txt_n");
							Log.d("JSON", "daily_forecast_cond_txt_n_" 
									+ String.valueOf(i) 
									+ ": " 
									+ daily_forecast_cond_txt_n);
							editor.putString("daily_forecast_cond_txt_n_" 
									+ String.valueOf(i),
									daily_forecast_cond_txt_n);
						}
					}
					
					if (!a_daily_forecast.isNull("date")) {
						// 当地时间
						String daily_forecast_date = a_daily_forecast.getString("date");
						Log.d("JSON", "daily_forecast_date_"
								+ String.valueOf(i)
								+ ": "
								+ daily_forecast_date);
						editor.putString("daily_forecast_date_"
								+ String.valueOf(i), 
								daily_forecast_date);
					}
					
					if (!a_daily_forecast.isNull("hum")) {
						// 湿度
						String daily_forecast_hum = a_daily_forecast.getString("hum");
						Log.d("JSON", "daily_forecast_hum_"
								+ String.valueOf(i)
								+ ": "
								+ daily_forecast_hum);
						editor.putString("daily_forecast_hum_"
								+ String.valueOf(i), 
								daily_forecast_hum);
					}
					
					if (!a_daily_forecast.isNull("pcpn")) {
						// 降雨量
						String daily_forecast_pcpn = a_daily_forecast.getString("pcpn");
						Log.d("JSON", "daily_forecast_pcpn_"
								+ String.valueOf(i)
								+ ": "
								+ daily_forecast_pcpn);
						editor.putString("daily_forecast_pcpn_"
								+ String.valueOf(i), 
								daily_forecast_pcpn);
					}
					
					if (!a_daily_forecast.isNull("pop")) {
						// 降雨概率
						String daily_forecast_pop = a_daily_forecast.getString("pop");
						Log.d("JSON", "daily_forecast_pop_"
								+ String.valueOf(i)
								+ ": "
								+ daily_forecast_pop);
						editor.putString("daily_forecast_pop_"
								+ String.valueOf(i), 
								daily_forecast_pop);
					}
					
					if (!a_daily_forecast.isNull("pres")) {
						// 气压
						String daily_forecast_pres = a_daily_forecast.getString("pres");
						Log.d("JSON", "daily_forecast_pres_"
								+ String.valueOf(i)
								+ ": "
								+ daily_forecast_pres);
						editor.putString("daily_forecast_pres_"
								+ String.valueOf(i), 
								daily_forecast_pres);
					}
					
					if (!a_daily_forecast.isNull("tmp")) {
						JSONObject tmp = a_daily_forecast.getJSONObject("tmp");
						if (!tmp.isNull("max")) {
							// 最高温
							String daily_forecast_tmp_max = tmp.getString("max");
							Log.d("JSON", "daily_forecast_tmp_max_"
									+ String.valueOf(i)
									+ ": "
									+ daily_forecast_tmp_max);
							editor.putString("daily_forecast_tmp_max_"
									+ String.valueOf(i), 
									daily_forecast_tmp_max);
						}
						
						if (!tmp.isNull("min")) {
							// 最低温
							String daily_forecast_tmp_min = tmp.getString("min");
							Log.d("JSON", "daily_forecast_tmp_min_"
									+ String.valueOf(i)
									+ ": "
									+ daily_forecast_tmp_min);
							editor.putString("daily_forecast_tmp_min_"
									+ String.valueOf(i), 
									daily_forecast_tmp_min);
						}
					}
					
					if (!a_daily_forecast.isNull("vis")) {
						// 能见度
						String daily_forecast_vis = a_daily_forecast.getString("vis");
						Log.d("JSON", "daily_forecast_vis_"
								+ String.valueOf(i)
								+ ": "
								+ daily_forecast_vis);
						editor.putString("daily_forecast_vis_"
								+ String.valueOf(i), 
								daily_forecast_vis);
					}
					
					if (!a_daily_forecast.isNull("wind")) {
						JSONObject wind = a_daily_forecast.getJSONObject("wind");
						if (!wind.isNull("deg")) {
							// 风向（角度）
							String daily_forecast_wind_deg = wind.getString("deg");
							Log.d("JSON", "daily_forecast_wind_deg_"
									+ String.valueOf(i)
									+ ": "
									+ daily_forecast_wind_deg);
							editor.putString("daily_forecast_wind_deg_"
									+ String.valueOf(i), 
									daily_forecast_wind_deg);
						}
						
						if (!wind.isNull("dir")) {
							// 风向（方向）
							String daily_forecast_wind_dir = wind.getString("dir");
							Log.d("JSON", "daily_forecast_wind_dir_"
									+ String.valueOf(i)
									+ ": "
									+ daily_forecast_wind_dir);
							editor.putString("daily_forecast_wind_dir_"
									+ String.valueOf(i), 
									daily_forecast_wind_dir);
						}
						
						if (!wind.isNull("sc")) {
							// 风力等级
							String daily_forecast_wind_sc = wind.getString("sc");
							Log.d("JSON", "daily_forecast_wind_sc_"
									+ String.valueOf(i)
									+ ": "
									+ daily_forecast_wind_sc);
							editor.putString("daily_forecast_wind_sc_"
									+ String.valueOf(i), 
									daily_forecast_wind_sc);
						}
						
						if (!wind.isNull("spd")) {
							// 风速
							String daily_forecast_wind_spd = wind.getString("spd");
							Log.d("JSON", "daily_forecast_wind_spd_"
									+ String.valueOf(i)
									+ ": "
									+ daily_forecast_wind_spd);
							editor.putString("daily_forecast_wind_spd_"
									+ String.valueOf(i), 
									daily_forecast_wind_spd);
						}
					}
				}
			}
			
			if (!weatherInfo.isNull("hourly_forecast")) {
				JSONArray hourly_forecast = weatherInfo.
						getJSONArray("hourly_forecast");
				for (int i = 0; i < hourly_forecast.length(); i++) {
					JSONObject a_hourly_forecast = 
							hourly_forecast.getJSONObject(i);
					if (!a_hourly_forecast.isNull("date")) {
						// 当地时间
						String hourly_forecast_date = a_hourly_forecast.getString("date");
						Log.d("JSON", "hourly_forecast_date_"
								+ String.valueOf(i)
								+ ": "
								+ hourly_forecast_date);
						editor.putString("hourly_forecast_date_"
								+ String.valueOf(i), 
								hourly_forecast_date);
					}
					
					if (!a_hourly_forecast.isNull("hum")) {
						// 湿度
						String hourly_forecast_hum = a_hourly_forecast.getString("hum");
						Log.d("JSON", "hourly_forecast_hum_"
								+ String.valueOf(i)
								+ ": "
								+ hourly_forecast_hum);
						editor.putString("hourly_forecast_hum_"
								+ String.valueOf(i), 
								hourly_forecast_hum);
					}
					
					if (!a_hourly_forecast.isNull("pop")) {
						// 降雨概率
						String hourly_forecast_pop = a_hourly_forecast.getString("pop");
						Log.d("JSON", "hourly_forecast_pop_"
								+ String.valueOf(i)
								+ ": "
								+ hourly_forecast_pop);
						editor.putString("hourly_forecast_pop_"
								+ String.valueOf(i), 
								hourly_forecast_pop);
					}
					
					if (!a_hourly_forecast.isNull("pres")) {
						// 气压
						String hourly_forecast_pres = a_hourly_forecast.getString("pres");
						Log.d("JSON", "hourly_forecast_pres_"
								+ String.valueOf(i)
								+ ": "
								+ hourly_forecast_pres);
						editor.putString("hourly_forecast_pres_"
								+ String.valueOf(i), 
								hourly_forecast_pres);
					}
					
					if (!a_hourly_forecast.isNull("wind")) {
						JSONObject wind = a_hourly_forecast.getJSONObject("wind");
						if (!wind.isNull("deg")) {
							// 风向（角度）
							String hourly_forecast_wind_deg = wind.getString("deg");
							Log.d("JSON", "hourly_forecast_wind_deg_"
									+ String.valueOf(i)
									+ ": "
									+ hourly_forecast_wind_deg);
							editor.putString("hourly_forecast_wind_deg_"
									+ String.valueOf(i), 
									hourly_forecast_wind_deg);
						}
						
						if (!wind.isNull("dir")) {
							// 风向（方向）
							String hourly_forecast_wind_dir = wind.getString("dir");
							Log.d("JSON", "hourly_forecast_wind_dir_"
									+ String.valueOf(i)
									+ ": "
									+ hourly_forecast_wind_dir);
							editor.putString("hourly_forecast_wind_dir_"
									+ String.valueOf(i), 
									hourly_forecast_wind_dir);
						}
						
						if (!wind.isNull("sc")) {
							// 风力等级
							String hourly_forecast_wind_sc = wind.getString("sc");
							Log.d("JSON", "hourly_forecast_wind_sc_"
									+ String.valueOf(i)
									+ ": "
									+ hourly_forecast_wind_sc);
							editor.putString("hourly_forecast_wind_sc_"
									+ String.valueOf(i), 
									hourly_forecast_wind_sc);
						}
						
						if (!wind.isNull("spd")) {
							// 风速
							String hourly_forecast_wind_spd = wind.getString("spd");
							Log.d("JSON", "hourly_forecast_wind_spd_"
									+ String.valueOf(i)
									+ ": "
									+ hourly_forecast_wind_spd);
							editor.putString("hourly_forecast_wind_spd_"
									+ String.valueOf(i), 
									hourly_forecast_wind_spd);
						}	
					}
				}
			}
			
			if (!weatherInfo.isNull("now")) {
				JSONObject now = weatherInfo.getJSONObject("now");
				if (!now.isNull("cond")) {
					JSONObject cond = now.getJSONObject("cond");
					if (!cond.isNull("code")) {
						// 天气代码
						String now_cond_code = cond.getString("code");
						Log.d("JSON", "now_cond_code: " + now_cond_code);
						editor.putString("now_cond_code", now_cond_code);
					}
					
					if (!cond.isNull("txt")) {
						// 天气描述
						String now_cond_txt = cond.getString("txt");
						Log.d("JSON", "now_cond_txt: " + now_cond_txt);
						editor.putString("now_cond_txt", now_cond_txt);
					}
				}
				
				if (!now.isNull("fl")) {
					// 体感温度
					String now_fl = now.getString("fl");
					Log.d("JSON", "now_fl: " + now_fl);
					editor.putString("now_fl", now_fl);
				}
				
				if (!now.isNull("hum")) {
					// 湿度
					String now_hum = now.getString("hum");
					Log.d("JSON", "now_hum: " + now_hum);
					editor.putString("now_hum", now_hum);
				}
				
				if (!now.isNull("pcpn")) {
					// 降雨量
					String now_pcpn = now.getString("pcpn");
					Log.d("JSON", "now_pcpn: " + now_pcpn);
					editor.putString("now_pcpn", now_pcpn);	
				}
				
				if (!now.isNull("pres")) {
					// 气压
					String now_pres = now.getString("pres");
					Log.d("JSON", "now_pres: " + now_pres);
					editor.putString("now_pres", now_pres);	
				}
						
				if (!now.isNull("tmp")) {
					// 当前温度
					String now_tmp = now.getString("tmp");
					Log.d("JSON", "now_tmp: " + now_tmp);
					editor.putString("now_tmp", now_tmp);
				}
				
				if (!now.isNull("vis")) {
					// 能见度
					String now_vis = now.getString("vis");
					Log.d("JSON", "now_vis: " + now_vis);
					editor.putString("now_vis", now_vis);
				}
				
				if (!now.isNull("wind")) {
					JSONObject wind = now.getJSONObject("wind");
					if (!wind.isNull("deg")) {
						// 风向（角度）
						String now_wind_deg = wind.getString("deg");
						Log.d("JSON", "now_wind_deg: " + now_wind_deg);
						editor.putString("now_wind_deg", now_wind_deg);
					}
						
					if (!wind.isNull("dir")) {
						// 风向（方向）
						String now_wind_dir = wind.getString("dir");
						Log.d("JSON", "now_wind_dir: " + now_wind_dir);
						editor.putString("now_wind_dir", now_wind_dir);	
					}
					
					if (!wind.isNull("sc")) {
						// 风力等级
						String now_wind_sc = wind.getString("sc");
						Log.d("JSON", "now_wind_sc: " + now_wind_sc);
						editor.putString("now_wind_sc", now_wind_sc);
					}
					
					if (!wind.isNull("spd")) {
						// 风速
						String now_wind_spd = wind.getString("spd");
						Log.d("JSON", "now_wind_spd: " + now_wind_spd);
						editor.putString("now_wind_spd", now_wind_spd);
					}
				}
			}
			
			if (!weatherInfo.isNull("suggestion")) {
				JSONObject suggestion = weatherInfo.getJSONObject("suggestion");
				if (!suggestion.isNull("comf")) {
					// 舒适指数
					JSONObject comf = suggestion.getJSONObject("comf");
					String suggestion_comf_brf = comf.getString("brf");
					Log.d("JSON", "suggestion_comf_brf: " + suggestion_comf_brf);
					editor.putString("suggestion_comf_brf", suggestion_comf_brf);
					String suggestion_comf_txt = comf.getString("txt");
					Log.d("JSON", "suggestion_comf_txt: " + suggestion_comf_txt);
					editor.putString("suggestion_comf_txt", suggestion_comf_txt);
				}
				
				if (!suggestion.isNull("cw")) {
					// 洗车指数
					JSONObject cw = suggestion.getJSONObject("cw");
					String suggestion_cw_brf = cw.getString("brf");
					Log.d("JSON", "suggestion_cw_brf: " + suggestion_cw_brf);
					editor.putString("suggestion_cw_brf", suggestion_cw_brf);
					String suggestion_cw_txt = cw.getString("txt");
					Log.d("JSON", "suggestion_cw_txt: " + suggestion_cw_txt);
					editor.putString("suggestion_cw_txt", suggestion_cw_txt);
				}
				
				if (!suggestion.isNull("drsg")) {
					// 穿衣指数
					JSONObject drsg = suggestion.getJSONObject("drsg");
					String suggestion_drsg_brf = drsg.getString("brf");
					Log.d("JSON", "suggestion_drsg_brf: " + suggestion_drsg_brf);
					editor.putString("suggestion_drsg_brf", suggestion_drsg_brf);
					String suggestion_drsg_txt = drsg.getString("txt");
					Log.d("JSON", "suggestion_drsg_txt: " + suggestion_drsg_txt);
					editor.putString("suggestion_drsg_txt", suggestion_drsg_txt);
				}
				
				if (!suggestion.isNull("flu")) {
					// 感冒指数
					JSONObject flu = suggestion.getJSONObject("flu");
					String suggestion_flu_brf = flu.getString("brf");
					Log.d("JSON", "suggestion_flu_brf: " + suggestion_flu_brf);
					editor.putString("suggestion_flu_brf", suggestion_flu_brf);
					String suggestion_flu_txt = flu.getString("txt");
					Log.d("JSON", "suggestion_flu_txt: " + suggestion_flu_txt);
					editor.putString("suggestion_flu_txt", suggestion_flu_txt);
				}
				
				if (!suggestion.isNull("sport")) {
					// 运动指数
					JSONObject sport = suggestion.getJSONObject("sport");
					String suggestion_sport_brf = sport.getString("brf");
					Log.d("JSON", "suggestion_sport_brf: " + suggestion_sport_brf);
					editor.putString("suggestion_sport_brf", suggestion_sport_brf);
					String suggestion_sport_txt = sport.getString("txt");
					Log.d("JSON", "suggestion_sport_txt: " + suggestion_sport_txt);
					editor.putString("suggestion_sport_txt", suggestion_sport_txt);
				}
				
				if (!suggestion.isNull("trav")) {
					// 旅游指数
					JSONObject trav = suggestion.getJSONObject("trav");
					String suggestion_trav_brf = trav.getString("brf");
					Log.d("JSON", "suggestion_trav_brf: " + suggestion_trav_brf);
					editor.putString("suggestion_trav_brf", suggestion_trav_brf);
					String suggestion_trav_txt = trav.getString("txt");
					Log.d("JSON", "suggestion_trav_txt: " + suggestion_trav_txt);
					editor.putString("suggestion_trav_txt", suggestion_trav_txt);
				}
				
				if (!suggestion.isNull("uv")) {
					// 紫外线指数
					JSONObject uv = suggestion.getJSONObject("uv");
					String suggestion_uv_brf = uv.getString("brf");
					Log.d("JSON", "suggestion_uv_brf: " + suggestion_uv_brf);
					editor.putString("suggestion_uv_brf", suggestion_uv_brf);
					String suggestion_uv_txt = uv.getString("txt");
					Log.d("JSON", "suggestion_uv_txt: " + suggestion_uv_txt);
					editor.putString("suggestion_uv_txt", suggestion_uv_txt);
				}
			}

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日",
					Locale.CHINA);
			editor.putString("current_date", sdf.format(new Date()));
			
			editor.putBoolean("city_selected", true);
			
			editor.commit();

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
}
