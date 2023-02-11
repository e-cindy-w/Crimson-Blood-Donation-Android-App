package com.example.crimson;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class editProfile extends AppCompatActivity {
    DatabaseReference donorDB;
    EditText fName, lName, nationalID, phoneNo, editTextDate;
    String fName2, lName2, nationalID2, bloodType2, phoneNo2, gender, editTextDate2;
    Button update;
    Spinner spinnerBloodType, spinnerSex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        donorDB = FirebaseDatabase.getInstance().getReference().child("Donors");
        fName = findViewById(R.id.editTextTextPersonName);
        lName = findViewById(R.id.editTextTextPersonName2);
        nationalID = findViewById(R.id.editTextNumberSigned);
        phoneNo = findViewById(R.id.editTextPhone);
        spinnerBloodType = findViewById(R.id.spinnerBloodType);
        spinnerSex = findViewById(R.id.spinnerSex);
        update = findViewById(R.id.button9);
        editTextDate = findViewById(R.id.editTextDate);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fName2 = fName.getText().toString().trim();
                lName2 = lName.getText().toString().trim();
                nationalID2 = nationalID.getText().toString().trim();
                bloodType2 = spinnerBloodType.getSelectedItem().toString().trim();
                phoneNo2 = phoneNo.getText().toString().trim();
                gender = spinnerSex.getSelectedItem().toString().trim();
                editTextDate2 = editTextDate.getText().toString().trim();

                HashMap hashMap = new HashMap();
                hashMap.put("firstName", fName2);
                hashMap.put("lastName", lName2);
                hashMap.put("nationalID", nationalID2);
                hashMap.put("phoneNo", phoneNo2);
                hashMap.put("sex", gender);
                hashMap.put("bloodType", bloodType2);
                hashMap.put("lastDonationDate", editTextDate2);


                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String currentUid = user.getUid();


                donorDB.child("A+").updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        Toast.makeText(editProfile.this, "Profile Update", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(editProfile.this, donorHomePage.class));
                    }
                });



            }
        });
    }

}