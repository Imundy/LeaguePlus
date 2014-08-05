package com.station226.league;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.league.R;

public class PlayerFragment extends Fragment{

	
	  /**
	   * Draws the fragment as it should
	   */
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	      Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.fragment_player,
	        container, false);
	    return view;
	  }
}
