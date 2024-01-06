package com.example.myapplication;

import java.util.Date;

public class User {
    private String userID;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    private String username;

    private String password;

    private String familyName;
    private String givenName;
    private String mailAdress;

    private int points;

    public User(String username, String password, String familyName, String givenName, String mailAdress) {
        this.username = username;
        this.password = password;
        this.familyName = familyName;
        this.givenName = givenName;
        this.mailAdress = mailAdress;
        this.points=0;
    }

    public String getFamilyName() {
        return familyName;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getMailAdress() {
        return mailAdress;
    }

    public void setMailAdress(String mailAdress) {
        this.mailAdress = mailAdress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User(String username, String password){this.username=username; this.password=password;}

    public User(){

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String toStringPointsUsername() {
        return String.format("Username: %s has points %d", username, points);
    }

    @Override
    public String toString() {
        return "User{" +
                "userID='" + userID + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", familyName='" + familyName + '\'' +
                ", givenName='" + givenName + '\'' +
                ", mailAdress='" + mailAdress + '\'' +
                ", points=" + points +
                '}';
    }
}
