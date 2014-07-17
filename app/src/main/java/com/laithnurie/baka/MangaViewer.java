package com.laithnurie.baka;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MangaViewer extends Activity {
	private WebView webView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manga_viewer);
        Bundle extras = getIntent().getExtras();
        String mangaPage = extras.getString("mangaPage");
        webView = (WebView) findViewById(R.id.webView1);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setSupportZoom(true);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.getSettings().setAllowContentAccess(true);
		webView.getSettings().setLoadWithOverviewMode(true);
	    webView.getSettings().setUseWideViewPort(true);
		webView.setWebViewClient(new WebViewClient() {
		    @Override
		    public boolean shouldOverrideUrlLoading(WebView wView, String url)
		    {
		       wView.loadUrl(url); 
		       return true;
		    }
		});
		webView.loadUrl(mangaPage);
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
