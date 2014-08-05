package com.station226.league;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//Author: Caleb Richard
//This class serves as a way to populate and keep a champion list
//from the JSON string from the champion request from the Riot API.
//This is kept separate from the actual Http requests in order to
//separate what must be done in the background and what can be done
//in the main thread.
public class ChampionList {

	//Map of champions that maps champion objects to champion id
	private Map<Long, Champion> championMap;
	private String JSONstring;
	
	//Constructor stores JSONstring with information on Champions
	public ChampionList(String JSON) {
		JSONstring = JSON;
	}
	
	//Access method for champion Map
	public Map<Long, Champion> getMap () {
		return championMap;
	}
	
	//returns champion object for specified id
	public Champion getChampion(long id) {
		return championMap.get(id);
	}
	
	//returns name of champion based on id
	public String getChampionName(long id) {
		return (championMap.get(id)).getName();
	}
	
	//populates the champion map with id, champion object 
	//key-value pairs. 
	public void populateList() {
		try {
			JSONArray ja = new JSONArray(JSONstring);
			JSONObject jo;

			
			//Values to create champions
			boolean active;
			int attackRank;
			int magicRank;
			int defenseRank;
			int difficultyRank;	
			boolean botEnabled;
			boolean botMmEnabled;
			boolean freeToPlay;
			long id;
			String name;
			Boolean rankedPlayEnabled;
			
			for (int i=0; i < ja.length(); i++) {
				jo = ja.getJSONObject(i);
				active = jo.getBoolean("active");
				attackRank = jo.getInt("attackRank");
				magicRank = jo.getInt("magicRank");
				defenseRank = jo.getInt("defenseRank");
				difficultyRank = jo.getInt("difficultyRank");	
				botEnabled = jo.getBoolean("botEnabled");
				botMmEnabled = jo.getBoolean("botMmEnabled");
				freeToPlay = jo.getBoolean("freeToPlay");
				id = jo.getLong("id");
				name = jo.getString("name");
				rankedPlayEnabled = jo.getBoolean("rankedPlayEnabled");
				
				//Create champion object to be put in map
				Champion temp = new Champion(active, attackRank, magicRank, 
						defenseRank, difficultyRank, botEnabled, botMmEnabled, 
						freeToPlay, id, name, rankedPlayEnabled);
				//Put id-champion pair in map
				championMap.put(temp.getId(), temp);
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
