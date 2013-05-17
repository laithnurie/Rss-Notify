package com.laithnurie.baka;

import android.app.Activity;
import android.app.Application;

/**
 * Created by laithnurie on 15/05/2013.
 */
public class RssApp extends Application {

	private static Activity currentActivity;

	public static Activity getCurrentActivity() {
		return currentActivity;
	}

	public static void setCurrentActivity(Activity currentActivity) {
		RssApp.currentActivity = currentActivity;
	}
}
