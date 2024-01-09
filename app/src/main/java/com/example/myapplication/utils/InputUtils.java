package com.example.myapplication.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.myapplication.model.InputMatch;
import com.example.myapplication.model.Match;
import com.example.myapplication.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class InputUtils {


    public void checkExistingInputs(Context context, String matchID, String userID, int homeTeamGoals, int awayTeamGoals) {
        DatabaseReference inputMatchesRef = FirebaseDatabase.getInstance().getReference().child("inputMatches");

        //Checking for existing inputs for the same match
        inputMatchesRef.orderByChild("matchID").equalTo(matchID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean inputExists = false;

                for (DataSnapshot inputSnapshot : dataSnapshot.getChildren()) {
                    InputMatch existingInputMatch = inputSnapshot.getValue(InputMatch.class);

                    if (existingInputMatch != null && existingInputMatch.getUserID().equals(userID) && existingInputMatch.getMatchID().equals(matchID)) {
                        inputExists = true;
                        break;
                    }

                }

                if (inputExists) {
                    //Alert the users they cannot make anymore predictions for this specific game
                    Toast.makeText(context, "Deja ati introdus un scor pentru acest meci", Toast.LENGTH_SHORT).show();
                    Log.d("InputMatch", "There is already an input for this specific match");
                } else {
                    //Save the prediction and proceed to calculate their points
                    InputMatch inputMatch = new InputMatch(userID, matchID, homeTeamGoals, awayTeamGoals);

                    inputMatchesRef.push()
                            .setValue(inputMatch)
                            .addOnSuccessListener(aVoid -> {
                                Log.d("Firebase", "InputMatch added successfully");
                                // Using the matchID, we get all the data from that specific match
                                DatabaseReference matchesRef = FirebaseDatabase.getInstance().getReference().child("matches");

                                matchesRef.child(matchID).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            //The match was found
                                            Match match = snapshot.getValue(Match.class);

                                            if (match != null) {
                                                Log.d("Firebase", "Match details: " + match.toString());
                                                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                                Log.d("Firebase", "current User Authenticated: " + firebaseUser.getEmail());
                                                if (firebaseUser != null) {
                                                    String userID = firebaseUser.getUid();
                                                    DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");
                                                    Log.d("Firebase", "User Is not null, user id is: " + userID);

                                                    usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                                                try {
                                                                    User currentUser = userSnapshot.getValue(User.class);
                                                                    if (currentUser != null && currentUser.getMailAdress().equals(firebaseUser.getEmail())) {
                                                                        Log.d("Firebase", "User details: " + currentUser.toString());

                                                                        // Starting the points algorithm
                                                                        int points = 0;

                                                                        //correct result (win, draw, lose)
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

                                                                        // Update user points
                                                                        Toast.makeText(context, "Ați prezis: " + inputMatch.getHomeTeamGoals() + "-" + inputMatch.getAwayTeamGoals(), Toast.LENGTH_SHORT).show();
                                                                        if(points>=0){
                                                                            Toast.makeText(context, "Ati primit " + points +
                                                                                    " puncte. Scorul a fost: " + match.getHomeTeamGoals() + "-" + match.getAwayTeamGoals(), Toast.LENGTH_LONG).show();
                                                                        }else {
                                                                            Toast.makeText(context, "Ati pierdut " + points +
                                                                                    " puncte. Scorul a fost: " + match.getHomeTeamGoals() + "-" + match.getAwayTeamGoals(), Toast.LENGTH_LONG).show();
                                                                        }
                                                                        Log.d("InputMatch", "User collected :" + points + " points");
                                                                        userSnapshot.getRef().child("points").setValue(currentUser.getPoints() + points);
                                                                    }
                                                                } catch (Exception e) {
                                                                    Log.e("Firebase", "Error processing user data: " + e.getMessage());
                                                                }
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {
                                                            Log.e("Firebase", "Database error: " + error.getMessage());
                                                        }
                                                    });
                                                }

                                            } else {
                                                Log.e("Firebase", "Match details are null");
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(context, "Failed to retrieve registered inputs", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            })
                            .addOnFailureListener(e -> Log.e("Firebase", "Error adding input match: " + e.getMessage()));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
                Toast.makeText(context, "Failed to retrieve registered inputs", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void doubleInputs(Context context, String matchID, String userID, int homeTeamGoals, int awayTeamGoals) {
        DatabaseReference inputMatchesRef = FirebaseDatabase.getInstance().getReference().child("inputMatches");

        //Checking for existing inputs for the same match
        inputMatchesRef.orderByChild("matchID").equalTo(matchID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean inputExists = false;

                for (DataSnapshot inputSnapshot : dataSnapshot.getChildren()) {
                    InputMatch existingInputMatch = inputSnapshot.getValue(InputMatch.class);

                    if (existingInputMatch != null && existingInputMatch.getUserID().equals(userID) && existingInputMatch.getMatchID().equals(matchID)) {
                        inputExists = true;
                        break;
                    }

                }

                if (inputExists) {
                    //Alert the users they cannot make anymore predictions for this specific game
                    Toast.makeText(context, "Deja ati introdus un scor pentru acest meci", Toast.LENGTH_SHORT).show();
                    Log.d("InputMatch", "There is already an input for this specific match");
                } else {
                    //Save the prediction and proceed to calculate their points
                    InputMatch inputMatch = new InputMatch(userID, matchID, homeTeamGoals, awayTeamGoals);

                    inputMatchesRef.push()
                            .setValue(inputMatch)
                            .addOnSuccessListener(aVoid -> {
                                Log.d("Firebase", "InputMatch added successfully");
                                // Using the matchID, we get all the data from that specific match
                                DatabaseReference matchesRef = FirebaseDatabase.getInstance().getReference().child("matches");

                                matchesRef.child(matchID).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            //The match was found
                                            Match match = snapshot.getValue(Match.class);

                                            if (match != null) {
                                                Log.d("Firebase", "Match details: " + match.toString());
                                                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                                Log.d("Firebase", "current User Authenticated: " + firebaseUser.getEmail());
                                                if (firebaseUser != null) {
                                                    String userID = firebaseUser.getUid();
                                                    DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");
                                                    Log.d("Firebase", "User Is not null, user id is: " + userID);

                                                    usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                                                try {
                                                                    User currentUser = userSnapshot.getValue(User.class);
                                                                    if (currentUser != null && currentUser.getMailAdress().equals(firebaseUser.getEmail())) {
                                                                        Log.d("Firebase", "User details: " + currentUser.toString());

                                                                        // Starting the points algorithm
                                                                        int points = 0;

                                                                        //correct result (win, draw, lose)
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

                                                                        points = points * 2;

                                                                        // Update user points
                                                                        Toast.makeText(context, "Ați prezis: " + inputMatch.getHomeTeamGoals() + "-" + inputMatch.getAwayTeamGoals(), Toast.LENGTH_SHORT).show();
                                                                        if (points >= 0) {
                                                                            Toast.makeText(context, "(DUBLAJ) Ați primit " + points +
                                                                                    " puncte. Scorul a fost: " + match.getHomeTeamGoals() + "-" + match.getAwayTeamGoals(), Toast.LENGTH_LONG).show();
                                                                        } else {
                                                                            Toast.makeText(context, "(DUBLAJ) Ați pierdut " + points +
                                                                                    " puncte. Scorul a fost: " + match.getHomeTeamGoals() + "-" + match.getAwayTeamGoals(), Toast.LENGTH_LONG).show();
                                                                        }
                                                                        Log.d("InputMatch", "User collected :" + points + " points");
                                                                        userSnapshot.getRef().child("points").setValue(currentUser.getPoints() + points);
                                                                    }
                                                                } catch (Exception e) {
                                                                    Log.e("Firebase", "Error processing user data: " + e.getMessage());
                                                                }
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {
                                                            Log.e("Firebase", "Database error: " + error.getMessage());
                                                        }
                                                    });
                                                }

                                            } else {
                                                Log.e("Firebase", "Match details are null");
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(context, "Failed to retrieve registered inputs", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            })
                            .addOnFailureListener(e -> Log.e("Firebase", "Error adding input match: " + e.getMessage()));
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
