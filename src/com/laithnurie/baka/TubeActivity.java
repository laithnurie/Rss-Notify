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
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class TubeActivity extends Activity {
  
/** Called when the activity is first created. */

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_tube);
    new loadTubeJson().execute();
  }

  
@TargetApi(3)
public class loadTubeJson extends AsyncTask<String, Integer, String> {
  	
  	ProgressDialog pd;


  	@Override
  	protected void onPreExecute() {
  		
  		pd = new ProgressDialog(TubeActivity.this);
  		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
  		pd.setMax(100);
  		pd.show();
  		
  	}
  	
  	@Override
  	protected String doInBackground(final String... params) {
  		StringBuilder builder = new StringBuilder();
  	    HttpClient client = new DefaultHttpClient();
  	    HttpGet httpGet = new HttpGet("http://api.tubeupdates.com/?method=get.status");
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
  	    	} 
  	    	else {
  	    		Log.e(TubeActivity.class.toString(), "Failed to download file");
  	    	}
  	    } 
  	    catch (ClientProtocolException e) {
  	      e.printStackTrace();
  	    } 
  	    catch (IOException e) {
  	      e.printStackTrace();
  	    }
  	    return builder.toString();
  	} 
  	
  	
  	@Override
  	protected void onProgressUpdate(Integer... progress) {
  		pd.incrementProgressBy(progress[0]);	
  	}
  	
  	@Override
      protected void onPostExecute(String result) {
  		try {
  	      JSONObject tubeJson = new JSONObject(result);
	      JSONObject response = tubeJson.getJSONObject("response");
	      JSONArray linesList = response.getJSONArray("lines");
  	      
  	      for (int i = 0; i < linesList.length(); i++) {
  	    	  
  	        JSONObject line = linesList.getJSONObject(i);
  	        String lineName = line.getString("name");
  	        String lineStatus = line.getString("status");
  	        
  	        Toast.makeText(getApplicationContext(), lineName + ": " + lineStatus, Toast.LENGTH_SHORT).show();
  	        
  	      }
  	    } catch (Exception e) {
  	      e.printStackTrace();
  	    }
  		pd.dismiss();
      }
  }
} 
