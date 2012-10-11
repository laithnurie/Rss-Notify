package com.laithnurie.baka;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MangaMenu extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		Button narutoBtn = (Button) findViewById(R.id.narutoBtn);
		
		narutoBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	mangaIntentSwitcher("naruto");
            }
        });
		
		
		Button bleachBtn = (Button) findViewById(R.id.bleachBtn);
		
		bleachBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	mangaIntentSwitcher("bleach");
            }
        });
	}
	
	public void mangaIntentSwitcher(String manga)
	{
		Intent i = new Intent(getApplicationContext(), MangaList.class);
    	i.putExtra("feedUrl", manga);
    	startActivity(i);
	}
}