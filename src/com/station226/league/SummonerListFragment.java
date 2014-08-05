package com.station226.league;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.league.R;

public class SummonerListFragment extends ListFragment {

	OnSummonerSelectedListener mListener;
	
	String[] summoner_names_ = new String[10];
	long[] summoner_ids_ = new long[10];
	long[] summoner_levels_ = new long[10];
	
	/**
	 * Draws the fragment as it should
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_list, container, false);
		
		return view;
	}
	
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Bundle bundle = getArguments();
		if(bundle!=null){
			loadNames(bundle);
		}else{
			/*String[] names = getResources().getStringArray(R.array.Champions);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
					android.R.layout.simple_list_item_1,names);
			this.setListAdapter(adapter);*/
		}
	}
	
	public void loadNames(Bundle bundle){
		summoner_names_=bundle.getStringArray("NAMES");
		summoner_ids_=bundle.getLongArray("IDS");
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1,summoner_names_);
		this.setListAdapter(adapter);
	}

	/**
	 * implements the onListItemClick callback for our current listView
	 * Should get the name from the ListView and pass it to the onSummonerSelected callback
	 */
	public void onListItemClick(ListView l, View v, int position, long id) {
		//String summonerName = l.toString();
		mListener.onSummonerSelected(summoner_names_[position],summoner_ids_[position],position );
	}

	/**
	 * Defines a callback for the summoner selection communication between
	 * fragments Sends the Summoner name back to the activity which then creates
	 * a new player fragment
	 * 
	 * @author Ian Mundy
	 * 
	 */
	public interface OnSummonerSelectedListener {
		public void onSummonerSelected(String SummonerName, long summonerId, int position);
	}

	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnSummonerSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnSummonerSelectedListener");
		}
	}
}
