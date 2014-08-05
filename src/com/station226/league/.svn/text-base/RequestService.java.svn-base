package com.station226.league;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

/**
 * Request class for League+
 * 
 * Interacts with the JSON wrapper classes
 * The result is returned to the caller by use of a Messenger
 * @author Ian Mundy
 *
 */

public class RequestService extends IntentService {
	
	private Messenger reply_messenger_;
	
	private String region_;
	
	final static int PLAYER_REQUEST_ = 0;
	final static int TEAM_REQUEST_ = 1;
	final static int MASTERIES_REQUEST_ = 2;
	final static int RUNES_REQUEST_ = 3;
	final static int UNRANKED_ = 5;

	/**
	 * Default (and only) Constructor for the RequestService
	 */
	public RequestService() {
		super("RequestService");

	}

	public int onStartCommand(Intent intent, int flags, int startId){
		super.onStartCommand(intent, flags, startId);
		Log.d(RequestService.class.getSimpleName(),"STARTED");
		reply_messenger_=(Messenger)intent.getExtras().get("MESSENGER");
		region_=intent.getExtras().getString("REGION");
		
		
		return START_NOT_STICKY;
	}
	
	
	@SuppressLint("DefaultLocale")
	@Override
	protected void onHandleIntent(Intent intent) {
		Log.d(RequestService.class.getSimpleName(),"HANDLE INTENT");
		Message message = Message.obtain();
		String summoner = "";
		//String masteries = "";
		//String runes = "";
		long summonerId = 0;
		
		switch(intent.getExtras().getInt("TYPE")){
		case PLAYER_REQUEST_:
			try{
				summonerId = intent.getExtras().getLong("ID");
				Log.d(RequestService.class.getSimpleName(),"SUMMONER ID IS "+ summonerId);
				summoner = 
						RiotJsonRequest.requestStats(region_,summonerId);
				Log.d(RequestService.class.getSimpleName(),"SUMMONER RETURNED: "+ summoner);
				try {
					JSONObject summonerStatsJSON = new JSONObject(summoner);
					Log.d(RequestService.class.getSimpleName(),"SUMMONER JSON: " + summonerStatsJSON.toString());
					
					//unranked stats
					JSONObject summonerJSON = new JSONObject(summoner);
					JSONArray summariesJSONArray = summonerJSON.getJSONArray("playerStatSummaries");
					JSONObject unrankedJSON = summariesJSONArray.getJSONObject(0);
					for (int i = 0; i < summariesJSONArray.length(); i++){
						unrankedJSON = summariesJSONArray.getJSONObject(i);
						if(unrankedJSON.getString("playerStatSummaryType").equals("Unranked")){
							//find Unranked stats and break
							break;
						}
					}
					
					JSONObject statsJSON = unrankedJSON.getJSONObject("aggregatedStats");
					long totalNeutralMinionsKilled = statsJSON.getLong("totalNeutralMinionsKilled");
					long totalMinionsKill = statsJSON.getLong("totalMinionKills");
					long totalChampionKills = statsJSON.getLong("totalChampionKills");
					long totalAssists = statsJSON.getLong("totalAssists");
					long totalTurretsKills = statsJSON.getLong("totalTurretsKilled");
					long wins = unrankedJSON.getLong("wins");
					
					//put stats into Bundle
					Bundle bundle = new Bundle();
					bundle.putLong("UNRANKEDtotalNeutralMinionsKilled", totalNeutralMinionsKilled);
					bundle.putLong("UNRANKEDtotalMinionsKill", totalMinionsKill);
					bundle.putLong("UNRANKEDtotalChampionKills", totalChampionKills);
					bundle.putLong("UNRANKEDtotalAssists", totalAssists);
					bundle.putLong("UNRANKEDtotalTurretsKills", totalTurretsKills);
					bundle.putLong("UNRANKEDwins", wins);
					bundle.putInt("POSITION", intent.getExtras().getInt("POSITION"));
					
					//now get team statistics
					summoner = RiotJsonRequest.requestLeague(region_,summonerId);
					JSONObject solo5v5JSON = null;
					JSONObject team5v5JSON = null;
					JSONObject threeJSON = null;
					JSONArray rankedJSON = null;
					Log.d(RequestService.class.getSimpleName(),"RANKED TEAM: " + summoner);
					if(!summoner.trim().equals("404")){
						rankedJSON = new JSONArray(summoner);
						for(int i = 0; i < rankedJSON.length(); i++){
							JSONObject json = rankedJSON.getJSONObject(i);
							String queue = json.getString("queue");
							if(queue.equals("RANKED_SOLO_5x5")){
								solo5v5JSON = json;
								JSONArray soloArray = solo5v5JSON.getJSONArray("entries");
								for(int j = 0; j < soloArray.length(); j++){
									if(soloArray.getJSONObject(j).getString("playerOrTeamId").equals(Long.toString(summonerId))){
										solo5v5JSON = soloArray.getJSONObject(j);
									}
								}
							}else if(queue.equals("RANKED_TEAM_5x5")){
								team5v5JSON = json;
								String teamId = team5v5JSON.getString("participantId");
								JSONArray teamArray = team5v5JSON.getJSONArray("entries");
								for(int j = 0; j < teamArray.length(); j++){
									if(teamArray.getJSONObject(j).getString("playerOrTeamId").equals(teamId)){
										team5v5JSON = teamArray.getJSONObject(j);
										break;
									}else if(j == teamArray.length()-1){
										team5v5JSON = null;
									}
								}
								
							}else if(queue.equals("RANKED_TEAM_3x3")){
								threeJSON = json;
								String teamId = threeJSON.getString("participantId");
								JSONArray teamArray = threeJSON.getJSONArray("entries");
								for(int j = 0; j < teamArray.length(); j++){
									if(teamArray.getJSONObject(j).getString("playerOrTeamId").equals(teamId)){
										threeJSON = teamArray.getJSONObject(j);
										break;
									}else if(j == teamArray.length()-1){
										threeJSON = null;
									}
								}
							}
						}
					}
					
					//putting solo ranked data in (if it exists)
					if(solo5v5JSON!=null){
						String soloLeagueName = solo5v5JSON.getString("leagueName");
						String soloLeagueTier = solo5v5JSON.getString("tier");
						String soloRank = solo5v5JSON.getString("rank");
						String soloWins = Long.toString(solo5v5JSON.getLong("wins"));
						String soloLP = Long.toString(solo5v5JSON.getLong("leaguePoints"));
						JSONObject soloSeries = null;
						if(solo5v5JSON.has("miniSeries")){
							soloSeries = solo5v5JSON.getJSONObject("miniSeries");
						}
						if(soloSeries!=null){
							bundle.putBoolean("SERIES", true);
							bundle.putLong("SERIESprogress", soloSeries.getString("progress").length());
							bundle.putLong("SERIESwins", soloSeries.getInt("wins"));
							bundle.putInt("SERIEStime",soloSeries.getInt("timeLeftToPlayMillis"));
						}else{
							bundle.putBoolean("SERIES", false);
						}
						bundle.putString("RANKEDleagueName", soloLeagueName);
						bundle.putString("RANKEDtier", soloLeagueTier);
						bundle.putString("RANKEDrank", soloRank);
						bundle.putString("RANKEDleaguePoints", soloLP);
						bundle.putString("RANKEDwins", soloWins);
						bundle.putBoolean("SOLO", true);
					}else{
						bundle.putBoolean("SOLO", false);
					}
					
					//putting team fives data in (if it exists)
					if(team5v5JSON!=null){
						String fivesLeagueName = team5v5JSON.getString("leagueName");
						String fivesTeamName = team5v5JSON.getString("playerOrTeamName");
						String fivesLeagueTier = team5v5JSON.getString("tier");
						String fivesRank = team5v5JSON.getString("rank");
						String fivesWins = Long.toString(team5v5JSON.getLong("wins"));
						String fivesLP = Long.toString(team5v5JSON.getLong("leaguePoints"));
						JSONObject fivesSeries = null;
						if(team5v5JSON.has("miniSeries")){
							fivesSeries = team5v5JSON.getJSONObject("miniSeries");
						}
						if(fivesSeries!=null){
							bundle.putBoolean("SERIESfives", true);
							bundle.putLong("SERIESprogressFives", fivesSeries.getString("progress").length());
							bundle.putLong("SERIESwinsFives", fivesSeries.getInt("wins"));
							bundle.putInt("SERIEStimeFives",fivesSeries.getInt("timeLeftToPlayMillis"));
						}else{
							bundle.putBoolean("SERIESfives", false);
						}
						bundle.putString("RANKEDteamNameFives", fivesTeamName);
						bundle.putString("RANKEDleagueNameFives", fivesLeagueName);
						bundle.putString("RANKEDtierFives", fivesLeagueTier);
						bundle.putString("RANKEDrankFives", fivesRank);
						bundle.putString("RANKEDleaguePointsFives", fivesLP);
						bundle.putString("RANKEDwinsFives", fivesWins);
						bundle.putBoolean("FIVES", true);
					}else{
						bundle.putBoolean("FIVES", false);
					}
					
					//putting team three data in (if it exists)
					if(threeJSON!=null){
						String threeLeagueName = threeJSON.getString("leagueName");
						String threeTeamName = threeJSON.getString("playerOrTeamName");
						String threeLeagueTier = threeJSON.getString("tier");
						String threeRank = threeJSON.getString("rank");
						String threeWins = Long.toString(threeJSON.getLong("wins"));
						String threeLP = Long.toString(threeJSON.getLong("leaguePoints"));
						JSONObject threeSeries = null;
						if(threeJSON.has("miniSeries")){
							threeSeries = threeJSON.getJSONObject("miniSeries");
						}
						if(threeSeries!=null){
							bundle.putBoolean("SERIESthrees", true);
							bundle.putLong("SERIESprogressThrees", threeSeries.getString("progress").length());
							bundle.putLong("SERIESwinsThrees", threeSeries.getInt("wins"));
							bundle.putInt("SERIEStimeThrees",threeSeries.getInt("timeLeftToPlayMillis"));
						}else{
							bundle.putBoolean("SERIESthrees", false);
						}
						bundle.putString("RANKEDteamNameThrees", threeTeamName);
						bundle.putString("RANKEDleagueNameThrees", threeLeagueName);
						bundle.putString("RANKEDtierThrees", threeLeagueTier);
						bundle.putString("RANKEDrankThrees", threeRank);
						bundle.putString("RANKEDleaguePointsThrees", threeLP);
						bundle.putString("RANKEDwinsThrees", threeWins);
						bundle.putBoolean("THREES", true);
					}else{
						bundle.putBoolean("THREES", false);
					}/*
					synchronized(this){
						try{
							Thread.sleep(1000);
						}catch(InterruptedException e){
							e.printStackTrace();
						}
					}*/
					//get Ranked Stats
					String rankedStats = RiotJsonRequest.requestStatsRanked(region_,summonerId);
					Log.d(RequestService.class.getSimpleName(),"RANKED STATS: " + rankedStats);
					if(!rankedStats.equals("404") && !rankedStats.equals("401")){
						JSONObject rankedStatsJSON = new JSONObject(rankedStats);
						JSONArray championsJSONArray = rankedStatsJSON.getJSONArray("champions");
						ArrayList<ChampionData> champList = new ArrayList<ChampionData>();
						for(int i = 0; i < championsJSONArray.length(); i++){
							JSONObject json = championsJSONArray.getJSONObject(i);
							JSONObject championStats = json.getJSONObject("stats");
							if(json.getLong("id")!=0){
								champList.add(new ChampionData(json.getLong("id"),
										championStats.getLong("totalSessionsPlayed"),
										championStats.getLong("totalSessionsWon"),
										championStats.getLong("totalSessionsLost")));
							}
						}
						Collections.sort(champList);
						if(champList.size()<5){
							bundle.putLong("CHAMPSPLAYED", champList.size());
						}else{
							bundle.putLong("CHAMPSPLAYED", 5);
						}
						for(int i = 0; i < champList.size()&&i<5; i++){
							bundle.putLong("CHAMP"+i+"ID", champList.get(i).getId());
							bundle.putLong("CHAMP"+i+"PLAYED", champList.get(i).getSessionsPlayed());
							bundle.putLong("CHAMP"+i+"WON", champList.get(i).getSessionsWon());
							bundle.putLong("CHAMP"+i+"LOST", champList.get(i).getSessionsLost());
						}
					}else{
						bundle.putLong("CHAMPSPLAYED", 0);
					}

					
					bundle.putInt("TYPE", PLAYER_REQUEST_);
					
					message.setData(bundle);
					reply_messenger_.send(message);
					
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}catch(IOException e){
				e.printStackTrace();
			}
			break;
		case MASTERIES_REQUEST_:
			try{
				summoner = 
						RiotJsonRequest.requestSummonerByName(region_,
								intent.getExtras().getString("SUMMONER"));
			}catch(IOException e){
				e.printStackTrace();
			}
			break;
		case RUNES_REQUEST_:
			try{
				summoner = 
						RiotJsonRequest.requestSummonerByName(region_,
								intent.getExtras().getString("SUMMONER"));
			}catch(IOException e){
				e.printStackTrace();
			}
			break;
		case TEAM_REQUEST_:
			try{
				String requestedSummoner = intent.getExtras().getString("SUMMONER");
				Log.d(RequestService.class.getSimpleName(),"TEAM REQUESTED of USER: " + 
												requestedSummoner);
				summoner = 
						RiotJsonRequest.requestSummonerByName(region_,
								requestedSummoner);
				Log.d(RequestService.class.getSimpleName(),"TEAM RETURNED: "+ summoner);
				JSONObject summonerJSON = new JSONObject(summoner);
				summonerJSON = summonerJSON.getJSONObject(requestedSummoner.toLowerCase());
				Log.d(RequestService.class.getSimpleName(),"SUMMONER JSON: " + summonerJSON.toString());
				int id = Integer.parseInt(summonerJSON.getString("id"));
				Log.d(RequestService.class.getSimpleName(), Integer.toString(id));
				Log.d(RequestService.class.getSimpleName(), summoner);
				
				String gameSummoners = RiotJsonRequest.requestGame(region_, id);
				JSONObject gameJSON = new JSONObject(gameSummoners);
				//Log.d(RequestService.class.getSimpleName(),"GAME JSON: " + gameJSON.toString());
				JSONArray gamesJSONArray = gameJSON.getJSONArray("games");
				JSONArray playersJSONArray = gamesJSONArray.getJSONObject(0).getJSONArray("fellowPlayers");
				Log.d(RequestService.class.getSimpleName(),"Players JSON: " + playersJSONArray.toString());
				
				int gameSize = 0;
				if(gamesJSONArray.getJSONObject(0).getString("subType").equals("RANKED_TEAM_3x3")||
						gamesJSONArray.getJSONObject(0).getString("subType").equals("NORMAL_3x3")){
					Log.d(RequestService.class.getSimpleName(),"GAMESIZE: " + 
							gamesJSONArray.getJSONObject(0).getString("subType").equals("RANKED_TEAM_3x3"));
					gameSize=6;
				}else{
					Log.d(RequestService.class.getSimpleName(),"GAMESIZE: " + 
							gamesJSONArray.getJSONObject(0).getString("subType").equals("RANKED_TEAM_3x3"));
					gameSize=10;
				}
				
				String summoners[] = new String[gameSize];
				long ids[] = new long[gameSize];
				long levels[] = new long[gameSize];
				
				ids[0] = id;
				for(int i = 0; i < gameSize-1; i++){
					ids[i+1] = Long.parseLong(playersJSONArray.getJSONObject(i).getString("summonerId"));
				}
				String names = RiotJsonRequest.requestSummonerById(region_, ids);
				JSONObject nameJSON = new JSONObject(names);
				summoners[0] = requestedSummoner;
				for(int i = 0; i < gameSize-1; i++){
					summoners[i+1] = nameJSON.getJSONObject(Long.toString(ids[i+1])).getString("name");
				}
				levels[0] = summonerJSON.getLong("summonerLevel");
				for(int i = 0; i < gameSize-1; i++){
					levels[i+1] = nameJSON.getJSONObject(Long.toString(ids[i+1])).getLong("summonerLevel");
				}
				Bundle data = new Bundle();
				data.putInt("TYPE", TEAM_REQUEST_);
				data.putStringArray("NAMES", summoners);
				data.putLongArray("IDS", ids);
				data.putLongArray("LEVELS", levels);
				data.putLong("GAMESIZE", gameSize);
				Log.d(RequestService.class.getSimpleName(),"FINISHED WITH NAMES: " + Arrays.toString(summoners));
				
				message.setData(data);
				reply_messenger_.send(message);
				
			}catch(IOException e){
				e.printStackTrace();
			}catch(JSONException e){
				e.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			
			break;
		}
		
	}

}
