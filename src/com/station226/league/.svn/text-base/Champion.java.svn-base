package com.station226.league;

//Author: Caleb Richard
//This class represents a champion in the form
//returned from the champion request from the
//Riot API. Included are public access methods
//for each of the data fields, incase they are
//needed for future use.
public class Champion {
	
	//Indicates if Champion is active
	private boolean active;
	
	//Various rankings for champion
	private int attackRank;
	private int magicRank;
	private int defenseRank;
	private int difficultyRank;	
	
	//Bot enabled flag (for custom games)
	private boolean botEnabled;
	
	//Bot enabled flag (for Co-op vs. AI)
	private boolean botMmEnabled;
	

	//Indicates is champion is free to play for the week
	private boolean freeToPlay;
	
	//champion id
	private long id;
	
	//champion name
	private String name;
	
	//Ranked play enabled flag
	private Boolean rankedPlayEnabled;
	
	//Constructor for champion
	public Champion(boolean tActive, int aRank, int mRank, int dRank, int diRank,
			boolean bEnabled, boolean bmEnabled, boolean ftp, long champid, String champname, Boolean rEnabled)
	{
		this.active = tActive;
		this.attackRank = aRank;
		this.magicRank = mRank;
		this.defenseRank = dRank;
		this.difficultyRank = diRank;
		this.botEnabled = bEnabled;
		this.botMmEnabled = bmEnabled;
		this.freeToPlay = ftp;
		this.id = champid;
		this.name = champname;
		this.rankedPlayEnabled = rEnabled;
	}
	
	//Access methods
	
	public boolean getActive() {
		return this.active;
	}
	
	public int getAttackRank() {
		return this.attackRank;
	}
	
	public int getMagicRank() {
		return this.magicRank;
	}
	
	public int getDefenseRank() {
		return this.defenseRank;
	}
	
	public int getDifficultyRank() {
		return this.difficultyRank;
	}
	
	public boolean getBotEnabled() {
		return this.botEnabled;
	}
	
	public boolean getBotMmEnabled() {
		return this.botMmEnabled;
	}
	
	public boolean getFreeToPlay() {
		return this.freeToPlay;
	}
	
	public long getId() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public boolean getRankedPlayEnabled() {
		return this.rankedPlayEnabled;
	}
}









