package com.example.mobileapp.fragment.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mobileapp.R;
import com.example.mobileapp.activity.LoginMobileApp;

import org.w3c.dom.Text;

public class SigninFragment extends Fragment {
    TextView txtSignup;
    View view;
    LoginMobileApp loginMobileApp;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_signin,container,false);
        txtSignup = view.findViewById(R.id.textSignup);
        loginMobileApp = (LoginMobileApp)  getActivity();
        txtSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginMobileApp.viewPager2.setCurrentItem(2);
            }
        });
        return view;
    }
}
