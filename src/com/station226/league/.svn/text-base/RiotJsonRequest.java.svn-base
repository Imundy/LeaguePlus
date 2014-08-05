/**
 * Class for making requests to the Riot servers
 * Requests are formed in the methods and then sent to the request method to be executed
 * The execute method will return the result of the request or an associated error code
 * 
 * @author Caleb Richard and Ian Mundy
 */

package com.station226.league;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import android.util.Log;

public class RiotJsonRequest {

	// Default port for Amazon server
	private static final int PORT = 20000;

	private static final String IP = "54.187.124.177";

	// Base URL for Riot API Requests
	static private String BASE_URL = "https://prod.api.pvp.net";

	// Riot key for API beta
	static private String API_KEY = "c2b7cb23-b0a0-4cc3-a97f-93eb81733b12";

	// End of Request URL
	static private String URL_END = "?api_key=" + API_KEY;

	// Forms entire request URL and returns JSON String from Execute method
	public static String requestChampion(String region) throws IOException {
		String request = BASE_URL + "/api/lol/" + region + "/v1.2/champion"
				+ URL_END;
		return executeRequest(request);
	}

	public static String requestGame(String region, long summonerId)
			throws IOException {
		String request = BASE_URL + "/api/lol/" + region
				+ "/v1.3/game/by-summoner/" + summonerId + "/recent" + URL_END;
		return executeRequest(request);
	}

	public static String requestLeague(String region, long summonerId)
			throws IOException {
		String request = BASE_URL + "/api/lol/" + region
				+ "/v2.3/league/by-summoner/" + summonerId + URL_END;
		return executeRequest(request);
	}

	public static String requestStats(String region, long summonerId)
			throws IOException {
		String request = BASE_URL + "/api/lol/" + region
				+ "/v1.3/stats/by-summoner/" + summonerId
				+ "/summary?season=SEASON4&" + URL_END.substring(1);
		return executeRequest(request);
	}

	public static String requestStatsRanked(String region, long summonerId)
			throws IOException {
		// https://prod.api.pvp.net/api/lol/na/v1.3/stats/by-summoner
		// /30225218/ranked?season=SEASON4&api_key=f567804b-0fa9-420a-b942-8b86c926b581

		String request = BASE_URL + "/api/lol/" + region
				+ "/v1.3/stats/by-summoner/" + summonerId
				+ "/ranked?season=SEASON4&" + URL_END.substring(1);
		return executeRequest(request);
	}

	public static String requestSummonerMasteries(String region, long summonerId)
			throws IOException {
		String request = BASE_URL + "/api/lol/" + region + "/v1.4/summoner/"
				+ summonerId + "/masteries" + URL_END;
		return executeRequest(request);
	}

	public static String requestSummonerRunes(String region, long summonerId)
			throws IOException {
		String request = BASE_URL + "/api/lol/" + region + "/v1.4/summoner/"
				+ summonerId + "/runes" + URL_END;
		return executeRequest(request);
	}

	public static String requestSummonerByName(String region,
			String summonerName) throws IOException {
		String request = BASE_URL + "/api/lol/" + region
				+ "/v1.4/summoner/by-name/" + summonerName + URL_END;
		return executeRequest(request);
	}

	public static String requestSummonerById(String region, long summonerId[])
			throws IOException {
		String idList = "";
		for (int i = 0; i < summonerId.length - 1; i++) {
			idList = idList + summonerId[i] + ",";
		}
		idList = idList + summonerId[summonerId.length - 1];
		String request = BASE_URL + "/api/lol/" + region + "/v1.4/summoner/"
				+ idList + URL_END;
		return executeRequest(request);
	}

	public static String requestSummonerListById(String region,
			String[] summonerIds) throws IOException {
		// Request must be list of names separated by commas
		String summonderId = summonerIds[0];
		for (int i = 1; i < summonerIds.length; i++) {
			summonderId = summonderId + "," + summonerIds[i];
		}
		String request = BASE_URL + "/api/lol/" + region + "/v1.4/summoner/"
				+ summonerIds + "/name" + URL_END;
		return executeRequest(request);
	}

	public static String requestTeam(String region, int summonerId)
			throws IOException {
		String request = BASE_URL + "/api/lol/" + region + "/v2.3/by-summoner/"
				+ summonerId + URL_END;
		return executeRequest(request);
	}

	public static String requestTeambyTeamId(String region, String teamId)
			throws IOException {
		String request = BASE_URL + "/api/lol/" + region
				+ "/v2.3/league/by-team/" + teamId + "/entry" + URL_END;
		return executeRequest(request);
	}

	// Established a connection to requested URL, creates input string
	// to receive JSON String, and returns JSON String
	private static String executeRequest(String url) throws IOException {

		Log.d(RiotJsonRequest.class.getSimpleName() , url);
		
		Socket s = new Socket(IP, PORT);
		PrintStream ps = new PrintStream(s.getOutputStream());
		//Log.d(RiotJsonRequest.class.getSimpleName() , "PS OPENED");
		BufferedReader r = new BufferedReader(new InputStreamReader(
				s.getInputStream()));
		//Log.d(RiotJsonRequest.class.getSimpleName() , "BR OPENED");
		ps.println(url);
		//Log.d(RiotJsonRequest.class.getSimpleName() , "URL PRNTED");
		String response = r.readLine();
		//Log.d(RiotJsonRequest.class.getSimpleName() , "RESPONSE: " + response);
		return response;

		// boolean unsuccessful = true;
		// int tries = 0;
		// String response = "";
		// while(unsuccessful && tries != 3){
		// unsuccessful = false;
		// URL requestUrl = new URL(url);
		// HttpsURLConnection connection = (HttpsURLConnection)
		// requestUrl.openConnection();
		// try{
		// Log.d(RiotJsonRequest.class.getSimpleName() , url);
		// connection.setRequestMethod("GET");
		// connection.getResponseCode();
		//
		// //connection.
		//
		// connection.connect();
		//
		//
		//
		//
		// //Needs error checking on connection
		//
		// InputStream in = new
		// BufferedInputStream(connection.getInputStream());
		// BufferedReader reader = new BufferedReader(new
		// InputStreamReader(in));
		// StringBuffer buffer = new StringBuffer();
		// String line;
		//
		// //reads response in and appends it to buffer
		// do{
		// line = reader.readLine();
		// buffer.append(line);
		// }while (line != null);
		//
		// //disconnects the HttpURLConnection so other requests can be made
		// connection.disconnect();
		// //Log.d(RiotJsonRequest.class.getSimpleName() , "RECEIVED: " +
		// buffer.toString());
		// response = buffer.toString();
		// }catch(Exception e){
		// String code = Integer.toString(connection.getResponseCode());
		// Log.d(RiotJsonRequest.class.getSimpleName() , "CODE: " + code);
		// if(code.equals("404")){
		// return code;
		// }
		// unsuccessful = true;
		// tries++;
		// connection.disconnect();
		// connection = null;
		// requestUrl = null;
		// try {
		// Thread.sleep(1000);
		// } catch (InterruptedException e1) {
		// e1.printStackTrace();
		// }
		// }
		// }
		// return response;
	}

}
