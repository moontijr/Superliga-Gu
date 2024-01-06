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


        thetaHomeIntercept = 0;
        thetaHomeCoefficient = 0;
        thetaAwayIntercept = 0;
        thetaAwayCoefficient = 0;
        trainModel(homeTeamGoals, awayTeamGoals);
    }

    private void trainModel(double[] homeTeamGoals, double[] awayTeamGoals) {
        int m = homeTeamGoals.length;
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
            List<Match> homeTeamMatches = filterMatchesByHomeTeam(matches, homeTeamId);
            return calculateAverageGoals(homeTeamMatches,homeTeamId);
        }

    public static double calculateAverageGoalsForTeamConceded(List<Match> matches, String homeTeamId) {
        List<Match> homeTeamMatches = filterMatchesByHomeTeam(matches, homeTeamId);
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
        double averageGoalsScored = calculateAverageGoalsForTeam(allMatches, teamId);

        double combinedGoalsScored = 0.6 * averageGoalsScored + 0.4 * expectedGoalsScored;

        return String.format("%d", Math.round(combinedGoalsScored));
    }

    public static String calculateExpectedGoalsConceded(String teamId, double expectedGoalsConceded, List<Match> allMatches) {
        double averageGoalsConceded = calculateAverageGoalsForTeamConceded(allMatches, teamId);

        double combinedGoalsConceded = 0.6 * averageGoalsConceded + 0.4 * expectedGoalsConceded;

        return String.format("%d", Math.round(combinedGoalsConceded));
    }

    public static String homeTeamGoalsExpectedFinal(String goalsScored, String goalsConceded) {
        double goalsScoredDouble = Double.parseDouble(goalsScored);
        double goalsConcededDouble = Double.parseDouble(goalsConceded);
        double averageGoals = (goalsScoredDouble + goalsConcededDouble) / 2.0;
        int roundedAverageGoals = (int) Math.round(averageGoals);
        return String.valueOf(roundedAverageGoals);
    }

    public static String awayTeamGoalsExpectedFinal(String goalsScored, String goalsConceded) {
        double goalsScoredDouble = Double.parseDouble(goalsScored);
        double goalsConcededDouble = Double.parseDouble(goalsConceded);

        double averageGoals = (goalsScoredDouble + goalsConcededDouble) / 2.0;

        int roundedAverageGoals = (int) Math.round(averageGoals);

        return String.valueOf(roundedAverageGoals);
    }

}