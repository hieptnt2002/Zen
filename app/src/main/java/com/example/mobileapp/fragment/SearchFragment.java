package com.example.mobileapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mobileapp.R;
import com.example.mobileapp.adapter.ProductAdapter;
import com.example.mobileapp.constants.Constants;
import com.example.mobileapp.model.Product;
import com.example.mobileapp.utils.Utils;
import com.example.mobileapp.view.ZenithActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchFragment extends Fragment {
    View view;
    TextView tvSearchResults;
    RecyclerView rvSearchResults;
    LinearLayout llHighFilter, llLowFilter, llPercentFilter;
    ProductAdapter findAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container, false);
        initGUI();
        Bundle bundle = this.getArguments();
        ZenithActivity z = (ZenithActivity) getActivity();
        z.mCurrentFragment = 999;
        if (bundle != null) {
            String name = "%" + bundle.getString("name") + "%";
            loadDataFind(name);
        }
        eventFilter();
        return view;
    }

    void initGUI() {
        tvSearchResults = view.findViewById(R.id.textView_searchResults);
        rvSearchResults = view.findViewById(R.id.recyclerView_searchResults);
        rvSearchResults.setHasFixedSize(true);
        rvSearchResults.setLayoutManager(new GridLayoutManager(getContext(), 2));
        llHighFilter = view.findViewById(R.id.box_caothap);
        llLowFilter = view.findViewById(R.id.box_thapcao);
        llPercentFilter = view.findViewById(R.id.box_percen);
    }

    private void loadDataFind(String name) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.API + "find_result.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayList<Product> mList = new ArrayList<>();
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject arr = jsonArray.getJSONObject(i);
                        mList.add(new Product(arr.getInt("id"), arr.getString("anh"), arr.getString("ten_sp"), arr.getInt("gia_sp"), arr.getInt("gia_km"), arr.getString("quatang"), arr.getString("mota"), arr.getInt("loaisp_id")));

                    }

                    findAdapter = new ProductAdapter(mList, getContext());
                    rvSearchResults.setAdapter(findAdapter);
                    findAdapter.setOnClickAddToCart(new ProductAdapter.OnClickAddCartListener() {
                        @Override
                        public void onClickAddToCart() {
                            if (Utils.listCart.size() != 0) {
                                TextView tvCart = getActivity().findViewById(R.id.num_cart);
                                tvCart.setText(String.valueOf(Utils.listCart.size()));
                            }
                        }
                    });
                    if (findAdapter != null) {
                        int itemHeight = getResources().getDimensionPixelSize(R.dimen.item_height_product); // chiều cao của một item
                        int numItems = 0;
                        if (findAdapter.getItemCount() % 2 == 0) {
                            numItems = findAdapter.getItemCount() / 2; // số lượng item trong RecyclerView
                        } else
                            numItems = (findAdapter.getItemCount() + 1) / 2; // số lượng item trong RecyclerView

                        int totalHeight = itemHeight * numItems; // tổng chiều cao của tất cả các item trong RecyclerView
                        ViewGroup.LayoutParams params = rvSearchResults.getLayoutParams();
                        params.height = totalHeight;
                        rvSearchResults.setLayoutParams(params);
                    }
                    //số sản phẩm tìm được
                    if (mList != null) tvSearchResults.setText(mList.size() + " sản phẩm");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Xảy ra lỗi", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                return params;
            }
        };
        Volley.newRequestQueue(view.getContext()).add(stringRequest);
    }

    public void eventFilter() {
        llHighFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (findAdapter != null) findAdapter.filterPriceProductHigh();
                llHighFilter.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.background_filter_click));
                llLowFilter.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.border_filter));
                llPercentFilter.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.border_filter));
            }
        });
        llLowFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (findAdapter != null) findAdapter.filterPriceProductLow();
                llHighFilter.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.border_filter));
                llLowFilter.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.background_filter_click));
                llPercentFilter.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.border_filter));

            }
        });
        llPercentFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (findAdapter != null) findAdapter.filterPriceProductPercent();
                llHighFilter.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.border_filter));
                llLowFilter.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.border_filter));
                llPercentFilter.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.background_filter_click));
            }
        });
    }
}
