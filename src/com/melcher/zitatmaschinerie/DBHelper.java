package com.melcher.zitatmaschinerie;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DBHelper extends
		com.readystatesoftware.sqliteasset.SQLiteAssetHelper {

	private static final String DB_NAME = "test_1.4";
	private static final int DB_VERSION = 1;
	public static final String DB_TABLE = "Quotes";
	public static final String WIKILINK = "wiki";
	public static final String SPEC_QUOTE = "spec_Quote";
	public static final String QUOTE_ID = "_id";
	public static final String ALREADY = "already";
	public static final String AUTHOR = "Author";
	public static final String QUOTES_SELECT_RANDOM = "SELECT _id, Author, spec_Quote FROM Quotes WHERE already = 1 ORDER BY RANDOM() LIMIT 1";
	public static final String QUOTES_SELECT_PART = "SELECT _id, spec_Quote FROM Quotes WHERE already IS NULL";
	public static final String QUOTES_SELECT_AUTHOR = "SELECT _id, spec_Quote FROM Quotes WHERE already = 1 AND Author = ?";
	public static final String AUTHOR_SELECT_SPINNER = "SELECT _id, Author FROM Quotes WHERE already = 1 GROUP BY Author";
	public static final String SELECT_WIKI = "SELECT _id, wiki FROM Quotes WHERE already = 1 AND Author = ? GROUP BY Author";

	public DBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion) {
		super.onUpgrade(db, oldversion, newversion);
	}

}
