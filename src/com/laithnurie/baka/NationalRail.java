package com.laithnurie.baka;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.support.v4.app.NavUtils;

public class NationalRail extends Activity {
	
	Button button;
	
	Spinner from;
    Spinner to;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_national_rail);
        
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
        		  this, R.array.station_name, android.R.layout.simple_spinner_item );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        from = (Spinner) findViewById( R.id.from_spinner );
        to = (Spinner) findViewById( R.id.to_spinner );
        
        from.setAdapter(adapter);
        to.setAdapter(adapter);
        
        button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View arg0) {
 
				String fromValue = from.getSelectedItem().toString();
				String toValue = to.getSelectedItem().toString();
							
				Intent nationalTrains = new Intent(getApplicationContext(), NationalTrains.class);
				nationalTrains.putExtra("from", fromValue);
				nationalTrains.putExtra("to",toValue);
	        	startActivity(nationalTrains);
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
