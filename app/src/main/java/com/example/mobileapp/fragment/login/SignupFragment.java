package com.example.mobileapp.fragment.login;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mobileapp.R;
import com.example.mobileapp.view.LoginMobileApp;
import com.example.mobileapp.constants.Constants;
import com.example.mobileapp.model.Account;
import com.example.mobileapp.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignupFragment extends Fragment {
    View view;
    EditText edtName, edtEmail, edtPass;
    Button btnSignup;
    String name, email, pass;
    LoginMobileApp loginMobileApp;
    ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_signup, container, false);
        initGUI();

        name = edtName.getText().toString();
        email = edtEmail.getText().toString();

        loadListAccount();
        eventClickBtn();
        return view;
    }

    void initGUI() {
        edtName = view.findViewById(R.id.signUp_user);
        edtEmail = view.findViewById(R.id.signUP_email);
        edtPass = view.findViewById(R.id.signUP_password);
        btnSignup = view.findViewById(R.id.buttonSignup);
        loginMobileApp = (LoginMobileApp) getActivity();
        progressBar = view.findViewById(R.id.progressbar);
    }

    boolean isCheckExist() {

        for (int i = 0; i < Utils.accountList.size(); i++) {
            if (name.equals(Utils.accountList.get(i).getName()) || email.equals(Utils.accountList.get(i).getEmail()))
                return true;
        }
        return false;
    }

    private void eventClickBtn() {
        edtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) edtName.setError("Bắt buộc nhập tên người dùng");
                else edtName.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        name = edtName.getText().toString();
                        email = edtEmail.getText().toString();
                        pass = edtPass.getText().toString();
                        if (name.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                            Toast.makeText(getContext(), "Không được để trống", Toast.LENGTH_SHORT).show();
                        } else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                            Toast.makeText(getContext(), "Email không hợp lệ", Toast.LENGTH_SHORT).show();
                        }else if (isCheckExist())
                            Toast.makeText(getContext(), "Tài khoản đã tồn tại", Toast.LENGTH_SHORT).show();
                        else eventCreateAcccount();
                        progressBar.setVisibility(View.GONE);
                    }
                }, 3000);
            }
        });
    }

    private void eventCreateAcccount() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.API + "sign__up.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("sign up successful")) {
                    Toast.makeText(view.getContext(), "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                    loginMobileApp.viewPager2.setCurrentItem(1);
                    loadListAccount();
                } else
                    Toast.makeText(view.getContext(), "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(view.getContext(), "Lỗi", Toast.LENGTH_SHORT).show();
                Log.d("AAA", "Loi!\n" + error.toString());
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", edtEmail.getText().toString());
                params.put("account_name", edtName.getText().toString());
                String pass = edtPass.getText().toString();
                pass = Utils.sha256(pass);
                params.put("account_password", pass);
                return params;
            }
        };
        Volley.newRequestQueue(getContext()).add(stringRequest);
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
