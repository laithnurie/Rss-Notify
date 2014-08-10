package com.laithnurie.baka;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

public class NationalRail extends Activity {
	
	private Button button;
	
	private AutoCompleteTextView from;
    private AutoCompleteTextView to;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_national_rail);
        
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
        		  this, R.array.station_names, android.R.layout.simple_spinner_item );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        from = (AutoCompleteTextView) findViewById( R.id.from_station );
        to = (AutoCompleteTextView) findViewById( R.id.to_station );
        
        from.setAdapter(adapter);
        to.setAdapter(adapter);
        
        button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View arg0) {

                String[] fromStation = from.getText().toString().split("-");
                String[] toStation = to.getText().toString().split("-");

                String fromValue = fromStation[fromStation.length - 1].replaceAll("\\s+","");
				String toValue = toStation[toStation.length - 1].replaceAll("\\s+", "");
							
				Intent nationalTrains = new Intent(getApplicationContext(), NationalTrains.class);
				nationalTrains.putExtra("from", fromValue);
				nationalTrains.putExtra("to",toValue);
	        	startActivity(nationalTrains);
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
