package com.example.mobileapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.example.mobileapp.R;
import com.example.mobileapp.adapter.LoginViewPagerAdapter;

public class LoginMobileApp extends AppCompatActivity {
    public ViewPager2 viewPager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        viewPager2 = findViewById(R.id.viewPager);
        LoginViewPagerAdapter loginViewPagerAdapter = new LoginViewPagerAdapter(this);
        viewPager2.setAdapter(loginViewPagerAdapter);
        viewPager2.setUserInputEnabled(false);
        viewPager2.setCurrentItem(0);
    }
}