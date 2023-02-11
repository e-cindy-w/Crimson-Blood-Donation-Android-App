package com.example.crimson;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ramotion.paperonboarding.PaperOnboardingFragment;
import com.ramotion.paperonboarding.PaperOnboardingPage;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                startActivity(new Intent(MainActivity.this, Login.class));
            }
        }, 2000);

        FragmentManager fragmentManager = getSupportFragmentManager();
        PaperOnboardingFragment paperOnboardingFragment = PaperOnboardingFragment.newInstance(getDataForOnBoarding());
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.animation, paperOnboardingFragment);
        fragmentTransaction.commit();
    }

    private ArrayList<PaperOnboardingPage> getDataForOnBoarding() {
        PaperOnboardingPage src1 = new PaperOnboardingPage("", "", Color.parseColor("#A20016"), R.drawable.logo, R.drawable.logotwo);

        ArrayList<PaperOnboardingPage> elements = new ArrayList<>();
        elements.add(src1);
        return elements;
    }

}