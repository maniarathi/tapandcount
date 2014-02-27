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
	
	public void editCountDescription(Count c, String newTitle) {
		// Update database
		ContentValues updateVals = new ContentValues();
		
		// Update date
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		String dateValue = dateFormat.format(date);
		
		// Set new values
		updateVals.put(CountOpenHelper.COLUMN_DESCRIPTION, newTitle);
		updateVals.put(CountOpenHelper.COLUMN_DATE, dateValue);
		updateVals.put(CountOpenHelper.COLUMN_VALUE, c.getValue());
		
		// Update
		int rowsChanged = database.update(CountOpenHelper.TAC_TABLE_NAME, updateVals, CountOpenHelper.COLUMN_ID + " = " + c.getId(), null);
		System.out.println("Updated table with " + rowsChanged + " rows changed.\n");
	}
	
	public void deleteCount(Count c) {
		long id = c.getId();
		database.delete(CountOpenHelper.TAC_TABLE_NAME, CountOpenHelper.COLUMN_ID + " = " + id, null);
		System.out.println("Deleted count with id = " + id);
	}
	
	public Count createCount(Count c) {
		// Check if argument count already exists in database
		Cursor existCheck = database.query(CountOpenHelper.TAC_TABLE_NAME, allColumns, CountOpenHelper.COLUMN_ID + " = " + c.getId(), null, null, null, null);
		if (existCheck.getCount() == 0) {
			// Add a new row to database
			ContentValues newVals = new ContentValues();
			
			// Get current date
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			String dateValue = dateFormat.format(date);
			
			// Generate a description using id
			Cursor latestIdCursor = database.query(CountOpenHelper.TAC_TABLE_NAME, allColumns, CountOpenHelper.COLUMN_ID + " = (SELECT MAX(" + CountOpenHelper.COLUMN_ID + ") FROM " + CountOpenHelper.TAC_TABLE_NAME + ");", null, null, null, null);
			long lastId = -1;
			if (latestIdCursor.moveToFirst()) {
				lastId = cursorToCount(latestIdCursor).getId();
			}
			lastId++;
			String description = "count_" + lastId;
			
			// Set column values (except autoincrement id)
			newVals.put(CountOpenHelper.COLUMN_DESCRIPTION, description);
			newVals.put(CountOpenHelper.COLUMN_DATE, dateValue);
			newVals.put(CountOpenHelper.COLUMN_VALUE, c.getValue());
			
			// Insert into DB
			long insertId = database.insert(CountOpenHelper.TAC_TABLE_NAME, null, newVals);
			
			// Get the id of the row that was just inserted
			Cursor cursor = database.query(CountOpenHelper.TAC_TABLE_NAME, allColumns, CountOpenHelper.COLUMN_ID + " = " + insertId, null, null, null, null);
			cursor.moveToFirst();
			Count lastestCount = cursorToCount(cursor);
			cursor.close();
			return lastestCount;
		} else {
			// Update database
			ContentValues updateVals = new ContentValues();
			
			// Update date
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			String dateValue = dateFormat.format(date);
			
			// Set new values
			updateVals.put(CountOpenHelper.COLUMN_DESCRIPTION, c.getDesc());
			updateVals.put(CountOpenHelper.COLUMN_DATE, dateValue);
			updateVals.put(CountOpenHelper.COLUMN_VALUE, c.getValue());
			
			// Update
			int rowsChanged = database.update(CountOpenHelper.TAC_TABLE_NAME, updateVals, CountOpenHelper.COLUMN_ID + " = " + c.getId(), null);
			System.out.println("Updated table with " + rowsChanged + " rows changed.\n");
			return c;
		}
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
