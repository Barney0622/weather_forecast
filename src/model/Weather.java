package model;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;

import org.json.*;

public class Weather {
	static JSONArray location = getLocation(fetchOpendata());;
	
	
	
	public static void main(String[] args) {
		String jsonData = fetchOpendata();
		if (jsonData != null) {
			getLocationName();
		}
	}
	
	
	public Weather() {
		
	}
	
	
	private static String fetchOpendata() {
		try {
			URL url = new URL("https://opendata.cwb.gov.tw/api/v1/rest/datastore/F-C0032-001?Authorization=CWB-D5573591-825C-4A60-B2A0-5B9D449669E5");
			URLConnection urlConn = url.openConnection();
			urlConn.connect();
			
			InputStream in = urlConn.getInputStream();
			BufferedInputStream bin = new BufferedInputStream(in);
			
			
			byte[] buf = new byte[1024*1024];
			int len;
			
			
			StringBuffer sb = new StringBuffer();
			while( (len = bin.read(buf)) != -1) {	
				sb.append(new String(buf, 0, len));
				
			}
			
			bin.close();
			
			return sb.toString();
			
		
		} catch (Exception e) {
			System.out.println(e.toString());
			return null;
		}
		
		
	}

	private static JSONArray getLocation(String json) {
		
		JSONObject root = new JSONObject(json);
		JSONObject records = root.getJSONObject("records");
		
		JSONArray location = records.getJSONArray("location");
		
		return location;

	}
	
	private static void getLocationName() {
		
		
		for(int i=0; i<location.length(); i++) {
			JSONObject locationvalue = location.getJSONObject(i);
			System.out.println(locationvalue.getString("locationName"));
			JSONObject locationweather = locationvalue.getJSONObject("WeatherElement");
		}
	}
}

