package com.example.mobileapp.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mobileapp.R;
import com.example.mobileapp.adapter.SliderImagesAdapter;
import com.example.mobileapp.constants.Constants;
import com.example.mobileapp.model.Account;
import com.example.mobileapp.model.Banner;
import com.example.mobileapp.model.Cart;
import com.example.mobileapp.model.Product;
import com.example.mobileapp.utils.Utils;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import me.relex.circleindicator.CircleIndicator;

public class ProductDetails extends AppCompatActivity {
    ImageView imgBack;
    FrameLayout flCart;
    ViewPager viewPager;
    CircleIndicator circleIndicator;
    TextView tvName, tvSalePrice, tvCost, tvColor, tvVoucher, tvInfoProduct, tvAddToCart, tvBuyNow, tvNumberCart;
    SliderImagesAdapter bannerAdapter;
    ArrayList<Banner> bannerList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_product_details);
        initGUI();
        Intent intent = getIntent();
        Product product = (Product) intent.getSerializableExtra("product");
        getData(product);
        if (product.getLoaisp_id() == 1) {
            loadDataBanner(product.getId() + "", Constants.API + "images_phone.php");
        } else if (product.getLoaisp_id() == 2) {
            loadDataBanner(product.getId() + "", "");
        }
        addCart(product);
        tvNumberCart.setText(String.valueOf(Utils.listCart.size()));
        imgBack.setOnClickListener(view ->
                startActivity(new Intent(this, ZenithActivity.class)));
    }


    void initGUI() {
        imgBack = findViewById(R.id.imageView_back);
        flCart = findViewById(R.id.frameLayout_cart);
        viewPager = findViewById(R.id.viewPager);
        circleIndicator = findViewById(R.id.circleIndicator);
        tvName = findViewById(R.id.textView_name_product);
        tvSalePrice = findViewById(R.id.textView_priceSale);
        tvCost = findViewById(R.id.textView_cost);
        tvColor = findViewById(R.id.textView_color);
        tvVoucher = findViewById(R.id.textView_voucher);
        tvInfoProduct = findViewById(R.id.textView_info_product);
        tvAddToCart = findViewById(R.id.textView_addtocart);
        tvBuyNow = findViewById(R.id.textView_buynow);
        tvNumberCart = findViewById(R.id.num_cart);

    }

    private void getData(Product product) {
        tvName.setText(product.getTen_sp());

        Locale locale = new Locale("vi", "VN"); // Thiết lập địa phương Việt Nam
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);

        String cost = currencyFormatter.format(product.getGia_sp());

        SpannableString spannableString = new SpannableString(cost);
        spannableString.setSpan(new StrikethroughSpan(), 0, cost.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvSalePrice.setText(currencyFormatter.format(product.getGia_km()));
        tvCost.setText(spannableString);
        tvVoucher.setText(product.getQuatang());
        tvInfoProduct.setText(product.getMota() + "");

    }

    private void loadDataBanner(String id, String url) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                bannerList = new ArrayList<>();
                try {

                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject arr = jsonArray.getJSONObject(i);
                        bannerList.add(new Banner(arr.getInt("id"), arr.getString("img")));

                    }
                    bannerAdapter = new SliderImagesAdapter(bannerList);
                    viewPager.setAdapter(bannerAdapter);
                    circleIndicator.setViewPager(viewPager);
                    bannerAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Chưa có ảnh sản phẩm", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }

    private void addCart(Product product) {
        tvAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProductToListCart(product);
                tvNumberCart.setText(String.valueOf(Utils.listCart.size()));
            }
        });
        flCart.setOnClickListener(view ->
                startActivity(new Intent(this, CartActivity.class)));
        tvBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProductToListCart(product);
                tvNumberCart.setText(String.valueOf(Utils.listCart.size()));
                startActivity(new Intent(getApplicationContext(), CartActivity.class));
            }
        });
    }

    private void addProductToListCart(Product product) {
        SharedPreferences preferences = getSharedPreferences(Utils.login_success, MODE_PRIVATE);
        String json = preferences.getString("object", "");
        if (json != null) {
            Gson gson = new Gson();
            Account account = gson.fromJson(json, Account.class);
            if (account != null) {
                if (Utils.listCart.size() > 0) {
                    boolean flag = false;
                    int quantity = 1;
                    for (int i = 0; i < Utils.listCart.size(); i++) {
                        if (Utils.listCart.get(i).getName().equals(product.getTen_sp())) {
                            Utils.listCart.get(i).setQuantity(quantity + Utils.listCart.get(i).getQuantity());
                            flag = true;
                            Utils.saveCart(getApplicationContext());
                        }
                    }
                    if (flag == false) {
                        Utils.listCart.add(new Cart(product.getAnh(), product.getTen_sp(), product.getGia_sp(), product.getGia_km(), 1, account.getId()));
                        Utils.saveCart(getApplicationContext());
                    }
                } else {
                    Utils.listCart.add(new Cart(product.getAnh(), product.getTen_sp(), product.getGia_sp(), product.getGia_km(), 1, account.getId()));
                    Utils.saveCart(getApplicationContext());
                }
            } else
                Toast.makeText(getApplicationContext(), "Bạn cần đăng nhập để mua hàng", Toast.LENGTH_SHORT).show();
        }
    }


}
