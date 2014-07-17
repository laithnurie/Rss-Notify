package com.laithnurie.baka.library;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.laithnurie.baka.MangaViewer;
import com.laithnurie.baka.R;

/**
 * Created by nuriel on 17/07/2014.
 */
public class AlertHelper {

    public static void createMangaNotifcation(final Context context, Manga lastChapter) {
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
    public static void createUpdateNotifcation(final Context context) {
        Intent updateIntent = new Intent(Intent.ACTION_VIEW);
        updateIntent.setData(Uri.parse("market://details?id=" + context.getPackageName()));


        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, updateIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(getResourceId(context, "notification", "drawable", context.getPackageName()))
                        .setContentTitle("New Version of the App!")
                        .setContentText("click here to update (> v <) ");
        mBuilder.setContentIntent(contentIntent);
        mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify("update",1, mBuilder.build());
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
