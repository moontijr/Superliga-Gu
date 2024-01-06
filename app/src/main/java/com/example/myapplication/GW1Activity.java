package com.example.myapplication;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.model.Match;
import com.example.myapplication.model.Matchday;
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

    private DatabaseReference matchDatabase;

    private InputUtils inputUtils;


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
        gamesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Match> games = new ArrayList<>();

                for (DataSnapshot gameSnapshot : dataSnapshot.getChildren()) {
                    Match game = gameSnapshot.getValue(Match.class);
                    if (game != null) {
                        games.add(game);
                    }
                }

                // fcsb-cfr
                String goluriFcsbMarcate = FirebaseMatchUtils.calculateExpectedGoalsScored("T1", 2.3, games);
                String goluriFcsbPrimite = FirebaseMatchUtils.calculateExpectedGoalsConceded("T1", 1.7, games);
                String goluriCfrPrimite = FirebaseMatchUtils.calculateExpectedGoalsConceded("T2", 1.3, games);
                String goluriCfrMarcate = FirebaseMatchUtils.calculateExpectedGoalsScored("T2", 1.9, games);
                String goluriFcsbExpected = FirebaseMatchUtils.homeTeamGoalsExpectedFinal(goluriFcsbMarcate, goluriCfrPrimite);
                String goluriCfrExpected = FirebaseMatchUtils.awayTeamGoalsExpectedFinal(goluriCfrMarcate, goluriFcsbPrimite);
                EditText editTextFcsb = findViewById(R.id.leftTeamGoals1);
                editTextFcsb.setHint(goluriFcsbExpected);
                EditText editTextCfr = findViewById(R.id.rightTeamGoals1);
                editTextCfr.setHint(goluriCfrExpected);
                //botosani-csu
                String goluriBotosaniMarcate = FirebaseMatchUtils.calculateExpectedGoalsScored("T10", 0.7, games);
                String goluriBotosaniPrimite = FirebaseMatchUtils.calculateExpectedGoalsConceded("T10 ", 3.2, games);
                String goluriCsuPrimite = FirebaseMatchUtils.calculateExpectedGoalsConceded("T3", 0.9, games);
                String goluriCsuMarcate = FirebaseMatchUtils.calculateExpectedGoalsScored("T3", 2.8, games);
                String goluriBotosaniExpected = FirebaseMatchUtils.homeTeamGoalsExpectedFinal(goluriBotosaniMarcate, goluriCsuPrimite);
                String goluriCsuExpected = FirebaseMatchUtils.awayTeamGoalsExpectedFinal(goluriCsuMarcate, goluriBotosaniPrimite);
                EditText editTextBotosani = findViewById(R.id.leftTeamGoals2);
                editTextBotosani.setHint(goluriBotosaniExpected);
                EditText editTextCsu = findViewById(R.id.rightTeamGoals2);
                editTextCsu.setHint(goluriCsuExpected);
                //dinamo-petrolul
                String goluriDinamoMarcate = FirebaseMatchUtils.calculateExpectedGoalsScored("T11", 0.4, games);
                String goluriDinamoPrimite = FirebaseMatchUtils.calculateExpectedGoalsConceded("T11 ", 2.8, games);
                String goluriPetrolulPrimite = FirebaseMatchUtils.calculateExpectedGoalsConceded("T14", 1.1, games);
                String goluriPetrolulMarcate = FirebaseMatchUtils.calculateExpectedGoalsScored("T14", 1.0, games);
                String goluriDinamoExpected = FirebaseMatchUtils.homeTeamGoalsExpectedFinal(goluriDinamoMarcate, goluriPetrolulPrimite);
                String goluriPetrolulExpected = FirebaseMatchUtils.awayTeamGoalsExpectedFinal(goluriPetrolulMarcate, goluriDinamoPrimite);
                EditText editTextDinamo = findViewById(R.id.leftTeamGoals3);
                editTextDinamo.setHint(goluriDinamoExpected);
                EditText editTextPetrolul = findViewById(R.id.rightTeamGoals3);
                editTextPetrolul.setHint(goluriPetrolulExpected);
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
        Team CSU = new Team("T3", "Universitatea Craiova", "CSU", "Romania");
        Team FCU = new Team("T4", "Stiinta Craiova", "FCU", "Romania");
        Team Otelul = new Team("T5", "Otelul Galati", "OTE", "Romania");
        Team UTA = new Team("T6", "Uta Arad", "UTA", "Romania");
        Team Hermannstadt = new Team("T7", "Hermannstadt", "HER", "Romania");
        Team Poli = new Team("T8", "Poli Iasi", "IAS", "Romania");
        Team Voluntari = new Team("T9", "Voluntari", "VOL", "Romania");
        Team Botosani = new Team("T10", "Botosani", "BOT", "Romania");
        Team Dinamo = new Team("T11", "Dinamo Bucuresti", "FCD", "Romania");
        Team Farul = new Team("T12", "Farul Constanta", "FAR", "Romania");
        Team Sepsi = new Team("T13", "Sepsi OSK", "OSK", "Romania");
        Team Petrolul = new Team("T14", "Petrolul Ploiesti", "PET", "Romania");
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


    public void confirmPredictionForFcsbCfr(View view) {
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
            }
            if (awayGoals == -1) {
                editTextCfr.setError("Enter a number of goals for the away team");
            }
            return;
        }

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            if (firebaseUser != null) {
                String userId = firebaseUser.getUid();
                inputUtils.checkExistingInputs(GW1Activity.this, fcsbCfr.getId(), userId, homeGoals, awayGoals);
            }


//        homeGoals = Integer.parseInt(editTextFcsb.getText().toString());
//        awayGoals = Integer.parseInt(editTextCfr.getText().toString());
//
//        //Checking if the user entered a number for both teams
//        if (homeGoals == -1 && awayGoals == -1) {
//            editTextFcsb.setError("Enter a number of goals for the home team");
//            editTextCfr.setError("Enter a number of goals for the away team");
//            return;
//        } else if (homeGoals == -1) {
//            editTextFcsb.setError("Enter a number of goals for the home team");
//        } else if (awayGoals == -1) {
//            editTextCfr.setError("Enter a number of goals for the away team");
//        } else {
//            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//            if (firebaseUser != null) {
//                String userId = firebaseUser.getUid();
//                inputUtils.checkExistingInputs(GW1Activity.this, fcsbCfr.getId(), userId, homeGoals, awayGoals);
//            }
//        }
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
