package com.laithnurie.baka;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


/**
 * Created by laithnurie on 20/05/2013.
 */
public class ShowSettingsActivity extends Activity {
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_settings_layout);

		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

		StringBuilder builder = new StringBuilder();

		builder.append("\n" + sharedPrefs.getBoolean("perform_updates", false));
		builder.append("\n" + sharedPrefs.getString("updates_interval", "-1"));
		builder.append("\n" + sharedPrefs.getString("location_receiver_phone_no", "NULL"));

		TextView settingsTextView = (TextView) findViewById(R.id.settings_text_view);
		settingsTextView.setText(builder.toString());
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_dashboard, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_settings:
				startActivity(new Intent(this, RssPreferences.class));
				return true;
		}
		return false;
	}
}