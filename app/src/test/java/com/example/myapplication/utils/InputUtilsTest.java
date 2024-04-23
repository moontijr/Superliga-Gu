package com.example.myapplication.utils;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

public class InputUtilsTest {

    @Test
    public void testAllCorrectPredictions() {
        int[] homeTeamGoals = {2, 1, 1};
        int[] awayTeamGoals = {1, 1, 2};
        int[] userHomeTeamGoals = {2, 1, 1};
        int[] userAwayTeamGoals = {1, 1, 2};
        int[] expectedPoints = {6, 6, 6};
        assertArrayEquals(expectedPoints, InputUtils.calculatePoints(homeTeamGoals, awayTeamGoals, userHomeTeamGoals, userAwayTeamGoals));
    }

    @Test
    public void testIncorrectPredictions() {
        int[] homeTeamGoals = {2, 1, 1};
        int[] awayTeamGoals = {1, 1, 2};
        int[] userHomeTeamGoals = {1, 1, 2};
        int[] userAwayTeamGoals = {1, 2, 1};
        int[] expectedPoints = {1, 1, -2};
        assertArrayEquals(expectedPoints, InputUtils.calculatePoints(homeTeamGoals, awayTeamGoals, userHomeTeamGoals, userAwayTeamGoals));
    }

    @Test
    public void testCorrectHomeGoals() {
        int[] homeTeamGoals = {2, 1, 1};
        int[] awayTeamGoals = {1, 1, 2};
        int[] userHomeTeamGoals = {2, 0, 1};
        int[] userAwayTeamGoals = {1, 1, 2};
        int[] expectedPoints = {6, 1, 6};
        assertArrayEquals(expectedPoints, InputUtils.calculatePoints(homeTeamGoals, awayTeamGoals, userHomeTeamGoals, userAwayTeamGoals));
    }

    @Test
    public void testCorrectAwayGoals() {
        int[] homeTeamGoals = {2, 1, 1};
        int[] awayTeamGoals = {1, 1, 2};
        int[] userHomeTeamGoals = {2, 1, 1};
        int[] userAwayTeamGoals = {1, 0, 2};
        int[] expectedPoints = {6, 1, 6};
        assertArrayEquals(expectedPoints, InputUtils.calculatePoints(homeTeamGoals, awayTeamGoals, userHomeTeamGoals, userAwayTeamGoals));
    }

    @Test
    public void testCorrectGoalDifference() {
        int[] homeTeamGoals = {2, 1, 1};
        int[] awayTeamGoals = {1, 1, 2};
        int[] userHomeTeamGoals = {3, 1, 1};
        int[] userAwayTeamGoals = {1, 1, 2};
        int[] expectedPoints = {4, 6, 6};
        assertArrayEquals(expectedPoints, InputUtils.calculatePoints(homeTeamGoals, awayTeamGoals, userHomeTeamGoals, userAwayTeamGoals));
    }

    @Test
    public void testMixedPredictions() {
        int[] homeTeamGoals = {2, 1, 1};
        int[] awayTeamGoals = {1, 1, 2};
        int[] userHomeTeamGoals = {3, 0, 1};
        int[] userAwayTeamGoals = {1, 2, 2};
        int[] expectedPoints = {4, 0, 6};
        assertArrayEquals(expectedPoints, InputUtils.calculatePoints(homeTeamGoals, awayTeamGoals, userHomeTeamGoals, userAwayTeamGoals));
    }
}
