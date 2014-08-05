package com.station226.league;

import java.lang.ref.WeakReference;
import java.util.Locale;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.league.R;
import com.station226.league.SummonerListFragment.OnSummonerSelectedListener;

public class PlayerActivity extends FragmentActivity implements
		OnSummonerSelectedListener {

	private boolean individual;

	private String region_;

	private String original_summoner_;

	SummonerListFragment summoner_list_fragment_;

	// game variables
	String[] summoner_names_ = new String[10];
	long[] summoner_ids_ = new long[10];
	long[] summoner_lvls_ = new long[10];
	// long game_size_ = 0;
	// private RequestService request_service_;
	Messenger messenger_ = new Messenger(new IncomingHandler(this));

	private ProgressDialog pDialog_;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		pDialog_ = new ProgressDialog(this);

		Intent intent = getIntent();
		individual = intent.getBooleanExtra("INDIVIDUAL", true);
		region_ = intent.getStringExtra("REGION");
		original_summoner_ = intent.getStringExtra("NAME");

		Log.d(PlayerActivity.class.getSimpleName(), "ORIGNAL SUMMONER IS: "
				+ original_summoner_);

		if (individual) {
			setContentView(R.layout.fragment_player);
			requestSinglePlayer();
		} else {
			setContentView(R.layout.activity_player_view);
			firstFragment(savedInstanceState);
		}
	}

	protected void onStop() {
		super.onStop();
		Intent intent = new Intent(this, RequestService.class);
		stopService(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home_screen, menu);
		return true;
	}

	private void requestSinglePlayer() {

	}

	public void requestPlayer(int position) {
		Intent intent = new Intent(this, RequestService.class);
		intent.putExtra("TYPE", RequestService.PLAYER_REQUEST_);
		intent.putExtra("MESSENGER", messenger_);
		intent.putExtra("REGION", region_);
		intent.putExtra("ID", summoner_ids_[position]);
		intent.putExtra("POSITION", position);
		startService(intent);

	}

	/**
	 * Populates the player fragment
	 */
	public void populatePlayer(Bundle bundle) {
		int position = bundle.getInt("POSITION");
		// TextView attributes
		TextView summonerName = (TextView) findViewById(R.id.summonerName);
		TextView levelAndServer = (TextView) findViewById(R.id.levelAndServer);
		TextView statistics = (TextView) findViewById(R.id.statsDisplay);
		TextView offensiveMasteries = (TextView) findViewById(R.id.offense);
		TextView defensiveMasteries = (TextView) findViewById(R.id.defense);
		TextView utilityMasteries = (TextView) findViewById(R.id.utility);
		TextView runeStats = (TextView) findViewById(R.id.runeStats);
		TextView champion1 = (TextView) findViewById(R.id.champion1Stats);
		TextView champion2 = (TextView) findViewById(R.id.champion2Stats);
		TextView champion3 = (TextView) findViewById(R.id.champion3Stats);
		TextView champion4 = (TextView) findViewById(R.id.champion4Stats);
		TextView champion5 = (TextView) findViewById(R.id.champion5Stats);

		// league textViews
		TextView team3v3 = (TextView) findViewById(R.id.team3v3);
		TextView team5v5 = (TextView) findViewById(R.id.team5v5);
		TextView solo5v5 = (TextView) findViewById(R.id.solo5v5);

		// ImageView attributes
		ImageView league5v5solo = (ImageView) findViewById(R.id.soloImage5v5);
		ImageView league5v5team = (ImageView) findViewById(R.id.teamImage5v5);
		ImageView league3v3 = (ImageView) findViewById(R.id.image3v3);
		ImageView champion1Image = (ImageView) findViewById(R.id.champion1);
		ImageView champion2Image = (ImageView) findViewById(R.id.champion2);
		ImageView champion3Image = (ImageView) findViewById(R.id.champion3);
		ImageView champion4Image = (ImageView) findViewById(R.id.champion4);
		ImageView champion5Image = (ImageView) findViewById(R.id.champion5);

		summonerName.setText(summoner_names_[position]);
		levelAndServer.setText("Level " + summoner_lvls_[position] + "\n"
				+ region_.toUpperCase(Locale.ENGLISH));
		statistics.setText("UNRANKED STATS: \n"
				+ "\tTotal Neutral Minions Killed: "
				+ bundle.getLong("UNRANKEDtotalNeutralMinionsKilled")
				+ "\n\tTotal Minion Kills: "
				+ bundle.getLong("UNRANKEDtotalMinionsKill")
				+ "\n\tTotal Champion Kills: "
				+ bundle.getLong("UNRANKEDtotalChampionKills")
				+ "\n\tTotal Assists: "
				+ bundle.getLong("UNRANKEDtotalAssists")
				+ "\n\tTotal Turrets Destroyed: "
				+ bundle.getLong("UNRANKEDtotalTurretsKills")
				+ "\n\tTotal Wins: " + bundle.getLong("UNRANKEDwins") + "\n");

		if (bundle.getBoolean("SOLO")) {
			String league = bundle.getString("RANKEDtier");
			setLeagueIcon(league, league5v5solo);
			String leagueName = bundle.getString("RANKEDleagueName");
			if (leagueName.length() > 10) {
				int space = leagueName.indexOf(" ");
				leagueName = leagueName.substring(0, space) + "\n"
						+ leagueName.substring(space + 1);
			}
			solo5v5.setText(leagueName + "\n" + league + " "
					+ bundle.getString("RANKEDrank") + "\n"
					+ bundle.getString("RANKEDwins") + " wins" + "\n"
					+ bundle.getString("RANKEDleaguePoints") + " LP");
		}

		if (bundle.getBoolean("FIVES")) {
			String league = bundle.getString("RANKEDtierFives");
			setLeagueIcon(league, league5v5team);
			String leagueName = bundle.getString("RANKEDleagueNameFives");
			// String teamName = bundle.getString("RANKEDteamNameFives");
			if (leagueName.length() > 10) {
				int space = leagueName.indexOf(" ");
				leagueName = leagueName.substring(0, space) + "\n"
						+ leagueName.substring(space + 1);
			}
			/*
			 * if(teamName.length()>10 && teamName.indexOf(" ") != -1){ int
			 * space = leagueName.indexOf(" "); teamName =
			 * teamName.substring(0,space) + "\n"+ teamName.substring(space+1);
			 * }
			 */
			team5v5.setText(leagueName + "\n" + league + " "
					+ bundle.getString("RANKEDrankFives") + "\n"
					+ bundle.getString("RANKEDwinsFives") + " wins" + "\n"
					+ bundle.getString("RANKEDleaguePointsFives") + " LP");
		}

		if (bundle.getBoolean("THREES")) {
			String league = bundle.getString("RANKEDtierThrees");
			setLeagueIcon(league, league3v3);
			String leagueName = bundle.getString("RANKEDleagueNameThrees");
			// String teamName = bundle.getString("RANKEDteamNameFives");
			if (leagueName.length() > 10) {
				int space = leagueName.indexOf(" ");
				leagueName = leagueName.substring(0, space) + "\n"
						+ leagueName.substring(space + 1);
			}
			/*
			 * if(teamName.length()>10 && teamName.indexOf(" ") != -1){ int
			 * space = leagueName.indexOf(" "); teamName =
			 * teamName.substring(0,space) + "\n"+ teamName.substring(space+1);
			 * }
			 */
			team3v3.setText(leagueName + "\n" + league + " "
					+ bundle.getString("RANKEDrankThrees") + "\n"
					+ bundle.getString("RANKEDwinsThrees") + " wins" + "\n"
					+ bundle.getString("RANKEDleaguePointsThrees") + " LP");
		}
		
		long champsPlayed = bundle.getLong("CHAMPSPLAYED");
		
		for(int i = 0; i < champsPlayed; i++){
			int id = Integer.parseInt(Long.toString(bundle.getLong("CHAMP"+i+"ID")));
			if(i==0){
				this.setChampion(id, champion1Image);
				champion1.setText(StaticUtilities.getChampionName(id));
			}else if(i == 1){
				this.setChampion(id, champion2Image);
				champion2.setText(StaticUtilities.getChampionName(id));
			}else if(i == 2){
				this.setChampion(id, champion3Image);
				champion3.setText(StaticUtilities.getChampionName(id));
			}else if(i == 3){
				this.setChampion(id, champion4Image);
				champion4.setText(StaticUtilities.getChampionName(id));
			}else if(i == 4){
				this.setChampion(id, champion5Image);
				champion5.setText(StaticUtilities.getChampionName(id));
			}
			bundle.getLong("CHAMP"+i+"PLAYED");
			bundle.getLong("CHAMP"+i+"WON");
			bundle.getLong("CHAMP"+i+"LOST");
		}

		pDialog_.cancel();
	}

	public void setLeagueIcon(String tier, ImageView image) {
		if (tier.toLowerCase(Locale.ENGLISH).equals("bronze")) {
			image.setImageDrawable(getResources().getDrawable(
					R.drawable.basic_bronze_tier_league_of_legends_emblem));
		} else if (tier.toLowerCase(Locale.ENGLISH).equals("silver")) {
			image.setImageDrawable(getResources().getDrawable(
					R.drawable.basic_silver_tier_league_of_legends_emblem));
		} else if (tier.toLowerCase(Locale.ENGLISH).equals("gold")) {
			image.setImageDrawable(getResources().getDrawable(
					R.drawable.basic_gold_tier_league_of_legends_emblem));
		} else if (tier.toLowerCase(Locale.ENGLISH).equals("platinum")) {
			image.setImageDrawable(getResources().getDrawable(
					R.drawable.basic_platinum_tier_league_of_legends_emblem));
		} else if (tier.toLowerCase(Locale.ENGLISH).equals("diamond")) {
			image.setImageDrawable(getResources().getDrawable(
					R.drawable.basic_diamond_tier_league_of_legends_emblem));
		} else if (tier.toLowerCase(Locale.ENGLISH).equals("challenger")) {
			image.setImageDrawable(getResources().getDrawable(
					R.drawable.challenge_tier_league_of_legends_emblem));
		}
	}

	/**
	 * For now initializes player list to a default string of ten champions The
	 * on click creates a fragment for the current player
	 */
	public void firstFragment(Bundle savedInstanceState) {
		/*
		 * if (findViewById(R.id.fragment_container) != null) {
		 * Log.d(PlayerActivity.class.getSimpleName(),"firstFragment entered");
		 * // However, if we're being restored from a previous state, // then we
		 * don't need to do anything and should return or else // we could end
		 * up with overlapping fragments. if (savedInstanceState != null) {
		 * return; }
		 * 
		 * // Create a new Fragment to be placed in the activity layout
		 * summoner_list_fragment_ = new SummonerListFragment();
		 * Log.d(PlayerActivity.class.getSimpleName(),"listFragment created");
		 * 
		 * // Add the fragment to the 'fragment_container' FrameLayout
		 * getSupportFragmentManager().beginTransaction()
		 * .add(R.id.fragment_container, summoner_list_fragment_).commit();
		 * Log.d(PlayerActivity.class.getSimpleName(),"listFragment committed");
		 * 
		 * 
		 * }
		 */

		Intent intent = new Intent(this, RequestService.class);
		intent.putExtra("TYPE", RequestService.TEAM_REQUEST_);
		intent.putExtra("SUMMONER", original_summoner_);
		intent.putExtra("MESSENGER", messenger_);
		intent.putExtra("REGION", region_);
		startService(intent);
		Log.d(PlayerActivity.class.getSimpleName(), "service started");

	}

	public void displayTeamFragment() {
		if (summoner_list_fragment_ != null) {
			getSupportFragmentManager().beginTransaction().remove(
					summoner_list_fragment_);
		}
		Bundle bundle = new Bundle();
		bundle.putStringArray("NAMES", summoner_names_);
		bundle.putLongArray("IDS", summoner_ids_);
		// bundle.putLong("GAMESIZE", game_size_);
		summoner_list_fragment_ = new SummonerListFragment();
		summoner_list_fragment_.setArguments(bundle);
		// summoner_list_fragment_.loadNames();
		Log.d(PlayerActivity.class.getSimpleName(),
				" THE REAL listFragment created");

		// Add the fragment to the 'fragment_container' FrameLayout
		getSupportFragmentManager().beginTransaction()
				.add(R.id.fragment_container, summoner_list_fragment_)
				.addToBackStack("SummonerList").commit();
		Log.d(PlayerActivity.class.getSimpleName(),
				"THE REAL listFragment committed");

	}

	public void onSummonerSelected(String summonerName, long summonerId,
			int position) {

		requestPlayer(position);
		pDialog_.setMessage("Requesting " + summonerName);
		pDialog_.show();
		// Create fragment and give it an argument specifying the article it
		// should show
		PlayerFragment newFragment = new PlayerFragment();

		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();

		// Replace whatever is in the fragment_container view with this
		// fragment,
		// and add the transaction to the back stack so the user can navigate
		// back
		transaction.replace(R.id.fragment_container, newFragment);
		transaction.addToBackStack(null);

		// Commit the transaction
		transaction.commit();

	}

	/**
	 * Class to handle incoming messages The messages must have a TYPE int for
	 * the corresponding switch statement.
	 * 
	 * @author Ian Mundy
	 * 
	 */
	static class IncomingHandler extends Handler {

		WeakReference<PlayerActivity> activity_;

		/**
		 * Constructor
		 * 
		 * @param activity
		 *            - the activity to refer back to
		 */
		public IncomingHandler(PlayerActivity activity) {
			activity_ = new WeakReference<PlayerActivity>(activity);
		}

		/**
		 * Handle a message from the requestService
		 */
		public void handleMessage(Message msg) {
			PlayerActivity playerActivity = activity_.get();

			switch (msg.getData().getInt("TYPE")) {
			case RequestService.PLAYER_REQUEST_:
				playerActivity.populatePlayer(msg.getData());
				break;
			case RequestService.TEAM_REQUEST_:
				playerActivity.summoner_names_ = msg.getData().getStringArray(
						"NAMES");
				playerActivity.summoner_ids_ = msg.getData()
						.getLongArray("IDS");
				playerActivity.summoner_lvls_ = msg.getData().getLongArray(
						"LEVELS");
				// playerActivity.game_size_=msg.getData().getLong("GAMESIZE");
				playerActivity.displayTeamFragment();
				break;
			}
			// do something

			playerActivity = null;

		}

	}

	public void setChampion(int championId, ImageView image){
		switch(championId){
		case 412:
			image.setImageDrawable(getResources().getDrawable(R.drawable.thresh_square));
			break;
		case 266:
			image.setImageDrawable(getResources().getDrawable(R.drawable.aatrox_square));
			break;
		case 23:
			image.setImageDrawable(getResources().getDrawable(R.drawable.tryndamere_square));
			break;
		case 79:
			image.setImageDrawable(getResources().getDrawable(R.drawable.gragas_square));
			break;
		case 69:
			image.setImageDrawable(getResources().getDrawable(R.drawable.cassiopeia_square));
			break;
		case 13:
			image.setImageDrawable(getResources().getDrawable(R.drawable.ryze_square));
			break;
		case 78:
			image.setImageDrawable(getResources().getDrawable(R.drawable.poppy_square));
			break;
		case 14:
			image.setImageDrawable(getResources().getDrawable(R.drawable.sion_square));
			break;
		case 1:
			image.setImageDrawable(getResources().getDrawable(R.drawable.annie_square));
			break;
		case 111:
			image.setImageDrawable(getResources().getDrawable(R.drawable.nautilus_square));
			break;
		case 43:
			image.setImageDrawable(getResources().getDrawable(R.drawable.karma_square));
			break;
		case 99:
			image.setImageDrawable(getResources().getDrawable(R.drawable.lux_square));
			break;
		case 103:
			image.setImageDrawable(getResources().getDrawable(R.drawable.ahri_square));
			break;
		case 2:
			image.setImageDrawable(getResources().getDrawable(R.drawable.olaf_square));
			break;
		case 112:
			image.setImageDrawable(getResources().getDrawable(R.drawable.viktor_square));
			break;
		case 34:
			image.setImageDrawable(getResources().getDrawable(R.drawable.anivia_square));
			break;
		case 86:
			image.setImageDrawable(getResources().getDrawable(R.drawable.garen_square));
			break;
		case 27:
			image.setImageDrawable(getResources().getDrawable(R.drawable.singed_square));
			break;
		case 127:
			image.setImageDrawable(getResources().getDrawable(R.drawable.lissandra_square));
			break;
		case 57:
			image.setImageDrawable(getResources().getDrawable(R.drawable.maokai_square));
			break;
		case 25:
			image.setImageDrawable(getResources().getDrawable(R.drawable.morgana_square));
			break;
		case 28:
			image.setImageDrawable(getResources().getDrawable(R.drawable.evelynn_square));
			break;
		case 105:
			image.setImageDrawable(getResources().getDrawable(R.drawable.fizz_square));
			break;
		case 238:
			image.setImageDrawable(getResources().getDrawable(R.drawable.zed_square));
			break;
		case 74:
			image.setImageDrawable(getResources().getDrawable(R.drawable.heimerdinger_square));
			break;
		case 68:
			image.setImageDrawable(getResources().getDrawable(R.drawable.rumble_square));
			break;
		case 37:
			image.setImageDrawable(getResources().getDrawable(R.drawable.sona_square));
			break;
		case 82:
			image.setImageDrawable(getResources().getDrawable(R.drawable.mordekaiser_square));
			break;
		case 96:
			image.setImageDrawable(getResources().getDrawable(R.drawable.kogmaw_square));
			break;
		case 55:
			image.setImageDrawable(getResources().getDrawable(R.drawable.katarina_square));
			break;
		case 117:
			image.setImageDrawable(getResources().getDrawable(R.drawable.lulu_square));
			break;
		case 22:
			image.setImageDrawable(getResources().getDrawable(R.drawable.ashe_square));
			break;
		case 30:
			image.setImageDrawable(getResources().getDrawable(R.drawable.karthus_square));
			break;
		case 12:
			image.setImageDrawable(getResources().getDrawable(R.drawable.alistar_square));
			break;
		case 122:
			image.setImageDrawable(getResources().getDrawable(R.drawable.darius_square));
			break;
		case 67:
			image.setImageDrawable(getResources().getDrawable(R.drawable.vayne_square));
			break;
		case 77:
			image.setImageDrawable(getResources().getDrawable(R.drawable.udyr_square));
			break;
		case 110:
			image.setImageDrawable(getResources().getDrawable(R.drawable.varus_square));
			break;
		case 89:
			image.setImageDrawable(getResources().getDrawable(R.drawable.leona_square));
			break;
		case 126:
			image.setImageDrawable(getResources().getDrawable(R.drawable.jayce_square));
			break;
		case 134:
			image.setImageDrawable(getResources().getDrawable(R.drawable.syndra_square));
			break;
		case 80:
			image.setImageDrawable(getResources().getDrawable(R.drawable.pantheon_square));
			break;
		case 92:
			image.setImageDrawable(getResources().getDrawable(R.drawable.riven_square));
			break;
		case 121:
			image.setImageDrawable(getResources().getDrawable(R.drawable.khazix_square));
			break;
		case 42:
			image.setImageDrawable(getResources().getDrawable(R.drawable.corki_square));
			break;
		case 51:
			image.setImageDrawable(getResources().getDrawable(R.drawable.caitlyn_square));
			break;
		case 76:
			image.setImageDrawable(getResources().getDrawable(R.drawable.nidalee_square));
			break;
		case 85:
			image.setImageDrawable(getResources().getDrawable(R.drawable.kennen_square));
			break;
		case 3:
			image.setImageDrawable(getResources().getDrawable(R.drawable.galio_square));
			break;
		case 45:
			image.setImageDrawable(getResources().getDrawable(R.drawable.veigar_square));
			break;
		case 104:
			image.setImageDrawable(getResources().getDrawable(R.drawable.graves_square));
			break;
		case 90:
			image.setImageDrawable(getResources().getDrawable(R.drawable.malzahar_square));
			break;
		case 254:
			image.setImageDrawable(getResources().getDrawable(R.drawable.vi_square));
			break;
		case 10:
			image.setImageDrawable(getResources().getDrawable(R.drawable.kayle_square));
			break;
		case 39:
			image.setImageDrawable(getResources().getDrawable(R.drawable.irelia_square));
			break;
		case 64:
			image.setImageDrawable(getResources().getDrawable(R.drawable.leesin_square));
			break;
		case 60:
			image.setImageDrawable(getResources().getDrawable(R.drawable.elise_square));
			break;
		case 106:
			image.setImageDrawable(getResources().getDrawable(R.drawable.volibear_square));
			break;
		case 20:
			image.setImageDrawable(getResources().getDrawable(R.drawable.nunu_square));
			break;
		case 4:
			image.setImageDrawable(getResources().getDrawable(R.drawable.twistedfate_square));
			break;
		case 24:
			image.setImageDrawable(getResources().getDrawable(R.drawable.jax_square));
			break;
		case 102:
			image.setImageDrawable(getResources().getDrawable(R.drawable.shyvana_square));
			break;
		case 36:
			image.setImageDrawable(getResources().getDrawable(R.drawable.drmundo_square));
			break;
		case 63:
			image.setImageDrawable(getResources().getDrawable(R.drawable.brand_square));
			break;
		case 131:
			image.setImageDrawable(getResources().getDrawable(R.drawable.diana_square));
			break;
		case 113:
			image.setImageDrawable(getResources().getDrawable(R.drawable.sejuani_square));
			break;
		case 8:
			image.setImageDrawable(getResources().getDrawable(R.drawable.vladimir_square));
			break;
		case 154:
			image.setImageDrawable(getResources().getDrawable(R.drawable.zac_square));
			break;
		case 133:
			image.setImageDrawable(getResources().getDrawable(R.drawable.quinn_square));
			break;
		case 84:
			image.setImageDrawable(getResources().getDrawable(R.drawable.akali_square));
			break;
		case 18:
			image.setImageDrawable(getResources().getDrawable(R.drawable.tristana_square));
			break;
		case 120:
			image.setImageDrawable(getResources().getDrawable(R.drawable.hecarim_square));
			break;
		case 15:
			image.setImageDrawable(getResources().getDrawable(R.drawable.sivir_square));
			break;
		case 236:
			image.setImageDrawable(getResources().getDrawable(R.drawable.lucian_square));
			break;
		case 107:
			image.setImageDrawable(getResources().getDrawable(R.drawable.rengar_square));
			break;
		case 19:
			image.setImageDrawable(getResources().getDrawable(R.drawable.warwick_square));
			break;
		case 72:
			image.setImageDrawable(getResources().getDrawable(R.drawable.skarner_square));
			break;
		case 54:
			image.setImageDrawable(getResources().getDrawable(R.drawable.malphite_square));
			break;
		case 157:
			image.setImageDrawable(getResources().getDrawable(R.drawable.yasuo_square));
			break;
		case 101:
			image.setImageDrawable(getResources().getDrawable(R.drawable.xerath_square));
			break;
		case 17:
			image.setImageDrawable(getResources().getDrawable(R.drawable.teemo_square));
			break;
		case 75:
			image.setImageDrawable(getResources().getDrawable(R.drawable.nasus_square));
			break;
		case 58:
			image.setImageDrawable(getResources().getDrawable(R.drawable.renekton_square));
			break;
		case 119:
			image.setImageDrawable(getResources().getDrawable(R.drawable.draven_square));
			break;
		case 35:
			image.setImageDrawable(getResources().getDrawable(R.drawable.shaco_square));
			break;
		case 50:
			image.setImageDrawable(getResources().getDrawable(R.drawable.swain_square));
			break;
		case 115:
			image.setImageDrawable(getResources().getDrawable(R.drawable.ziggs_square));
			break;
		case 40:
			image.setImageDrawable(getResources().getDrawable(R.drawable.janna_square));
			break;
		case 91:
			image.setImageDrawable(getResources().getDrawable(R.drawable.talon_square));
			break;
		case 61:
			image.setImageDrawable(getResources().getDrawable(R.drawable.orianna_square));
			break;
		case 9:
			image.setImageDrawable(getResources().getDrawable(R.drawable.fiddlesticks_square));
			break;
		case 114:
			image.setImageDrawable(getResources().getDrawable(R.drawable.fiora_square));
			break;
		case 31:
			image.setImageDrawable(getResources().getDrawable(R.drawable.chogath_square));
			break;
		case 33:
			image.setImageDrawable(getResources().getDrawable(R.drawable.rammus_square));
			break;
		case 7:
			image.setImageDrawable(getResources().getDrawable(R.drawable.leblanc_square));
			break;
		case 26:
			image.setImageDrawable(getResources().getDrawable(R.drawable.zilean_square));
			break;
		case 16:
			image.setImageDrawable(getResources().getDrawable(R.drawable.soraka_square));
			break;
		case 56:
			image.setImageDrawable(getResources().getDrawable(R.drawable.nocturne_square));
			break;
		case 222:
			image.setImageDrawable(getResources().getDrawable(R.drawable.jinx_square));
			break;
		case 83:
			image.setImageDrawable(getResources().getDrawable(R.drawable.yorick_square));
			break;
		case 6:
			image.setImageDrawable(getResources().getDrawable(R.drawable.urgot_square));
			break;
		case 21:
			image.setImageDrawable(getResources().getDrawable(R.drawable.missfortune_square));
			break;
		case 62:
			image.setImageDrawable(getResources().getDrawable(R.drawable.monkeyking_square));
			break;
		case 53:
			image.setImageDrawable(getResources().getDrawable(R.drawable.blitzcrank_square));
			break;
		case 98:
			image.setImageDrawable(getResources().getDrawable(R.drawable.shen_square));
			break;
		case 5:
			image.setImageDrawable(getResources().getDrawable(R.drawable.xinzhao_square));
			break;
		case 29:
			image.setImageDrawable(getResources().getDrawable(R.drawable.twitch_square));
			break;
		case 11:
			image.setImageDrawable(getResources().getDrawable(R.drawable.masteryi_square));
			break;
		case 44:
			image.setImageDrawable(getResources().getDrawable(R.drawable.taric_square));
			break;
		case 32:
			image.setImageDrawable(getResources().getDrawable(R.drawable.amumu_square));
			break;
		case 41:
			image.setImageDrawable(getResources().getDrawable(R.drawable.gangplank_square));
			break;
		case 48:
			image.setImageDrawable(getResources().getDrawable(R.drawable.trundle_square));
			break;
		case 38:
			image.setImageDrawable(getResources().getDrawable(R.drawable.kassadin_square));
			break;
		case 161:
			image.setImageDrawable(getResources().getDrawable(R.drawable.velkoz_square));
			break;
		case 143:
			image.setImageDrawable(getResources().getDrawable(R.drawable.zyra_square));
			break;
		case 267:
			image.setImageDrawable(getResources().getDrawable(R.drawable.nami_square));
			break;
		case 59:
			image.setImageDrawable(getResources().getDrawable(R.drawable.jarvaniv_square));
			break;
		case 81:
			image.setImageDrawable(getResources().getDrawable(R.drawable.ezreal_square));
			break;
		}
	}
}
