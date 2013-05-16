package com.laithnurie.baka;

import android.app.Activity;
import android.app.Application;

import org.json.JSONArray;

/**
 * Created by laithnurie on 15/05/2013.
 */
public class RssApp extends Application {

	private static Activity appCurrentActivity;
	private static JSONArray appWeatherJson;

	public static Activity getCurrentActivity() {
		return appCurrentActivity;
	}

	public static void setCurrentActivity(Activity currentActivity) {
		appCurrentActivity = currentActivity;
	}

	public static JSONArray getWeatherJson() {
		return appWeatherJson;
	}

	public static void setWeatherJson(JSONArray weatherJson) {
		appWeatherJson = weatherJson;
	}

	public static void clearWeatherJson() {
		appWeatherJson = new JSONArray();
	}


}
