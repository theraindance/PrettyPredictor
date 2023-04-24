package com.miniproject.football.Service;
import java.io.StringReader;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.client.RestTemplate;

import com.miniproject.football.Model.Match;
import com.miniproject.football.Model.User;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;


public class MatchTeam {
    RestTemplate template = new RestTemplate();
    String URL = "https://fixturedownload.com/feed/json/epl-2022";
    
    public String getTeamName(Model model, @ModelAttribute User userObject){
        ResponseEntity<String> responseEntity = template.getForEntity(URL, String.class);
        String payload = responseEntity.getBody();
        //get Body of Json
        StringReader stringReader = new StringReader(payload);
        JsonReader jsonReader = Json.createReader(stringReader);
        //read an JSON array from a string
        JsonArray jsonArray = jsonReader.readArray();
        // System.out.println(jsonArray);      
        //line 25 to 28, build to Json object or array, in order to get whatever you want
        //System.out.println(jsonArray.get(3));
        System.out.println(jsonArray.size());
        // String hello = userObject.getHomeTeam();
        // System.out.println();
        String all = "";


        if (Match.matches.isEmpty()){
            for (int j = 0; j < jsonArray.size(); j++){
                JsonObject obj = jsonArray.getJsonObject(j);
                Match match = new Match();
                match.setHomeTeam(obj.getString("HomeTeam"));
                match.setAwayTeam(obj.getString("AwayTeam"));
                try{
                    if (obj.get("HomeTeamScore")==null){
                        // System.out.println("Home team score is null");
                        }
                    else{
                        Integer homeTeamScore = (Integer) obj.getInt("HomeTeamScore");
                        match.setHomeTeamScore(homeTeamScore.toString());
                        Integer awayTeamScore = (Integer) obj.getInt("AwayTeamScore");
                        match.setAwayTeamScore(awayTeamScore.toString());
                        }
                    }
                catch (Exception e){
                    //if HomeTeamScore is null, dont add the match, just continue to search for more
                    //matche where score is not null
                    continue;
                }
                match.setLocation(obj.getString("Location"));
                match.setRoundNumber(obj.getInt("RoundNumber"));
                Match.matches.add(match);
                System.out.println("Match + " + j + " Info: " + match.toString());
            }
            System.out.println("FINISHED CALLING API");
            System.out.println("MATCH SIZE: " + Match.matches.size());
        }
        return all;
                    
    }

}
