package com.laithnurie.baka;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

public class TravelMenu extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel);
        
        DashboardClickListener dBClickListener = new DashboardClickListener();
        findViewById(R.id.tube).setOnClickListener(dBClickListener);
        findViewById(R.id.national).setOnClickListener(dBClickListener);
    }
    
    private class DashboardClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            Intent i = null;
            switch (v.getId()) {
                case R.id.tube:
                    i = new Intent(TravelMenu.this, TubeActivity.class);
                    break;
                case R.id.national:
                    i = new Intent(TravelMenu.this, NationalRail.class);
                    break;
                default:
                    break;
            }
            if(i != null) {
                startActivity(i);
            }
        }
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
