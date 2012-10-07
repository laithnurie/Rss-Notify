package com.androidpeople.xml.parsing;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;



public class MangasDataSource {
	
	// Database fields
		private SQLiteDatabase database;
		private MySQLiteHelper dbHelper;
		private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
				MySQLiteHelper.COLUMN_MANGA };

		public MangasDataSource(Context context) {
			dbHelper = new MySQLiteHelper(context);
		}

		public void open() throws SQLException {
			database = dbHelper.getWritableDatabase();
		}

		public void close() {
			dbHelper.close();
		}

		public Manga createManga(String lastManga) {
			ContentValues values = new ContentValues();
			values.put(MySQLiteHelper.COLUMN_MANGA, lastManga);
			long insertId = database.insert(MySQLiteHelper.TABLE_MANGAS, null,
					values);
			Cursor cursor = database.query(MySQLiteHelper.TABLE_MANGAS,
					allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
					null, null, null);
			cursor.moveToFirst();
			Manga newManga = cursorToManga(cursor);
			cursor.close();
			return newManga;
		}

		public void deleteManga(Manga comment) {
			long id = comment.getId();
			System.out.println("Manga deleted with id: " + id);
			database.delete(MySQLiteHelper.TABLE_MANGAS, MySQLiteHelper.COLUMN_ID
					+ " = " + id, null);
		}

		public List<Manga> getAllMangas() {
			List<Manga> mangas = new ArrayList<Manga>();

			Cursor cursor = database.query(MySQLiteHelper.TABLE_MANGAS,
					allColumns, null, null, null, null, null);

			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				Manga manga = cursorToManga(cursor);
				mangas.add(manga);
				cursor.moveToNext();
			}
			// Make sure to close the cursor
			cursor.close();
			return mangas;
		}

		private Manga cursorToManga(Cursor cursor) {
			Manga manga = new Manga();
			manga.setId(cursor.getLong(0));
			manga.setManga(cursor.getString(1));
			return manga;
		}

}
