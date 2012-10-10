package com.laithnurie.rssmanga;

import android.content.Context;


public class DatabaseConnection 
{
	private MangasDataSource datasource;
	
	public void addMangaToDB(Context context, String manga)
	{
		datasource = new MangasDataSource(context);
		datasource.open();
		datasource.createManga(manga);
		datasource.close();
	}
}
