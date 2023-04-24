package com.miniproject.football.Model;

import java.util.ArrayList;
import java.util.List;

public class Match {
    private String HomeTeam;
    private String HomeTeamScore;
    private String AwayTeam;
    private String AwayTeamScore;
    private int RoundNumber;
    private String Location;
    public static List<Match> matches = new ArrayList();
    public String getHomeTeam() {
        return HomeTeam;
    }
    public void setHomeTeam(String homeTeam) {
        HomeTeam = homeTeam;
    }
    public String getHomeTeamScore() {
        return HomeTeamScore;
    }
    public void setHomeTeamScore(String homeTeamScore) {
        HomeTeamScore = homeTeamScore;
    }
    public String getAwayTeam() {
        return AwayTeam;
    }
    public void setAwayTeam(String awayTeam) {
        AwayTeam = awayTeam;
    }
    public String getAwayTeamScore() {
        return AwayTeamScore;
    }
    public void setAwayTeamScore(String awayTeamScore) {
        AwayTeamScore = awayTeamScore;
    }
    public int getRoundNumber() {
        return RoundNumber;
    }
    public void setRoundNumber(int roundNumber) {
        RoundNumber = roundNumber;
    }
    public String getLocation() {
        return Location;
    }
    public void setLocation(String location) {
        Location = location;
    }
    public static List<Match> getMatches() {
        return matches;
    }
    public static void setMatches(List<Match> matches) {
        Match.matches = matches;
    }
    @Override
    public String toString() {
        return "Match [AwayTeam=" + AwayTeam + ", AwayTeamScore=" + AwayTeamScore + ", HomeTeam=" + HomeTeam
                + ", HomeTeamScore=" + HomeTeamScore + ", Location=" + Location + ", RoundNumber=" + RoundNumber + "]";
    }
     
    
    
}
