package com.example.myapplication;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.myapplication.model.InputMatch;
import com.example.myapplication.model.Match;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class InputUtils {

    public void calculatePoints(Context context, String matchID, InputMatch inputMatch) {
        // Using the matchID, we get all the data from that specific match
        DatabaseReference matchesRef = FirebaseDatabase.getInstance().getReference().child("matches");

        matchesRef.child(matchID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    //The match was found
                    Match match = snapshot.getValue(Match.class);

                    //Searching for the user associated with Firebase Auth
                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    if(firebaseUser != null){
                        String emaill = firebaseUser.getEmail();
                        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");
                        usersRef.child(emaill).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    //The user was found
                                    User currentUser = snapshot.getValue(User.class);

                                    //Starting the points algorithm
                                    int points = 0;

                                    //correct result (win, draw, lose)
                                    assert match != null;
                                    if (match.getHomeTeamGoals() > match.getAwayTeamGoals() && inputMatch.getHomeTeamGoals() > inputMatch.getAwayTeamGoals() ||
                                            match.getHomeTeamGoals() == match.getAwayTeamGoals() && inputMatch.getHomeTeamGoals() == inputMatch.getAwayTeamGoals() ||
                                            match.getHomeTeamGoals() < match.getAwayTeamGoals() && inputMatch.getHomeTeamGoals() < inputMatch.getAwayTeamGoals()) {
                                        points += 3;
                                    }
                                    //correct # of goals scored by the home team
                                    if (match.getHomeTeamGoals() == inputMatch.getHomeTeamGoals()) {
                                        points += 1;
                                    }
                                    //correct # of goals scored by the away team
                                    if (match.getAwayTeamGoals() == inputMatch.getAwayTeamGoals()) {
                                        points += 1;
                                    }
                                    if (match.getHomeTeamGoals() - match.getAwayTeamGoals() == inputMatch.getHomeTeamGoals() - inputMatch.getAwayTeamGoals()) {
                                        points += 1;
                                    }
                                    //opposite result
                                    if (match.getHomeTeamGoals() > match.getAwayTeamGoals() && inputMatch.getHomeTeamGoals() < inputMatch.getAwayTeamGoals() ||
                                            match.getHomeTeamGoals() < match.getAwayTeamGoals() && inputMatch.getHomeTeamGoals() > inputMatch.getAwayTeamGoals()) {
                                        points -= 2;
                                    }

                                    assert currentUser != null;
                                    currentUser.setPoints(currentUser.getPoints() + points);
                                    inputMatch.setPointsCollected(points);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Failed to retrieve registered inputs", Toast.LENGTH_SHORT).show();
            }
        });
        
    }

    public void checkExistingInputs(Context context, String matchID, String userID, int homeTeamGoals, int awayTeamGoals) {
        DatabaseReference inputMatchesRef = FirebaseDatabase.getInstance().getReference().child("inputMatches");

        //Checking for existing inputs for the same match
        inputMatchesRef.orderByChild("matchID").equalTo(matchID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean inputExists = false;

                for (DataSnapshot inputSnapshot : dataSnapshot.getChildren()) {
                    if (inputSnapshot.child("matchID").getValue(String.class).equals(matchID)) {
                        inputExists = true;

                    }
                }

                if(inputExists) {
                    //Alert the users they cannot make anymore predictions for this specific game
                    Toast.makeText(context, "Deja ati introdus un scor pentru acest meci", Toast.LENGTH_SHORT).show();
                }else{
                    //Save the prediction and proceed to calculate their points
                    InputMatch newInputMatch = new InputMatch(userID, matchID, homeTeamGoals, awayTeamGoals);
                    inputMatchesRef.push()
                            .setValue(newInputMatch)
                            .addOnSuccessListener(aVoid -> {
                                Log.d("Firebase", "InputMatch added successfully");
                            })
                            .addOnFailureListener(e -> Log.e("Firebase", "Error adding input match: " + e.getMessage()));

                    calculatePoints(context,matchID,newInputMatch);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
                Toast.makeText(context, "Failed to retrieve registered inputs", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
