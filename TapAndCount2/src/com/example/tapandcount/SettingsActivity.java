package com.example.tapandcount;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
				editor.putBoolean("settings_extra_options", isChecked);
				editor.commit();
			}
		});
		
		// Set settings
		multitouchSwitch.setChecked(settings.getBoolean("settings_multitouch_allowed", false));
		extraoptionsSwitch.setChecked(settings.getBoolean("settings_extra_options", false));
		
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
	
	public void setBackgroundColor(View v) {
		int chosenBackground = R.color.myBackground;
		
		switch (v.getId()) {
		case R.id.background1:
			chosenBackground = R.color.background_red;
			break;
		case R.id.background2:
			chosenBackground = R.color.background_orange;
			break;
		case R.id.background3:
			chosenBackground = R.color.background_yellow;
			break;
		case R.id.background4:
			chosenBackground = R.color.background_green;
			break;
		case R.id.background5:
			chosenBackground = R.color.background_blue;
			break;
		case R.id.background6:
			chosenBackground = R.color.background_purple;
			break;
		case R.id.background7:
			chosenBackground = R.color.background_black;
			break;
		case R.id.background8:
			chosenBackground = R.color.background_white;
			break;
		case R.id.background9:
			chosenBackground = R.color.background_brown;
			break;
		}
		
		// Set setting
		final SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("settings_background", chosenBackground);
		editor.commit();
	}
}
