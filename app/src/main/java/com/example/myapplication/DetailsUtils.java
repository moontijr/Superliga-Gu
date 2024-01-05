package com.example.myapplication;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.myapplication.model.Match;
import com.example.myapplication.model.Player;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DetailsUtils {

    public static void showPlayersPopupForTeams(Context context, String teamId1, String teamId2, String loggedInPlayerId, String gameId) {
        // Create a dialog for the players popup
        Dialog playersPopupDialog = new Dialog(context);
        playersPopupDialog.setContentView(R.layout.game1popup);

        // Find views in the popup layout
        Spinner playerSpinner = playersPopupDialog.findViewById(R.id.playerSpinner);
        AutoCompleteTextView playerNameAutoComplete = playersPopupDialog.findViewById(R.id.playerNameAutoComplete);
        Button submitPlayerButton = playersPopupDialog.findViewById(R.id.submitPlayerButton);

        // Populate the Spinner with the list of players
        List<Player> playerList = getPlayerListForTeams(context, teamId1, teamId2);
        PlayerSpinnerAdapter spinnerAdapter = new PlayerSpinnerAdapter(context, playerList);
        playerSpinner.setAdapter(spinnerAdapter);

        // Set up the click listener for the submit button
        submitPlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the selected player from the Spinner or the custom input
                Player selectedPlayer;
                if (playerSpinner.getSelectedItem() != null) {
                    selectedPlayer = (Player) playerSpinner.getSelectedItem();
                } else {
                    String customPlayerName = playerNameAutoComplete.getText().toString().trim();
                    // Find the player with the entered name in the player list
                    selectedPlayer = findPlayerByName(playerList, customPlayerName);
                }

                if (selectedPlayer != null) {
                    // Add POTM info to the database
                    addPOTMInfo(loggedInPlayerId, selectedPlayer.getId(), gameId);

                    // Dismiss the popup
                    playersPopupDialog.dismiss();
                }
            }
        });

        // Show the players popup
        playersPopupDialog.show();
    }

    // Function to find a player by name in the list
    private static Player findPlayerByName(List<Player> playerList, String playerName) {
        for (Player player : playerList) {
            if (player.toString().equalsIgnoreCase(playerName)) {
                return player;
            }
        }
        return null; // Player not found
    }




    private static List<Player> getPlayerListForTeams(Context context, String teamId1, String teamId2) {
        List<Player> playerList = new ArrayList<>();

        DatabaseReference playersRef = FirebaseDatabase.getInstance().getReference().child("players");

        // Combine players from both teams using OR condition
        playersRef.orderByChild("teamId").equalTo(teamId1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot playerSnapshot : dataSnapshot.getChildren()) {
                    Player player = playerSnapshot.getValue(Player.class);
                    if (player != null) {
                        playerList.add(player);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
                Toast.makeText(context, "Failed to retrieve players", Toast.LENGTH_SHORT).show();
            }
        });

        // Combine players from both teams using OR condition
        playersRef.orderByChild("teamId").equalTo(teamId2).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot playerSnapshot : dataSnapshot.getChildren()) {
                    Player player = playerSnapshot.getValue(Player.class);
                    if (player != null) {
                        playerList.add(player);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
                Toast.makeText(context, "Failed to retrieve players", Toast.LENGTH_SHORT).show();
            }
        });

        return playerList;
    }


    private static void updatePlayerList(Context context, List<Player> playerList, ListView playerListView) {
        ArrayAdapter<Player> playerAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, android.R.id.text1, playerList);
        playerListView.setAdapter(playerAdapter);
        playerAdapter.notifyDataSetChanged();
    }

    public static void addPOTMInfo(String userId, String playerId, String gameId) {
        DatabaseReference potmInfoRef = FirebaseDatabase.getInstance().getReference().child("potm_info");

        // Check if an entry with the same userId and gameId already exists
        potmInfoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean entryExists = false;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    POTMInfo existingPOTMInfo = snapshot.getValue(POTMInfo.class);

                    // Check if an entry with the same gameId already exists for the user
                    if (existingPOTMInfo != null && existingPOTMInfo.getUserId().equals(userId)
                            && existingPOTMInfo.getGameId().equals(gameId)) {
                        entryExists = true;
                        break;
                    }
                }

                // If entry doesn't exist, add a new POTMInfo
                if (!entryExists) {
                    String potmInfoKey = potmInfoRef.push().getKey();
                    POTMInfo potmInfo = new POTMInfo(userId, playerId, gameId);
                    potmInfoRef.child(potmInfoKey).setValue(potmInfo, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError == null) {
                                // Entry added successfully, update points for the POTM
                                checkAndSetPoints("M1","P2");
                                checkAndSetPoints("M20","P13");
                                checkAndSetPoints("M21","P5");
                            } else {
                                // Handle the error during POTMInfo insertion
                                Log.e("POTM", "Error adding POTMInfo: " + databaseError.getMessage());
                            }
                        }
                    });
                } else {
                    // Handle the case where an entry with the same userId and gameId already exists
                    // You can show a message or perform any necessary actions
                    Log.d("POTM", "Entry with the same userId and gameId already exists.");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors in the database query
                Log.e("POTM", "Error checking for existing entry: " + databaseError.getMessage());
            }
        });
    }


    public static void updatePointsForPOTM(String gameId, String playerId) {
        DatabaseReference gamesRef = FirebaseDatabase.getInstance().getReference().child("games");
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser == null) {
            // User not authenticated
            return;
        }

        String userId = currentUser.getUid();

        gamesRef.child(gameId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Match game = dataSnapshot.getValue(Match.class);

                if (game != null && game.getPotmId().equals(playerId)) {
                    // The provided playerId is the POTM for this game
                    // Update points for the logged-in user
                    DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");
                    setPointsTo5();



                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors in the game data query
                Log.e("UpdatePoints", "Error querying game data: " + databaseError.getMessage());
            }
        });
    }

    public static void checkAndSetPoints(String gameId, String playerId) {
        DatabaseReference potmInfoRef = FirebaseDatabase.getInstance().getReference().child("potm_info");

        potmInfoRef.orderByChild("gameId").equalTo(gameId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean entryExists = false;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    POTMInfo potmInfo = snapshot.getValue(POTMInfo.class);

                    // Check if an entry with the specified playerId exists for the gameId
                    if (potmInfo != null && potmInfo.getPlayerId().equals(playerId)) {
                        entryExists = true;
                        break;
                    }
                }

                // If the entry exists, call the setPointsTo5 function
                if (entryExists) {
                    setPointsTo5();
                } else {
                    // Entry doesn't exist, handle accordingly (optional)
                    // You can show a message or perform any necessary actions
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors in the database query
                // Log.e("POTM", "Error checking for existing entry: " + databaseError.getMessage());
            }
        });
    }

    public static void setPointsTo5() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");

        usersRef.orderByChild("points").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);

                    if (currentUser != null && currentUser.getEmail().equals(user.getMailAdress())) {
                        // Update points for the logged-in user
                        int currentPoints = user.getPoints();
                        int newPoints = currentPoints + 5;

                        // Push the updated points to the database
                        userSnapshot.getRef().child("points").setValue(newPoints)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Log.d("DEBUG", "Points updated successfully to " + newPoints + " for user: " + currentUser.getUid());
                                    } else {
                                        Log.e("DEBUG", "Error updating points: " + task.getException().getMessage());
                                    }
                                });
                        break; // No need to continue iterating if we found the logged-in user
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DEBUG", "Database error: " + error.getMessage());
            }
        });
    }
}
