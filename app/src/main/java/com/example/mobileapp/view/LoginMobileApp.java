package com.example.mobileapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.mobileapp.R;
import com.example.mobileapp.adapter.LoginViewPagerAdapter;
import com.example.mobileapp.utils.Utils;

public class LoginMobileApp extends AppCompatActivity {
    public ViewPager2 viewPager2;
    public int currentItem = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        viewPager2 = findViewById(R.id.viewPager);
        LoginViewPagerAdapter loginViewPagerAdapter = new LoginViewPagerAdapter(this);
        viewPager2.setAdapter(loginViewPagerAdapter);
        viewPager2.setUserInputEnabled(false);
        currentItem = getIntent().getIntExtra("currentItem",0);
        viewPager2.setCurrentItem(currentItem);
        SharedPreferences prefs = getSharedPreferences(Utils.login_success, MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);
        if (isLoggedIn) {
            // Chuyển hướng sang màn hình chính
            startActivity(new Intent(this, ZenithActivity.class));
        } else {
            // Hiển thị màn hình đăng nhập
        }
    }
}