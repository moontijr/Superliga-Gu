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

import com.example.myapplication.model.Player;
import com.example.myapplication.repository.MatchRepository;
import com.example.myapplication.repository.PlayerRepository;
import com.example.myapplication.repository.TeamRepository;
import com.example.myapplication.repository.UserRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    private UserRepository userRepository = new UserRepository();
    private PlayerRepository playerRepository = new PlayerRepository();
    private TeamRepository teamRepository = new TeamRepository();
    private MatchRepository matchRepository = new MatchRepository();
    private DatabaseReference mDatabase;

    private DatabaseReference playersDatabase;


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

        playersDatabase = FirebaseDatabase.getInstance().getReference().child("players");

        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        if (mDatabase == null) {
            Log.e("Database", "Database reference is null");
        } else {
            Log.d("Database", "Database reference initialized successfully");
        }

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            addPlayersToDb();
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

    private void addPlayersToDb(){
        Player Coman = new Player("P1","Florinel","Coman","Forward","Romania",4500000,"T1");
        Player Olaru = new Player("P2","Darius","Olaru","Midfielder","Romania",6000000,"T1");
        Player Manea = new Player("P3","Cristian","Manea","Defender","Romania",3500000,"T2");
        Player Birligea = new Player("P4","Daniel","Birligea","Forward","Romania",1000000,"T2");
        Player Amzar = new Player("P5","Costin","Amzar","Defender","Romania",4500000,"T3");
        Player Grozav = new Player("P6","Gicu","Grozav","Forward","Romania",350000,"T4");
        Player Budescu = new Player("P7","Constantin","Budescu","Forward","Romania",800000,"T5");
        Player Niczuly = new Player("P8","Roland","Niczuly","Goalkeeper","Romania",450000,"T6");
        Player Oroian = new Player("P9","Alexandru","Oroian","Defender","Romania",480000,"T7");
        Player Phelipe = new Player("P10","Luis","Phelipe","Forward","Brazil",452000,"T8");
        Player Nemec = new Player("P11","Adam","Nemec","Forward","Romania",155000,"T9");
        Player Ducan = new Player("P12","Razvan","Ducan","Goalkeeper","Romania",250000,"T10");
        Player Mitrita = new Player("P13","Alexandru","Mitrita","Forward","Romania",250000,"T11");
        Player Bauza = new Player("P14","Juan","Bauza","Forward","Argentina",220000,"T14");
        Player Miculescu = new Player("P15","David","Miculescu","Forward","Romania",424000,"T13");
        Player Nistor = new Player("P16","Dan","Nistor","Midfielder","Romania",390000,"T14");
        Player Moldovan = new Player("P17","Horatiu","Moldovan","Goalkeeper","Romania",410000,"T15");
        Player Krasniqi = new Player("P18","Ermal","Krasniqi","Forward","Kosovo",580000,"T16");
        Player Omrani = new Player("P19","Bilel","Omrani","Forward","France",250000,"T4");
        Player Mazilu = new Player("P20","Adrian","Mazilu","Forward","Romania",320000,"T5");
        addPlayersToDatabase(Coman);
        addPlayersToDatabase(Olaru);
        addPlayersToDatabase(Manea);
        addPlayersToDatabase(Birligea);
        addPlayersToDatabase(Amzar);
        addPlayersToDatabase(Grozav);
        addPlayersToDatabase(Bauza);
        addPlayersToDatabase(Budescu);
        addPlayersToDatabase(Niczuly);
        addPlayersToDatabase(Oroian);
        addPlayersToDatabase(Phelipe);
        addPlayersToDatabase(Nemec);
        addPlayersToDatabase(Ducan);
        addPlayersToDatabase(Mitrita);
        addPlayersToDatabase(Miculescu);
        addPlayersToDatabase(Nistor);
        addPlayersToDatabase(Moldovan);
        addPlayersToDatabase(Krasniqi);
        addPlayersToDatabase(Omrani);
        addPlayersToDatabase(Mazilu);


    }

    private void addPlayersToDatabase(Player player){
        playersDatabase.child(player.getId()).setValue(player)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firebase", "Team " + player.getFirstName() + " added successfully");
                })
                .addOnFailureListener(e -> {
                    Log.e("Firebase", "Error adding " + player.getFirstName() + ": " + e.getMessage());
                });
    }
}