package com.example.tapandcount;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;

public class SettingsActivity extends Activity {
	
	public static final String PREFS_NAME = "TACPrefFile";
	
	private Switch multitouchSwitch;
	private Switch extraoptionsSwitch;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_settings);
		
		// Open settings file
		final SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		
		// Set listeners for the switches
		multitouchSwitch = (Switch) findViewById(R.id.switch_multitouch);
		multitouchSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				SharedPreferences.Editor editor = settings.edit();
				editor.putBoolean("settings_multitouch_allowed", isChecked);
				editor.commit();
			}
		});
		extraoptionsSwitch = (Switch) findViewById(R.id.switch_extrabuttons);
		extraoptionsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				SharedPreferences.Editor editor = settings.edit();
				editor.putBoolean("setting_extra_options", isChecked);
				editor.commit();
			}
		});
		
		// Set settings
		multitouchSwitch.setChecked(settings.getBoolean("settings_multitouch_allowed", false));
		extraoptionsSwitch.setChecked(settings.getBoolean("setting_extra_options", false));
		
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
			Intent countIntent = new Intent(this, MainActivity.class);
			startActivity(countIntent);
			return true;
		case R.id.action_history:
			Intent historyIntent = new Intent(this, HistoryActivity.class);
			startActivity(historyIntent);
			return true;
		case R.id.action_settings:
			return true;
		default:
			return super.onOptionsItemSelected(item);
			
		}
	}
}
