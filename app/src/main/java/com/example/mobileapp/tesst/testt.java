package com.example.mobileapp.tesst;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mobileapp.R;
import com.example.mobileapp.adapter.ProductAdapter;
import com.example.mobileapp.model.Product;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class testt extends AppCompatActivity {
    RecyclerView rc;
    LinearLayoutManager layoutManager;
    ArrayList<test> l;
    RCADAPTER adapter;
    RCADAPTER laptopAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testt);
         rc = findViewById(R.id.rc);
        rc.setHasFixedSize(true);
         layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rc.setLayoutManager(layoutManager);
         loadDataLaptop("http://hiepvakhanh21-001-site1.htempurl.com/product_iii.php");
//        autoRecycleView();

    }
    private void autoRecycleView() {
        LinearSnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(rc);
        Timer timerRe = new Timer();
        timerRe.schedule(new TimerTask() {
            @Override
            public void run() {
                if (layoutManager.findLastCompletelyVisibleItemPosition() < adapter.getItemCount() - 1) {
                    layoutManager.smoothScrollToPosition(rc, new RecyclerView.State(), layoutManager.findLastCompletelyVisibleItemPosition() + 1);
                } else
                    layoutManager.smoothScrollToPosition(rc, new RecyclerView.State(), 0);
            }

        }, 0, 5000);
    }
    public void loadDataLaptop(String url_DT) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_DT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {
                    JSONArray jsonArray = new JSONArray(response);
                    String jsonString = jsonArray.toString();

                    // Chuyển đổi chuỗi JSON thành ArrayList<Product> sử dụng Gson
                    Type productListType = new TypeToken<ArrayList<Product>>() {}.getType();
                    ArrayList<Product> laptopList = new Gson().fromJson(jsonString, productListType);
                    ProductAdapter productAdapter= new ProductAdapter(laptopList,testt.this);
                    rc.setAdapter(productAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(testt.this, "Xảy ra lỗi", Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(this).add(stringRequest);
    }
}