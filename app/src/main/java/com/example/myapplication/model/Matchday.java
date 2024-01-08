package com.example.myapplication.model;

public class Matchday {
    private String id;
    private String potgw;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Matchday(String id) {
        this.id = id;
    }

    public String getPotgw() {
        return potgw;
    }

    public void setPotgw(String potgw) {
        this.potgw = potgw;
    }

    public Matchday(String id, String potgw) {
        this.id = id;
        this.potgw=potgw;
    }
}
