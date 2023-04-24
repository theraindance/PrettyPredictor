package com.miniproject.football.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
public class XGScraper {
    public static List<TeamData> scrape() {
        try {
            String url = "https://footystats.org/england/premier-league/xg";
            Document doc = Jsoup.connect(url).get();
            List<TeamData> teamDataList = new ArrayList<>();

            // Extract element data for each team           
            Elements xGEle = doc.select("td.green");
            Elements xGAEle = doc.select("td.ga");
            Elements xTeamEle = doc.select("td.team.bold.hover-modal-parent.detailed-stats-team-name-size.mobify-fixed > a");

            ArrayList<String> clubNames = new ArrayList<>();

            // select elements inside xTeamEle, filter the h3 tag for team name, add to clubNames
            Elements elements = xTeamEle.select("*");
            
                for (Element element : elements) {
                    if (element.tagName().equals("h3")) {
                        String text = element.text().trim();
                        if (!text.isEmpty()) {
                            text = text.replace("AFC", "").replace("FC", "").trim();
                            clubNames.add(text);
                        }
                    }
                }

                // use clubNames, get corresponding xG and xGA, add all 3 elememts to teamData
                for (int i = 0; i < clubNames.size(); i++) {
                    String club = clubNames.get(i);
                    Element xGElem = xGEle.get(i);
                    Element xGAElem = xGAEle.get(i);
                    double xG = Double.parseDouble(xGElem.text().trim());
                    double xGA = Double.parseDouble(xGAElem.text().trim());
                    teamDataList.add(new TeamData(club, xG, xGA));
                }

                //iterates through teamData and print out all 3 elements
                // for (TeamData teamData : teamDataList) {
                //     System.out.println(teamData.teamName + " " + teamData.xG + " " + teamData.xGA);
                // }
           
            return teamDataList;

        } catch (IOException e) {
            // print stack trace, return empty list
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // define data structure to store extracted data
        public static class TeamData {
            public String teamName;
            double xG;
            double xGA;
    
        public TeamData(String teamName, double xG, double xGA) {
            this.teamName = teamName;
            this.xG = xG;
            this.xGA = xGA;
        }

        public String getTeamName() {
            this.teamName = teamName;
            return teamName;
        }

        public void setTeamName(String teamName) {
            this.teamName = teamName;
        }

        public double getxG() {
            return xG;
        }

        public void setxG(double xG) {
            this.xG = xG;
        }

        public double getxGA() {
            return xGA;
        }

        public void setxGA(double xGA) {
            this.xGA = xGA;
        }
    }
}



