package com.example.crimson;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class bloodBankHomePage extends AppCompatActivity {
    EditText editText, editText2, editText3;
    Button submit;
    DatabaseReference bloodBankDB;
    String bankName, phoneNo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_bank_home_page);
        editText = findViewById(R.id.editTextTextPersonName3);
        editText2 = findViewById(R.id.editTextPhone2);
        editText3 = findViewById(R.id.editTextTextPersonName4);
        submit = findViewById(R.id.button7);
        bloodBankDB = FirebaseDatabase.getInstance().getReference("Blood Banks");


        editText3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bankName = editText.getText().toString().trim();
                phoneNo = editText2.getText().toString().trim();
                Bundle bundle = new Bundle();
                bundle.putString("key1", bankName);
                bundle.putString("key2", phoneNo);
                Intent intent2 = new Intent(bloodBankHomePage.this, findLocation.class);
                intent2.putExtras(bundle);
                startActivity(intent2);
                System.out.println(bankName + phoneNo);

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(bloodBankHomePage.this, "Enter blood bank location!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}