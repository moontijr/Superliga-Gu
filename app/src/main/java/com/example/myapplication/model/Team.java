package com.example.myapplication.model;

import java.io.Serializable;

public class Team implements Serializable {
    private String id;
    private String name;
    private String Abbreviation;
    private String country;

    public Team(String id, String name, String abbreviation, String country) {
        this.id = id;
        this.name = name;
        Abbreviation = abbreviation;
        this.country = country;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbbreviation() {
        return Abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        Abbreviation = abbreviation;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
