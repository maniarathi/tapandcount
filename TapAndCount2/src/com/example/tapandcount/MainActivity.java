package com.example.tapandcount;

import com.example.tapandcount.R;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	public static final String PREFS_NAME = "TACPrefFile";
	
	private CountDataSource ds;
	
	private boolean showExtraOptions = false;
	private boolean multiTouchAllowed = false;
	Count currentCount;
	// For multitouch use in later version
	private Integer numTouches = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_counting);
		
		
		
		// Setup database
		ds = new CountDataSource(this);
		ds.open();
		
		// Initialize
		currentCount = new Count();
		
		// Restore saved preferences
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		currentCount.setId(settings.getLong("saved_id", -1));
		currentCount.setDesc(settings.getString("saved_desc", "new_count"));
		currentCount.setValue(settings.getInt("saved_value", 0));
		
		showExtraOptions = settings.getBoolean("settings_extra_options", false);
		multiTouchAllowed = settings.getBoolean("settings_multitouch_allowed", false);
		
		// Show buttons if set
		if (showExtraOptions) {
			Button decreaseButton = (Button) findViewById(R.id.button_decrement);
			decreaseButton.setVisibility(View.VISIBLE);
			Button saveButton = (Button) findViewById(R.id.button_save);
			saveButton.setVisibility(View.VISIBLE);
		} else {
			Button decreaseButton = (Button) findViewById(R.id.button_decrement);
			decreaseButton.setVisibility(View.GONE);
			Button saveButton = (Button) findViewById(R.id.button_save);
			saveButton.setVisibility(View.GONE);
		}
		
		// Enable multitouch
		if (multiTouchAllowed) {
			// TODO: Enable multitouch
		}
		
		// Set background color
		int backgroundColor = settings.getInt("settings_background", R.color.myBackground);
		View countingLayout = findViewById(R.id.counting_layout);
		countingLayout.setBackgroundColor(getResources().getColor(backgroundColor));
		
		// Get intent
		Intent i = getIntent();
		Bundle extras = i.getExtras();
		if (extras != null) {
			currentCount.setId(extras.getLong("to_load_id"));
			currentCount.setDate(extras.getString("to_load_date"));
			currentCount.setDesc(extras.getString("to_load_desc"));
			currentCount.setValue(extras.getInt("to_load_value"));
			setCount();
			Toast.makeText(this, String.valueOf(currentCount.getValue()), Toast.LENGTH_SHORT).show();
		}
		
	}
	
	@Override
	public void onResume() {
		// Open db
		ds.open();
		// Restore saved preferences
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		currentCount.setId(settings.getLong("saved_id", -1));
		currentCount.setDesc(settings.getString("saved_desc", "new_count"));
		currentCount.setValue(settings.getInt("saved_value", 0));
		setCount();
		// Set background color
		int backgroundColor = settings.getInt("settings_background", R.color.myBackground);
		RelativeLayout countingLayout = (RelativeLayout) findViewById(R.id.counting_layout);
		countingLayout.setBackgroundColor(getResources().getColor(backgroundColor));
		super.onResume();
	}

	@Override
	public void onPause() {
		// Close db
		ds.close();
		// Save preferences
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putLong("saved_id", currentCount.getId());
		editor.putString("saved_desc", currentCount.getDesc());
		editor.putInt("saved_value", currentCount.getValue());
		editor.commit();
		
		super.onPause();
	}
	
	@Override
	public void onStop() {
		super.onStop();
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putLong("saved_id", currentCount.getId());
		editor.putString("saved_desc", currentCount.getDesc());
		editor.putInt("saved_value", currentCount.getValue());
		editor.commit();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected (MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_count:
			return true;
		case R.id.action_history:
			Intent historyIntent = new Intent(this, HistoryActivity.class);
			startActivity(historyIntent);
			return true;
		case R.id.action_settings:
			Intent settingsIntent = new Intent(this, SettingsActivity.class);
			startActivity(settingsIntent);
			return true;
		case R.id.action_about:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder	.setTitle("About App")
					.setMessage("Version: 1.0\nDeveloped by: Arathi Mani\nContact: mani.arathi@gmail.com")
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
			AlertDialog dialog = builder.create();
			dialog.show();
			return true;
		default:
			return super.onOptionsItemSelected(item);
			
		}
	}
	
	public void setCount() {
		TextView countView = (TextView) findViewById(R.id.countValueId);
		Integer currentCountVal = currentCount.getValue();
		countView.setText(currentCountVal.toString());
	}
	
	public void increment(View v) {
		if (currentCount.getValue() == Integer.MAX_VALUE) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder	.setTitle("Limit Reached")
					.setMessage("Sorry, you have reached the upper limit for counting. Please save and restart another count.")
					.setCancelable(false)
					.setNegativeButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					});
			AlertDialog dialog = builder.create();
			dialog.show();
		} else {
		int lastVal = currentCount.getValue();
		lastVal++;
		currentCount.setValue(lastVal);
		setCount();
		}
	}
	
	public void decrement(View v) {
		if (currentCount.getValue() == 0) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder	.setTitle("Limit Reached")
					.setMessage("Sorry, you have reached the lower limit for counting. Please increment instead.")
					.setCancelable(false)
					.setNegativeButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					});
			AlertDialog dialog = builder.create();
			dialog.show();
		} else {
			int lastVal = currentCount.getValue();
			lastVal--;
			currentCount.setValue(lastVal);
			setCount();
		}
	}
	
	public void save(View v) {
	    ds.createCount(currentCount);
	    // Reset count
	    currentCount = new Count();
	    setCount();
	    // Show toast notification
	    String s = "Count has been saved to history!";
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(this, s, duration);
		toast.show();

	}
}
