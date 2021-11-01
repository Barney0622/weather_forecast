package model;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

import org.json.*;

public class Weather {
	static JSONArray location = getLocation(fetchOpendata());;


	public Weather() {
		String jsonData = fetchOpendata();
		if (jsonData != null) {
			
			insertLocation();
			insertElement();
			insertTime();
			insertRecord();
			
			
		}else {
			System.out.println("NoData!");
		}
	}

	private static String fetchOpendata() {
		try {
			URL url = new URL(
					"https://opendata.cwb.gov.tw/api/v1/rest/datastore/F-C0032-001?Authorization=CWB-D5573591-825C-4A60-B2A0-5B9D449669E5");
			URLConnection urlConn = url.openConnection();
			urlConn.connect();

			InputStream in = urlConn.getInputStream();
			BufferedInputStream bin = new BufferedInputStream(in);

			byte[] buf = new byte[1024 * 1024];
			int len;

			StringBuffer sb = new StringBuffer();
			while ((len = bin.read(buf)) != -1) {
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


	private static void insertTime() {
		Properties prop = new Properties();
		prop.put("user", "root");
		prop.put("password", "root");

		String timeSql = ("INSERT INTO time(tid, st, et) VALUES (?,?,?)");

		String setF0 = ("SET FOREIGN_KEY_CHECKS = 0;");
		String truncateLocation = ("Truncate TABLE time");
		String setF1 = ("SET FOREIGN_KEY_CHECKS = 1;");

		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:8889/weather", prop)) {
			PreparedStatement set0 = connection.prepareStatement(setF0);
			set0.execute();
			PreparedStatement truncate = connection.prepareStatement(truncateLocation);
			truncate.execute();
			PreparedStatement set1 = connection.prepareStatement(setF1);
			set1.execute();

			// time
			PreparedStatement pstime = connection.prepareStatement(timeSql);

			JSONObject locationvalue = location.getJSONObject(1);

			String locationName = locationvalue.getString("locationName");
			JSONArray weatherElement = locationvalue.getJSONArray("weatherElement");

			JSONObject welement = weatherElement.getJSONObject(1);

			String elementName = welement.getString("elementName");

			JSONArray time = welement.getJSONArray("time");

			for (int k = 0; k < time.length(); k++) {
				int tid = k + 1;

				JSONObject telement = time.getJSONObject(k);

				String startTime = telement.getString("startTime");

				String endTime = telement.getString("endTime");

//				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

//				java.sql.Date st = null;
//				java.sql.Date et = null;

//				try {
//
//					java.util.Date udate1 = sdf.parse(startTime);
//					java.util.Date udate2 = sdf.parse(endTime);
//
//					st = new java.sql.Date(udate1.getTime());
//					et = new java.sql.Date(udate2.getTime());
//
//				} catch (Exception e) {
//
//					e.printStackTrace();
//
//				}
				pstime.setInt(1, tid);
				pstime.setString(2, startTime);
				pstime.setString(3, endTime);

				pstime.executeLargeUpdate();

			}

			System.out.println("time完成");

		} catch (Exception e) {
			System.out.println(e.toString());
		}

	}

	private static void insertLocation() {
		Properties prop = new Properties();
		prop.put("user", "root");
		prop.put("password", "root");

		String locationSql = ("INSERT INTO location(lid, lname) VALUES (?,?)");

		String setF0 = ("SET FOREIGN_KEY_CHECKS = 0;");
		String truncateLocation = ("Truncate  TABLE location");
		String setF1 = ("SET FOREIGN_KEY_CHECKS = 1;");

		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:8889/weather", prop)) {
			PreparedStatement set0 = connection.prepareStatement(setF0);
			set0.execute();
			PreparedStatement truncate = connection.prepareStatement(truncateLocation);
			truncate.execute();
			PreparedStatement set1 = connection.prepareStatement(setF1);
			set1.execute();

			// location
			PreparedStatement pslocation = connection.prepareStatement(locationSql);

			for (int i = 0; i < location.length(); i++) {
				int lid = i + 1;
				JSONObject locationvalue = location.getJSONObject(i);

				String locationName = locationvalue.getString("locationName");

				pslocation.setInt(1, lid);
				pslocation.setString(2, locationName);

				pslocation.executeUpdate();

			}
			System.out.println("location完成");

		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	private static void insertElement() {
		Properties prop = new Properties();
		prop.put("user", "root");
		prop.put("password", "root");

		String elementSql = ("INSERT INTO element(eid, ename) VALUES (?,?)");

		String setF0 = ("SET FOREIGN_KEY_CHECKS = 0;");
		String truncateLocation = ("Truncate TABLE element");
		String setF1 = ("SET FOREIGN_KEY_CHECKS = 1;");

		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:8889/weather", prop)) {
			PreparedStatement set0 = connection.prepareStatement(setF0);
			set0.execute();
			PreparedStatement truncate = connection.prepareStatement(truncateLocation);
			truncate.execute();
			PreparedStatement set1 = connection.prepareStatement(setF1);
			set1.execute();

			// element
			PreparedStatement pselement = connection.prepareStatement(elementSql);

			JSONObject locationvalue = location.getJSONObject(0);

			String locationName = locationvalue.getString("locationName");

			JSONArray weatherElement = locationvalue.getJSONArray("weatherElement");

			for (int j = 0; j < weatherElement.length(); j++) {
				int eid = j + 1;

				JSONObject welement = weatherElement.getJSONObject(j);

				String elementName = welement.getString("elementName");

				pselement.setInt(1, eid);
				pselement.setString(2, elementName);
				pselement.executeUpdate();

			}

			System.out.println("element完成");

		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	private static void insertRecord() {
		Properties prop = new Properties();
		prop.put("user", "root");
		prop.put("password", "root");

		String recordSql = ("INSERT INTO record(lid, eid, tid, parameter) VALUES (?,?,?,?)");

		String setF0 = ("SET FOREIGN_KEY_CHECKS = 0;");
		String truncateLocation = ("Truncate TABLE record");
		String setF1 = ("SET FOREIGN_KEY_CHECKS = 1;");

		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:8889/weather", prop)) {
			PreparedStatement set0 = connection.prepareStatement(setF0);
			set0.execute();
			PreparedStatement truncate = connection.prepareStatement(truncateLocation);
			truncate.execute();
			PreparedStatement set1 = connection.prepareStatement(setF1);
			set1.execute();

			// record
			PreparedStatement psrecord = connection.prepareStatement(recordSql);

			for (int i = 0; i < location.length(); i++) {
				int lid = i+1;
				JSONObject locationvalue = location.getJSONObject(i);

				String locationName = locationvalue.getString("locationName");

				
				JSONArray weatherElement = locationvalue.getJSONArray("weatherElement");

				for (int j = 0; j < weatherElement.length(); j++) {
					int eid = j+1;
					
					JSONObject welement = weatherElement.getJSONObject(j);

					String elementName = welement.getString("elementName");

					JSONArray time = welement.getJSONArray("time");
					for (int k = 0; k < time.length(); k++) {
						int tid = k+1;
						
						JSONObject telement = time.getJSONObject(k);

						String startTime = telement.getString("startTime");
						
						String endTime = telement.getString("endTime");
						
						JSONObject parameter = telement.getJSONObject("parameter");

						String parameterName = parameter.getString("parameterName");
						
						psrecord.setInt(1, lid);
						psrecord.setInt(2, eid);
						psrecord.setInt(3, tid);
						psrecord.setString(4, parameterName);
						psrecord.executeUpdate();
						
					}
				}
			}

			System.out.println("record完成");

		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

}
