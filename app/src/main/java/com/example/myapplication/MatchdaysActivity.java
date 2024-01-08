package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewTreeViewModelKt;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.myapplication.model.Match;
import com.example.myapplication.model.Matchday;
import com.example.myapplication.model.Team;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MatchdaysActivity extends AppCompatActivity {

    private ImageView button;

    private ImageButton buttonGw1;
    private ImageButton buttonGw2;
    private ImageButton buttonGw3;
    private ImageButton buttonGw4;
    private ImageButton buttonGw5;
    private ImageButton buttonGw6;
    private ImageButton buttonGw7;
    private ImageButton buttonGw8;

    DatabaseReference mDatabase;
    DatabaseReference matchDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matchdays);
        ImageView logoImageView = findViewById(R.id.logoImageView);


        mDatabase = FirebaseDatabase.getInstance().getReference().child("matchdays");
        matchDatabase = FirebaseDatabase.getInstance().getReference().child("games");

        Matchday demoMatchday=new Matchday("Demo1","P2");
        Matchday demoMatchday2=new Matchday("Demo2","P3");
        Match match = new Match("M1","T1","T2",1,1,"Demo1","P2");
        Match match1 = new Match("M2","T1","T3",1,0,"Demo1","P1");
        Match match2 = new Match("M3","T3","T2",0,1,"Demo1","P1");
        Match match3 = new Match("M4","T3","T11",0,1,"Demo1","P1");
        Match match4 = new Match("M5","T1","T11",1,2,"Demo1","P1");
        Match match5 = new Match("M6","T10","T1",0,2,"Demo1","P1");
        Match match6 = new Match("M7","T2","T12",2,0,"Demo1","P1");
        Match match7 = new Match("M8","T3","T10",0,0,"Demo1","P1");
        Match match8 = new Match("M9","T11","T2",2,2,"Demo1","P1");
        Match match9 = new Match("M10","T10","T11",0,2,"Demo1","P1");
        Match match10 = new Match("M11","T12","T14",1,2,"Demo1","P1");
        Match match11 = new Match("M12","T14","T11",1,1,"Demo1","P1");
        Match match12 = new Match("M13","T3","T4",0,0,"Demo1","P1");
        Match matchBotosaniCsu=new Match("M20","T10","T11",3,0,"DEMO1","P13");
        Match matchDinamoPetrolul=new Match("M21","T3","T4",1,1,"DEMO1","P5");
        Match matchFarulSepsi=new Match("M22","T5","T6",2,1,"DEMO1","P8");
        addMatchdayToDatabase(demoMatchday);
        addMatchdayToDatabase(demoMatchday2);
        addMatchToDatabase(match);
        addMatchToDatabase(match1);
        addMatchToDatabase(match2);
        addMatchToDatabase(match3);
        addMatchToDatabase(match4);
        addMatchToDatabase(match5);
        addMatchToDatabase(match6);
        addMatchToDatabase(match7);
        addMatchToDatabase(match8);
        addMatchToDatabase(match9);
        addMatchToDatabase(match10);
        addMatchToDatabase(match11);
        addMatchToDatabase(match12);
        addMatchToDatabase(matchBotosaniCsu);
        addMatchToDatabase(matchDinamoPetrolul);
        addMatchToDatabase(matchFarulSepsi);
        logoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLeaderboardActivity();
            }
        });

        ImageButton gw1 = findViewById(R.id.gameweekButton1);
        gw1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGW1();
            }
        });

        ImageButton gw2 = findViewById(R.id.gameweekButton2);
        gw2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGW2();
            }
        });

        ImageButton gw3 = findViewById(R.id.gameweekButton3);
        gw3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGW3();
            }
        });

        ImageButton gw4 = findViewById(R.id.gameweekButton4);
        gw4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGW4();
            }
        });

        ImageButton gw5 = findViewById(R.id.gameweekButton5);
        gw5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGW5();
            }
        });

        ImageButton gw6 = findViewById(R.id.gameweekButton6);
        gw6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGW6();
            }
        });

        ImageButton gw7 = findViewById(R.id.gameweekButton7);
        gw7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGW7();
            }
        });
    }

        private void addMatchdayToDatabase(Matchday matchday) {
            mDatabase.child(matchday.getId()).setValue(matchday)
                    .addOnSuccessListener(aVoid -> {
                        Log.d("Firebase", "Matchday " + matchday + " added successfully");
                    })
                    .addOnFailureListener(e -> {
                        Log.e("Firebase", "Error adding " + matchday + ": " + e.getMessage());
                    });
        }

        private void addMatchToDatabase(Match match){
            matchDatabase.child(match.getId()).setValue(match)
                    .addOnSuccessListener(aVoid -> {
                        Log.d("Firebase", "Match " + match + " added successfully");
                    })
                    .addOnFailureListener(e -> {
                        Log.e("Firebase", "Error adding " + match + ": " + e.getMessage());
                    });
        }



    public void openLeaderboardActivity(){
        Intent intent= new Intent(this, LeaderboardActivity.class);
        startActivity(intent);
    }

    public void openGW1(){
        Intent intent= new Intent(this, GW1Activity.class);
        startActivity(intent);
    }

    public void openGW2(){
        Intent intent= new Intent(this, GW2Activity.class);
        startActivity(intent);
    }

    public void openGW3(){
        Intent intent= new Intent(this, GW3Activity.class);
        startActivity(intent);
    }

    public void openGW4(){
        Intent intent= new Intent(this, GW4Activity.class);
        startActivity(intent);
    }

    public void openGW5(){
        Intent intent= new Intent(this, GW5Activity.class);
        startActivity(intent);
    }

    public void openGW6(){
        Intent intent= new Intent(this, GW6Activity.class);
        startActivity(intent);
    }

    public void openGW7(){
        Intent intent= new Intent(this, GW7Activity.class);
        startActivity(intent);
    }

    public void openGW8(){
        Intent intent= new Intent(this, GW8Activity.class);
        startActivity(intent);
    }

    public void onLogoClick(View view) {
        // Start the new activity
        button = findViewById(R.id.logoImageView);
        button.setOnClickListener(v -> openLeaderboardActivity());
    }
    public void onGameweek1ButtonClick(View view){
        buttonGw1 = findViewById(R.id.gameweekButton1);
        buttonGw1.setOnClickListener(v -> openGW1());
    }

    public void onGameweek2ButtonClick(View view){
        buttonGw2 = findViewById(R.id.gameweekButton2);
        buttonGw2.setOnClickListener(v -> openGW1());
    }

    public void onGameweek3ButtonClick(View view){
        buttonGw3 = findViewById(R.id.gameweekButton3);
        buttonGw3.setOnClickListener(v -> openGW3());
    }

    public void onGameweek4ButtonClick(View view){
        buttonGw4 = findViewById(R.id.gameweekButton4);
        buttonGw4.setOnClickListener(v -> openGW4());
    }

    public void onGameweek5ButtonClick(View view){
        buttonGw5 = findViewById(R.id.gameweekButton5);
        buttonGw5.setOnClickListener(v -> openGW5());
    }

    public void onGameweek6ButtonClick(View view){
        buttonGw6 = findViewById(R.id.gameweekButton6);
        buttonGw6.setOnClickListener(v -> openGW6());
    }

    public void onGameweek7ButtonClick(View view){
        buttonGw7 = findViewById(R.id.gameweekButton7);
        buttonGw7.setOnClickListener(v -> openGW7());
    }

    public void onGameweek8ButtonClick(View view){
        buttonGw8 = findViewById(R.id.gameweekButton8);
        buttonGw8.setOnClickListener(v -> openGW8());
    }

    //opening the popup with the rules
    public void rules(View view) {
        Dialog popupDialog = new Dialog(this);

        popupDialog.setContentView(R.layout.rules);

        popupDialog.show();
    }

    //signing out from the app through Firebase
    public void signOut(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}