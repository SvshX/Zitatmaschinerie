package com.melcher.zitatmaschinerie;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class SQLAdapter {

	private DBHelper dbhelper;
	private Context con;
	private SQLiteDatabase database;

	public SQLAdapter(Context c) {
		con = c;
	}

	public SQLAdapter open() throws SQLException {
		dbhelper = new DBHelper(con);
		database = dbhelper.getWritableDatabase();
		return this;

	}

	public void close() {
		dbhelper.close();
	}

	public Cursor getRandomQuote() {

		Cursor c = database.rawQuery(DBHelper.QUOTES_SELECT_RANDOM, null);

		if (c != null) {
			c.moveToFirst();
		}
		return c;
	}

	public void insertData(String quote) {
		ContentValues cv = new ContentValues();
		cv.put(DBHelper.SPEC_QUOTE, quote);
		database.insert(DBHelper.DB_TABLE, null, cv);
	}

	public Cursor readData() {

		Cursor c = database.rawQuery(DBHelper.QUOTES_SELECT_PART, null);

		if (c != null) {
			c.moveToFirst();
		}
		return c;
	}

	public Cursor readAuthorData(String str) {
		Cursor c = database.rawQuery(DBHelper.QUOTES_SELECT_AUTHOR,
				new String[] { str });
		if (c != null) {
			c.moveToFirst();
		}
		return c;
	}

	public Cursor readWiki(String s) {

		Cursor c = database.rawQuery(DBHelper.SELECT_WIKI, new String[] { s });

		if (c != null) {
			c.moveToFirst();
		}
		return c;
	}

	public Cursor loadSpinnerData() {

		Cursor c = database.rawQuery(DBHelper.AUTHOR_SELECT_SPINNER, null);

		if (c != null) {
			c.moveToFirst();
		}
		return c;
	}

	public int updateData(long quoteID, String quoteName) {
		ContentValues cvUpdate = new ContentValues();
		cvUpdate.put(DBHelper.SPEC_QUOTE, quoteName);
		int i = database.update(DBHelper.DB_TABLE, cvUpdate, DBHelper.QUOTE_ID
				+ " = " + quoteID, null);
		return i;
	}

	public void deleteData(long quoteID) {
		database.delete(DBHelper.DB_TABLE, DBHelper.QUOTE_ID + "=" + quoteID,
				null);
	}

}
