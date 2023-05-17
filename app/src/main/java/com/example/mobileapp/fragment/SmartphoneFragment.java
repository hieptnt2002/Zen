package com.example.mobileapp.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mobileapp.R;
import com.example.mobileapp.adapter.BannerChildAdapter;
import com.example.mobileapp.adapter.ProductAdapter;
import com.example.mobileapp.constants.Constants;
import com.example.mobileapp.model.Banner;
import com.example.mobileapp.model.Product;
import com.example.mobileapp.utils.Utils;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class SmartphoneFragment extends Fragment {
    AutoCompleteTextView inputSearch;
    TextInputLayout inputLayout;
    ViewPager viewPager;
    CircleIndicator circleIndicator;
    RecyclerView rvSmartphone;
    ProductAdapter smartAdapter;
    BannerChildAdapter bannerAdapter;
    View view;
    LinearLayout llHighFilter, llLowFilter, llPercentFilter;
    ProgressBar progressBar;
    ArrayList<Product> mList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_product, container, false);
        initGUI();
        progressBar.setVisibility(View.VISIBLE);
        loadDataRvSmart();
        slider();
        eventFilter();
        search();
        return view;
    }

    void initGUI() {
        inputSearch = view.findViewById(R.id.autoCompleteText_search);
        inputLayout = view.findViewById(R.id.textInputLayout);
        viewPager = view.findViewById(R.id.slider_smartphone);
        circleIndicator = view.findViewById(R.id.circleIndicator_sm);
        rvSmartphone = view.findViewById(R.id.recycleView_smartphone);
        llHighFilter = view.findViewById(R.id.box_caothap);
        llLowFilter = view.findViewById(R.id.box_thapcao);
        llPercentFilter = view.findViewById(R.id.box_percen);
        progressBar = view.findViewById(R.id.load_product);

    }

    void search() {
        ArrayAdapter suggestAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, Utils.suggestSearchList);
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                inputLayout.setHintEnabled(false);
                inputSearch.setAdapter(suggestAdapter);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (smartAdapter != null) {
                    smartAdapter.filterNameProduct(inputSearch.getText().toString());
                }
            }
        });
        inputSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == KeyEvent.ACTION_DOWN || event.getAction() == KeyEvent.KEYCODE_ENTER) {

                    Bundle bundle = new Bundle();
                    bundle.putString("name", String.valueOf(inputSearch.getText()));
                    SearchFragment searchFragment = new SearchFragment();
                    searchFragment.setArguments(bundle);
                    replaceFragment(searchFragment);

                    return true;
                }
                return false;
            }
        });
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.commit();
    }

    public void eventFilter() {
        llHighFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (smartAdapter != null) smartAdapter.filterPriceProductHigh();
                llHighFilter.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.background_filter_click));
                llLowFilter.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.border_filter));
                llPercentFilter.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.border_filter));
            }
        });
        llLowFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (smartAdapter != null) smartAdapter.filterPriceProductLow();
                llHighFilter.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.border_filter));
                llLowFilter.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.background_filter_click));
                llPercentFilter.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.border_filter));

            }
        });
        llPercentFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (smartAdapter != null) smartAdapter.filterPriceProductPercent();
                llHighFilter.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.border_filter));
                llLowFilter.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.border_filter));
                llPercentFilter.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.background_filter_click));
            }
        });
    }

    void slider() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.API + "slider_smart.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayList<Banner> bannerList = new ArrayList<>();
                try {

                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject arr = jsonArray.getJSONObject(i);
                        bannerList.add(new Banner(arr.getInt("id"), arr.getString("img")));
                    }
                    bannerAdapter = new BannerChildAdapter(bannerList);
                    viewPager.setAdapter(bannerAdapter);
                    circleIndicator.setViewPager(viewPager);
                    bannerAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());
                    autoSlideImages();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(view.getContext(), "Xảy ra lỗi", Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(view.getContext()).add(stringRequest);
    }

    private void autoSlideImages() {
        // Tạo Handler và Runnable  thực hiện slide tự động
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                int currentItem = viewPager.getCurrentItem();
                int count = viewPager.getAdapter().getCount();
                viewPager.setCurrentItem((currentItem + 1) % count);

                // Lặp lại chạy lại task sau 5 giây
                handler.postDelayed(this, 6000);
            }
        };
        // Bắt đầu thực hiện slide tự động khi Activity hoặc Fragment được khởi tạo
        handler.postDelayed(runnable, 6000);
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                    viewPager.removeCallbacks(runnable);
                else if (event.getAction() == MotionEvent.ACTION_UP)
                    viewPager.postDelayed(runnable, 6000);
                return false;
            }
        });

    }

    public void loadDataRvSmart() {
        rvSmartphone.setHasFixedSize(true);
        rvSmartphone.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rvSmartphone.setNestedScrollingEnabled(false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.API + "data_smartphone.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mList = new ArrayList<>();
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject arr = jsonArray.getJSONObject(i);
                        mList.add(new Product(arr.getInt("id"), arr.getString("anh"), arr.getString("ten_sp"), arr.getInt("gia_sp"), arr.getInt("gia_km"), arr.getString("quatang"), arr.getString("mota"), arr.getInt("loaisp_id")));

                    }
                    smartAdapter = new ProductAdapter(mList, getContext());
                    rvSmartphone.setAdapter(smartAdapter);
                    smartAdapter.setOnClickAddToCart(new ProductAdapter.OnClickAddCartListener() {
                        @Override
                        public void onClickAddToCart() {
                            if (Utils.listCart.size() != 0) {
                                TextView tvCart = getActivity().findViewById(R.id.num_cart);
                                tvCart.setText(String.valueOf(Utils.listCart.size()));
                            }
                        }
                    });
                    if (smartAdapter != null) {
                        int itemHeight = getResources().getDimensionPixelSize(R.dimen.item_height_product); // chiều cao của một item
                        int numItems = 0;
                        if (smartAdapter.getItemCount() % 2 == 0) {
                            numItems = smartAdapter.getItemCount() / 2; // số lượng item trong RecyclerView
                        } else
                            numItems = (smartAdapter.getItemCount() + 1) / 2; // số lượng item trong RecyclerView

                        int totalHeight = itemHeight * numItems; // tổng chiều cao của tất cả các item trong RecyclerView
                        ViewGroup.LayoutParams params = rvSmartphone.getLayoutParams();
                        params.height = totalHeight;
                        rvSmartphone.setLayoutParams(params);
                    }
                    progressBar.setVisibility(View.INVISIBLE);

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
