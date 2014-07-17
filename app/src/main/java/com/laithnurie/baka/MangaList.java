package com.laithnurie.baka;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MangaList extends Activity {

    private ListView lv;
    private Context appContext;

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

        if (result != null) {
            if(result.size()>0){

                SharedPreferences mangaData = PreferenceManager.getDefaultSharedPreferences(appContext);
                SharedPreferences.Editor mangaEditor = mangaData.edit();
                mangaEditor.putInt(feedURL + "lc", result.size());
                mangaEditor.apply();
            }
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
                i.putExtra("mangaPage", "http://www.laithlab.me/manga/" + feedURL + "/" + fullObject.getChapterNo() + "/0");
                startActivity(i);
            }
        });
        overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
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
