package com.laithnurie.baka;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.laithnurie.baka.library.Manga;
import com.laithnurie.baka.library.mangaAdapter;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MangaList extends Activity {

    ArrayList<Manga> mangaList;
    ListView lv;
    int noOfMangas;
    Context appContext;
    String chapterParam;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manga_list);
        appContext = this;


        Bundle extras = getIntent().getExtras();
        String feedURL = extras.getString("feedUrl");

        new loadMangaList().execute(feedURL);

        lv = (ListView) findViewById(R.id.srListView);
        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Object o = lv.getItemAtPosition(position);
                Manga fullObject = (Manga) o;

                Log.e("mangalist", chapterParam);

                Intent i = new Intent(getApplicationContext(), MangaViewer.class);
                i.putExtra("mangaPage", "http://www.laithlab.me/manga/" + chapterParam + "/" + fullObject.getChapter() + "/0");
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                startActivity(new Intent(this, RssPreferences.class));
                return true;
        }
        return false;
    }

    public class loadMangaList extends AsyncTask<String, Integer, ArrayList<Manga>> {

        ProgressDialog pd;
        Manga chapter;

        @Override
        protected void onPreExecute() {

            pd = new ProgressDialog(MangaList.this);
            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pd.setCanceledOnTouchOutside(false);
            pd.setMax(100);
            pd.show();
        }

        @Override
        protected ArrayList<Manga> doInBackground(final String... params) {
            mangaList = new ArrayList<Manga>();
            mangaList.clear();
            chapterParam = params[0];
            String url = "http://www.laithlab.me/manga/" + chapterParam + "/chapters.json";
            StringBuilder builder = new StringBuilder();
            HttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);

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
                    Log.e(MangaList.class.toString(), "Fetching Data request failed");
                }
            } catch (ClientProtocolException e) {
                Log.e(MangaList.class.toString(), e.toString());
            } catch (IOException e) {
                Log.e(MangaList.class.toString(), e.toString());
            }

            try {
                JSONArray mangasJson = new JSONArray(builder.toString());

                noOfMangas = mangasJson.length();
                mangasJson.getJSONArray(0);

                for(int i = 0; i < noOfMangas; i++){
                    JSONArray manga = mangasJson.getJSONArray(i);
                    chapter = new Manga();
                    chapter.setChapter(manga.get(0).toString());
                    chapter.setDesc(manga.get(2).toString());

                    Log.v("Manga json object:- ", manga.toString());

                    mangaList.add(chapter);

                }

            } catch (JSONException e) {
                e.printStackTrace();
                Log.v("JSON Exception", e.getMessage());
            }

        return mangaList;
        }


        @Override
        protected void onProgressUpdate(Integer... progress) {
            pd.incrementProgressBy(progress[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<Manga> result) {
            lv.setAdapter(new mangaAdapter(MangaList.this, result));
            pd.dismiss();
        }
    }

}
