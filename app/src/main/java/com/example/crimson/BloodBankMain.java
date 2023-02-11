package com.example.crimson;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class BloodBankMain extends AppCompatActivity {
    Button viewDonors, searchDonors, logout;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_bank_main);

        viewDonors = findViewById(R.id.viewDonors);
        searchDonors = findViewById(R.id.searchDonors);
        logout = findViewById(R.id.button12);
        mAuth = FirebaseAuth.getInstance();


        viewDonors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BloodBankMain.this, listOfDonors.class));
            }
        });

        searchDonors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BloodBankMain.this, searchDonors.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                finish();
                startActivity(new Intent(BloodBankMain.this, Login.class));

            }
        });
    }
}