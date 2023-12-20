package com.example.myapplication;

import com.example.myapplication.model.Match;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class FirebaseMatchUtils {

    private double thetaHomeIntercept;
    private double thetaHomeCoefficient;
    private double thetaAwayIntercept;
    private double thetaAwayCoefficient;
    private double learningRate = 0.01;
    private int maxIterations = 1000;

    public FirebaseMatchUtils(double[] homeTeamGoals, double[] awayTeamGoals) {
        if (homeTeamGoals.length != awayTeamGoals.length || homeTeamGoals.length == 0) {
            throw new IllegalArgumentException("Invalid input data.");
        }

        // Initialize coefficients
        thetaHomeIntercept = 0;
        thetaHomeCoefficient = 0;
        thetaAwayIntercept = 0;
        thetaAwayCoefficient = 0;

        // Train the model
        trainModel(homeTeamGoals, awayTeamGoals);
    }

    private void trainModel(double[] homeTeamGoals, double[] awayTeamGoals) {
        int m = homeTeamGoals.length; // Number of training examples

        for (int iteration = 0; iteration < maxIterations; iteration++) {
            double homeTeamErrorSum = 0;
            double awayTeamErrorSum = 0;

            for (int i = 0; i < m; i++) {
                double homeTeamPrediction = predictHomeTeamGoals(i);
                double awayTeamPrediction = predictAwayTeamGoals(i);

                double homeTeamError = homeTeamPrediction - homeTeamGoals[i];
                double awayTeamError = awayTeamPrediction - awayTeamGoals[i];

                homeTeamErrorSum += homeTeamError;
                awayTeamErrorSum += awayTeamError;
            }

            thetaHomeIntercept -= learningRate * (1.0 / m) * homeTeamErrorSum;
            thetaAwayIntercept -= learningRate * (1.0 / m) * awayTeamErrorSum;

            for (int i = 0; i < m; i++) {
                double homeTeamPrediction = predictHomeTeamGoals(i);
                double awayTeamPrediction = predictAwayTeamGoals(i);

                double homeTeamError = homeTeamPrediction - homeTeamGoals[i];
                double awayTeamError = awayTeamPrediction - awayTeamGoals[i];

                thetaHomeCoefficient -= learningRate * (1.0 / m) * homeTeamError * i;
                thetaAwayCoefficient -= learningRate * (1.0 / m) * awayTeamError * i;
            }
        }
    }

    public double predictHomeTeamGoals(double input) {
        return thetaHomeIntercept + thetaHomeCoefficient * input;
    }

    public double predictAwayTeamGoals(double input) {
        return thetaAwayIntercept + thetaAwayCoefficient * input;
    }

    public static double calculateAverageGoalsForTeam(List<Match> matches, String homeTeamId) {
            // Filter matches by home team ID
            List<Match> homeTeamMatches = filterMatchesByHomeTeam(matches, homeTeamId);

            // Calculate average goals scored per match
            return calculateAverageGoals(homeTeamMatches,homeTeamId);


        }

    public static double calculateAverageGoalsForTeamConceded(List<Match> matches, String homeTeamId) {
        // Filter matches by home team ID
        List<Match> homeTeamMatches = filterMatchesByHomeTeam(matches, homeTeamId);

        // Calculate average goals scored per match
        return calculateAverageGoalsConceded(homeTeamMatches,homeTeamId);
    }

    public static List<Match> filterMatchesByHomeTeam(List<Match> allMatches, String homeTeamId) {
        List<Match> homeTeamMatches = new ArrayList<>();

        for (Match match : allMatches) {
            if (homeTeamId.equals(match.getHomeTeamId())||homeTeamId.equals(match.getAwayTeamId())) {
                homeTeamMatches.add(match);
            }
        }

        return homeTeamMatches;
    }

    public static double calculateAverageGoals(List<Match> matches, String teamId) {
        if (matches.isEmpty()) {
            return 0.0;
        }

        int totalGoals = 0;
        for (Match match : matches) {
            if(match.getHomeTeamId().equals(teamId))
            totalGoals += match.getHomeTeamGoals();
            else
                if(match.getAwayTeamId().equals(teamId))
                    totalGoals+=match.getAwayTeamGoals();
        }

        return (double) totalGoals / matches.size();
    }

    public static double calculateAverageGoalsConceded(List<Match> matches,String teamId) {
        if (matches.isEmpty()) {
            return 0.0;
        }

        int totalGoals = 0;
        for (Match match : matches) {
            if(match.getHomeTeamId().equals(teamId))
                totalGoals += match.getAwayTeamGoals();
            else
            if(match.getAwayTeamId().equals(teamId))
                totalGoals+=match.getHomeTeamGoals();
        }

        return (double) totalGoals / matches.size();
    }

    public static String calculateExpectedGoalsScored(String teamId, double expectedGoalsScored, List<Match> allMatches) {
        // Calculate the average goals scored
        double averageGoalsScored = calculateAverageGoalsForTeam(allMatches, teamId);

        // Combine with the expected goals, with weights of 60% for average goals and 40% for expected goals
        double combinedGoalsScored = 0.6 * averageGoalsScored + 0.4 * expectedGoalsScored;

        // Round to the nearest integer and format as a string without trailing .0
        return String.format("%d", Math.round(combinedGoalsScored));
    }

    public static String calculateExpectedGoalsConceded(String teamId, double expectedGoalsConceded, List<Match> allMatches) {
        // Calculate the average goals conceded
        double averageGoalsConceded = calculateAverageGoalsForTeamConceded(allMatches, teamId);

        // Combine with the expected goals, with weights of 60% for average goals and 40% for expected goals
        double combinedGoalsConceded = 0.6 * averageGoalsConceded + 0.4 * expectedGoalsConceded;

        // Round to the nearest integer and format as a string without trailing .0
        return String.format("%d", Math.round(combinedGoalsConceded));
    }

    public static String homeTeamGoalsExpectedFinal(String goalsScored, String goalsConceded) {
        // Convert strings to doubles
        double goalsScoredDouble = Double.parseDouble(goalsScored);
        double goalsConcededDouble = Double.parseDouble(goalsConceded);

        // Calculate the average of goals scored and conceded
        double averageGoals = (goalsScoredDouble + goalsConcededDouble) / 2.0;

        // Round to the nearest integer
        int roundedAverageGoals = (int) Math.round(averageGoals);

        // Convert the rounded result to a string
        return String.valueOf(roundedAverageGoals);
    }

    public static String awayTeamGoalsExpectedFinal(String goalsScored, String goalsConceded) {
        // Convert strings to doubles
        double goalsScoredDouble = Double.parseDouble(goalsScored);
        double goalsConcededDouble = Double.parseDouble(goalsConceded);

        // Calculate the average of goals scored and conceded
        double averageGoals = (goalsScoredDouble + goalsConcededDouble) / 2.0;

        // Round to the nearest integer
        int roundedAverageGoals = (int) Math.round(averageGoals);

        // Convert the rounded result to a string
        return String.valueOf(roundedAverageGoals);
    }

}