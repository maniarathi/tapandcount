package com.example.tapandcount;

import com.example.tapandcount.R;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	public static final String PREFS_NAME = "TACPrefFile";
	
	private boolean showExtraOptions = false;
	private boolean multiTouchAllowed = false;
	private Integer lastCount;
	private Switch multitouchSwitch;
	private Switch extraoptionsSwitch;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_counting);
		
		// Initialize
		lastCount = 0;
		
		// Restore saved preferences
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		lastCount = settings.getInt("saved_count", 0);
		
		
		// Fetch settings
		/*multitouchSwitch = (Switch) findViewById(R.id.switch_multitouch);
		multiTouchAllowed = multitouchSwitch.isChecked();
		extraoptionsSwitch = (Switch) findViewById(R.id.switch_extrabuttons);
		showExtraOptions = extraoptionsSwitch.isChecked();
		
		// Show buttons if set
		if (showExtraOptions) {
			// TODO: Show extra buttons
		}
		
		// Enable multitouch
		if (multiTouchAllowed) {
			// TODO: Enable multitouch
		}*/
	}
	
	@Override
	public void onResume() {
		// Restore saved preferences
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		lastCount = settings.getInt("saved_count", 0);
		setCount();
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("saved_count", lastCount);
	
		// Commit the edits!
		editor.commit();
	}
	
	@Override
	public void onStop() {
		super.onStop();
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("saved_count", lastCount);
	
		// Commit the edits!
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
		default:
			return super.onOptionsItemSelected(item);
			
		}
	}

	public void setCount() {
		TextView countView = (TextView) findViewById(R.id.countValueId);
		countView.setText(lastCount.toString());
	}
	
	public void increment(View v) {
		if (lastCount.equals(Integer.MAX_VALUE)) {
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
		lastCount++;
		setCount();
		}
	}
}