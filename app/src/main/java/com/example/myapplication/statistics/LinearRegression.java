package com.example.myapplication.statistics;

public class LinearRegression {

    private double thetaHomeIntercept;
    private double thetaHomeCoefficient;
    private double thetaAwayIntercept;
    private double thetaAwayCoefficient;
    private double learningRate = 0.01;
    private int maxIterations = 1000;

    public LinearRegression(double[] homeTeamGoals, double[] awayTeamGoals) {

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
}