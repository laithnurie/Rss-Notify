package com.laithnurie.baka.library;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.laithnurie.baka.MangaViewer;
import com.laithnurie.baka.R;

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

        if (intent.getExtras() != null && checkForManga) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo ni = cm.getActiveNetworkInfo();
            if (ni != null && ni.isAvailable() && ni.isConnected() ) {
                Log.i("app", "Network " + ni.getTypeName() + " connected");
                checkMangaLastChapter("naruto");
                checkMangaLastChapter("bleach");
                checkMangaLastChapter("one-piece");

            } else if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
                Log.d("NetworkListener", "There's no network connectivity");
            }
        }
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
                showNotification(context, lastChapter);
                mangaEditor.putInt(mangaName + "lc", result.size());
                mangaEditor.apply();
            } else {
                Log.v("NetworkListener","No new Manga");
            }
        }
    }

    private void showNotification(Context context, Manga lastChapter) {

        int requestID = (int) System.currentTimeMillis();

        Intent viewManga = new Intent(context, MangaViewer.class);
        viewManga.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        viewManga.putExtra("mangaPage", "http://www.laithlab.me/manga/" + lastChapter.getManga() + "/" + lastChapter.getChapterNo() + "/0");

        PendingIntent contentIntent = PendingIntent.getActivity(context, requestID, viewManga, PendingIntent.FLAG_CANCEL_CURRENT);

        String iconString = lastChapter.getManga();

        if(iconString.equals("one-piece")){
            iconString = "onepiece";
        }

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(getResourceId(context, iconString, "drawable", context.getPackageName()))
                        .setContentTitle("New Chapter of " + lastChapter.getManga() + "!")
                        .setContentText(lastChapter.getDesc());
        mBuilder.setContentIntent(contentIntent);
        mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(lastChapter.getManga(),1, mBuilder.build());

}
    private static int getResourceId(Context context, String pVariableName, String pResourcename, String pPackageName)
    {
        try {
            return context.getResources().getIdentifier(pVariableName, pResourcename, pPackageName);
        } catch (Exception e) {
            e.printStackTrace();
            return R.drawable.manga_btn;
        }
    }

}