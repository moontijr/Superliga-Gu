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

        Dialog playersPopupDialog = new Dialog(context);
        playersPopupDialog.setContentView(R.layout.game1popup);

        Spinner playerSpinner = playersPopupDialog.findViewById(R.id.playerSpinner);
        AutoCompleteTextView playerNameAutoComplete = playersPopupDialog.findViewById(R.id.playerNameAutoComplete);
        Button submitPlayerButton = playersPopupDialog.findViewById(R.id.submitPlayerButton);

        List<Player> playerList = getPlayerListForTeams(context, teamId1, teamId2);
        PlayerSpinnerAdapter spinnerAdapter = new PlayerSpinnerAdapter(context, playerList);
        playerSpinner.setAdapter(spinnerAdapter);

        submitPlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Player selectedPlayer;
                if (playerSpinner.getSelectedItem() != null) {
                    selectedPlayer = (Player) playerSpinner.getSelectedItem();
                } else {
                    String customPlayerName = playerNameAutoComplete.getText().toString().trim();
                    selectedPlayer = findPlayerByName(playerList, customPlayerName);
                }

                if (selectedPlayer != null) {
                    addPOTMInfo(loggedInPlayerId, selectedPlayer.getId(), gameId);

                    playersPopupDialog.dismiss();
                }
            }
        });

        playersPopupDialog.show();
    }

    private static Player findPlayerByName(List<Player> playerList, String playerName) {
        for (Player player : playerList) {
            if (player.toString().equalsIgnoreCase(playerName)) {
                return player;
            }
        }
        return null;
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
                Toast.makeText(context, "Failed to retrieve players", Toast.LENGTH_SHORT).show();
            }
        });

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

        potmInfoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean entryExists = false;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    POTMInfo existingPOTMInfo = snapshot.getValue(POTMInfo.class);

                    if (existingPOTMInfo != null && existingPOTMInfo.getUserId().equals(userId)
                            && existingPOTMInfo.getGameId().equals(gameId)) {
                        entryExists = true;
                        break;
                    }
                }

                if (!entryExists) {
                    String potmInfoKey = potmInfoRef.push().getKey();
                    POTMInfo potmInfo = new POTMInfo(userId, playerId, gameId);
                    potmInfoRef.child(potmInfoKey).setValue(potmInfo, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError == null) {
                                if(gameId == "M1")
                                checkAndSetPoints("M1","P2");
                                else if (gameId == "M20")
                                checkAndSetPoints("M20","P13");
                                else if(gameId == "M21")
                                checkAndSetPoints("M21","P5");
                                else if(gameId == "M22")
                                    checkAndSetPoints("M22","P8");
                            } else {
                                // Handle the error during POTMInfo insertion
                                Log.e("POTM", "Error adding POTMInfo: " + databaseError.getMessage());
                            }
                        }
                    });
                } else {
                    Log.d("POTM", "Entry with the same userId and gameId already exists.");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("POTM", "Error checking for existing entry: " + databaseError.getMessage());
            }
        });
    }


    public static void updatePointsForPOTM(String gameId, String playerId) {
        DatabaseReference gamesRef = FirebaseDatabase.getInstance().getReference().child("games");
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser == null) {
            return;
        }

        String userId = currentUser.getUid();

        gamesRef.child(gameId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Match game = dataSnapshot.getValue(Match.class);

                if (game != null && game.getPotmId().equals(playerId)) {
                    DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");
                    setPointsTo5();



                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
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

                    if (potmInfo != null && potmInfo.getPlayerId().equals(playerId)) {
                        setPointsTo5();
                    }
                }

                if (entryExists) {
                    setPointsTo5();
                } else {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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
                        int currentPoints = user.getPoints();
                        int newPoints = currentPoints + 5;

                        userSnapshot.getRef().child("points").setValue(newPoints)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Log.d("DEBUG", "Points updated successfully to " + newPoints + " for user: " + currentUser.getUid());
                                    } else {
                                        Log.e("DEBUG", "Error updating points: " + task.getException().getMessage());
                                    }
                                });
                        break;
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
