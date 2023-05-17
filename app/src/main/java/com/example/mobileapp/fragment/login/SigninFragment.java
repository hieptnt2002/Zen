package com.example.mobileapp.fragment.login;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mobileapp.R;
import com.example.mobileapp.constants.Constants;
import com.example.mobileapp.view.LoginMobileApp;
import com.example.mobileapp.model.Account;
import com.example.mobileapp.view.ZenithActivity;
import com.example.mobileapp.utils.Utils;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SigninFragment extends Fragment {
    TextView txtSignup, txtUser, txtPass;
    Button btnLogin;
    View view;
    LoginMobileApp loginMobileApp;
    ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_signin, container, false);
        initGUI();
        loadListAccount();
        onClickBtnLogin();
        return view;
    }

    private void initGUI() {
        txtSignup = view.findViewById(R.id.textSignup);
        loginMobileApp = (LoginMobileApp) getActivity();
        txtUser = view.findViewById(R.id.textUser);
        txtPass = view.findViewById(R.id.text_Pass);
        btnLogin = view.findViewById(R.id.buttonLogin);
        progressBar = view.findViewById(R.id.progressbar);
        txtSignup.setOnClickListener(view ->
                loginMobileApp.viewPager2.setCurrentItem(2)
        );
    }

    Account isCheckExist(String user, String email, String pass) {
        pass = Utils.sha256(pass);

        for (int i = 0; i < Utils.accountList.size(); i++) {
            if ((user.equals(Utils.accountList.get(i).getName()) || email.equals(Utils.accountList.get(i).getEmail())) &&
                    pass.equals(Utils.accountList.get(i).getPass()))
                return Utils.accountList.get(i);
        }
        return null;
    }

    private void onClickBtnLogin() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String user, email, pass;
                        user = email = txtUser.getText().toString();
                        pass = txtPass.getText().toString();
                        Account account = isCheckExist(user, email, pass);
                        if (user.isEmpty() || pass.isEmpty())
                            Toast.makeText(loginMobileApp, "Không được để trống", Toast.LENGTH_SHORT).show();
                        else if (account != null) {
                            Gson gson = new Gson();
                            String json = gson.toJson(account);
                            SharedPreferences.Editor editor = getActivity().getSharedPreferences(Utils.login_success, MODE_PRIVATE).edit();
                            editor.putString("object", json);
                            editor.putBoolean("isLoggedIn", true);
                            editor.apply();
                            Toast.makeText(loginMobileApp, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getContext(), ZenithActivity.class));
                        } else
                            Toast.makeText(loginMobileApp, "Bạn nhập sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }, 3000);
            }
        });
    }

    public void loadListAccount() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.API + "list_account.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject arr = jsonArray.getJSONObject(i);
                        Utils.accountList.add(new Account(arr.getInt("id"), arr.getString("email"), arr.getString("name"), arr.getString("pass")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Xảy ra lỗi", Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(getContext()).add(stringRequest);
    }
}
