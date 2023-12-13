package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    private Button button;

    //test commit

    private Button popUpButton;
    Dialog popUpDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("Firebase","Initializing Firebase");
        FirebaseApp.initializeApp(this);
        Log.d("Firebase","Firebase initialized succesfully");

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        mDatabase= FirebaseDatabase.getInstance().getReference().child("users");

        TextView registerButton = findViewById(R.id.RegisterButton);

        popUpDialog=new Dialog(this);

        registerButton.setOnClickListener(v -> {
            if (popUpDialog != null && popUpDialog.isShowing()) {
                // Dismiss the existing dialog
                popUpDialog.dismiss();
            }

            // Inflate the popup layout
            View popupView = getLayoutInflater().inflate(R.layout.popup, null);

            // Find the EditText views in the popup view
            EditText editTextUsername = popupView.findViewById(R.id.Username);
            EditText editTextParola = popupView.findViewById(R.id.Password);
            EditText editTextFamilyName = popupView.findViewById(R.id.NumeFamilie);
            EditText editTextGivenName = popupView.findViewById(R.id.Prenume);
            EditText editTextMailAdress = popupView.findViewById(R.id.Email);

            // Set up a button in the popup layout to save the user data
            Button saveButton = popupView.findViewById(R.id.Inregistreaza);
            saveButton.setOnClickListener(q -> {
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
        });

        button = (Button) findViewById(R.id.loginButton);
        button.setOnClickListener(v -> openActivity2());
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

    private void addUserToDatabase(String username,String password){
        User user = new User(username,password);

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

        EditText editTextUsername = findViewById(R.id.editTextUsername);
        EditText editTextPassword = findViewById(R.id.editTextPassword);

        addUserToDatabase(editTextUsername.getText().toString(),editTextPassword.getText().toString());
    }

    public void onRegisterButtonClick(View view){

        if (popUpDialog != null && popUpDialog.isShowing()) {
            // Dismiss the existing dialog
            popUpDialog.dismiss();
        }

        // Inflate the popup layout
        View popupView = getLayoutInflater().inflate(R.layout.popup, null);

        // Find the EditText views in the popup view
        EditText editTextUsername = popupView.findViewById(R.id.Username);
        EditText editTextParola = popupView.findViewById(R.id.Password);
        EditText editTextFamilyName = popupView.findViewById(R.id.NumeFamilie);
        EditText editTextGivenName = popupView.findViewById(R.id.Prenume);
        EditText editTextMailAdress = popupView.findViewById(R.id.Email);

        // Set up a button in the popup layout to save the user data
        Button saveButton = popupView.findViewById(R.id.Inregistreaza);
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
        popUpDialog.show(); }

    private void addUserToDb(String username,String password,String familyName, String givenName, String email){
        User user = new User(username,password,familyName,givenName,email);

        mDatabase.push()
                .setValue(user)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firebase", "User added successfully");
                    // Add any additional actions if needed
                })
                .addOnFailureListener(e -> Log.e("Firebase", "Error adding user: " + e.getMessage()));


    }
}