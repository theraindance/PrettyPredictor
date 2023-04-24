package com.miniproject.football.Controller;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.miniproject.football.Model.Match;
import com.miniproject.football.Model.User;
import com.miniproject.football.RedisConfig.RedisService;
import com.miniproject.football.Service.MatchTeam;
import com.miniproject.football.Service.PredictMachine;
import com.miniproject.football.Service.TeamService;
import com.miniproject.football.Service.XGScraper;

@Controller
public class FbTrackerController {
    @Autowired
    RedisService rs;    
    
    @Autowired
    private static XGScraper scraper;
    
    @GetMapping("/home")
    public static String home(Model model) {
        List<XGScraper.TeamData> teamDataList = XGScraper.scrape();
        model.addAttribute("teamDataList", teamDataList);
        return "home";
    }

    // @GetMapping("/")
    // public String homePage() {
    //     return "home";
    // }


    
    @PostMapping("/results")
    public String resultPage(@RequestParam String homeTeam, @RequestParam String awayTeam, Model model) throws IOException, InterruptedException {
        PredictMachine predictMachine = new PredictMachine();
        int[] scores = predictMachine.predict(homeTeam,awayTeam);
        //int[] scores = new int[]{0, 0};
        int homeScore = scores[0];
        int awayScore = scores[1];
        model.addAttribute("homeTeam", homeTeam);
        model.addAttribute("awayTeam", awayTeam);
        model.addAttribute("homeScore", homeScore);
        model.addAttribute("awayScore", awayScore);
        return "results";
    }

    @GetMapping("/fbfav")
    public String goeshome(Model model, @ModelAttribute User user){

        //ScoreService scoService = new ScoreService();
        //List<User> homeTeamScoreList = scoService.getHomeScore();
        //System.out.println("this is from hometeam score service " + homeTeamScoreList);
        
        TeamService service = new TeamService();
        //System.out.println(service);
        List<String> footBallList = service.getFootballTeams();
        //System.out.println(footBallList);
        
        //the 3 lines of code below call the MatchResult service and returns the location/football result)
        // MatchResult matchResult = new MatchResult();
        // String location = matchResult.getFbMatchResult();
        // System.out.println(location);
 
        model.addAttribute("teamlisting", footBallList);
        return "fbfavteam";
        
    }

    
    
    @PostMapping("/track")
    public String fbtrack(Model model, @ModelAttribute User userObject){
        String username = userObject.getName();
        System.out.println("Creating new user: " + username);
        model.addAttribute("user",new User());
        model.addAttribute("HomeTeam", userObject.getHomeTeam());
        model.addAttribute("name", userObject.getName());
        model.addAttribute("email", userObject.getEmail());
	
	    MatchTeam selectMT = new MatchTeam();
        String selectTeam = selectMT.getTeamName(model, userObject);
        String favoriteTeam = userObject.getHomeTeam();
        System.out.println("Fav team: " + favoriteTeam);
        List<Match> matches = Match.matches;
        System.out.println("Matches size: " + matches.size());
        List<Match> favoriteMatches = new ArrayList();
        for (Match match : matches){
            if (match.getHomeTeam().equals(favoriteTeam) || match.getAwayTeam().equals(favoriteTeam)){
                favoriteMatches.add(match);
            }
        }
        System.out.println("Fav Matches size: " + favoriteMatches.size());
        model.addAttribute("matches", favoriteMatches);
        System.out.println("SAVING user: " + userObject.getName());
	    rs.save(userObject);
        return "trackmyteam";
        }

    @PostMapping("/track2")
        public String track2(Model model, @ModelAttribute User userObject){

            User usernewobj = rs.checkuserexist(userObject.getName(), userObject.getEmail());
            
            if(usernewobj == null)
            {   
               
                return "erroruser";
           }

                        model.addAttribute("user",new User());
                        model.addAttribute("HomeTeam", usernewobj.getHomeTeam());
                        model.addAttribute("name", usernewobj.getName());
                        model.addAttribute("email", usernewobj.getEmail());
            
                    System.out.println("Retrieved user: " + usernewobj.getName()) ;
                    MatchTeam selectMT = new MatchTeam();
                    String selectTeam = selectMT.getTeamName(model, usernewobj);
                    String favoriteTeam = usernewobj.getHomeTeam();
                    System.out.println("Fav team: " + favoriteTeam);
                    List<Match> matches = Match.matches;
                    System.out.println("Matches size: " + matches.size());
                    List<Match> favoriteMatches = new ArrayList();
                    for (Match match : matches){
                        if (match.getHomeTeam().equals(favoriteTeam) || match.getAwayTeam().equals(favoriteTeam)){
                            favoriteMatches.add(match);
                        }
                    }
                    model.addAttribute("matches", favoriteMatches);
                
                    return "trackmyteam";
        }

    @GetMapping("/signinform")
    public String signin(Model model, @ModelAttribute User User){
        return "signinform";
    }

    @GetMapping("/userlists")
    public String listofuser(Model model, @ModelAttribute User userObject){

        ArrayList<User> Users = rs.getAllUsers();
        ArrayList<String> UsersEmail = rs.getAllEmail();
        ArrayList<String> UsersTeam = rs.getAllTeam();

        model.addAttribute("userList",Users);
        model.addAttribute("emailList",UsersEmail);
        model.addAttribute("teamList",UsersTeam);
        
        return "userlists";
    }
}