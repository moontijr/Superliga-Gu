package com.example.myapplication.model;

public class DoubleMatch {
    private String id;

    private String userID;

    private String matchdayID;

    private String matchID;

    private int homeTeamGoals;
    private int awayTeamGoals;

    public DoubleMatch(String id, String userID, String matchdayID, String matchID, int homeTeamGoals, int awayTeamGoals) {
        this.id = id;
        this.userID = userID;
        this.matchdayID = matchdayID;
        this.matchID = matchID;
        this.homeTeamGoals = homeTeamGoals;
        this.awayTeamGoals = awayTeamGoals;
    }

    public DoubleMatch(String userID, String matchdayID, String matchID, int homeTeamGoals, int awayTeamGoals) {
        this.userID = userID;
        this.matchdayID = matchdayID;
        this.matchID = matchID;
        this.homeTeamGoals = homeTeamGoals;
        this.awayTeamGoals = awayTeamGoals;
    }

    public DoubleMatch() {
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getMatchdayID() {
        return matchdayID;
    }

    public void setMatchdayID(String matchdayID) {
        this.matchdayID = matchdayID;
    }

    public String getMatchID() {
        return matchID;
    }

    public void setMatchID(String matchID) {
        this.matchID = matchID;
    }

    public int getHomeTeamGoals() {
        return homeTeamGoals;
    }

    public void setHomeTeamGoals(int homeTeamGoals) {
        this.homeTeamGoals = homeTeamGoals;
    }

    public int getAwayTeamGoals() {
        return awayTeamGoals;
    }

    public void setAwayTeamGoals(int awayTeamGoals) {
        this.awayTeamGoals = awayTeamGoals;
    }
}
