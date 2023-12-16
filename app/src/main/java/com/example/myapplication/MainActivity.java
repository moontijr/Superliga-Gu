package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;


    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Firebase initialization
        Log.d("Firebase", "Initializing Firebase");
        FirebaseApp.initializeApp(this);
        Log.d("Firebase", "Firebase initialized successfully");

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        if (mDatabase == null) {
            Log.e("Database", "Database reference is null");
        } else {
            Log.d("Database", "Database reference initialized successfully");
        }

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MatchdaysActivity.class));
            finish();
        }

        //Register dialog button
        Button registerPopupButton = findViewById(R.id.registerPopupButton);
        registerPopupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRegisterPopupDialog();
            }
        });

        //Login dialog button
        Button loginPopupButton = findViewById(R.id.loginPopupButton);
        loginPopupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoginPopupDialog();
            }
        });
    }

    //Register Dialog
    private void showRegisterPopupDialog() {
        Dialog popupDialog = new Dialog(this);

        popupDialog.setContentView(R.layout.register_popup);

        // Find the EditText views in the popup view
        EditText editTextUsername = popupDialog.findViewById(R.id.Username);
        EditText editTextPassword = popupDialog.findViewById(R.id.Password);
        EditText editTextFamilyName = popupDialog.findViewById(R.id.NumeFamilie);
        EditText editTextGivenName = popupDialog.findViewById(R.id.Prenume);
        EditText editTextMailAddress = popupDialog.findViewById(R.id.Email);
        Button registerAction = popupDialog.findViewById(R.id.registerAction);

        //Set up the register action
        registerAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get values from the EditText fields
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();
                String familyName = editTextFamilyName.getText().toString();
                String givenName = editTextGivenName.getText().toString();
                String email = editTextMailAddress.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    // Show an error message or handle the invalid email case
                    editTextMailAddress.setError("Enter a valid email address");
                    return;  // Stop further processing if the email is invalid
                }

                if (TextUtils.isEmpty(password)) {
                    editTextPassword.setError("Enter a password");
                    return;
                }

                //Register the user in Firebase
                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "New user registered.", Toast.LENGTH_SHORT).show();

                            // Call the method to add the extra user details to the database
                            addUserToDb(username, password, familyName, givenName, email);

                            startActivity(new Intent(getApplicationContext(), MatchdaysActivity.class));
                        } else {
                            Toast.makeText(MainActivity.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                popupDialog.dismiss();
            }
        });

        popupDialog.show();
    }


    //Insert extra details of the user
    private void addUserToDb(String username, String password, String familyName, String givenName, String email) {
        User user = new User(username, password, familyName, givenName, email);

        mDatabase.push()
                .setValue(user)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firebase", "User added successfully");
                    // Add any additional actions if needed
                })
                .addOnFailureListener(e -> Log.e("Firebase", "Error adding user: " + e.getMessage()));


    }

    private void showLoginPopupDialog() {
        Dialog popupDialog = new Dialog(this);

        popupDialog.setContentView(R.layout.login_popup);

        // Find the EditText views in the popup view
        EditText editTextEmail = popupDialog.findViewById(R.id.loginEmail);
        EditText editTextPassword = popupDialog.findViewById(R.id.loginPassword);
        Button loginAction = popupDialog.findViewById(R.id.loginAction);

        //Set up login action
        loginAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get values from the EditText fields
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    // Show an error message or handle the invalid email case
                    editTextEmail.setError("Enter a valid email address");
                    return;  // Stop further processing if the email is invalid
                }

                if (TextUtils.isEmpty(password)) {
                    editTextPassword.setError("Enter a password");
                    return;
                }

                //Login the user
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "User connected.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MatchdaysActivity.class));
                        }else {
                            Toast.makeText(MainActivity.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


        popupDialog.show();
    }
}