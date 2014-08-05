package com.station226.league;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.example.league.R;

public class HomeScreenActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_screen);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home_screen, menu);
		return true;
	}
	
	//String for telling if we are searching an individual
	private static final String individual = "INDIVIDUAL";
	
	/**
	 * When clicked starts a new PlayerSearchActivty and passes it an extra
	 * Extra tells next activity whether it is a game search or individual search
	 * @param view
	 */
	public void scoutPlayer(View view){
		Intent intent = new Intent(HomeScreenActivity.this,PlayerSearchActivity.class);
		intent.putExtra(individual, true);
		this.startActivity(intent);
	}

	/**
	 * When clicked starts a new PlayerSearchActivty and passes it an extra
	 * Extra tells next activity whether it is a game search or individual search
	 * @param view
	 */
	public void searchGame(View view){
		Intent intent = new Intent(this,PlayerSearchActivity.class);
		intent.putExtra(individual, false);
		this.startActivity(intent);
	}
	

}
