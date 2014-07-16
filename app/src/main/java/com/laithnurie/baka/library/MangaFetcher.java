package com.laithnurie.baka.library;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.laithnurie.baka.MangaList;

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

/**
 * Created by nuriel on 16/07/2014.
 */
public class MangaFetcher extends AsyncTask<String, Integer, ArrayList<Manga>> {

    ProgressDialog pd;
    Manga chapter;
    ArrayList<Manga> mangaList;
    boolean triggeredByUser;
    Context context;

    public MangaFetcher(boolean triggeredByUser, Context context) {

        this.triggeredByUser = triggeredByUser;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        if (triggeredByUser) {
            pd = new ProgressDialog(context);
            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pd.setCanceledOnTouchOutside(false);
            pd.setMax(100);
            pd.show();
        }
    }

    @Override
    protected ArrayList<Manga> doInBackground(final String... params) {
        mangaList = new ArrayList<Manga>();
        mangaList.clear();
        String chapterParam = params[0];
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

            for (int i = 0; i < mangasJson.length(); i++) {
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
        if (triggeredByUser) {
            pd.incrementProgressBy(progress[0]);
        }
    }

    @Override
    protected void onPostExecute(ArrayList<Manga> result) {
        if (triggeredByUser) {
            pd.dismiss();
        }
    }
}
