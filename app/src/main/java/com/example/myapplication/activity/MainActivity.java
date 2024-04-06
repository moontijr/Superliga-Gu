package com.example.myapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.model.Player;
import com.example.myapplication.model.User;
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

        Log.d("Firebase", "Initializing Firebase");
        FirebaseApp.initializeApp(this);
        Log.d("Firebase", "Firebase initialized successfully");


        playersDatabase = FirebaseDatabase.getInstance().getReference().child("players");

        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        if (mDatabase == null) {
            Log.e("Database", "Database reference is null");
        } else {
            Log.d("Database", "Database reference initialized successfully");
        }

        firebaseAuth = FirebaseAuth.getInstance();

        Button registerPopupButton = findViewById(R.id.registerPopupButton);
        registerPopupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRegisterPopupDialog();
            }
        });

        Button loginPopupButton = findViewById(R.id.loginPopupButton);
        loginPopupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoginPopupDialog();
            }
        });
    }

    private void showRegisterPopupDialog() {
        Dialog popupDialog = new Dialog(this);

        popupDialog.setContentView(R.layout.register_popup);

        EditText editTextUsername = popupDialog.findViewById(R.id.Username);
        EditText editTextPassword = popupDialog.findViewById(R.id.Password);
        EditText editTextFamilyName = popupDialog.findViewById(R.id.NumeFamilie);
        EditText editTextGivenName = popupDialog.findViewById(R.id.Prenume);
        EditText editTextMailAddress = popupDialog.findViewById(R.id.Email);
        Button registerAction = popupDialog.findViewById(R.id.registerAction);

        registerAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();
                String familyName = editTextFamilyName.getText().toString();
                String givenName = editTextGivenName.getText().toString();
                String email = editTextMailAddress.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    editTextMailAddress.setError("Enter a valid email address");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    editTextPassword.setError("Enter a password");
                    return;
                }

                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Bine ați venit!", Toast.LENGTH_SHORT).show();

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


    private void addUserToDb(String username, String password, String familyName, String givenName, String email) {
        User user = new User(username, password, familyName, givenName, email);

        mDatabase.push()
                .setValue(user)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firebase", "User added successfully");
                })
                .addOnFailureListener(e -> Log.e("Firebase", "Error adding user: " + e.getMessage()));
    }

    private void showLoginPopupDialog() {
        Dialog popupDialog = new Dialog(this);

        popupDialog.setContentView(R.layout.login_popup);

        EditText editTextEmail = popupDialog.findViewById(R.id.loginEmail);
        EditText editTextPassword = popupDialog.findViewById(R.id.loginPassword);
        Button loginAction = popupDialog.findViewById(R.id.loginAction);
        loginAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    editTextEmail.setError("Enter a valid email address");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    editTextPassword.setError("Enter a password");
                    return;
                }

                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Bine ați revenit!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MatchdaysActivity.class));
                        } else {
                            Toast.makeText(MainActivity.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        popupDialog.show();
    }

    private void addPlayersToDb() {
        Player Coman = new Player("P1", "Florinel", "Coman", "Forward", "Romania", 4500000, "T1");
        Player Olaru = new Player("P2", "Darius", "Olaru", "Midfielder", "Romania", 6000000, "T1");
        Player Tavi = new Player("P21", "Tavi", "Popescu", "Forward", "Romania", 2000000, "T1");
        Player Ngezana = new Player("P22", "Syiabonga", "Ngezana", "Defender", "South-Africa", 6000000, "T1");
        Player Sut = new Player("P23", "Adrian", "Sut", "Midfielder", "Romania", 2250000, "T1");
        Player Tarnovanu = new Player("P24", "Stefan", "Tarnovanu", "Goalkeeper", "Romania", 1000000, "T1");
        Player Baba = new Player("P25", "Baba", "Alhassan", "Midfielder", "Romania", 800000, "T1");
        Player Manea = new Player("P3", "Cristian", "Manea", "Defender", "Romania", 3500000, "T2");
        Player Birligea = new Player("P4", "Daniel", "Birligea", "Forward", "Romania", 1000000, "T2");
        Player Otele = new Player("P26", "Philipe", "Otele", "Forward", "Romania", 1000000, "T2");
        Player Tachtsidis = new Player("P27", "Panagyotis", "Tachtsidis", "Midfielder", "Romania", 1000000, "T2");
        Player Sava = new Player("P28", "Razvan", "Sava", "Goalkeeper", "Romania", 1000000, "T2");
        Player Deac = new Player("P29", "Ciprian", "Deac", "Forward", "Romania", 1000000, "T2");
        Player Amzar = new Player("P5", "Costin", "Amzar", "Defender", "Romania", 4500000, "T3");
        Player Moura = new Player("P30", "Gabriel", "Moura", "Defender", "Romania", 4500000, "T3");
        Player Ghezali = new Player("P31", "Lamine", "Ghezali", "Defender", "Romania", 4500000, "T3");
        Player Politic = new Player("P32", "Denis", "Politic", "Defender", "Romania", 4500000, "T3");
        Player Golubovic = new Player("P33", "Adrian", "Golubovic", "Defender", "Romania", 4500000, "T3");
        Player Grozav = new Player("P34", "Gicu", "Grozav", "Forward", "Romania", 350000, "T4");
        Player Budescu = new Player("P35", "Constantin", "Budescu", "Forward", "Romania", 800000, "T5");
        Player Ticu = new Player("P36", "Valentin", "Ticu", "Defender", "Romania", 800000, "T5");
        Player Papp = new Player("P37", "Paul", "Papp", "Defender", "Romania", 800000, "T5");
        Player Jefferson = new Player("P38", "Alex", "Jefferson", "Forward", "Brazil", 800000, "T5");
        Player Jair = new Player("P39", "Mias", "Jair", "Forward", "Brazil", 800000, "T5");
        Player Niczuly = new Player("P8", "Roland", "Niczuly", "Goalkeeper", "Romania", 450000, "T6");
        Player Ciobotariu = new Player("P40", "Denis", "Ciobotariu", "Goalkeeper", "Romania", 450000, "T6");
        Player Balasa = new Player("P41", "Mihai", "Balasa", "Goalkeeper", "Romania", 450000, "T6");
        Player Stefan = new Player("P42", "Florin", "Stefan", "Goalkeeper", "Romania", 450000, "T6");
        Player Junior = new Player("P43", "Francisco", "Junior", "Goalkeeper", "Romania", 450000, "T6");
        Player Oroian = new Player("P9", "Alexandru", "Oroian", "Defender", "Romania", 480000, "T7");
        Player Phelipe = new Player("P10", "Luis", "Phelipe", "Forward", "Brazil", 452000, "T8");
        Player Nemec = new Player("P11", "Adam", "Nemec", "Forward", "Romania", 155000, "T9");
        Player Ducan = new Player("P12", "Razvan", "Ducan", "Goalkeeper", "Romania", 250000, "T10");
        Player Mitrita = new Player("P13", "Alexandru", "Mitrita", "Forward", "Romania", 250000, "T11");
        Player Bauza = new Player("P14", "Juan", "Bauza", "Forward", "Argentina", 220000, "T14");
        Player Miculescu = new Player("P15", "David", "Miculescu", "Forward", "Romania", 424000, "T13");
        Player Nistor = new Player("P16", "Dan", "Nistor", "Midfielder", "Romania", 390000, "T14");
        Player Moldovan = new Player("P17", "Horatiu", "Moldovan", "Goalkeeper", "Romania", 410000, "T15");
        Player Krasniqi = new Player("P18", "Ermal", "Krasniqi", "Forward", "Kosovo", 580000, "T16");
        Player Omrani = new Player("P19", "Bilel", "Omrani", "Forward", "France", 250000, "T4");
        Player Mazilu = new Player("P20", "Adrian", "Mazilu", "Forward", "Romania", 320000, "T5");
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
        addPlayersToDatabase(Tavi);
        addPlayersToDatabase(Ngezana);
        addPlayersToDatabase(Sut);
        addPlayersToDatabase(Tarnovanu);
        addPlayersToDatabase(Baba);
        addPlayersToDatabase(Sava);
        addPlayersToDatabase(Tachtsidis);
        addPlayersToDatabase(Otele);
        addPlayersToDatabase(Deac);
        addPlayersToDatabase(Moura);
        addPlayersToDatabase(Ghezali);
        addPlayersToDatabase(Politic);
        addPlayersToDatabase(Golubovic);
        addPlayersToDatabase(Ticu);
        addPlayersToDatabase(Papp);
        addPlayersToDatabase(Jefferson);
        addPlayersToDatabase(Jair);
        addPlayersToDatabase(Ciobotariu);
        addPlayersToDatabase(Stefan);
        addPlayersToDatabase(Balasa);
        addPlayersToDatabase(Junior);

    }

    private void addPlayersToDatabase(Player player) {
        playersDatabase.child(player.getId()).setValue(player)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firebase", "Team " + player.getFirstName() + " added successfully");
                })
                .addOnFailureListener(e -> {
                    Log.e("Firebase", "Error adding " + player.getFirstName() + ": " + e.getMessage());
                });
    }
}