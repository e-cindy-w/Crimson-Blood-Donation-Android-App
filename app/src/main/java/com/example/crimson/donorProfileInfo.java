package com.example.crimson;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Calendar;

public class donorProfileInfo extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    EditText fName, lName, nationalID, phoneNo;
    String fName2, lName2, nationalID2, bloodType2, phoneNo2, emailAdd, gender, userID, lastDonationDate;
    Button submit;
    Spinner spinnerBloodType, spinnerSex;
    DatabaseReference donorsDB;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_profile_info);

        fName = findViewById(R.id.editTextTextPersonName);
        lName = findViewById(R.id.editTextTextPersonName2);
        nationalID = findViewById(R.id.editTextNumberSigned);
        phoneNo = findViewById(R.id.editTextPhone);
        submit = findViewById(R.id.button9);
        spinnerBloodType = findViewById(R.id.spinnerBloodType);
        spinnerSex = findViewById(R.id.spinnerSex);

        emailAdd = getIntent().getStringExtra("key");
        donorsDB = FirebaseDatabase.getInstance().getReference("Donors");



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fName2 = fName.getText().toString().trim();
                lName2 = lName.getText().toString().trim();
                nationalID2 = nationalID.getText().toString().trim();
                bloodType2 = spinnerBloodType.getSelectedItem().toString().trim();
                phoneNo2 = phoneNo.getText().toString().trim();
                gender = spinnerSex.getSelectedItem().toString().trim();
                userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                if(TextUtils.isEmpty(fName2)){
                    fName.setError("First Name cannot be empty");
                    fName.requestFocus();
                } else if(TextUtils.isEmpty(lName2)){
                    lName.setError("Last Name cannot be empty");
                    lName.requestFocus();
                } else if(TextUtils.isEmpty(nationalID2)){
                    nationalID.setError("National ID cannot be empty");
                    nationalID.requestFocus();
                }else if(TextUtils.isEmpty(phoneNo2)){
                    phoneNo.setError("Phone number cannot be empty");
                    phoneNo.requestFocus();
                }else {
                    builder = new AlertDialog.Builder(donorProfileInfo.this);
                    builder.setTitle("Crimson")
                            .setMessage("Have you ever donated blood?")
                            .setCancelable(true)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //Datepicker
                                    DialogFragment datePicker = new DatePickerFragment();
                                    datePicker.show(getSupportFragmentManager(), "date picker");
                                }
                            })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                            insertDonors();
                                        }
                                    }).show();
                }

            }
        });
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, i);
        c.set(Calendar.MONTH, i1);
        c.set(Calendar.DAY_OF_MONTH, i2);
        lastDonationDate = DateFormat.getDateInstance(DateFormat.MEDIUM).format(c.getTime());
        insertDonors();


    }

    private void insertDonors() {
        donorUser donorUser = new donorUser(userID, fName2, lName2, gender, nationalID2, bloodType2, phoneNo2, lastDonationDate);
        switch (donorUser.bloodType) {
            case "A+":
                donorsDB.child("A+").push().setValue(donorUser);
                Toast.makeText(donorProfileInfo.this, "Added to profile", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(donorProfileInfo.this, donorHomePage.class));
                break;
            case "A-":
                donorsDB.child("A-").push().setValue(donorUser);
                Toast.makeText(donorProfileInfo.this, "Added to profile", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(donorProfileInfo.this, donorHomePage.class));
                break;
            case "B+":
                donorsDB.child("B+").push().setValue(donorUser);
                Toast.makeText(donorProfileInfo.this, "Added to profile", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(donorProfileInfo.this, donorHomePage.class));
                break;
            case "B-":
                donorsDB.child("B-").push().setValue(donorUser);
                Toast.makeText(donorProfileInfo.this, "Added to profile", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(donorProfileInfo.this, donorHomePage.class));
                break;
            case "O+":
                donorsDB.child("O+").push().setValue(donorUser);
                Toast.makeText(donorProfileInfo.this, "Added to profile", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(donorProfileInfo.this, donorHomePage.class));
                break;
            case "O-":
                donorsDB.child("O-").push().setValue(donorUser);
                Toast.makeText(donorProfileInfo.this, "Added to profile", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(donorProfileInfo.this, donorHomePage.class));
                break;
            case "AB+":
                donorsDB.child("AB+").push().setValue(donorUser);
                Toast.makeText(donorProfileInfo.this, "Added to profile", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(donorProfileInfo.this, donorHomePage.class));
                break;
            case "AB-":
                donorsDB.child("AB-").push().setValue(donorUser);
                Toast.makeText(donorProfileInfo.this, "Added to profile", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(donorProfileInfo.this, donorHomePage.class));
                break;
        }
    }
}
