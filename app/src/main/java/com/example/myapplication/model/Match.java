package com.example.myapplication.model;

public class Match {
    private String id;
    private String homeTeamId;
    private String awayTeamId;
    private int homeTeamGoals;
    private int awayTeamGoals;
    private String matchdayId;
    private String potmId;

    public String getId() {
        return id;
    }

    public Match(){

    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHomeTeamId() {
        return homeTeamId;
    }

    public void setHomeTeamId(String homeTeamId) {
        this.homeTeamId = homeTeamId;
    }

    public String getAwayTeamId() {
        return awayTeamId;
    }

    public void setAwayTeamId(String awayTeamId) {
        this.awayTeamId = awayTeamId;
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

    public String getMatchdayId() {
        return matchdayId;
    }

    public void setMatchdayId(String matchdayId) {
        this.matchdayId = matchdayId;
    }

    public String getPotmId() {
        return potmId;
    }

    public void setPotmId(String potmId) {
        this.potmId = potmId;
    }

    public Match(String id, String homeTeamId, String awayTeamId, int homeTeamGoals, int awayTeamGoals, String matchdayId, String potmId) {
        this.id = id;
        this.homeTeamId = homeTeamId;
        this.awayTeamId = awayTeamId;
        this.homeTeamGoals = homeTeamGoals;
        this.awayTeamGoals = awayTeamGoals;
        this.matchdayId = matchdayId;
        this.potmId = potmId;
    }
}
