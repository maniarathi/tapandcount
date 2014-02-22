package com.example.tapandcount;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class CountDataSource {
	private SQLiteDatabase database;
	private CountOpenHelper helper;
	private String[] allColumns = {CountOpenHelper.COLUMN_ID, CountOpenHelper.COLUMN_DATE, CountOpenHelper.COLUMN_DESCRIPTION, CountOpenHelper.COLUMN_VALUE};
	
	public CountDataSource (Context context) {
		helper = new CountOpenHelper(context);
	}
	
	public void open() throws SQLException {
		database = helper.getWritableDatabase();
	}
	
	public void close() {
		helper.close();
	}
	
	public void deleteAllHistory() {
		database.delete(CountOpenHelper.TAC_TABLE_NAME, null, null);
	}
	
	public Count createCount(String desc, int v) {
		ContentValues values = new ContentValues();
		
		// Get current date
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		String dateValue = dateFormat.format(date);
		
		// Set column values (except autoincrement id)
		values.put(CountOpenHelper.COLUMN_DESCRIPTION, desc);
		values.put(CountOpenHelper.COLUMN_DATE, dateValue);
		values.put(CountOpenHelper.COLUMN_VALUE, v);
		
		// Insert into DB
		long insertId = database.insert(CountOpenHelper.TAC_TABLE_NAME, null, values);
		
		// Get the id of the row that was just inserted
		Cursor cursor = database.query(CountOpenHelper.TAC_TABLE_NAME, allColumns, CountOpenHelper.COLUMN_ID + " = " + insertId, null, null, null, null);
		cursor.moveToFirst();
		Count lastestCount = cursorToCount(cursor);
		cursor.close();
		return lastestCount;
	}
	
	public List<Count> getAllCounts() {
		List<Count> counts = new ArrayList<Count>();

	    Cursor cursor = database.query(CountOpenHelper.TAC_TABLE_NAME, allColumns, null, null, null, null, null);
	    cursor.moveToFirst();
	    
	    while (!cursor.isAfterLast()) {
	    	Count count = cursorToCount(cursor);
	    	counts.add(count);
	    	cursor.moveToNext();
	    }

	    cursor.close();
	    return counts;
	}
	
	private Count cursorToCount(Cursor c) {
		Count count = new Count();
		count.setId(c.getLong(0));
		count.setDate(c.getString(1));
		count.setDesc(c.getString(2));
		count.setValue(c.getInt(3));
		return count;
	}
}
