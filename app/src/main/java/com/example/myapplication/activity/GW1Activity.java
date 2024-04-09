package com.example.myapplication.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.statistics.LinearRegression;
import com.example.myapplication.R;
import com.example.myapplication.model.Match;
import com.example.myapplication.model.Team;
import com.example.myapplication.model.User;
import com.example.myapplication.utils.DetailsUtils;
import com.example.myapplication.utils.FirebaseMatchUtils;
import com.example.myapplication.utils.InputUtils;
import com.example.myapplication.utils.ToastUtils;
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
    private DatabaseReference matchDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gw1);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("teams");
        addTeamsToDb();

        gamesRef = FirebaseDatabase.getInstance().getReference("games");

        matchDatabase = FirebaseDatabase.getInstance().getReference("matches");


        // Retrieve and display games
        retrieveAndDisplayGames();
    }


    private void retrieveAndDisplayGames() {
        gamesRef.addListenerForSingleValueEvent(new ValueEventListener() {
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
                            usernameId = user.getUsername();

                        }
                    }
                }

                double[] fcsbGoluri = getTeamGoalsScoredExpected(games, "T1");
                double[] fcsbGoluriLuate = getTeamGoalsConcededExpected(games, "T1");

                LinearRegression linearRegression = new LinearRegression(fcsbGoluri, fcsbGoluriLuate);
                double input = getNumberOfGamesForATeamFromGames(games, "T1"); // Some input value, such as the number of past games

                double predictedHomeTeamGoals = linearRegression.predictHomeTeamGoals(input);
                double predictedAwayTeamGoals = linearRegression.predictAwayTeamGoals(input);

                double[] cfrGoluri = getTeamGoalsScoredExpected(games, "T2");
                double[] cfrGoluriLuate = getTeamGoalsConcededExpected(games, "T2");

                LinearRegression linearRegression2 = new LinearRegression(cfrGoluri, cfrGoluriLuate);
                double input2 = getNumberOfGamesForATeamFromGames(games, "T2"); // Some input value, such as the number of past games

                double predictedHomeTeamGoals2 = linearRegression2.predictHomeTeamGoals(input2);
                double predictedAwayTeamGoals2 = linearRegression2.predictAwayTeamGoals(input2);

                // fcsb-cfr
                String goluriFcsbMarcate = FirebaseMatchUtils.calculateExpectedGoalsScored("T1", predictedHomeTeamGoals, games);
                String goluriFcsbPrimite = FirebaseMatchUtils.calculateExpectedGoalsConceded("T1", predictedAwayTeamGoals, games);
                String goluriCfrPrimite = FirebaseMatchUtils.calculateExpectedGoalsConceded("T2", predictedHomeTeamGoals2, games);
                String goluriCfrMarcate = FirebaseMatchUtils.calculateExpectedGoalsScored("T2", predictedAwayTeamGoals2, games);
                String goluriFcsbExpected = FirebaseMatchUtils.homeTeamGoalsExpectedFinal(goluriFcsbMarcate, goluriCfrPrimite);
                String goluriCfrExpected = FirebaseMatchUtils.awayTeamGoalsExpectedFinal(goluriCfrMarcate, goluriFcsbPrimite);
                EditText editTextFcsb = findViewById(R.id.leftTeamGoals1);
                editTextFcsb.setHint(goluriFcsbExpected);
                EditText editTextCfr = findViewById(R.id.rightTeamGoals1);
                editTextCfr.setHint(goluriCfrExpected);

                Button teamPlayersPopupButton = findViewById(R.id.moreButton1);

                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

                Button potgwPopupButton = findViewById(R.id.potgwButton);

                potgwPopupButton.setOnClickListener(v -> {
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                    if (currentUser != null) {
                        String username = currentUser.getEmail();

                        DetailsUtils.showAllPlayersPopup(GW1Activity.this, username, "Demo1");
                    } else {
                    }
                });


//                Button teamPlayersPopupButton = findViewById(R.id.moreButton1);


                teamPlayersPopupButton.setOnClickListener(v -> {
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                    if (currentUser != null) {
                        String username = currentUser.getEmail();

                        DetailsUtils.showPlayersPopupForTeams(GW1Activity.this, "T1", "T2", username, "M1");
                    } else {
                    }
                });


                //botosani-csu
                double[] botosaniGoluri = getTeamGoalsScoredExpected(games, "T10");
                double[] botosaniGoluriLuate = getTeamGoalsConcededExpected(games, "T10");

                LinearRegression linearRegressionBotosani = new LinearRegression(botosaniGoluri, botosaniGoluriLuate);
                double inputBotosani = getNumberOfGamesForATeamFromGames(games, "T10"); // Some input value, such as the number of past games

                double predictedHomeTeamGoalsBotosani = linearRegression.predictHomeTeamGoals(inputBotosani);
                double predictedAwayTeamGoalsBotosani = linearRegression.predictAwayTeamGoals(inputBotosani);

                double[] csuGoluri = getTeamGoalsScoredExpected(games, "T11");
                double[] csuGoluriLuate = getTeamGoalsConcededExpected(games, "T11");

                LinearRegression linearRegressionCsu = new LinearRegression(csuGoluri, csuGoluriLuate);
                double inputCsu = getNumberOfGamesForATeamFromGames(games, "T11"); // Some input value, such as the number of past games

                double predictedHomeTeamGoalsCsu = linearRegressionCsu.predictHomeTeamGoals(inputCsu);
                double predictedAwayTeamGoalsCsu = linearRegressionBotosani.predictAwayTeamGoals(inputCsu);

                String goluriBotosaniMarcate = FirebaseMatchUtils.calculateExpectedGoalsScored("T10", predictedHomeTeamGoalsBotosani, games);
                String goluriBotosaniPrimite = FirebaseMatchUtils.calculateExpectedGoalsConceded("T10", predictedAwayTeamGoalsBotosani, games);
                String goluriCsuPrimite = FirebaseMatchUtils.calculateExpectedGoalsConceded("T11", predictedHomeTeamGoalsCsu, games);
                String goluriCsuMarcate = FirebaseMatchUtils.calculateExpectedGoalsScored("T11", predictedAwayTeamGoalsCsu, games);
                String goluriBotosaniExpected = FirebaseMatchUtils.homeTeamGoalsExpectedFinal(goluriBotosaniMarcate, goluriCsuPrimite);
                String goluriCsuExpected = FirebaseMatchUtils.awayTeamGoalsExpectedFinal(goluriCsuMarcate, goluriBotosaniPrimite);
                EditText editTextBotosani = findViewById(R.id.leftTeamGoals2);
                editTextBotosani.setHint(goluriBotosaniExpected);
                EditText editTextCsu = findViewById(R.id.rightTeamGoals2);
                editTextCsu.setHint(goluriCsuExpected);

                Button teamPlayersPopupButton2 = findViewById(R.id.moreButton2);


                teamPlayersPopupButton2.setOnClickListener(v -> {
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                    if (currentUser != null) {
                        String username = currentUser.getEmail();

                        DetailsUtils.showPlayersPopupForTeams(GW1Activity.this, "T10", "T11", username, "M20");
                    } else {
                    }
                });

                //dinamo-petrolul

                double[] dinamoGoluri = getTeamGoalsScoredExpected(games, "T3");
                double[] dinamoGoluriLuate = getTeamGoalsConcededExpected(games, "T3");

                LinearRegression linearRegressionDinamo = new LinearRegression(dinamoGoluri, dinamoGoluriLuate);
                double inputDinamo = getNumberOfGamesForATeamFromGames(games, "T13"); // Some input value, such as the number of past games

                double predictedHomeTeamGoalsDinamo = linearRegressionDinamo.predictHomeTeamGoals(inputDinamo);
                double predictedAwayTeamGoalsDinamo = linearRegressionDinamo.predictAwayTeamGoals(inputDinamo);

                double[] petrolulGoluri = getTeamGoalsScoredExpected(games, "T4");
                double[] petrolulGoluriLuate = getTeamGoalsConcededExpected(games, "T4");

                LinearRegression linearRegressionPetrolul = new LinearRegression(petrolulGoluri, petrolulGoluriLuate);
                double inputPetrolul = getNumberOfGamesForATeamFromGames(games, "T4"); // Some input value, such as the number of past games

                double predictedHomeTeamGoalsPetrolul = linearRegressionPetrolul.predictHomeTeamGoals(inputPetrolul);
                double predictedAwayTeamGoalsPetrolul = linearRegressionBotosani.predictAwayTeamGoals(inputPetrolul);

                // dinamo-petrolul
                String goluriDinamoMarcate = FirebaseMatchUtils.calculateExpectedGoalsScored("T3", predictedHomeTeamGoalsDinamo, games);
                String goluriDinamoPrimite = FirebaseMatchUtils.calculateExpectedGoalsConceded("T3", predictedAwayTeamGoalsDinamo, games);
                String goluriPetrolulPrimite = FirebaseMatchUtils.calculateExpectedGoalsConceded("T4", predictedHomeTeamGoalsPetrolul, games);
                String goluriPetrolulMarcate = FirebaseMatchUtils.calculateExpectedGoalsScored("T4", predictedAwayTeamGoalsPetrolul, games);
                String goluriDinamoExpected = FirebaseMatchUtils.homeTeamGoalsExpectedFinal(goluriDinamoMarcate, goluriPetrolulPrimite);
                String goluriPetrolulExpected = FirebaseMatchUtils.awayTeamGoalsExpectedFinal(goluriPetrolulMarcate, goluriDinamoPrimite);
                EditText editTextDinamo = findViewById(R.id.leftTeamGoals3);
                editTextDinamo.setHint(goluriDinamoExpected);
                EditText editTextPetrolul = findViewById(R.id.rightTeamGoals3);
                editTextPetrolul.setHint(goluriPetrolulExpected);

                Button teamPlayersPopupButton3 = findViewById(R.id.moreButton3);


                teamPlayersPopupButton3.setOnClickListener(v -> {
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                    if (currentUser != null) {
                        String username = currentUser.getEmail();

                        DetailsUtils.showPlayersPopupForTeams(GW1Activity.this, "T3", "T4", username, "M21");
                    } else {
                    }
                });


                //farul-sepsi
                String goluriFarulMarcate = FirebaseMatchUtils.calculateExpectedGoalsScored("T12", 1.4, games);
                String goluriFarulPrimite = FirebaseMatchUtils.calculateExpectedGoalsConceded("T12 ", 1.3, games);
                String goluriSepsiPrimite = FirebaseMatchUtils.calculateExpectedGoalsConceded("T13", 3.2, games);
                String goluriSepsiMarcate = FirebaseMatchUtils.calculateExpectedGoalsScored("T13", 1.4, games);
                String goluriFarulExpected = FirebaseMatchUtils.homeTeamGoalsExpectedFinal(goluriFarulMarcate, goluriSepsiPrimite);
                String goluriSepsiExpected = FirebaseMatchUtils.awayTeamGoalsExpectedFinal(goluriSepsiMarcate, goluriFarulPrimite);
                EditText editTextFarul = findViewById(R.id.leftTeamGoals4);
                editTextFarul.setHint(goluriFarulExpected);
                EditText editTextSepsi = findViewById(R.id.rightTeamGoals4);
                editTextSepsi.setHint(goluriSepsiExpected);

                Button teamPlayersPopupButton4 = findViewById(R.id.moreButton4);

                teamPlayersPopupButton4.setOnClickListener(v -> {
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                    if (currentUser != null) {
                        String username = currentUser.getEmail();

                        DetailsUtils.showPlayersPopupForTeams(GW1Activity.this, "T5", "T6", username, "M22");
                    } else {
                    }
                });


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
            if (match.getHomeTeamId().equals(teamId)) {
                expectedGoalsList.add((double) match.getHomeTeamGoals());
            }
        }

        double[] expectedGoalsArray = new double[expectedGoalsList.size()];
        for (int i = 0; i < expectedGoalsList.size(); i++) {
            expectedGoalsArray[i] = expectedGoalsList.get(i);
        }

        return expectedGoalsArray;
    }

    private static double[] getTeamGoalsConcededExpected(List<Match> matches, String teamId) {
        List<Double> expectedGoalsList = new ArrayList<>();

        for (Match match : matches) {
            if (match.getHomeTeamId().equals(teamId)) {
                expectedGoalsList.add((double) match.getAwayTeamGoals());
            }
        }


        double[] expectedGoalsArray = new double[expectedGoalsList.size()];
        for (int i = 0; i < expectedGoalsList.size(); i++) {
            expectedGoalsArray[i] = expectedGoalsList.get(i);
        }

        return expectedGoalsArray;
    }

    private static double getNumberOfGamesForATeamFromGames(List<Match> matches, String teamId) {
        Double nrOfGames = 0.0;
        for (Match match : matches) {
            if (match.getHomeTeamId().equals(teamId) || match.getAwayTeamId().equals(teamId))
                nrOfGames = nrOfGames += 1;
        }
        return nrOfGames;

    }


    public void confirmPredictionForFcsbCfr(View view, Context context) {
        //test match
        Match fcsbCfr = new Match("MSteCfr", "T1", "T2", 1, 1, "Demo1");
        addMatchToDatabase(fcsbCfr);

        EditText editTextFcsb = findViewById(R.id.leftTeamGoals1);
        EditText editTextCfr = findViewById(R.id.rightTeamGoals1);

        int homeGoals = -1; //convention
        int awayGoals = -1; //convention

        try {
            homeGoals = Integer.parseInt(editTextFcsb.getText().toString());
            awayGoals = Integer.parseInt(editTextCfr.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return;
        }

        //Checking if the user entered a number for both teams
        if (homeGoals == -1 || awayGoals == -1) {
            if (homeGoals == -1) {
                editTextFcsb.setError("Enter a number of goals for the home team");
                ToastUtils.showToast("Nu ati introdus niciun numar pentru echipa gazda!",context);
            }
            if (awayGoals == -1) {
                ToastUtils.showToast("Nu ati introdus niciun numar pentru echipa oaspete!",context);
                editTextCfr.setError("Enter a number of goals for the away team");
            }
            return;
        }

        //Checking if the user entered a number too big
        if (homeGoals >= 99 || awayGoals >= 99) {
            ToastUtils.showToast("Nu puteti introduce un scor asa mare!", context);
        }

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            String userId = firebaseUser.getUid();
            InputUtils inputUtils = new InputUtils();
            inputUtils.checkExistingInputs(GW1Activity.this, fcsbCfr.getId(), userId, homeGoals, awayGoals);
        }
    }

    public void confirmDoubleForFcsbCfr(View view, Context context) {
        //test match
        Match fcsbCfr = new Match("MSteCfr", "T1", "T2", 1, 1, "Demo1");
        addMatchToDatabase(fcsbCfr);

        EditText editTextFcsb = findViewById(R.id.leftTeamGoals1);
        EditText editTextCfr = findViewById(R.id.rightTeamGoals1);


        int homeGoals = -1; //convention
        int awayGoals = -1; //convention

        try {
            homeGoals = Integer.parseInt(editTextFcsb.getText().toString());
            awayGoals = Integer.parseInt(editTextCfr.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return;
        }

        //Checking if the user entered a number for both teams
        if (homeGoals == -1 || awayGoals == -1) {
            if (homeGoals == -1) {
                editTextFcsb.setError("Enter a number of goals for the home team");
                ToastUtils.showToast("Nu ati introdus niciun numar pentru echipa gazda!",context);
            }
            if (awayGoals == -1) {
                editTextCfr.setError("Enter a number of goals for the away team");
                ToastUtils.showToast("Nu ati introdus niciun numar pentru echipa oaspete!",context);
            }
            return;
        }

        //Checking if the user entered a number too big
        if (homeGoals >= 99 || awayGoals >= 99) {
            ToastUtils.showToast("Nu puteti introduce un scor asa mare!", context);
        }

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            String userId = firebaseUser.getUid();
            InputUtils inputUtils = new InputUtils();
            inputUtils.doubleInputs(GW1Activity.this, fcsbCfr.getMatchdayId(), fcsbCfr.getId(), userId, homeGoals, awayGoals);
        }

    }

    public void confirmPredictionForBotosaniCsu(View view, Context context) {
        //test match
        Match botCsu = new Match("MBotCsu", "T10", "T11", 1, 3, "Demo1");
        addMatchToDatabase(botCsu);

        EditText editTextBot = findViewById(R.id.leftTeamGoals2);
        EditText editTextCsu = findViewById(R.id.rightTeamGoals2);


        int homeGoals = -1; //convention
        int awayGoals = -1; //convention

        try {
            homeGoals = Integer.parseInt(editTextBot.getText().toString());
            awayGoals = Integer.parseInt(editTextCsu.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return;
        }

        //Checking if the user entered a number for both teams
        if (homeGoals == -1 || awayGoals == -1) {
            if (homeGoals == -1) {
                ToastUtils.showToast("Nu ati introdus niciun numar pentru echipa gazda!",context);
                editTextBot.setError("Enter a number of goals for the home team");
            }
            if (awayGoals == -1) {
                ToastUtils.showToast("Nu ati introdus niciun numar pentru echipa oaspete!",context);
                editTextCsu.setError("Enter a number of goals for the away team");
            }
            return;
        }

        //Checking if the user entered a number too big
        if (homeGoals >= 99 || awayGoals >= 99) {
            ToastUtils.showToast("Nu puteti introduce un scor asa mare!", context);
        }

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            String userId = firebaseUser.getUid();
            InputUtils inputUtils = new InputUtils();
            inputUtils.checkExistingInputs(GW1Activity.this, botCsu.getId(), userId, homeGoals, awayGoals);
        }
    }

    public void confirmDoubleForBotCsu(View view, Context context) {
        //test match
        Match botCsu = new Match("MBotCsu", "T10", "T11", 1, 3, "Demo1");
        addMatchToDatabase(botCsu);

        EditText editTextBot = findViewById(R.id.leftTeamGoals2);
        EditText editTextCsu = findViewById(R.id.rightTeamGoals2);


        int homeGoals = -1; //convention
        int awayGoals = -1; //convention

        try {
            homeGoals = Integer.parseInt(editTextBot.getText().toString());
            awayGoals = Integer.parseInt(editTextCsu.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return;
        }

        //Checking if the user entered a number for both teams
        if (homeGoals == -1 || awayGoals == -1) {
            if (homeGoals == -1) {
                editTextBot.setError("Enter a number of goals for the home team");
                ToastUtils.showToast("Nu ati introdus niciun numar pentru echipa gazda!",context);
            }
            if (awayGoals == -1) {
                editTextCsu.setError("Enter a number of goals for the away team");
                ToastUtils.showToast("Nu ati introdus niciun numar pentru echipa oaspete!",context);
            }
            return;
        }

        //Checking if the user entered a number too big
        if (homeGoals >= 99 || awayGoals >= 99) {
            ToastUtils.showToast("Nu puteti introduce un scor asa mare!", context);
        }

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            String userId = firebaseUser.getUid();
            InputUtils inputUtils = new InputUtils();
            inputUtils.doubleInputs(GW1Activity.this, botCsu.getMatchdayId(), botCsu.getId(), userId, homeGoals, awayGoals);
        }
    }

    public void confirmPredictionForDinPet(View view, Context context) {
        //test match
        Match dinPet = new Match("MDinPet", "T3", "T4", 1, 2, "Demo1");
        addMatchToDatabase(dinPet);

        EditText editTextDin = findViewById(R.id.leftTeamGoals3);
        EditText editTextPet = findViewById(R.id.rightTeamGoals3);


        int homeGoals = -1; //convention
        int awayGoals = -1; //convention

        try {
            homeGoals = Integer.parseInt(editTextDin.getText().toString());
            awayGoals = Integer.parseInt(editTextPet.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return;
        }

        //Checking if the user entered a number for both teams
        if (homeGoals == -1 || awayGoals == -1) {
            if (homeGoals == -1) {
                //editTextDin.setError("Enter a number of goals for the home team");
                ToastUtils.showToast("Nu ati introdus niciun numar pentru echipa gazda!",context);
            }
            if (awayGoals == -1) {
                //editTextPet.setError("Enter a number of goals for the away team");
                ToastUtils.showToast("Nu ati introdus niciun numar pentru echipa oaspete!",context);
            }
            return;
        }

        //Checking if the user entered a number too big
        if (homeGoals >= 99 || awayGoals >= 99) {
            ToastUtils.showToast("Nu puteti introduce un scor asa mare!", context);
        }

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            String userId = firebaseUser.getUid();
            InputUtils inputUtils = new InputUtils();
            inputUtils.checkExistingInputs(GW1Activity.this, dinPet.getId(), userId, homeGoals, awayGoals);
        }
    }

    public void confirmDoubleForDinPet(View view, Context context) {
        //test match
        Match dinPet = new Match("MDinPet", "T3", "T4", 1, 2, "Demo1");
        addMatchToDatabase(dinPet);

        EditText editTextDin = findViewById(R.id.leftTeamGoals3);
        EditText editTextPet = findViewById(R.id.rightTeamGoals3);


        int homeGoals = -1; //convention
        int awayGoals = -1; //convention

        try {
            homeGoals = Integer.parseInt(editTextDin.getText().toString());
            awayGoals = Integer.parseInt(editTextPet.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return;
        }

        //Checking if the user entered a number for both teams
        if (homeGoals == -1 || awayGoals == -1) {
            if (homeGoals == -1) {
                editTextDin.setError("Enter a number of goals for the home team");
                ToastUtils.showToast("Nu ati introdus niciun numar pentru echipa gazda!",context);
            }
            if (awayGoals == -1) {
                editTextPet.setError("Enter a number of goals for the away team");
                ToastUtils.showToast("Nu ati introdus niciun numar pentru echipa oaspete!",context);
            }
            return;
        }

        //Checking if the user entered a number too big
        if (homeGoals >= 99 || awayGoals >= 99) {
            ToastUtils.showToast("Nu puteti introduce un scor asa mare!", context);
        }

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            String userId = firebaseUser.getUid();
            InputUtils inputUtils = new InputUtils();
            inputUtils.doubleInputs(GW1Activity.this, dinPet.getMatchdayId(),dinPet.getId(), userId, homeGoals, awayGoals);
        }
    }

    public void confirmPredictionForFarOsk(View view, Context context) {
        //test match
        Match farOsk = new Match("MFarOsk", "T10", "T11", 3, 0, "Demo1");
        addMatchToDatabase(farOsk);

        EditText editTextFar = findViewById(R.id.leftTeamGoals4);
        EditText editTextOsk = findViewById(R.id.rightTeamGoals4);


        int homeGoals = -1; //convention
        int awayGoals = -1; //convention

        try {
            homeGoals = Integer.parseInt(editTextFar.getText().toString());
            awayGoals = Integer.parseInt(editTextOsk.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return;
        }

        //Checking if the user entered a number for both teams
        if (homeGoals == -1 || awayGoals == -1) {
            if (homeGoals == -1) {
                editTextFar.setError("Enter a number of goals for the home team");
                ToastUtils.showToast("Nu ati introdus niciun numar pentru echipa gazda!",context);
            }
            if (awayGoals == -1) {
                editTextOsk.setError("Enter a number of goals for the away team");
                ToastUtils.showToast("Nu ati introdus niciun numar pentru echipa oaspete!",context);
            }
            return;
        }

        //Checking if the user entered a number too big
        if (homeGoals >= 99 || awayGoals >= 99) {
            ToastUtils.showToast("Nu puteti introduce un scor asa mare!", context);
        }

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            String userId = firebaseUser.getUid();
            InputUtils inputUtils = new InputUtils();
            inputUtils.checkExistingInputs(GW1Activity.this, farOsk.getId(), userId, homeGoals, awayGoals);
        }
    }

    public void confirmDoubleForFarOsk(View view, Context context) {
        //test match
        Match farOsk = new Match("MFarOsk", "T10", "T11", 3, 0, "Demo1");
        addMatchToDatabase(farOsk);

        EditText editTextFar = findViewById(R.id.leftTeamGoals4);
        EditText editTextOsk = findViewById(R.id.rightTeamGoals4);


        int homeGoals = -1; //convention
        int awayGoals = -1; //convention

        try {
            homeGoals = Integer.parseInt(editTextFar.getText().toString());
            awayGoals = Integer.parseInt(editTextOsk.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return;
        }

        //Checking if the user entered a number for both teams
        if (homeGoals == -1 || awayGoals == -1) {
            if (homeGoals == -1) {
                editTextFar.setError("Enter a number of goals for the home team");
                ToastUtils.showToast("Nu ati introdus niciun numar pentru echipa gazda!",context);
            }
            if (awayGoals == -1) {
                editTextOsk.setError("Enter a number of goals for the away team");
                ToastUtils.showToast("Nu ati introdus niciun numar pentru echipa oaspete!",context);
            }
            return;
        }

        //Checking if the user entered a number too big
        if (homeGoals >= 99 || awayGoals >= 99) {
            ToastUtils.showToast("Nu puteti introduce un scor asa mare!", context);
        }

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            String userId = firebaseUser.getUid();
            InputUtils inputUtils = new InputUtils();
            inputUtils.doubleInputs(GW1Activity.this, farOsk.getMatchdayId(),farOsk.getId(), userId, homeGoals, awayGoals);
        }
    }

    private void addMatchToDatabase(Match match) {
        matchDatabase.child(match.getId()).setValue(match)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firebase", "Match " + match + " added successfully");
                })
                .addOnFailureListener(e -> {
                    Log.e("Firebase", "Error adding " + match + ": " + e.getMessage());
                });
    }
}
