package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity2 extends AppCompatActivity {

    private ImageView button;

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
    }

    public void openLeaderboardActivity(){
        Intent intent= new Intent(this, LeaderboardActivity.class);
        startActivity(intent);
    }

    public void onLogoClick(View view) {
        // Start the new activity
        button = findViewById(R.id.logoImageView);
        button.setOnClickListener(v -> openLeaderboardActivity());
    }
}