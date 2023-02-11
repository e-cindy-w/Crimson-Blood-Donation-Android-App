package com.example.crimson;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class donorHomePage extends AppCompatActivity {
    Button logout, viewBloodAppeals, deleteAcc;
    FirebaseAuth mAuth;
    AlertDialog.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_home_page);
        mAuth = FirebaseAuth.getInstance();
        logout = findViewById(R.id.logout);
        viewBloodAppeals = findViewById(R.id.viewBloodAppeals);
        deleteAcc = findViewById(R.id.deleteAcc);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                finish();
                startActivity(new Intent(donorHomePage.this, Login.class));

            }
        });

        viewBloodAppeals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(donorHomePage.this, editProfile.class));

            }
        });

        deleteAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder = new AlertDialog.Builder(donorHomePage.this);
                builder.setTitle("Crimson")
                        .setMessage("This action can not be reversed! \nDo you wish to proceed?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                user.delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(donorHomePage.this, "User account deleted", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(donorHomePage.this, Registration.class));
                                                }
                                            }
                                        });
                                }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        }).show();


            }
        });

    }
}