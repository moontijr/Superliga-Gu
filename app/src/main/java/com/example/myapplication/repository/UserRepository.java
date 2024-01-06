package com.example.myapplication.repository;

import com.example.myapplication.User;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public interface UserFetchCallback {
        void onUserFetched(List<User> userList);

        void onFailure(Exception e);
    }

    // Fetch all users from the "users" collection
    public void getAllUsers(UserFetchCallback callback) {
        db.collection("users")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<User> userList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            User user = document.toObject(User.class);
                            userList.add(user);
                        }
                        callback.onUserFetched(userList);
                    } else {
                        callback.onFailure(task.getException());
                    }
                });
    }

    // Fetch a specific user by UID
    public void getUserByUid(String uid, UserFetchCallback callback) {
        DocumentReference userRef = db.collection("users").document(uid);

        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                User user = task.getResult().toObject(User.class);
                if (user != null) {
                    List<User> userList = new ArrayList<>();
                    userList.add(user);
                    callback.onUserFetched(userList);
                } else {
                    callback.onUserFetched(new ArrayList<>());
                }
            } else {
                callback.onFailure(task.getException());
            }
        });
    }
}
