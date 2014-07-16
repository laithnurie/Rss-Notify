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
import com.laithnurie.baka.library.MangaAdapter;
import com.laithnurie.baka.library.MangaFetcher;

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
import java.util.concurrent.ExecutionException;

public class MangaList extends Activity {

    ListView lv;
    Context appContext;
    String chapterParam;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manga_list);
        appContext = this;

        Bundle extras = getIntent().getExtras();
        final String feedURL = extras.getString("feedUrl");

        ArrayList<Manga> result = null;
        try {
            result = new MangaFetcher(true, appContext).execute(feedURL).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        lv = (ListView) findViewById(R.id.srListView);
        lv.setAdapter(new MangaAdapter(MangaList.this, result));
        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Object o = lv.getItemAtPosition(position);
                Manga fullObject = (Manga) o;

                Log.e("mangalist", feedURL);

                Intent i = new Intent(getApplicationContext(), MangaViewer.class);
                i.putExtra("mangaPage", "http://www.laithlab.me/manga/" + feedURL + "/" + fullObject.getChapter() + "/0");
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
}
