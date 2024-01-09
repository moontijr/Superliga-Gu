package com.example.myapplication.model;

public class POTMInfo {
    private String userId;
    private String playerId;
    private String gameId;

    public POTMInfo() {
    }

    public POTMInfo(String userId, String playerId, String gameId) {
        this.userId = userId;
        this.playerId = playerId;
        this.gameId = gameId;
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

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }
}
