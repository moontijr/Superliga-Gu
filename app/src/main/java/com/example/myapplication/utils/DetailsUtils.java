package com.example.myapplication.utils;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.myapplication.model.POTGWInfo;
import com.example.myapplication.model.POTMInfo;
import com.example.myapplication.adapter.PlayerSpinnerAdapter;
import com.example.myapplication.R;
import com.example.myapplication.model.Player;
import com.example.myapplication.model.User;
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
                    addPOTMInfo(loggedInPlayerId, selectedPlayer.getId(), gameId,context);

                    playersPopupDialog.dismiss();
                }
            }
        });

        playersPopupDialog.show();
    }

    public static void showAllPlayersPopup(Context context, String loggedInPlayerId, String gameweekId) {

        Dialog playersPopupDialog = new Dialog(context);
        playersPopupDialog.setContentView(R.layout.potgwpopup);

        Spinner playerSpinner = playersPopupDialog.findViewById(R.id.playerSpinner);
        AutoCompleteTextView playerNameAutoComplete = playersPopupDialog.findViewById(R.id.playerNameAutoComplete);
        Button submitPlayerButton = playersPopupDialog.findViewById(R.id.submitPlayerButton);

        List<Player> playerList = getAllPlayerList(context);
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
                    addPOTGWInfo(loggedInPlayerId, selectedPlayer.getId(), gameweekId,context);

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


    private static List<Player> getAllPlayerList(Context context) {
        List<Player> playerList = new ArrayList<>();

        DatabaseReference playersRef = FirebaseDatabase.getInstance().getReference().child("players");

        // Combine players from both teams using OR condition
        playersRef.addListenerForSingleValueEvent(new ValueEventListener() {
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

        playersRef.addListenerForSingleValueEvent(new ValueEventListener() {
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

    public static void addPOTMInfo(String userId, String playerId, String gameId,Context context) {
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
                                checkAndSetPoints("M1","P2",context);
                                else if (gameId == "M20")
                                checkAndSetPoints("M20","P13",context);
                                else if(gameId == "M21")
                                checkAndSetPoints("M21","P5",context);
                                else if(gameId == "M22")
                                    checkAndSetPoints("M22","P8",context);
                            } else {
                                // Handle the error during POTMInfo insertion
                                Log.e("POTM", "Error adding POTMInfo: " + databaseError.getMessage());
                            }
                        }
                    });
                } else {
                    Log.d("POTM", "Entry with the same userId and gameId already exists.");
                    ToastUtils.showToast("Deja ati facut o predictie pentru acest meci, ne pare rau!",context);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("POTM", "Error checking for existing entry: " + databaseError.getMessage());
            }
        });
    }

    public static void addPOTGWInfo(String userId, String playerId, String gameweekId,Context context) {
        DatabaseReference potmInfoRef = FirebaseDatabase.getInstance().getReference().child("potgw_info");

        potmInfoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean entryExists = false;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    POTGWInfo existingPOTMInfo = snapshot.getValue(POTGWInfo.class);

                    if (existingPOTMInfo != null && existingPOTMInfo.getUserId().equals(userId)
                            && existingPOTMInfo.getGameweekId().equals(gameweekId)) {
                        entryExists = true;
                        break;
                    }
                }

                if (!entryExists) {
                    String potmInfoKey = potmInfoRef.push().getKey();
                    POTGWInfo potmInfo = new POTGWInfo(userId, playerId, gameweekId);
                    potmInfoRef.child(potmInfoKey).setValue(potmInfo, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError == null) {
                                if(gameweekId == "Demo1")
                                    checkAndSetPointsForPotgw("Demo1","P2",context);
                            } else {
                                // Handle the error during POTMInfo insertion
                                Log.e("POTM", "Error adding POTMInfo: " + databaseError.getMessage());
                            }
                        }
                    });
                } else {
                    Log.d("POTM", "Entry with the same userId and gameId already exists.");
                    ToastUtils.showToast("Deja ati facut o predictie pentru acest lucru, ne pare rau",context);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("POTM", "Error checking for existing entry: " + databaseError.getMessage());
            }
        });
    }



    public static void checkAndSetPoints(String gameId, String playerId,Context context) {
        DatabaseReference potmInfoRef = FirebaseDatabase.getInstance().getReference().child("potm_info");

        potmInfoRef.orderByChild("gameId").equalTo(gameId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean entryExists = false;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    POTMInfo potmInfo = snapshot.getValue(POTMInfo.class);

                    if (potmInfo != null && potmInfo.getPlayerId().equals(playerId)) {
                        ToastUtils.showToast("Felicitari, ai primit 3 puncte!",context);
                        setPointsTo3();
                    }
                    else {
                        ToastUtils.showToast("Ghinion, incearca la meciul urmator!",context);
                    }
                }

                if (entryExists) {
                    ToastUtils.showToast("Felicitari, verifica clasamentul pentru a vedea cate puncte ai acum",context);
                    setPointsTo3();
                } else {
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void checkAndSetPointsForPotgw(String gameweekId, String playerId,Context context) {
        DatabaseReference potmInfoRef = FirebaseDatabase.getInstance().getReference().child("potgw_info");

        potmInfoRef.orderByChild("gameweekId").equalTo(gameweekId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean entryExists = false;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    POTGWInfo potmInfo = snapshot.getValue(POTGWInfo.class);

                    if (potmInfo != null && potmInfo.getPlayerId().equals(playerId)) {
                        setPointsTo5();
                        ToastUtils.showToast("Felicitari, ai primit 5 puncte!",context);
                    }
                    else {
                        ToastUtils.showToast("Ghinion, incearca la etapa urmatoare!",context);
                    }
                }

                if (entryExists) {
                    setPointsTo5();
                    ToastUtils.showToast("Felicitari!",context);
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


    public static void setPointsTo3() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");

        usersRef.orderByChild("points").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);

                    if (currentUser != null && currentUser.getEmail().equals(user.getMailAdress())) {
                        int currentPoints = user.getPoints();
                        int newPoints = currentPoints + 3;

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
