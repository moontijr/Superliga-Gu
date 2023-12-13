package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    Dialog popUpDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("Firebase", "Initializing Firebase");
        FirebaseApp.initializeApp(this);
        Log.d("Firebase", "Firebase initialized succesfully");

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");

        Button registerButton = findViewById(R.id.registerButton);

        popUpDialog = new Dialog(this);

        registerButton.setOnClickListener(v -> {
            if (popUpDialog != null && popUpDialog.isShowing()) {
                // Dismiss the existing dialog
                popUpDialog.dismiss();
            }

            // Inflate the popup layout
            View popupView = getLayoutInflater().inflate(R.layout.register_popup, null);

            // Find the EditText views in the popup view
            EditText editTextUsername = popupView.findViewById(R.id.Username);
            EditText editTextPassword = popupView.findViewById(R.id.Password);
            EditText editTextFamilyName = popupView.findViewById(R.id.NumeFamilie);
            EditText editTextGivenName = popupView.findViewById(R.id.Prenume);
            EditText editTextMailAddress = popupView.findViewById(R.id.Email);

            // Set up a button in the popup layout to save the user data
            Button saveButton = popupView.findViewById(R.id.registerAction);
            saveButton.setOnClickListener(q -> {
                // Get values from the EditText fields
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();
                String familyName = editTextFamilyName.getText().toString();
                String givenName = editTextGivenName.getText().toString();
                String email = editTextMailAddress.getText().toString();

//                // Call the method to add the user to the database
//                addUserToDb(username, password, familyName, givenName, email);

                //TODO register pt Auth

                // Dismiss the popup after saving
                popUpDialog.dismiss();
            });

            // Set the content view of the popup dialog
            popUpDialog.setContentView(popupView);

            // Make the dialog background transparent
            Objects.requireNonNull(popUpDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            // Show the popup dialog
            popUpDialog.show();
        });

        Button loginButton = (Button) findViewById(R.id.loginButton);

        popUpDialog = new Dialog(this);

        loginButton.setOnClickListener(v -> {
            if (popUpDialog != null && popUpDialog.isShowing()) {
                // Dismiss the existing dialog
                popUpDialog.dismiss();
            }

            // Inflate the popup layout
            View popupView = getLayoutInflater().inflate(R.layout.login_popup, null);

            // Find the EditText views in the popup view
            EditText editTextMailAddress = popupView.findViewById(R.id.loginEmail);
            EditText editTextPassword = popupView.findViewById(R.id.loginPassword);

            // Set up a button in the popup layout to save the user data
            Button saveButton = popupView.findViewById(R.id.loginAction);
            saveButton.setOnClickListener(q -> {
                // Get values from the EditText fields
                String email = editTextMailAddress.getText().toString();
                String password = editTextPassword.getText().toString();

                //TODO login FBA

                // Show the popup dialog
                popUpDialog.show();
            });
            // Set the content view of the popup dialog
            popUpDialog.setContentView(popupView);

            // Make the dialog background transparent
            Objects.requireNonNull(popUpDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            // Show the popup dialog
            popUpDialog.show();

        });

        if (mDatabase == null) {
            Log.e("Database", "Database reference is null");
        } else {
            Log.d("Database", "Database reference initialized successfully");
        }
    }

    public void onRegisterButtonClick(View view) {

        if (popUpDialog != null && popUpDialog.isShowing()) {
            // Dismiss the existing dialog
            popUpDialog.dismiss();
        }

        // Inflate the popup layout
        View popupView = getLayoutInflater().inflate(R.layout.register_popup, null);

        // Find the EditText views in the popup view
        EditText editTextUsername = popupView.findViewById(R.id.Username);
        EditText editTextParola = popupView.findViewById(R.id.Password);
        EditText editTextFamilyName = popupView.findViewById(R.id.NumeFamilie);
        EditText editTextGivenName = popupView.findViewById(R.id.Prenume);
        EditText editTextMailAdress = popupView.findViewById(R.id.Email);

        // Set up a button in the popup layout to save the user data
        Button saveButton = popupView.findViewById(R.id.registerAction);
        saveButton.setOnClickListener(v -> {
            // Get values from the EditText fields
            String username = editTextUsername.getText().toString();
            String password = editTextParola.getText().toString();
            String familyName = editTextFamilyName.getText().toString();
            String givenName = editTextGivenName.getText().toString();
            String email = editTextMailAdress.getText().toString();

            // Call the method to add the user to the database
            addUserToDb(username, password, familyName, givenName, email);

            // Dismiss the popup after saving
            popUpDialog.dismiss();
        });

        // Set the content view of the popup dialog
        popUpDialog.setContentView(popupView);

        // Make the dialog background transparent
        Objects.requireNonNull(popUpDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Show the popup dialog
        popUpDialog.show();
    }

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
}