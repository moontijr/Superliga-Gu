package com.example.myapplication.repository;

import com.example.myapplication.model.Team;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class TeamRepository {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public interface TeamFetchCallback {
        void onTeamFetched(List<Team> teamList);

        void onFailure(Exception e);
    }

    // Fetch all teams from the "teams" collection
    public void getAllTeams(TeamFetchCallback callback) {
        db.collection("teams")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Team> teamList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Team team = document.toObject(Team.class);
                            teamList.add(team);
                        }
                        callback.onTeamFetched(teamList);
                    } else {
                        callback.onFailure(task.getException());
                    }
                });
    }

    // Fetch a specific team by ID
    public void getTeamById(String teamId, TeamFetchCallback callback) {
        DocumentReference teamRef = db.collection("teams").document(teamId);

        teamRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Team team = task.getResult().toObject(Team.class);
                if (team != null) {
                    List<Team> teamList = new ArrayList<>();
                    teamList.add(team);
                    callback.onTeamFetched(teamList);
                } else {
                    callback.onTeamFetched(new ArrayList<>());
                }
            } else {
                callback.onFailure(task.getException());
            }
        });
    }
}

