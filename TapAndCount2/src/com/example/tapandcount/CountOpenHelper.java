package com.example.tapandcount;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CountOpenHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "counts";
	private static final int DATABASE_VERSION = 2;
    private static final String COUNTS_TABLE_NAME = "counts";
    private static final String COUNTS_TABLE_CREATE =
                "CREATE TABLE " + COUNTS_TABLE_NAME + " (" +
                "ID INTEGER, " +
                "VALUE INTEGER);";

    CountOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(COUNTS_TABLE_CREATE);
    }

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
}
