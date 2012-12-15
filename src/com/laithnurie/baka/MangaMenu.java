package com.laithnurie.baka;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
public class MangaMenu extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		
		DashboardClickListener dBClickListener = new DashboardClickListener();
        findViewById(R.id.narutoBtn).setOnClickListener(dBClickListener);
        findViewById(R.id.bleachBtn).setOnClickListener(dBClickListener);
        findViewById(R.id.onepieceBtn).setOnClickListener(dBClickListener);
		
		
		
	}
	
	private class DashboardClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.narutoBtn:
                	mangaIntentSwitcher("naruto");
                    break;
                case R.id.bleachBtn:
                	mangaIntentSwitcher("bleach");
                    break;
                case R.id.onepieceBtn:
                	mangaIntentSwitcher("one_piece");
                    break;
                default:
                    break;
            }

        }
}
	public void mangaIntentSwitcher(String manga)
	{
		Intent i = new Intent(getApplicationContext(), MangaList.class);
    	i.putExtra("feedUrl", manga);
    	startActivity(i);
	}
}