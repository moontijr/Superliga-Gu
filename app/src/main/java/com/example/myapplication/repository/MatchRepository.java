package com.example.myapplication.repository;

import com.example.myapplication.model.Match;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MatchRepository {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public interface MatchFetchCallback {
        void onMatchFetched(List<Match> matchList);

        void onFailure(Exception e);
    }

    public void getAllMatches(MatchFetchCallback callback) {
        db.collection("matches")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Match> matchList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Match match = document.toObject(Match.class);
                            matchList.add(match);
                        }
                        callback.onMatchFetched(matchList);
                    } else {
                        callback.onFailure(task.getException());
                    }
                });
    }

    public void getMatchById(String matchId, MatchFetchCallback callback) {
        DocumentReference matchRef = db.collection("games").document(matchId);

        matchRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Match match = task.getResult().toObject(Match.class);
                if (match != null) {
                    List<Match> matchList = new ArrayList<>();
                    matchList.add(match);
                    callback.onMatchFetched(matchList);
                } else {
                    callback.onMatchFetched(new ArrayList<>());
                }
            } else {
                callback.onFailure(task.getException());
            }
        });
    }
}

