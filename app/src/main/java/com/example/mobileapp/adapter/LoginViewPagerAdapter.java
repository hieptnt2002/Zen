package com.example.mobileapp.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.mobileapp.fragment.login.ForgotPassFragment;
import com.example.mobileapp.fragment.login.StartFragment;
import com.example.mobileapp.fragment.login.SigninFragment;
import com.example.mobileapp.fragment.login.SignupFragment;

public class LoginViewPagerAdapter extends FragmentStateAdapter {
    public LoginViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0: return new StartFragment();
            case 1: return new SigninFragment();
            case 2: return new SignupFragment();
            case 3: return new ForgotPassFragment();
            default: return  new StartFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
