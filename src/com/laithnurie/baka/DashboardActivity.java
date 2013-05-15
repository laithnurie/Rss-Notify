package com.laithnurie.baka;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class DashboardActivity extends Activity {
    Activity currentActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        currentActivity = this;


        //attach event handler to dash buttons
        DashboardClickListener dBClickListener = new DashboardClickListener();
        findViewById(R.id.mangaSection).setOnClickListener(dBClickListener);
        findViewById(R.id.travel).setOnClickListener(dBClickListener);
        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                Log.v("json", getWeatherFeed());
                //Toast.makeText(currentActivity, getWeatherFeed(), Toast.LENGTH_SHORT).show();
                final String feed = getWeatherFeed();

                currentActivity.runOnUiThread(new Thread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), feed, Toast.LENGTH_SHORT).show();
                    }

                }));
            }
        });
        t.start();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_dashboard, menu);
        return true;
    }

    private class DashboardClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            Intent i = null;
            switch (v.getId()) {
                case R.id.mangaSection:
                    i = new Intent(DashboardActivity.this, MangaMenu.class);
                    break;
                case R.id.travel:
                    i = new Intent(DashboardActivity.this, TravelMenu.class);
                    break;
                default:
                    break;
            }

            if (i != null) {
                startActivity(i);
            }
        }
    }

    public String getWeatherFeed() {


        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet("http://api.wunderground.com/api/976964edc995ef5b/geolookup/q/37.776289,-122.395234.json");
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