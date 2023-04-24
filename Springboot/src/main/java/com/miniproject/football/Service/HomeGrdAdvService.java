package com.miniproject.football.Service;

import java.io.FileReader;
import java.util.List;

import org.springframework.stereotype.Service;
import au.com.bytecode.opencsv.CSVReader;


@Service
public class HomeGrdAdvService {

    public static double homeGroAdv(String homeTeam) {
        try {
                String csvFilePath = "./football.csv";
                CSVReader reader = new CSVReader(new FileReader(csvFilePath));
                List<String[]> rows = reader.readAll();

                double homeGoalScored = 0.0;
                double homeGoalConcede = 0.0;

                for (String[] row : rows) {
                    if (row[0].equals(homeTeam)) {
                        homeGoalScored += Double.parseDouble(row[2]);
                        homeGoalConcede += Double.parseDouble(row[3]);
                    }
                }

                double homeAdvantage = Math.min(1.3, (homeGoalScored / homeGoalConcede));
                System.out.println("Total home goals scored: " + homeGoalScored);
                System.out.println("Total home goals conceded: " + homeGoalConcede);

                return homeAdvantage;
            } catch (Exception e) {
                throw new RuntimeException("Error loading data", e);
            }
    }
}

