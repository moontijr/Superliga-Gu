package com.example.myapplication.repository;

import com.example.myapplication.model.Player;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class PlayerRepository {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public interface PlayerFetchCallback {
        void onPlayerFetched(List<Player> playerList);

        void onFailure(Exception e);
    }

    // Fetch all players from the "players" collection
    public void getAllPlayers(PlayerFetchCallback callback) {
        db.collection("players")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Player> playerList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Player player = document.toObject(Player.class);
                            playerList.add(player);
                        }
                        callback.onPlayerFetched(playerList);
                    } else {
                        callback.onFailure(task.getException());
                    }
                });
    }

    // Fetch a specific player by ID
    public void getPlayerById(String playerId, PlayerFetchCallback callback) {
        DocumentReference playerRef = db.collection("players").document(playerId);

        playerRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Player player = task.getResult().toObject(Player.class);
                if (player != null) {
                    List<Player> playerList = new ArrayList<>();
                    playerList.add(player);
                    callback.onPlayerFetched(playerList);
                } else {
                    callback.onPlayerFetched(new ArrayList<>());
                }
            } else {
                callback.onFailure(task.getException());
            }
        });
    }
}
