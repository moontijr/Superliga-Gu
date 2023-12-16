package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class MainActivity2 extends AppCompatActivity {

    private ImageView button;

    private ImageButton buttonGw1;
    private ImageButton buttonGw2;
    private ImageButton buttonGw3;
    private ImageButton buttonGw4;
    private ImageButton buttonGw5;
    private ImageButton buttonGw6;
    private ImageButton buttonGw7;
    private ImageButton buttonGw8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2);
        ImageView logoImageView = findViewById(R.id.logoImageView);
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
}