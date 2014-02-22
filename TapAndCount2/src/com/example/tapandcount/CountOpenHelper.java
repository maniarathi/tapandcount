package com.example.tapandcount;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CountOpenHelper extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 2;
	private static final String DATABASE_NAME = "TACDB";
    public static final String TAC_TABLE_NAME = "history";
    
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_DESCRIPTION = "desc";
    public static final String COLUMN_VALUE = "value";
    
    private static final String TAC_TABLE_CREATE =
                "CREATE TABLE " + TAC_TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY, " +
                COLUMN_DATE + " TEXT, " + 
                COLUMN_DESCRIPTION + " TEXT, " + 
                COLUMN_VALUE + " INTEGER);";

    CountOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TAC_TABLE_CREATE);
    }

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
		Log.w(CountOpenHelper.class.getName(), "Upgrading database from version " + oldV + " to " + newV + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TAC_TABLE_NAME);
		onCreate(db);
		
	}
}
