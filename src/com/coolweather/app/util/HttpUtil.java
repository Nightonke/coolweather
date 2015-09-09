package com.coolweather.app.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import java.net.HttpURLConnection;


import android.util.Log;

public class HttpUtil {

	// 189a0f299820e4b19e267b9af5990ba3
	private static String API_KEY = "9a97ae4966744735adc0102f2366c55f";
	
	public static void sendHttpRequest(final String address,
			final HttpCallbackListener listener) {

		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpURLConnection connection = null;
				try {
					URL url = new URL(address + "&key=" + API_KEY);
					Log.d("weather", address);
					connection = (HttpURLConnection)url.openConnection();
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(8000);
//					connection.setRequestProperty("Apikey", API_KEY);
					connection.setReadTimeout(8000);
					InputStream in = connection.getInputStream();
					BufferedReader reader = 
							new BufferedReader(new InputStreamReader(in));
					StringBuilder response = new StringBuilder();
					String lineString;
					while ((lineString = reader.readLine()) != null) {
						Log.d("weather", lineString);
						response.append(lineString);
					}
					if (listener != null) {
						listener.onFinish(response.toString());
					}
				} catch (Exception e) {
					if (listener != null) {
						listener.onError(e);
						e.printStackTrace();
					}
				} finally {
					if (connection != null) {
						connection.disconnect();
					}
				}
			}
		}).start();
		
	}
	
}
