package com.example.crimson;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Login extends AppCompatActivity {
    EditText emailAddress, password;
    Button createAcc, signInGoogle, login;
    FirebaseAuth mAuth;
    DatabaseReference donorsDBRef;
    String userId, currentUid;
    ProgressBar progressBar;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailAddress = findViewById(R.id.editTextTextEmailAddress2);
        password = findViewById(R.id.editTextTextPassword2);

        login = findViewById(R.id.button5);
        signInGoogle = findViewById(R.id.button6);
        createAcc = findViewById(R.id.button8);

        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                loginUser();
            }
        });

        createAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Registration.class));
            }
        });

    }

    private  void loginUser(){
        String email = emailAddress.getText().toString().trim();
        String pswd = password.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            progressBar.setVisibility(View.INVISIBLE);
            emailAddress.setError("Email cannot be empty");
            emailAddress.requestFocus();
        } else if(TextUtils.isEmpty(pswd)){
            progressBar.setVisibility(View.INVISIBLE);
            password.setError("Password cannot be empty");
            password.requestFocus();
        }
        else{
            mAuth.signInWithEmailAndPassword(email, pswd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
                        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                        donorsDBRef = db.child("Donors");

                        donorsDBRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if(task.isSuccessful()){
                                    for(DataSnapshot snapshot: task.getResult().getChildren()){
                                        for(DataSnapshot innerSnapshot : snapshot.getChildren()){
                                            if (innerSnapshot.child("userId").exists()) {
                                                userId = innerSnapshot.child("userId").getValue().toString();
                                                System.out.println(userId);
                                                if(userId.equals(currentUid)){
                                                    startActivity(new Intent(Login.this, donorHomePage.class));
                                                    count = 1;
                                                    break;
                                                }
                                                if(count == 0){
                                                    startActivity(new Intent(Login.this, BloodBankMain.class));
                                                }
                                            }
                                        }
                                    }

                                }
                            }
                        });
//                        startActivity(new Intent(Login.this, BloodBankMain.class));
                    }else{
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(Login.this, "Unsuccessful user login." + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

}