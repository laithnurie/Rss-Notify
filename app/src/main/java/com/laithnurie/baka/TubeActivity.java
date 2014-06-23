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

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class TubeActivity extends Activity {

    /**
     * Called when the activity is first created.
     */

    TextView bakerloo;
    TextView central;
    TextView circle;
    TextView district;
    TextView dlr;
    TextView hammersmith;
    TextView jubilee;
    TextView metropolitan;
    TextView northern;
    TextView overground;
    TextView piccadily;
    TextView victoria;
    TextView waterloocity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tube);

        bakerloo = (TextView) findViewById(R.id.baker_stat);
        central = (TextView) findViewById(R.id.central_stat);
        circle = (TextView) findViewById(R.id.circle_stat);
        district = (TextView) findViewById(R.id.district_stat);
        dlr = (TextView) findViewById(R.id.dlr_stat);
        hammersmith = (TextView) findViewById(R.id.hammer_stat);
        jubilee = (TextView) findViewById(R.id.jubilee_stat);
        metropolitan = (TextView) findViewById(R.id.metro_stat);
        northern = (TextView) findViewById(R.id.north_stat);
        overground = (TextView) findViewById(R.id.over_stat);
        piccadily = (TextView) findViewById(R.id.piccadily_stat);
        victoria = (TextView) findViewById(R.id.victoria_stat);
        waterloocity = (TextView) findViewById(R.id.water_stat);

        new loadTubeJson().execute();

    }


    public class loadTubeJson extends AsyncTask<String, Integer, String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {

            pd = new ProgressDialog(TubeActivity.this);
            pd.setCanceledOnTouchOutside(false);
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
                } else {
                    Log.e(TubeActivity.class.toString(), "Fetching Data request failed");
                }
            } catch (ClientProtocolException e) {

                Log.e(TubeActivity.class.toString(), e.toString());

            } catch (IOException e) {
                Log.e(TubeActivity.class.toString(), e.toString());
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

                    Log.i("tube", lineName + " : " + lineStatus);

                    switch (i) {
                        case 0:
                            bakerloo.setText(lineStatus);
                            break;
                        case 1:
                            central.setText(lineStatus);
                            break;
                        case 2:
                            circle.setText(lineStatus);
                            break;
                        case 3:
                            district.setText(lineStatus);
                            break;
                        case 4:
                            dlr.setText(lineStatus);
                            break;
                        case 5:
                            hammersmith.setText(lineStatus);
                            break;
                        case 6:
                            jubilee.setText(lineStatus);
                            break;
                        case 7:
                            metropolitan.setText(lineStatus);
                            break;
                        case 8:
                            northern.setText(lineStatus);
                            break;
                        case 9:
                            overground.setText(lineStatus);
                            break;
                        case 10:
                            piccadily.setText(lineStatus);
                            break;
                        case 11:
                            victoria.setText(lineStatus);
                            break;
                        case 12:
                            waterloocity.setText(lineStatus);
                            break;
                        default:
                            break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            pd.dismiss();
        }
    }
} 