package com.miniproject.football.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties
public class User {
    
    
    
    private String name;
    private String email;
    private String HomeTeam;
    private int HomeTeamScore;
    private String AwayTeam;
    private int AwayTeamScore;
    private int RoundNumber;
    private String Location;
     
    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public User(String name, String email, String HomeTeam, String AwayTeam, int HomeTeamScore, int AwayTeamScore) {
        this.name=name;
        this.email=email;
        this.HomeTeam=HomeTeam;
        this.HomeTeamScore=HomeTeamScore;
        this.AwayTeam=AwayTeam;
        this.AwayTeamScore=AwayTeamScore;
    }

    public User(){
        
    }
    
    public int getRoundNumber() {
        return RoundNumber;
    }
    public void setRoundNumber(int roundNumber) {
        RoundNumber = roundNumber;
    }
    
    public int getHomeTeamScore() {
        return HomeTeamScore;
    }

    public void setHomeTeamScore(int homeTeamScore) {
        HomeTeamScore = homeTeamScore;
    }

    public int getAwayTeamScore() {
        return AwayTeamScore;
    }
    public void setAwayTeamScore(int awayTeamScore) {
        AwayTeamScore = awayTeamScore;
    }

    public String getHomeTeam() {
        return HomeTeam;
    }
    public void setHomeTeam(String homeTeam) {
        HomeTeam = homeTeam;
    }
    
    public String getAwayTeam() {
        return AwayTeam;
    }

    public void setAwayTeam(String awayTeam) {
        AwayTeam = awayTeam;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}