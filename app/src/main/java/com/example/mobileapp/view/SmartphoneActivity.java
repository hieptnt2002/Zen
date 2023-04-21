package com.example.mobileapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mobileapp.adapter.BannerAdapter;
import com.example.mobileapp.adapter.ProductAdapter;
import com.example.mobileapp.model.Product;
import com.example.mobileapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class SmartphoneActivity extends AppCompatActivity {
    ArrayList arrBanner;
    BannerAdapter bannerAdapter;
    ViewPager viewPager;
    CircleIndicator circleIndicator;
    Timer timer;
    RecyclerView rcPhone;
    ProductAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_product);
        initGUI();
        arrBanner = new ArrayList<>();

        bannerAdapter = new BannerAdapter( arrBanner);
        viewPager.setAdapter(bannerAdapter);
        circleIndicator.setViewPager(viewPager);
        bannerAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());
        autoSlideImages();
        list_phone("http://hiepvakhanh21-001-site1.htempurl.com/product_iii.php");
    }

    private void autoSlideImages() {
        if (arrBanner == null || arrBanner.isEmpty() || viewPager == null) {
            return;
        }
        if (timer == null) {
            timer = new Timer();
        }
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        int currenItem = viewPager.getCurrentItem();
                        int totalItem = arrBanner.size() - 1;
                        if (currenItem < totalItem) {
                            currenItem++;
                            viewPager.setCurrentItem(currenItem);
                        } else viewPager.setCurrentItem(0);
                    }
                });
            }
        }, 5000, 6000);
    }

    private void initGUI() {
        viewPager = findViewById(R.id.slider_smartphone);
        circleIndicator = findViewById(R.id.circleIndicator_sm);
        rcPhone = findViewById(R.id.recycleView_smartphone);
    }

    public void list_phone(String url_DT) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_DT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                rcPhone.setHasFixedSize(true);
                GridLayoutManager layoutCategory = new GridLayoutManager(SmartphoneActivity.this, 2);
                rcPhone.setLayoutManager(layoutCategory);
                ArrayList<Product> listProduct = new ArrayList<>();
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject arr = jsonArray.getJSONObject(i);
                        listProduct.add(new Product(arr.getInt("id"), arr.optString("anh"), arr.getString("ten_sp"), arr.getInt("gia_sp"), arr.getInt("gia_km"), arr.getString("quatang")));
                    }

                    productAdapter = new ProductAdapter(listProduct, SmartphoneActivity.this);
                    rcPhone.setAdapter(productAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SmartphoneActivity.this, "Xảy ra lỗi", Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(this).add(stringRequest);
    }

}