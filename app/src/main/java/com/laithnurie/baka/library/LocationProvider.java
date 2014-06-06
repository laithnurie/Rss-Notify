package com.laithnurie.baka.library;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.laithnurie.baka.RssApp;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Locale;

/**
 * Created by laithnurie on 15/05/2013.
 */


public class LocationProvider {

	LocationManager lm;
	LocationListener ll;

	public void getLocation(Activity activity, String providerType) {
		lm = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

		Criteria crit = new Criteria();
		crit.setAccuracy(Criteria.ACCURACY_FINE);
		String provider = lm.getBestProvider(crit, false);
		Location loc = lm.getLastKnownLocation(provider);

		try {
			sendSMS(Double.toString(loc.getLatitude()),Double.toString(loc.getLongitude()));
			Log.v("loclis", "lat " +Double.toString(loc.getLatitude()));
			Log.v("loclis", "lon " +Double.toString(loc.getLongitude()));

		} catch (NullPointerException e) {
			Log.e("loclis", e.getMessage());
		}

//		ll = new LocLis();
//
//		if (providerType == "gps") {
//			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ll);
//		}
//		if (providerType == "net") {
//			lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, ll);
//		}
	}

	private class LocLis implements LocationListener {
		@Override
		public void onLocationChanged(final Location location) {
			if (location != null) {
				Log.v("loclis", location.getLatitude() + " " + location.getLongitude());
				Geocoder gcd = new Geocoder(RssApp.getCurrentActivity(), Locale.getDefault());
				List<Address> addresses = null;
				try {
					addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (addresses.size() > 0) {
					Log.v("loclis", addresses.get(0).getLocality());
				}

				Thread t = new Thread(new Runnable() {

					@Override
					public void run() {

						String lat = Double.toString(location.getLatitude());
						String longit = Double.toString(location.getLongitude());
						Log.v("loclis", "lat " + lat);
						Log.v("loclis", "longit " + longit);
						sendSMS(lat, longit);
						lm.removeUpdates(ll);
					}
				});
				t.start();
			}
		}

		@Override
		public void onProviderDisabled(String provider) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	}


	public void sendSMS(String lat, String lon) {

		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(RssApp.getCurrentActivity());


		String phoneNo = sharedPrefs.getString("location_receiver_phone_no", "NULL");

		String message = "https://maps.google.co.uk/maps?q=" + lat + "," + lon;


		SmsManager smsManager = SmsManager.getDefault();
		smsManager.sendTextMessage(phoneNo, null, message, null, null);

		Toast.makeText(RssApp.getCurrentActivity(), "text sent to" + phoneNo + " with " + message, Toast.LENGTH_LONG).show();
	}

	public String getWeatherFeed(Double lat, Double lon) {


		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		String wunderground = "http://api.wunderground.com/api/976964edc995ef5b/conditions/q/" + lat + "," + lon + ".json";
		HttpGet httpGet = new HttpGet(wunderground);
		try {
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
			} else {
				Log.e(this.toString(), "Failed to download file");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return builder.toString();
	}
}
