package com.miniproject.football.Service;

import java.util.Random;

import org.springframework.stereotype.Service;

@Service
public class LuckService {
    public static double luckFactor() {
        // Generate a random number to decide whether luck comes into play
        Random random = new Random();
        double luckRoll = random.nextDouble(10)+1;
        if (luckRoll >= 7 && luckRoll < 8) {
            // Generate a random multiplier number between 0.4 and 1.6 if luck comes into play
            double luckFactor = 0.4 + (1.6 - 0.4) * random.nextDouble();
            return luckFactor;
        } if (luckRoll >= 9.7) {
            // Generate a random multiplier number between 0.1 and 3.0 if luck comes into play
            double luckFactor = 0.1 + (3 - 0.1) * random.nextDouble();
            return luckFactor;
        }
        
        else {
            // Return 1 if luck does not come into play
            return 1.0;
        }
    }

    public static double luckFactor2() {
        // Generate a random number to decide whether luck comes into play
        Random random = new Random();
        double luckRoll = random.nextDouble(10)+1; // decimal enabled
        if (luckRoll >= 7 && luckRoll < 8) {
            // Generate a random multiplier number between 0.4 and 1.6 if luck comes into play
            double luckFactor2 = 0.4 + (1.6 - 0.4) * random.nextDouble();
            return luckFactor2;
        } if (luckRoll >= 9.7) {
            // Generate a random multiplier number between 0.1 and 3.0 if luck comes into play
            double luckFactor2 = 0.1 + (3 - 0.1) * random.nextDouble();
            return luckFactor2;
        } else {
            // Return 1 if luck does not come into play
            return 1.0;
        }
    }
}
