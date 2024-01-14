package com.example.myapplication.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.model.User;
import com.example.myapplication.repository.MatchRepository;
import com.example.myapplication.repository.PlayerRepository;
import com.example.myapplication.repository.TeamRepository;
import com.example.myapplication.repository.UserRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LeaderboardActivity extends AppCompatActivity {

    DatabaseReference reference;
    TextView firstPlaceTextView, secondPlaceTextView, thirdPlaceTextView;
    ListView usersListView;

    private UserRepository userRepository = new UserRepository();
    private PlayerRepository playerRepository = new PlayerRepository();
    private TeamRepository teamRepository = new TeamRepository();
    private MatchRepository matchRepository = new MatchRepository();

    private int loggedInUserPoints = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.leaderboard);

        firstPlaceTextView = findViewById(R.id.firstPlaceTextView);
        secondPlaceTextView = findViewById(R.id.secondPlaceTextView);
        thirdPlaceTextView = findViewById(R.id.thirdPlaceTextView);
        usersListView = findViewById(R.id.usersListView);

        TextView userPointsTextView = findViewById(R.id.userPointsTextView);

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");

        usersRef.orderByChild("points").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                firstPlaceTextView.setText("");
                secondPlaceTextView.setText("");
                thirdPlaceTextView.setText("");

                List<User> userList = new ArrayList<>();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    userList.add(user);

                    Log.d("DEBUG", "FirebaseEmail: " + currentUser.getEmail());
                    Log.d("DEBUG", "UserEmail: " + user.getMailAdress());

                    if (currentUser != null && currentUser.getEmail().equals(user.getMailAdress())) {
                        loggedInUserPoints = user.getPoints();
                        Log.d("DEBUG", "Points for logged-in user: " + loggedInUserPoints);

                        userPointsTextView.setText("Dumneavoastră aveți " + loggedInUserPoints + " puncte");

                    }
                }



                Collections.sort(userList, new Comparator<User>() {
                    @Override
                    public int compare(User user1, User user2) {
                        return Integer.compare(user2.getPoints(), user1.getPoints());
                    }
                });

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

                List<String> userNames = new ArrayList<>();
                for (int i = 3; i < userList.size(); i++) {
                    User user = userList.get(i);
                    userNames.add((i + 1) + ". " + user.toStringPointsUsername());
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                        LeaderboardActivity.this,
                        android.R.layout.simple_list_item_1,
                        userNames
                );

                usersListView.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Error reading users", databaseError.toException());
            }
        });
    }
}
