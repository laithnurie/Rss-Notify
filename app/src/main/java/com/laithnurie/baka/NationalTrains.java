package com.laithnurie.baka;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class NationalTrains extends Activity {

    private String fromStation;
    private String toStation;
    private String trainUrlDate;
    private int dip;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_national_trains);

        Bundle extras = getIntent().getExtras();
        fromStation = extras.getString("from");
        toStation = extras.getString("to");
        dip = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) 1, getResources().getDisplayMetrics());

        trainUrlDate = "http://ojp.nationalrail.co.uk/service/ldb/liveTrainsJson?departing=true&liveTrainsFrom=" + fromStation + "&liveTrainsTo=" + toStation + "&serviceId=";
        new loadTrainJson().execute();
    }

    public class loadTrainJson extends AsyncTask<String, Integer, String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {

            pd = new ProgressDialog(NationalTrains.this);
            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pd.setCanceledOnTouchOutside(false);
            pd.setMax(100);
            pd.show();
        }

        @Override
        protected String doInBackground(final String... params) {
            StringBuilder builder = new StringBuilder();
            HttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(trainUrlDate);

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
                    Log.e(NationalTrains.class.toString(), "Failed to download file");
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
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

            TextView statusTV;
            TextView timeTV;
            TextView destTV;

            TableLayout tl = (TableLayout) findViewById(R.id.trainsTable);

            View v = new View(getApplicationContext());
            v.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 1));
            v.setBackgroundColor(Color.rgb(50, 50, 50));

            try {

                JSONObject trainJson = new JSONObject(result);
                JSONArray linesList = trainJson.getJSONArray("trains");
                Log.v("trains", "lineList:" + linesList.length());

                for (int i = 0; i < linesList.length(); i++) {

                    JSONArray line = linesList.getJSONArray(i);
                    String time = line.getString(1);
                    String dest = line.getString(2).replace("amp;", "");
                    String status = line.getString(3).replace("&lt;br/&gt;", "");

                    timeTV = rowTextView(time);
                    destTV = rowTextView(dest);
                    statusTV = rowTextView(status);

                    tl.addView(tableRow(timeTV, statusTV, destTV));
                    tl.addView(v);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            pd.dismiss();
        }
    }

    TextView rowTextView(String rowText) {

        TextView cell = new TextView(getApplicationContext());
        cell.setPadding(10 * dip, 10 * dip, 10 * dip, 10 * dip);
        cell.setText(rowText);
        cell.setTextColor(getResources().getColor(R.color.piccadily));
        cell.setWidth(100 * dip);

        return cell;
    }

    TableRow tableRow(TextView timeTV, TextView statusTV, TextView destTV) {

        TableRow tr = new TableRow(getApplicationContext());
        tr.setGravity(Gravity.CENTER);
        tr.setBackgroundColor(getResources().getColor(R.color.status));
        tr.addView(timeTV);
        tr.addView(statusTV);
        tr.addView(destTV);

        return tr;
    }
}
