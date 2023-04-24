package com.miniproject.football.Service;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class xGoalService {
    // Variables to store xG and xGA values for selected teams
    private double homeTeamXG = 0;
    private double homeTeamXGA = 0;
    private double awayTeamXG = 0;
    private double awayTeamXGA = 0;

    // Method to retrieve xG and xGA values for selected home and away teams
    public void xGoalServices(String homeTeam, String awayTeam) {
        // Call XGScraper class to retrieve team data
        List<XGScraper.TeamData> teamDataList = XGScraper.scrape();

        // Find xG and xGA values for selected teams
        for (XGScraper.TeamData teamData : teamDataList) {
            if (teamData.teamName.equalsIgnoreCase(homeTeam)) {
                homeTeamXG = teamData.xG;
                homeTeamXGA = teamData.xGA;
                System.out.println("Home team!!!!!!!!!!!!!!!!!!!!!! " + homeTeam + ": xG = " + homeTeamXG + ", xGA = " + homeTeamXGA);
            } else if (teamData.teamName.equalsIgnoreCase(awayTeam)) {
                awayTeamXG = teamData.xG;
                awayTeamXGA = teamData.xGA;
                System.out.println("Away team!!!!!!!!!!!! " + awayTeam + ": xG = " + awayTeamXG + ", xGA = " + awayTeamXGA);
            }
        }
    }

    // Getters for the variables
    public double getHomeTeamXG() {
        return homeTeamXG;
    }

    public double getHomeTeamXGA() {
        return homeTeamXGA;
    }

    public double getAwayTeamXG() {
        return awayTeamXG;
    }

    public double getAwayTeamXGA() {
        return awayTeamXGA;
    }

    // Method to calculate xG for home team, higher weight for homeXG
    public double getHomeTeamXGavg() {
        return (homeTeamXG + homeTeamXG +homeTeamXG - awayTeamXG + awayTeamXGA) / 3.8;
    }

    // Method to calculate xG for away team, higher weight for awayXG
    public double getAwayTeamXGavg() {
        return (awayTeamXG + awayTeamXG + awayTeamXG - homeTeamXG + homeTeamXGA) / 3.8;
    }
}

