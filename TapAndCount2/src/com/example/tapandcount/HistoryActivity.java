package com.example.tapandcount;

import java.util.List;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class HistoryActivity extends ListActivity {

	private CountDataSource ds;
	private List<Count> values;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_history);
		
		// Setup database
		ds = new CountDataSource(this);
	    ds.open();

	    // Show history in list view
	    values = ds.getAllCounts();
	    if (values.size() == 0) {
	    	Toast.makeText(this, "History is empty", Toast.LENGTH_SHORT).show();
	    }
	    ArrayAdapter<Count> adapter = new ArrayAdapter<Count>(this, android.R.layout.simple_list_item_1, values);
	    setListAdapter(adapter);
	    
	    ListView listView = getListView();
	    listView.setTextFilterEnabled(true);
	    listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
				registerForContextMenu(v);
				openContextMenu(v);
				Count selected = (Count) getListAdapter().getItem(position);
				unregisterForContextMenu(v);
			}
	    });
	}

	@Override
	protected void onResume() {
		ds.open();
	    super.onResume();
	}

	@Override
	protected void onPause() {
		ds.close();
		super.onPause();
	}
	
	@Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.floating_menu, menu);
    }
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// Find which Count was selected
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		Count chosen = values.get(info.position);
		
		
		switch (item.getItemId()) {
		case R.id.action_rename:
			// TODO: Rename description
			return true;
		case R.id.action_delete:
			// TODO: Delete entry
			return true;
		case R.id.action_load_count:
			// TODO: Load count
			return true;
		default:
			return super.onContextItemSelected(item);
		}
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
			return true;
		case R.id.action_settings:
			Intent settingsIntent = new Intent(this, SettingsActivity.class);
			startActivity(settingsIntent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
			
		}
	}
}
