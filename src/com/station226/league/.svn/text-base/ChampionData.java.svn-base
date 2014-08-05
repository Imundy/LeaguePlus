package com.station226.league;


public class ChampionData implements Comparable<ChampionData>{
	private long id_;
	private long sessions_played_;
	private long sessions_won_;
	private long sessions_lost_;
	
	public ChampionData(long id, long sessionsPlayed, long sessionsWon, long sessionsLost){
		id_=id;
		sessions_played_=sessionsPlayed;
		sessions_won_=sessionsWon;
		sessions_lost_=sessionsLost;
	}
	
	public long getId(){
		return id_;
	}
	
	public long getSessionsPlayed(){
		return sessions_played_;
	}
	
	public long getSessionsWon(){
		return sessions_won_;
	}
	
	public long getSessionsLost(){
		return sessions_lost_;
	}

	@Override
	public int compareTo(ChampionData c2) {
	    if (this.sessions_played_ < c2.sessions_played_)
	        return 1;
	    if (this.sessions_played_ == c2.sessions_played_)
	        return 0;

	    return -1;
	}
	
	
}
