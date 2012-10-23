package com.laithnurie.baka;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

public class DashboardActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        
        //attach event handler to dash buttons
        DashboardClickListener dBClickListener = new DashboardClickListener();
        findViewById(R.id.mangaSection).setOnClickListener(dBClickListener);
        findViewById(R.id.travel).setOnClickListener(dBClickListener);
        findViewById(R.id.dashboard_button_manage).setOnClickListener(dBClickListener);
        findViewById(R.id.dashboard_button_personalbests).setOnClickListener(dBClickListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_dashboard, menu);
        return true;
    }
    
    private class DashboardClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            Intent i = null;
            switch (v.getId()) {
                case R.id.mangaSection:
                    i = new Intent(DashboardActivity.this, MangaMenu.class);
                    break;
                case R.id.travel:
                    i = new Intent(DashboardActivity.this, TravelMenu.class);
                    break;
                case R.id.dashboard_button_manage:
                    i = new Intent(DashboardActivity.this, MangaMenu.class);
                    break;
                case R.id.dashboard_button_personalbests:
                    i = new Intent(DashboardActivity.this, MangaMenu.class);
                    break;
                default:
                    break;
            }
            if(i != null) {
                startActivity(i);
            }
        }
    }
    
    
}


