package com.example.tapandcount;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class HistoryActivity extends ListActivity {

	private String edittedTitle;
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
	    final ArrayAdapter<Count> adapter = new ArrayAdapter<Count>(this, android.R.layout.simple_list_item_1, values);
	    setListAdapter(adapter);
	    
	    final ListView listView = getListView();
	    listView.setTextFilterEnabled(true);
	    listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, final int position, long id) {
				AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
				builder	.setTitle("Options")
						.setItems(R.array.history_options, new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								switch (which) {
								case 0:
									// Edit title
									editTitle(values.get(position));
									// Get new values
									values = ds.getAllCounts();
									listView.setAdapter(adapter);
									adapter.notifyDataSetChanged();
									listView.invalidateViews();
									break;
								case 1:
									// Delete count
									deleteCount(values.get(position));
									adapter.remove(adapter.getItem(position));
									listView.invalidateViews();
									break;
								case 2:
									// Load count
									loadCount(values.get(position));
									break;
								default:
									break;
								}
										
							}
						});
				builder.create().show();
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
	
	public void editTitle(final Count c) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Edit Title");
		final EditText input = new EditText(this);
		input.setInputType(InputType.TYPE_CLASS_TEXT);
		builder.setView(input);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() { 
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        edittedTitle = input.getText().toString();
		        c.setDesc(edittedTitle);
		        ds.createCount(c);
		    }
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        dialog.cancel();
		    }
		});

		builder.show();
	}
	
	public void deleteCount(Count c) {
		ds.deleteCount(c);
	}
	
	public void loadCount(Count c) {
		Intent i = new Intent(getApplicationContext(), MainActivity.class);
		i.putExtra("to_load_id", c.getId());
		i.putExtra("to_load_desc", c.getDesc());
		i.putExtra("to_load_date", c.getDate());
		i.putExtra("to_load_value", c.getValue());
		startActivity(i);
	}
}
