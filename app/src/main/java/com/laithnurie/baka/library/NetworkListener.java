package com.laithnurie.baka.library;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by nuriel on 16/07/2014.
 */
public class NetworkListener extends BroadcastReceiver {

    private Context context;
    private SharedPreferences sharedPreferences;
    @Override
    public void onReceive(Context broadCastcontext, Intent intent) {
        Log.d("NetworkListener", "Network connectivity change");
        context = broadCastcontext;

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(broadCastcontext);
        boolean checkForManga = sharedPreferences.getBoolean("perform_updates",true);

        if (intent.getExtras() != null) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo ni = cm.getActiveNetworkInfo();
            if (ni != null && ni.isAvailable() && ni.isConnected() ) {
                Log.i("app", "Network " + ni.getTypeName() + " connected");

                Boolean newVersionExist = false;
                try {
                    newVersionExist = new newVersion().execute().get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                if(newVersionExist){
                    Log.i("app", "Network " + ni.getTypeName() + " connected");
                    AlertHelper.createUpdateNotifcation(context);
                }
                if (checkForManga) {fetchLastChapters();}
            } else if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
                Log.d("NetworkListener", "There's no network connectivity");
            }
        }
    }

    private void fetchLastChapters() {
        checkMangaLastChapter("naruto");
        checkMangaLastChapter("bleach");
        checkMangaLastChapter("one-piece");
    }

    private void checkMangaLastChapter(String mangaName) {
        ArrayList<Manga> result = null;
        try {
            result = new MangaFetcher(false, context).execute(mangaName).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        SharedPreferences.Editor mangaEditor = sharedPreferences.edit();
        int LastChapter =  sharedPreferences.getInt(mangaName + "lc",0);
        if (result != null) {
            if (result.size() > LastChapter){
                Manga lastChapter = result.get(0);
                AlertHelper.createMangaNotifcation(context, lastChapter);
                mangaEditor.putInt(mangaName + "lc", result.size());
                mangaEditor.apply();
            } else {
                Log.v("NetworkListener","No new Manga");
            }
        }
    }

    private class newVersion extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(final Void... params) {

            try {
                String curVersion = context.getPackageManager().getPackageInfo(context.getPackageName(),0).versionName;
                String newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + context.getPackageName() + "&hl=en")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get()
                        .select("div[itemprop=softwareVersion]")
                        .first()
                        .ownText();

                int majorNewVersion = Integer.parseInt(newVersion.split("\\.")[0]);
                int majorCurrentVersion = Integer.parseInt(curVersion.split("\\.")[0]);
                return (majorCurrentVersion) < (majorNewVersion);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }}