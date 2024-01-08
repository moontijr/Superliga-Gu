package com.example.myapplication.model;

public class InputMatch {
    private String id;

    private String userID;

    private String matchdayID;

    private String matchID;

    private int homeTeamGoals;
    private int awayTeamGoals;

    private int pointsCollected;

    private String potmID;

    public int getPointsCollected() {
        return pointsCollected;
    }

    public InputMatch() {
    }

    public void setPointsCollected(int pointsCollected) {
        this.pointsCollected = pointsCollected;
    }

    public InputMatch(String userID, String matchID, int homeTeamGoals, int awayTeamGoals) {
        this.userID = userID;
        this.matchID = matchID;
        this.homeTeamGoals = homeTeamGoals;
        this.awayTeamGoals = awayTeamGoals;
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

    public String getPotmID() {
        return potmID;
    }

    public void setPotmID(String potmID) {
        this.potmID = potmID;
    }

    public InputMatch(String id, String userID, String matchdayID, String matchID, int homeTeamGoals, int awayTeamGoals, String potmID) {
        this.id = id;
        this.userID = userID;
        this.matchdayID = matchdayID;
        this.matchID = matchID;
        this.homeTeamGoals = homeTeamGoals;
        this.awayTeamGoals = awayTeamGoals;
        this.potmID = potmID;
    }
}
