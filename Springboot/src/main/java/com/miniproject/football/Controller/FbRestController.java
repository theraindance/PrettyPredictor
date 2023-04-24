package com.miniproject.football.Controller;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.ui.Model;
import com.miniproject.football.Model.User;
import com.miniproject.football.Model.Match;
import com.miniproject.football.Service.MatchTeam;
import com.miniproject.football.Service.PredictMachine;
import com.miniproject.football.Service.XGScraper;
import com.miniproject.football.RedisConfig.RedisService;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

@RestController
//@CrossOrigin(origins = "http://localhost:4200")
public class FbRestController {
        @Autowired
        RedisService rs;

        @GetMapping("/track/{username}/{email}")
        public ResponseEntity userId(@PathVariable String username, String email, String Hometeam){
            try {
                
                User userx = rs.get(username);
                JsonObjectBuilder builder = Json.createObjectBuilder();
                builder.add("Name:",userx.getName());
                builder.add("Email:",userx.getEmail());
                builder.add("HomeTeam:",userx.getHomeTeam());
                
                JsonObject body = builder.build();

                   return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(body.toString());
                
            } 
            
            catch (Exception e) {
                JsonObjectBuilder builder = Json.createObjectBuilder();
                builder.add("error","data not found");
                JsonObject body = builder.build();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body.toString());

            }
            
            
        }

        //@CrossOrigin(origins = "http://localhost:4200")
        @PostMapping("/tracking")
            public ResponseEntity<String> fbtrack(Model model, @RequestBody Map<String, String> request) {
                String username = request.get("name");
                String email = request.get("email");
                User userObject = new User();
                System.out.println(username + " right here right now");

                System.out.println("Creating new user: " + username);
                model.addAttribute("user", new User());
                model.addAttribute("HomeTeam", userObject.getHomeTeam());
                model.addAttribute("name", username);
                model.addAttribute("email", email);

                MatchTeam selectMT = new MatchTeam();
                String selectTeam = selectMT.getTeamName(model, userObject);
                String favoriteTeam = userObject.getHomeTeam();
                System.out.println("Fav team: " + favoriteTeam);

                List<Match> matches = Match.matches;
                // System.out.println("Matches size: " + matches.size());

                List<Match> favoriteMatches = new ArrayList();
                for (Match match : matches) {
                    if (match.getHomeTeam().equals(favoriteTeam) || match.getAwayTeam().equals(favoriteTeam)) {
                        favoriteMatches.add(match);
                    }
                }
                System.out.println("Fav Matches size: " + favoriteMatches.size());

                model.addAttribute("matches", favoriteMatches);
                System.out.println("SAVING user: " + username);
                System.out.println("SAVING email: " + email);
                
                userObject.setName(username);
                userObject.setEmail(email);
                rs.save(userObject);
                List<User> users = rs.getAllUsers();
                for (User user : users) {
                    System.out.println("Username: " + user.getName() + ", Email: " + user.getEmail());
                }
                
                JsonObjectBuilder builder = Json.createObjectBuilder();
                builder.add("Name:",username);
                
                JsonObject body = builder.build();
                System.out.println(body +" this is json");
                return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(body.toString());
            

                //return "predictscore";
            }


           
        @GetMapping("/teams")
            public List<String> getTeams() {
                    List<XGScraper.TeamData> teamDataList = XGScraper.scrape();
                    List<String> teams = new ArrayList<>();
                    for (XGScraper.TeamData teamData : teamDataList) {
                        teams.add(teamData.getTeamName());
                    }

                    return teams;
                }

        @GetMapping("/teams2")
            public List<String> getTeams2() {
                List<XGScraper.TeamData> teamDataList = XGScraper.scrape();
                List<String> teamNames = new ArrayList<>();
                for (XGScraper.TeamData teamData : teamDataList) {
                    teamNames.add(teamData.teamName);
                }
                
                for (String team : teamNames) {
                    System.out.println(team);
                }
                return teamNames;
            }
        
        //@CrossOrigin(origins = "http://localhost:4200")
        @GetMapping("/username")
            public ResponseEntity<JsonObject> getUserData() {
                JsonObjectBuilder builder = Json.createObjectBuilder();
                builder.add("Name", "username"); // Replace "username" with the actual username value
                
                JsonObject body = builder.build();
                System.out.println(body + " this is json2222222222222222");

                return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(body);
            }

            String homeTeamName = "";
            String awayTeamName = "";

            //@CrossOrigin(origins = "http://localhost:4200")            
            @PostMapping("/result")
            public ResponseEntity<?> predict(@RequestBody Map<String, String> body) throws IOException, InterruptedException {
                String homeTeamName = body.get("homeTeam");
                String awayTeamName = body.get("awayTeam");
                String homeTeam = body.get("homeTeam");
                String awayTeam = body.get("awayTeam");
                System.out.println(homeTeam + " they were here " + awayTeam);
                PredictMachine predictMachine = new PredictMachine();
                int[] scores = predictMachine.predict(homeTeam,awayTeam);
                int homeScore = scores[0];
                int awayScore = scores[1];
                Map<String, Integer> result = new HashMap<>();
                result.put("homeScore", homeScore);
                result.put("awayScore", awayScore);
                return ResponseEntity.ok(result);
            }
            

            //@CrossOrigin(origins = "http://localhost:4200")
            @GetMapping("/getteams")
            public ResponseEntity<?> getTeamsName() {
                Map<String, String> teams = new HashMap<>();
                teams.put("homeTeam", homeTeamName);
                teams.put("awayTeam", awayTeamName);
                System.out.println(homeTeamName +" here are the team names " + awayTeamName);
                return ResponseEntity.ok(teams);
}

                

    
}
