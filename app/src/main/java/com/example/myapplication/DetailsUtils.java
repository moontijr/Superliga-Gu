package com.example.myapplication;

import android.app.Dialog;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.myapplication.model.Player;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DetailsUtils {

    public static void showPlayersPopupForTeams(Context context, String teamId1, String teamId2) {
        Dialog playersPopupDialog = new Dialog(context);

        // Inflate the layout for the players popup
        playersPopupDialog.setContentView(R.layout.game1popup);

        // Find the necessary views in the popup view
        ListView playerListView = playersPopupDialog.findViewById(R.id.playerListView);

        // Populate the list of players based on the teamIds
        getPlayerListForTeam(context, teamId1, teamId2, playerListView);

        // Show the players popup
        playersPopupDialog.show();
    }

    private static void getPlayerListForTeam(Context context, String teamId1, String teamId2, ListView playerListView) {
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

                // Notify your adapter if needed
                updatePlayerList(context, playerList, playerListView);
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

                // Notify your adapter if needed
                updatePlayerList(context, playerList, playerListView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
                Toast.makeText(context, "Failed to retrieve players", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private static void updatePlayerList(Context context, List<Player> playerList, ListView playerListView) {
        ArrayAdapter<Player> playerAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, android.R.id.text1, playerList);
        playerListView.setAdapter(playerAdapter);
        playerAdapter.notifyDataSetChanged();
    }
}