package com.station226.league;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.league.R;

public class PlayerSearchActivity extends Activity{
	private boolean individual;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player_search_screen);
		Intent intent = getIntent();
		individual = intent.getBooleanExtra("INDIVIDUAL", true);
		
		//populate the spinner with regions
		Spinner spinner = (Spinner) findViewById(R.id.regionSpinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource
				(this, R.array.Servers, R.layout.spinner_item);
		adapter.setDropDownViewResource(R.layout.spinner_item);
		spinner.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home_screen, menu);
		return true;
	}
	
	/**
	 * OnClick for finding information based on self
	 * @param view
	 */
	public void findCurrent(View view){
		createPlayerActivity();
	}
	
	
	/**
	 * OnClick for scouting a certain player or their game
	 * @param view
	 */
	public void scoutPlayers(View view){
		createPlayerActivity();
	}
	
	/**
	 * Creates and starts the playerActivity, passing it the individual tag
	 * @param view
	 */
	public void createPlayerActivity(){
		Intent intent = new Intent(this,PlayerActivity.class);
		intent.putExtra("INDIVIDUAL", individual);
		String region = getRegion(((Spinner)findViewById(R.id.regionSpinner))
				.getSelectedItem().toString());
		intent.putExtra("REGION", region);
		String name = (((EditText)findViewById(R.id.name1)).getText().toString());
		Log.d(PlayerSearchActivity.class.getSimpleName(),"!!!!!" + region);
		intent.putExtra("NAME", name);
		this.startActivity(intent);
	}
	
	private String getRegion(String region){
		if(region.equals("North America")){
			return "na";
		}
		
		return"na";
	}
}
