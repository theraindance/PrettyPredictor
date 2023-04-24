package com.miniproject.football.Service;

import java.io.StringReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;

public class TeamService {
    RestTemplate template = new RestTemplate();
    String URL = "https://fixturedownload.com/feed/json/epl-2022";

    public List<String> getFootballTeams(){
        ResponseEntity<String> responseEntity = template.getForEntity(URL, String.class);
        String payload = responseEntity.getBody();
        //get Body of Json
        StringReader stringReader = new StringReader(payload);
        JsonReader jsonReader = Json.createReader(stringReader);
        //read an JSON array from a string
        JsonArray jsonArray = jsonReader.readArray();       
        //line 25 to 28, build to Json object or array, in order to get whatever you want
        //System.out.println(jsonArray.get(3));
        Set<String> footBallSet = new HashSet<>();
        
        //empty set for the footballteam to populate in using the "for" loop
        //in jsonArray, for every v, loop through v.
        for (JsonValue jv : jsonArray){
            String homeTeam = jv.asJsonObject().getString("HomeTeam");
            footBallSet.add(homeTeam);
            //System.out.println("TeamService home team" + homeTeam);
        }
        System.out.println("TeamService match list size" +footBallSet.size());
        for (String s : footBallSet){
             //System.out.println(s);
             String homeTeamList = s;
        }

        
        List<String> footBallList = footBallSet.stream().toList();
        return footBallList;


    }
}