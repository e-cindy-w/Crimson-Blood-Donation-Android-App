package com.example.crimson;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

public class Registration extends AppCompatActivity {
    private static final int RC_SIGN_IN = 123;
    EditText emailAddress, password;
    Button createAcc, signUpGoogle, login;
    FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    ProgressBar progressBar2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        createAcc = findViewById(R.id.button);
        signUpGoogle = findViewById(R.id.button2);
        login = findViewById(R.id.button4);
        progressBar2 = findViewById(R.id.progressBar2);


        emailAddress = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextTextPassword);

        mAuth = FirebaseAuth.getInstance();

        createAcc.setOnClickListener(view -> {
            progressBar2.setVisibility(View.VISIBLE);
            createUser();
        });

        login.setOnClickListener(view -> {
            startActivity(new Intent(Registration.this, Login.class));
        });

        signUpGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestGoogleSignIn();
                signIn();
            }
        });

    }
    private void createUser(){
        String email = emailAddress.getText().toString().trim();
        String pswd = password.getText().toString().trim();
        if(TextUtils.isEmpty(email)){
            emailAddress.setError("Email cannot be empty");
            emailAddress.requestFocus();
            progressBar2.setVisibility(View.INVISIBLE);
        } else if(TextUtils.isEmpty(pswd)){
            password.setError("Password cannot be empty");
            password.requestFocus();
            progressBar2.setVisibility(View.INVISIBLE);
        } else{
            mAuth.createUserWithEmailAndPassword(email, pswd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(Registration.this, "User registration successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Registration.this, UserType.class));
                    } else{
                        progressBar2.setVisibility(View.INVISIBLE);
                        Toast.makeText(Registration.this, "Unsuccessful user registration." + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void requestGoogleSignIn(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signIn(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e){
                progressBar2.setVisibility(View.INVISIBLE);
                Toast.makeText(Registration.this, "Unsuccessful user registration." + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
    }
}

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Registration.this, "User registration successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Registration.this, UserType.class));
                        } else{
                            progressBar2.setVisibility(View.INVISIBLE);
                            Toast.makeText(Registration.this, "Unsuccessful user registration." + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}