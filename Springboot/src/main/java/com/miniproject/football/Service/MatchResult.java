package com.miniproject.football.Service;
import java.io.StringReader;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.client.RestTemplate;

import com.miniproject.football.Model.User;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;


public class MatchResult {
    RestTemplate template = new RestTemplate();
    String URL = "https://fixturedownload.com/feed/json/epl-2022";
    
    public String getMatchResult(Model model, @ModelAttribute User userObject){
        ResponseEntity<String> responseEntity = template.getForEntity(URL, String.class);
        String payload = responseEntity.getBody();
        //get Body of Json
        StringReader stringReader = new StringReader(payload);
        JsonReader jsonReader = Json.createReader(stringReader);
        //read an JSON array from a string
        JsonArray jsonArray = jsonReader.readArray();       
        //line 25 to 28, build to Json object or array, in order to get whatever you want
        //System.out.println(jsonArray.get(3));
        System.out.println("MatchResult jsonArray size" + jsonArray.size());
        String all = "";
        
        try {
        for(int i = 0; i < jsonArray.size(); i++) {  
              
            // store each object in JSONObject  
            JsonObject obj = jsonArray.getJsonObject(i);
            //System.out.println(jsonArray.getJsonObject(0));
            if(userObject.getHomeTeam().equals(obj.getString("HomeTeam"))||userObject.getHomeTeam().equals(obj.getString("AwayTeam"))) {
            //if(userObject.getHomeTeam().equals(obj.getString("HomeTeam"))||userObject.getHomeTeam().equals(obj.getString("AwayTeam"))) {
                //String newObj = jsonArray.getJsonObject(i).get(obj).toString();
                Integer htScore = obj.getInt("HomeTeamScore");
                //String htScore = newObj("HomeTeamScore");
                if (htScore != null){
                String hm=String.valueOf(htScore);
                all += hm;}

                Integer atScore = obj.getInt("AwayTeamScore");
                if (atScore != null){
                String aw=String.valueOf(atScore);
                //System.out.println(s);
                all += aw;}
                // return s;
            }  
            // get field value from JSONObject using get() method  

        }  
                } catch (Exception e) {
                    
                }
        return all;
                    
    }

}
