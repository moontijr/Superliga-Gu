package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.model.Match;
import com.example.myapplication.model.Team;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GW1Activity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private DatabaseReference gamesRef;

    private String usernameId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gw1);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("teams");
        addTeamsToDb();

        gamesRef = FirebaseDatabase.getInstance().getReference("games");


        // Retrieve and display games
        retrieveAndDisplayGames();
    }



    private void retrieveAndDisplayGames() {
        gamesRef.addValueEventListener(new ValueEventListener() {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Match> games = new ArrayList<>();

                for (DataSnapshot gameSnapshot : dataSnapshot.getChildren()) {
                    Match game = gameSnapshot.getValue(Match.class);
                    if (game != null) {
                        games.add(game);
                    }

                    List<User> userList = new ArrayList<>();
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        User user = userSnapshot.getValue(User.class);
                        userList.add(user);


                        if (currentUser != null && currentUser.getEmail().equals(user.getMailAdress())) {
                            // Store the points for the logged-in user
                            usernameId = user.getUsername();

                        }
                    }
                }

                double[] fcsbGoluri = getTeamGoalsScoredExpected(games,"T1");
                double[] fcsbGoluriLuate= getTeamGoalsConcededExpected(games,"T1");

                LinearRegression linearRegression = new LinearRegression(fcsbGoluri,fcsbGoluriLuate);
                double input = getNumberOfGamesForATeamFromGames(games,"T1"); // Some input value, such as the number of past games

                // Make predictions
                double predictedHomeTeamGoals = linearRegression.predictHomeTeamGoals(input);
                double predictedAwayTeamGoals = linearRegression.predictAwayTeamGoals(input);

                double[] cfrGoluri = getTeamGoalsScoredExpected(games,"T2");
                double[] cfrGoluriLuate= getTeamGoalsConcededExpected(games,"T2");

                LinearRegression linearRegression2 = new LinearRegression(cfrGoluri,cfrGoluriLuate);
                double input2 = getNumberOfGamesForATeamFromGames(games,"T2"); // Some input value, such as the number of past games

                // Make predictions
                double predictedHomeTeamGoals2 = linearRegression2.predictHomeTeamGoals(input2);
                double predictedAwayTeamGoals2 = linearRegression2.predictAwayTeamGoals(input2);

                // fcsb-cfr
                String goluriFcsbMarcate=FirebaseMatchUtils.calculateExpectedGoalsScored("T1",predictedHomeTeamGoals,games);
                String goluriFcsbPrimite=FirebaseMatchUtils.calculateExpectedGoalsConceded("T1",predictedAwayTeamGoals,games);
                String goluriCfrPrimite=FirebaseMatchUtils.calculateExpectedGoalsConceded("T2",predictedHomeTeamGoals2,games);
                String goluriCfrMarcate=FirebaseMatchUtils.calculateExpectedGoalsScored("T2",predictedAwayTeamGoals2,games);
                String goluriFcsbExpected=FirebaseMatchUtils.homeTeamGoalsExpectedFinal(goluriFcsbMarcate,goluriCfrPrimite);
                String goluriCfrExpected=FirebaseMatchUtils.awayTeamGoalsExpectedFinal(goluriCfrMarcate,goluriFcsbPrimite);
                EditText editTextFcsb = findViewById(R.id.leftTeamGoals1);
                editTextFcsb.setHint(goluriFcsbExpected);
                EditText editTextCfr = findViewById(R.id.rightTeamGoals1);
                editTextCfr.setHint(goluriCfrExpected);

                Button teamPlayersPopupButton = findViewById(R.id.moreButton1);

                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();


                teamPlayersPopupButton.setOnClickListener(v -> {
                    // Assuming you have Firebase Authentication set up
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                    if (currentUser != null) {
                        String username = currentUser.getEmail();
                        // If username is not available in the display name, you might have to retrieve it from your database

                        // Replace "team1" with the actual team ID you want to use
                        DetailsUtils.showPlayersPopupForTeams(GW1Activity.this, "T1", "T2", username, "M1");
                    } else {
                        // Handle the case when the user is not logged in
                        // You can show a login prompt or take appropriate action
                    }
                });



                //botosani-csu
                double[] botosaniGoluri = getTeamGoalsScoredExpected(games,"T10");
                double[] botosaniGoluriLuate= getTeamGoalsConcededExpected(games,"T10");

                LinearRegression linearRegressionBotosani = new LinearRegression(botosaniGoluri,botosaniGoluriLuate);
                double inputBotosani = getNumberOfGamesForATeamFromGames(games,"T10"); // Some input value, such as the number of past games

                // Make predictions
                double predictedHomeTeamGoalsBotosani = linearRegression.predictHomeTeamGoals(inputBotosani);
                double predictedAwayTeamGoalsBotosani = linearRegression.predictAwayTeamGoals(inputBotosani);

                double[] csuGoluri = getTeamGoalsScoredExpected(games,"T11");
                double[] csuGoluriLuate= getTeamGoalsConcededExpected(games,"T11");

                LinearRegression linearRegressionCsu = new LinearRegression(csuGoluri,csuGoluriLuate);
                double inputCsu = getNumberOfGamesForATeamFromGames(games,"T11"); // Some input value, such as the number of past games

                // Make predictions
                double predictedHomeTeamGoalsCsu = linearRegressionCsu.predictHomeTeamGoals(inputCsu);
                double predictedAwayTeamGoalsCsu = linearRegressionBotosani.predictAwayTeamGoals(inputCsu);

                // botosani-csu
                String goluriBotosaniMarcate=FirebaseMatchUtils.calculateExpectedGoalsScored("T10",predictedHomeTeamGoalsBotosani,games);
                String goluriBotosaniPrimite=FirebaseMatchUtils.calculateExpectedGoalsConceded("T10",predictedAwayTeamGoalsBotosani,games);
                String goluriCsuPrimite=FirebaseMatchUtils.calculateExpectedGoalsConceded("T11",predictedHomeTeamGoalsCsu,games);
                String goluriCsuMarcate=FirebaseMatchUtils.calculateExpectedGoalsScored("T11",predictedAwayTeamGoalsCsu,games);
                String goluriBotosaniExpected=FirebaseMatchUtils.homeTeamGoalsExpectedFinal(goluriBotosaniMarcate,goluriCsuPrimite);
                String goluriCsuExpected=FirebaseMatchUtils.awayTeamGoalsExpectedFinal(goluriCsuMarcate,goluriBotosaniPrimite);
                EditText editTextBotosani = findViewById(R.id.leftTeamGoals2);
                editTextBotosani.setHint(goluriBotosaniExpected);
                EditText editTextCsu = findViewById(R.id.rightTeamGoals2);
                editTextCsu.setHint(goluriCsuExpected);

                Button teamPlayersPopupButton2 = findViewById(R.id.moreButton2);


                teamPlayersPopupButton2.setOnClickListener(v -> {
                    // Assuming you have Firebase Authentication set up
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                    if (currentUser != null) {
                        String username = currentUser.getEmail();
                        // If username is not available in the display name, you might have to retrieve it from your database

                        // Replace "team1" with the actual team ID you want to use
                        DetailsUtils.showPlayersPopupForTeams(GW1Activity.this, "T10", "T11", username, "M20");
                    } else {
                        // Handle the case when the user is not logged in
                        // You can show a login prompt or take appropriate action
                    }
                });

                //dinamo-petrolul

                double[] dinamoGoluri = getTeamGoalsScoredExpected(games,"T3");
                double[] dinamoGoluriLuate= getTeamGoalsConcededExpected(games,"T3");

                LinearRegression linearRegressionDinamo = new LinearRegression(dinamoGoluri,dinamoGoluriLuate);
                double inputDinamo = getNumberOfGamesForATeamFromGames(games,"T13"); // Some input value, such as the number of past games

                // Make predictions
                double predictedHomeTeamGoalsDinamo = linearRegressionDinamo.predictHomeTeamGoals(inputDinamo);
                double predictedAwayTeamGoalsDinamo = linearRegressionDinamo.predictAwayTeamGoals(inputDinamo);

                double[] petrolulGoluri = getTeamGoalsScoredExpected(games,"T4");
                double[] petrolulGoluriLuate= getTeamGoalsConcededExpected(games,"T4");

                LinearRegression linearRegressionPetrolul = new LinearRegression(petrolulGoluri,petrolulGoluriLuate);
                double inputPetrolul = getNumberOfGamesForATeamFromGames(games,"T4"); // Some input value, such as the number of past games

                // Make predictions
                double predictedHomeTeamGoalsPetrolul = linearRegressionPetrolul.predictHomeTeamGoals(inputPetrolul);
                double predictedAwayTeamGoalsPetrolul = linearRegressionBotosani.predictAwayTeamGoals(inputPetrolul);

                // dinamo-petrolul
                String goluriDinamoMarcate=FirebaseMatchUtils.calculateExpectedGoalsScored("T3",predictedHomeTeamGoalsDinamo,games);
                String goluriDinamoPrimite=FirebaseMatchUtils.calculateExpectedGoalsConceded("T3",predictedAwayTeamGoalsDinamo,games);
                String goluriPetrolulPrimite=FirebaseMatchUtils.calculateExpectedGoalsConceded("T4",predictedHomeTeamGoalsPetrolul,games);
                String goluriPetrolulMarcate=FirebaseMatchUtils.calculateExpectedGoalsScored("T4",predictedAwayTeamGoalsPetrolul,games);
                String goluriDinamoExpected=FirebaseMatchUtils.homeTeamGoalsExpectedFinal(goluriDinamoMarcate,goluriPetrolulPrimite);
                String goluriPetrolulExpected=FirebaseMatchUtils.awayTeamGoalsExpectedFinal(goluriPetrolulMarcate,goluriDinamoPrimite);
                EditText editTextDinamo = findViewById(R.id.leftTeamGoals3);
                editTextDinamo.setHint(goluriDinamoExpected);
                EditText editTextPetrolul = findViewById(R.id.rightTeamGoals3);
                editTextPetrolul.setHint(goluriPetrolulExpected);

                Button teamPlayersPopupButton3 = findViewById(R.id.moreButton3);



                teamPlayersPopupButton3.setOnClickListener(v -> {
                    // Assuming you have Firebase Authentication set up
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                    if (currentUser != null) {
                        String username = currentUser.getEmail();
                        // If username is not available in the display name, you might have to retrieve it from your database

                        // Replace "team1" with the actual team ID you want to use
                        DetailsUtils.showPlayersPopupForTeams(GW1Activity.this, "T3", "T4", username, "M21");
                    } else {
                        // Handle the case when the user is not logged in
                        // You can show a login prompt or take appropriate action
                    }
                });




                //farul-sepsi
                String goluriFarulMarcate=FirebaseMatchUtils.calculateExpectedGoalsScored("T12",1.4,games);
                String goluriFarulPrimite=FirebaseMatchUtils.calculateExpectedGoalsConceded("T12 ",1.3,games);
                String goluriSepsiPrimite=FirebaseMatchUtils.calculateExpectedGoalsConceded("T13",3.2,games);
                String goluriSepsiMarcate=FirebaseMatchUtils.calculateExpectedGoalsScored("T13",1.4,games);
                String goluriFarulExpected=FirebaseMatchUtils.homeTeamGoalsExpectedFinal(goluriFarulMarcate,goluriSepsiPrimite);
                String goluriSepsiExpected=FirebaseMatchUtils.awayTeamGoalsExpectedFinal(goluriSepsiMarcate,goluriFarulPrimite);
                EditText editTextFarul = findViewById(R.id.leftTeamGoals4);
                editTextFarul.setHint(goluriFarulExpected);
                EditText editTextSepsi = findViewById(R.id.rightTeamGoals4);
                editTextSepsi.setHint(goluriSepsiExpected);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Error retrieving games: " + databaseError.getMessage());
            }
        });
    }







    private void addTeamsToDb() {
        Team FCSB = new Team("T1", "FCSB", "FCSB", "Romania");
        Team CFR = new Team("T2", "CFR Cluj", "CFR", "Romania");
        Team CSU = new Team("T11", "Universitatea Craiova", "CSU", "Romania");
        Team FCU = new Team("T14", "Stiinta Craiova", "FCU", "Romania");
        Team Otelul = new Team("T12", "Otelul Galati", "OTE", "Romania");
        Team UTA = new Team("T13", "Uta Arad", "UTA", "Romania");
        Team Hermannstadt = new Team("T7", "Hermannstadt", "HER", "Romania");
        Team Poli = new Team("T8", "Poli Iasi", "IAS", "Romania");
        Team Voluntari = new Team("T9", "Voluntari", "VOL", "Romania");
        Team Botosani = new Team("T10", "Botosani", "BOT", "Romania");
        Team Dinamo = new Team("T3", "Dinamo Bucuresti", "FCD", "Romania");
        Team Farul = new Team("T5", "Farul Constanta", "FAR", "Romania");
        Team Sepsi = new Team("T6", "Sepsi OSK", "OSK", "Romania");
        Team Petrolul = new Team("T4", "Petrolul Ploiesti", "PET", "Romania");
        Team UCluj = new Team("T15", "Universitatea Cluj", "UCJ", "Romania");
        Team Rapid = new Team("T16", "Rapid Bucuresti", "RAP", "Romania");

        addTeamToDatabase(FCSB);
        addTeamToDatabase(CSU);
        addTeamToDatabase(CFR);
        addTeamToDatabase(FCU);
        addTeamToDatabase(Voluntari);
        addTeamToDatabase(Dinamo);
        addTeamToDatabase(UTA);
        addTeamToDatabase(Farul);
        addTeamToDatabase(Petrolul);
        addTeamToDatabase(UCluj);
        addTeamToDatabase(Rapid);
        addTeamToDatabase(Sepsi);
        addTeamToDatabase(Otelul);
        addTeamToDatabase(Hermannstadt);
        addTeamToDatabase(Poli);
        addTeamToDatabase(Botosani);


    }



    private void addTeamToDatabase(Team team) {
        mDatabase.child(team.getId()).setValue(team)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firebase", "Team " + team.getName() + " added successfully");
                })
                .addOnFailureListener(e -> {
                    Log.e("Firebase", "Error adding " + team.getName() + ": " + e.getMessage());
                });
    }

    private static double[] getTeamGoalsScoredExpected(List<Match> matches, String teamId) {
        List<Double> expectedGoalsList = new ArrayList<>();

        for (Match match : matches) {
            // Check if the match involves the specified team as the home team
            if (match.getHomeTeamId().equals(teamId)) {
                expectedGoalsList.add((double) match.getHomeTeamGoals());
            }
            // You might want to add another condition to check if the team is the away team
            // and handle away team goals accordingly
        }

        // Convert the list to an array
        double[] expectedGoalsArray = new double[expectedGoalsList.size()];
        for (int i = 0; i < expectedGoalsList.size(); i++) {
            expectedGoalsArray[i] = expectedGoalsList.get(i);
        }

        return expectedGoalsArray;
    }

    private static double[] getTeamGoalsConcededExpected(List<Match> matches, String teamId) {
        List<Double> expectedGoalsList = new ArrayList<>();

        for (Match match : matches) {
            // Check if the match involves the specified team as the home team
            if (match.getHomeTeamId().equals(teamId)) {
                expectedGoalsList.add((double) match.getAwayTeamGoals());
            }
            // You might want to add another condition to check if the team is the away team
            // and handle away team goals accordingly
        }


        // Convert the list to an array
        double[] expectedGoalsArray = new double[expectedGoalsList.size()];
        for (int i = 0; i < expectedGoalsList.size(); i++) {
            expectedGoalsArray[i] = expectedGoalsList.get(i);
        }

        return expectedGoalsArray;
    }

    private static double getNumberOfGamesForATeamFromGames(List<Match> matches, String teamId){
        Double nrOfGames=0.0;
        for(Match match : matches){
            if(match.getHomeTeamId().equals(teamId)||match.getAwayTeamId().equals(teamId))
                nrOfGames=nrOfGames+=1;
        }
        return nrOfGames;

    }
}
