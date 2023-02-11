package com.example.crimson;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class UserType extends AppCompatActivity {
    RadioGroup users;
    RadioButton radioBtn;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_type);

        users = (RadioGroup)findViewById(R.id.radioGroup);
        submit = (Button)findViewById(R.id.button);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selected = users.getCheckedRadioButtonId();
                radioBtn = (RadioButton)findViewById(selected);
                if(radioBtn.getText().toString().trim().equals("Donor")){
                    startActivity(new Intent(UserType.this, donorProfileInfo.class));
                } else if(radioBtn.getText().toString().trim().equals("Blood Bank")){
                    startActivity(new Intent(UserType.this, bloodBankHomePage.class));
                }
//                Toast.makeText(UserType.this, radioBtn.getText(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}