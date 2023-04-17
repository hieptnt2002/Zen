package com.example.mobileapp.tesst;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobileapp.R;
import com.google.android.material.textfield.TextInputLayout;

public class gacon extends AppCompatActivity {

    //Mảng dữ liệu từ gợi ý (string)
    private static final String[] PRODUCTS = new String[]
            {
                    "Điện thoại XY",
                    "Máy tính AZ",
                    "Iphone", "Tai nghe", "Loa",
                    "Điện thoại XY",
                    "Máy tính AZ",
                    "Điện thoại XY",
                    "Máy tính AZ",
                    "Điện thoại XY",
                    "Máy tính AZ",
                    "Điện thoại XY",
                    "Máy tính AZ",
                    "Điện thoại XY",
                    "Máy tính AZ"
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gacon);


        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) AutoCompleteTextView textView = findViewById(R.id.product);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, PRODUCTS);
        TextInputLayout textInputLayout = findViewById(R.id.ga);
        textView.setAdapter(adapter);
        textView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0) textInputLayout.setHintEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
             if(s.length() > 10)    Toast.makeText(gacon.this, "Đã xong", Toast.LENGTH_SHORT).show();
            }
        });
        EditText editText = findViewById(R.id.chitestthoi);
        textView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == KeyEvent.ACTION_DOWN || event.getAction() == KeyEvent.KEYCODE_ENTER){
                    Toast.makeText(getApplicationContext(), "Đã thực hiện tìm kiếm", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });
    }
}