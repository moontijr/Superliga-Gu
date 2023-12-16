package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LeaderboardActivity extends AppCompatActivity {

    DatabaseReference reference;
    TextView firstPlaceTextView, secondPlaceTextView, thirdPlaceTextView;
    ListView usersListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leaderboard);

        firstPlaceTextView = findViewById(R.id.firstPlaceTextView);
        secondPlaceTextView = findViewById(R.id.secondPlaceTextView);
        thirdPlaceTextView = findViewById(R.id.thirdPlaceTextView);
        usersListView = findViewById(R.id.usersListView);

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");

        // Use addValueEventListener to listen for changes in the data
        usersRef.orderByChild("points").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Clear the existing content
                firstPlaceTextView.setText("");
                secondPlaceTextView.setText("");
                thirdPlaceTextView.setText("");

                // Convert the data to a list of users
                List<User> userList = new ArrayList<>();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    userList.add(user);
                }

                // Sort the user list based on points in descending order
                Collections.sort(userList, new Comparator<User>() {
                    @Override
                    public int compare(User user1, User user2) {
                        return Integer.compare(user2.getPoints(), user1.getPoints());
                    }
                });

                // Display the top 3 users on the podium
                for (int i = 0; i < Math.min(3, userList.size()); i++) {
                    User user = userList.get(i);
                    if (i == 0) {
                        firstPlaceTextView.setText((i + 1) + ". " + user.toStringPointsUsername());
                    } else if (i == 1) {
                        secondPlaceTextView.setText((i + 1) + ". " + user.toStringPointsUsername());
                    } else if (i == 2) {
                        thirdPlaceTextView.setText((i + 1) + ". " + user.toStringPointsUsername());
                    }
                }

                // Display the rest of the users in the list
                List<String> userNames = new ArrayList<>();
                for (int i = 3; i < userList.size(); i++) {
                    User user = userList.get(i);
                    userNames.add((i + 1) + ". " + user.toStringPointsUsername());
                }

                // Set up ArrayAdapter for the ListView
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                        LeaderboardActivity.this,
                        android.R.layout.simple_list_item_1,
                        userNames
                );

                // Set the ArrayAdapter to the ListView
                usersListView.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Error reading users", databaseError.toException());
            }
        });
    }
}
