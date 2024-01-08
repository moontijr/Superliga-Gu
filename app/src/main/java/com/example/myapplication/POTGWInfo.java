package com.example.myapplication;

public class POTGWInfo {
    private String userId;
    private String playerId;
    private String gameweekId;

    public POTGWInfo(String userId, String playerId, String gameweekId) {
        this.userId = userId;
        this.playerId = playerId;
        this.gameweekId = gameweekId;
    }

    public POTGWInfo(){

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getGameweekId() {
        return gameweekId;
    }

    public void setGameweekId(String gameweekId) {
        this.gameweekId = gameweekId;
    }
}
