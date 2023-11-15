package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class MainActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("Firebase","Initializing Firebase");
        FirebaseApp.initializeApp(this);
        Log.d("Firebase","Firebase initialized succesfully");

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        mDatabase= FirebaseDatabase.getInstance().getReference().child("users");

        button = (Button) findViewById(R.id.loginButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity2();
            }
        });
        if (mDatabase == null) {
            Log.e("Database", "Database reference is null");
        } else {
            Log.d("Database", "Database reference initialized successfully");
        }
    }

    public void openActivity2(){
        Intent intent= new Intent(this, MainActivity2.class);
        startActivity(intent);
    }

    private void addUserToDatabase(String username){
        User user = new User(username);

        mDatabase.push()
                .setValue(user)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firebase", "User added successfully");
                    // Add any additional actions if needed
                })
                .addOnFailureListener(e -> Log.e("Firebase", "Error adding user: " + e.getMessage()));


    }

    public void onAddUserButtonClick(View view){
        Log.d("Button","add user button clicked");
        addUserToDatabase("exampleUsername");
    }
}