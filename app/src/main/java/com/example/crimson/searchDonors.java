package com.example.crimson;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class searchDonors extends AppCompatActivity {
    Spinner spinnerBloodType;
    String bloodType, lastDonationDate, donorPhoneNo, fullName, firstName, lastName, bankName, bankPhone, bankLocation, latitude, longitude;
    Button submit;
    ListView myListView;
    TextView textView10;
    DatabaseReference donorsDBRef, bloodBankDBRef;
    List<donorUser> donorUserList;
    ArrayList<String> donorPhoneNumbers = new ArrayList<>();
    ArrayList<String> donorFullNames = new ArrayList<>();
    FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_donors);

        spinnerBloodType = findViewById(R.id.spinnerBloodType);
        submit = findViewById(R.id.button10);
        textView10 = findViewById(R.id.textView10);
        donorUserList = new ArrayList<>();
        myListView = findViewById(R.id.myListView);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bloodType = spinnerBloodType.getSelectedItem().toString().trim();

                switch (bloodType) {
                    case "A+":
                        donorsDBRef = FirebaseDatabase.getInstance().getReference("Donors").child("A+");
                        searchForDonor();
                        break;
                    case "A-":
                        donorsDBRef = FirebaseDatabase.getInstance().getReference("Donors").child("A-");
                        searchForDonor();
                        break;
                    case "B+":
                        donorsDBRef = FirebaseDatabase.getInstance().getReference("Donors").child("B+");
                        searchForDonor();
                        break;
                    case "B-":
                        donorsDBRef = FirebaseDatabase.getInstance().getReference("Donors").child("B-");
                        searchForDonor();
                        break;
                    case "O+":
                        donorsDBRef = FirebaseDatabase.getInstance().getReference("Donors").child("O+");
                        searchForDonor();
                        break;
                    case "O-":
                        donorsDBRef = FirebaseDatabase.getInstance().getReference("Donors").child("O-");
                        searchForDonor();
                        break;
                    case "AB+":
                        donorsDBRef = FirebaseDatabase.getInstance().getReference("Donors").child("AB+");
                        searchForDonor();
                        break;
                    case "AB-":
                        donorsDBRef = FirebaseDatabase.getInstance().getReference("Donors").child("AB-");
                        searchForDonor();
                        break;
                }
            }
        });
    }

    private void searchForDonor() {
        donorsDBRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DAY_OF_YEAR, -56);
                Date datedays = cal.getTime();
                String beforeDate = datedays.toString();

                donorUserList.clear();

                for (DataSnapshot donorDataSnap : snapshot.getChildren()) {
                    if (donorDataSnap.child("lastDonationDate").exists()) {
                        lastDonationDate = donorDataSnap.child("lastDonationDate").getValue().toString();

                        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);

                        try {
                            Date lastDonation = formatter.parse(lastDonationDate);
                            System.out.println(lastDonation);
                            if (lastDonation.before(datedays)) {
                                donorUser donorUser = donorDataSnap.getValue(donorUser.class);
                                donorUserList.add(donorUser);

                                firstName = donorDataSnap.child("firstName").getValue().toString();
                                lastName = donorDataSnap.child("lastName").getValue().toString();
                                fullName = firstName + " " + lastName;
                                donorPhoneNo = donorDataSnap.child("phoneNo").getValue().toString();
                                donorPhoneNumbers.add(donorPhoneNo);
                                donorFullNames.add(fullName);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }else{
                        donorUser donorUser = donorDataSnap.getValue(donorUser.class);
                        donorUserList.add(donorUser);
                        firstName = donorDataSnap.child("firstName").getValue().toString();
                        lastName = donorDataSnap.child("lastName").getValue().toString();
                        fullName = firstName + " " + lastName;
                        donorPhoneNo = donorDataSnap.child("phoneNo").getValue().toString();
                        donorPhoneNumbers.add(donorPhoneNo);
                        donorFullNames.add(fullName);
                    }
                }
                notifyDonor();
                ListAdapter adapter = new ListAdapter(searchDonors.this, donorUserList);
                myListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void notifyDonor(){
        bloodBankDBRef = FirebaseDatabase.getInstance().getReference("Blood Banks");
        bloodBankDBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot bloodBankSnap : snapshot.getChildren()){
                    bankName = bloodBankSnap.child("bankName").getValue().toString();
                    bankPhone = bloodBankSnap.child("phoneNo").getValue().toString();
                    bankLocation = bloodBankSnap.child("location").getValue().toString();
                }
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                        for (int i = 0; i < donorPhoneNumbers.size(); i++){
                            String message1 = "Hello, " + donorFullNames.get(i) + "\nA blood donation appeal has been made at " + bankName + "\nIf you are available, please call " + bankPhone + " or follow the link below.";
                            String message = message1.trim();
                            try {
                                SmsManager smsManager = SmsManager.getDefault();
                                smsManager.sendTextMessage(donorPhoneNumbers.get(i), null, message, null, null);
                                smsManager.sendTextMessage(donorPhoneNumbers.get(i), null, "http://www.maps.google.com", null, null);
                                textView10.setText("Donors have been notified.");
                                textView10.setBackgroundColor(Color.parseColor("#3cb043"));
                            }catch (Exception e){
                                e.printStackTrace();
                                Toast.makeText(searchDonors.this, "Failed to notify donors. Try again", Toast.LENGTH_LONG).show();
                            }
                        }
                    }else{
                        requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 1);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}