package com.example.crimson;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class listOfDonors extends AppCompatActivity {
    Button APlus, Aminus, BPlus, BMinus, OPlus, OMinus, ABPlus, ABMinus;
    ListView myListView;
    List<donorUser> donorUserList;

    DatabaseReference donorsDBRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_donors);

        myListView = findViewById(R.id.myListView);
        donorUserList = new ArrayList<>();

        APlus = findViewById(R.id.APlus);
        Aminus = findViewById(R.id.Aminus);
        BPlus = findViewById(R.id.BPlus);
        BMinus = findViewById(R.id.BMinus);
        OPlus = findViewById(R.id.OPlus);
        OMinus = findViewById(R.id.OMinus);
        ABPlus = findViewById(R.id.ABPlus);
        ABMinus = findViewById(R.id.ABMinus);

        APlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                donorsDBRef = FirebaseDatabase.getInstance().getReference("Donors").child("A+");
                retrieveData();
            }
        });

        Aminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                donorsDBRef = FirebaseDatabase.getInstance().getReference("Donors").child("A-");
                retrieveData();
            }
        });

        BPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                donorsDBRef = FirebaseDatabase.getInstance().getReference("Donors").child("B+");
                retrieveData();
            }
        });

        BMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                donorsDBRef = FirebaseDatabase.getInstance().getReference("Donors").child("B-");
                retrieveData();
            }
        });

        OPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                donorsDBRef = FirebaseDatabase.getInstance().getReference("Donors").child("O+");
                retrieveData();
            }
        });

        OMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                donorsDBRef = FirebaseDatabase.getInstance().getReference("Donors").child("O-");
                retrieveData();
            }
        });

        ABPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                donorsDBRef = FirebaseDatabase.getInstance().getReference("Donors").child("AB+");
                retrieveData();
            }
        });

        ABMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                donorsDBRef = FirebaseDatabase.getInstance().getReference("Donors").child("AB-");
                retrieveData();
            }
        });

    }

    private void retrieveData() {
        donorsDBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                donorUserList.clear();

                for(DataSnapshot donorDataSnap : snapshot.getChildren()){
                    donorUser donorUser = donorDataSnap.getValue(donorUser.class);
                    donorUserList.add(donorUser);
                }
                ListAdapter adapter = new ListAdapter(listOfDonors.this, donorUserList);
                myListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}