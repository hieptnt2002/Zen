package com.example.mobileapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mobileapp.constants.Constants;
import com.example.mobileapp.R;
import com.example.mobileapp.adapter.CartAdapter;
import com.example.mobileapp.fragment.BottomSheetFragment;
import com.example.mobileapp.model.Cart;
import com.example.mobileapp.model.Order;
import com.example.mobileapp.tesst.ZenithActivity;
import com.example.mobileapp.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {
    RecyclerView rvCart;
    TextView tvOut, tvTotal;
    LinearLayout layoutPay;
    Locale locale = new Locale("vi", "VN"); // Thiết lập địa phương Việt Nam
    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
    int total = 0;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        initGUI();
        tvOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ZenithActivity.class));
            }
        });
        rvCart.setHasFixedSize(true);
        rvCart.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        CartAdapter cartAdapter = new CartAdapter(Utils.listCart,this);
        rvCart.setAdapter(cartAdapter);

        if (Utils.listCart != null) {
            for (int i = 0; i < Utils.listCart.size(); i++) {
                total += Utils.listCart.get(i).getSaleprice() * Utils.listCart.get(i).getQuantity();
                Utils.total_pay = total;
            }
        }

        tvTotal.setText(String.valueOf(currencyFormatter.format(total)));
        cartAdapter.setOnClickQuantity(new CartAdapter.OnClickQuantity() {
            @Override
            public void onCick() {
                int total = 0;
                if (Utils.listCart != null) {
                    for (int i = 0; i < Utils.listCart.size(); i++) {
                        total += Utils.listCart.get(i).getSaleprice() * Utils.listCart.get(i).getQuantity();
                        Utils.total_pay = total;
                    }
                }
                tvTotal.setText(String.valueOf(currencyFormatter.format(total)));

            }
        });
        clickToOrder();
    }

    private void initGUI() {
        rvCart = findViewById(R.id.recycleView_cart);
        tvOut = findViewById(R.id.textView_out);
        tvTotal = findViewById(R.id.textView_total);
        layoutPay = findViewById(R.id.btn_add_pay);
    }

    private void clickToOrder() {
        layoutPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Order order = new Order(Utils.listCart, Utils.total_pay);
                BottomSheetFragment bottomSheetFragment = BottomSheetFragment.newInstance(order);
                bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
//                bottomSheetFragment.setCancelable(false);
            }
        });
    }


}