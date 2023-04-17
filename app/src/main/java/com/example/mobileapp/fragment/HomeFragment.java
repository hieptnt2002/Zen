package com.example.mobileapp.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mobileapp.adapter.MenuAdapter;
import com.example.mobileapp.constants.Constants;
import com.example.mobileapp.R;
import com.example.mobileapp.adapter.BannerAdapter;
import com.example.mobileapp.adapter.CategoryAdapter;
import com.example.mobileapp.adapter.FilterAdapter;
import com.example.mobileapp.adapter.ProductAdapter;
import com.example.mobileapp.model.Banner;
import com.example.mobileapp.model.Category;
import com.example.mobileapp.model.Menu;
import com.example.mobileapp.model.Product;
import com.example.mobileapp.tesst.OnItemClickListener;
import com.example.mobileapp.tesst.ZenithActivity;
import com.example.mobileapp.utils.Utils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import me.relex.circleindicator.CircleIndicator;


public class HomeFragment extends Fragment {
    //
    View view;
    ViewPager viewPager;
    CircleIndicator circleIndicator;

    List<Banner> bannerList;
    Timer timer;
    TextView txtCart;
    ArrayList<Category> cateList;
    CategoryAdapter cateAdapter;
    ProductAdapter smartAdapter, laptopAdapter;
    BannerAdapter bannerAdapter;
    CategoryAdapter categoryAdapter;
    LinearLayoutManager layoutProduct, layoutLaptop;
    ProgressBar loading_cate;
    AutoCompleteTextView inputSearch;
    TextInputLayout textInputLayout;
    TextView tvLaptop;
    RecyclerView rvSmart, rvCate, rvLaptop, rvFilter_Smartphone, rvFilter_Laptop;
    public int clickPosition;
    NavigationView mNavigationView;
    BottomNavigationView bottomNavigationView;

    List<String> suggestList = new ArrayList<>();
    ZenithActivity zenithActivity;
    ArrayAdapter suggestAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        zenithActivity = (ZenithActivity) getActivity();

        initGUI();
        loading_cate.setVisibility(View.VISIBLE);
        setLayoutRv();
        loadDataRvBanner();
        loadDataRvCategory();
        loadDataRvFilSm();
        loadDataRvSmart();
        loadDataRvLap();
        loadDataRvFilterLap();
        search();
         return view;

    }

    private void initGUI() {
        mNavigationView = getActivity().findViewById(R.id.nav_header);
        bottomNavigationView = getActivity().findViewById(R.id.bottom_nav);
        viewPager = view.findViewById(R.id.viewPager_home);
        circleIndicator = view.findViewById(R.id.circleIndicator_home);
        rvSmart = view.findViewById(R.id.recyclerView_productHome);

        loading_cate = view.findViewById(R.id.load_categoryHome);
        inputSearch = view.findViewById(R.id.autoCompleteText_search);
        textInputLayout = view.findViewById(R.id.textInputLayout);
        tvLaptop = view.findViewById(R.id.textView_laptopHome);
        rvCate = view.findViewById(R.id.recyclerView_categoryHome);
        rvLaptop = view.findViewById(R.id.recyclerView_laptopHome);
        rvFilter_Smartphone = view.findViewById(R.id.rv_filter_smartphoneHome);
        rvFilter_Laptop = view.findViewById(R.id.rv_filter_laptopHome);
    }
    void search(){
        suggestAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1,Utils.suggestSearchList);
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textInputLayout.setHintEnabled(false);
                inputSearch.setAdapter(suggestAdapter);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setLayoutRv() {
        rvCate.setHasFixedSize(true);
        rvLaptop.setHasFixedSize(true);
        rvSmart.setHasFixedSize(true);
        rvFilter_Laptop.setHasFixedSize(true);
        rvFilter_Smartphone.setHasFixedSize(true);

        LinearLayoutManager layoutFilSm =  new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        LinearLayoutManager layoutSmart = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        LinearLayoutManager layoutFillap =  new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        LinearLayoutManager layoutLap = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        GridLayoutManager layoutCategory = new GridLayoutManager(this.getContext(), 5);

        rvFilter_Smartphone.setLayoutManager(layoutFilSm);
        rvFilter_Laptop.setLayoutManager(layoutFillap);
        rvLaptop.setLayoutManager(layoutLap);
        rvSmart.setLayoutManager(layoutSmart);
        rvCate.setLayoutManager(layoutCategory);
    }

    public void loadDataRvCategory() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.API_URL_CATEGORY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayList<Category> categoryList = new ArrayList<>();
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject arr = jsonArray.getJSONObject(i);
                        categoryList.add(new Category(arr.getInt("id"), arr.getString("tensp"), arr.optString("anh")));
                    }
                    categoryAdapter = new CategoryAdapter(categoryList, getContext());
                    rvCate.setAdapter(categoryAdapter);
                    loading_cate.setVisibility(View.INVISIBLE);
                    eventClickRvCate();
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

    private void loadDataRvBanner() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.API_URL_BANNER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayList<Banner> bannerList = new ArrayList<>();
                try {

                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject arr = jsonArray.getJSONObject(i);
                        bannerList.add(new Banner(arr.getInt("id"), arr.getString("img")));
                    }
                    bannerAdapter = new BannerAdapter(bannerList);
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

    private void eventClickRvCate() {
        categoryAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                switch (position) {
                    case 0:
                        replaceFragment(new SmartphoneFragment());
                        mNavigationView.getMenu().findItem(R.id.nav_smart).setChecked(true);
                        bottomNavigationView.getMenu().findItem(R.id.nav_smart).setChecked(true);
                        zenithActivity.mCurrentFragment = zenithActivity.FRAGMENT_SMART;
                        break;
                    case 1:
                        replaceFragment(new LaptopFragment());
                        mNavigationView.getMenu().findItem(R.id.nav_lap).setChecked(true);
                        bottomNavigationView.getMenu().findItem(R.id.nav_lap).setChecked(true);
                        zenithActivity.mCurrentFragment = zenithActivity.FRAGMENT_LAPTOP;
                        break;
                    case 3:
                        replaceFragment(new SmartphoneFragment());
                        mNavigationView.getMenu().findItem(R.id.nav_smart).setChecked(true);
                        bottomNavigationView.getMenu().findItem(R.id.nav_smart).setChecked(true);
                        zenithActivity.mCurrentFragment = zenithActivity.FRAGMENT_SMART;
                        break;

                }
            }
        });
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.commit();
    }
    private void loadDataRvFilSm() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.API_URL_TH_SMARTPHONE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                List<String> mList = new ArrayList<>();
                try {

                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject arr = jsonArray.getJSONObject(i);
                        mList.add(arr.getString("ten_thuonghieu"));
                        if (i == jsonArray.length() - 1) mList.add("Xem tất cả");
                    }
                    FilterAdapter mFilterAdapter = new FilterAdapter(mList);
                    rvFilter_Smartphone.setAdapter(mFilterAdapter);
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

    public void loadDataRvSmart() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.API_URL_SMARTPHONE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayList<Product> mList = new ArrayList<>();
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject arr = jsonArray.getJSONObject(i);
                        mList.add(new Product(arr.getInt("id"), arr.getString("anh"), arr.getString("ten_sp"), arr.getInt("gia_sp"), arr.getInt("gia_km"), arr.getString("quatang")));
                        suggestList.add(arr.getString("ten_sp"));
                        Utils.suggestSearchList.add(arr.getString("ten_sp"));

                    }

                    smartAdapter = new ProductAdapter(mList, getContext());
                    rvSmart.setAdapter(smartAdapter);
                    smartAdapter.setOnClickAddToCart(new ProductAdapter.OnClickAddCartListener() {
                        @Override
                        public void onClickAddToCart() {
                            if(Utils.listCart.size() !=0){
                                TextView tvCart = getActivity().findViewById(R.id.num_cart);
                                tvCart.setText(String.valueOf(Utils.listCart.size()));
                            }
                        }
                    });
                    autoSlideRv(rvSmart);
//                    ArrayAdapter testAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, suggestList);
//                    autoCompleteText_search.setAdapter(testAdapter);
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

    public void loadDataRvLap() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.API_URL_LAPTOP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayList<Product> laptopList = new ArrayList<>();

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject arr = jsonArray.getJSONObject(i);
                        laptopList.add(new Product(arr.getInt("id"), arr.getString("anh"), arr.getString("ten_sp"), arr.getInt("gia_sp"), arr.getInt("gia_km"), arr.getString("quatang")));
                        Utils.suggestSearchList.add(arr.getString("ten_sp"));
                    }

                    laptopAdapter = new ProductAdapter(laptopList, getContext());
                    rvLaptop.setAdapter(laptopAdapter);
                    laptopAdapter.setOnClickAddToCart(new ProductAdapter.OnClickAddCartListener() {
                        @Override
                        public void onClickAddToCart() {
                         if(Utils.listCart.size() !=0){
                             TextView tvCart = getActivity().findViewById(R.id.num_cart);
                             tvCart.setText(String.valueOf(Utils.listCart.size()));
                         }
                        }
                    });
                    autoSlideRv(rvLaptop);

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

    private void loadDataRvFilterLap() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.API_URL_TH_LAPTOP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                List<String> mList = new ArrayList<>();
                try {

                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject arr = jsonArray.getJSONObject(i);
                        mList.add(arr.getString("ten_thuonghieu"));
                        if (i == jsonArray.length() - 1) mList.add("Xem tất cả");
                    }
                    FilterAdapter mFilterAdapter = new FilterAdapter(mList);
                    rvFilter_Laptop.setAdapter(mFilterAdapter);
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
    //
    public void autoSlideRv(final RecyclerView recyclerView) {
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            int nextPosition = 0;
            LinearLayoutManager layoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());

            @Override
            public void run() {

                int lastVisible = layoutManager.findLastCompletelyVisibleItemPosition();
                if (nextPosition < recyclerView.getAdapter().getItemCount()) {
                    if (lastVisible < recyclerView.getAdapter().getItemCount() - 1) {
                        nextPosition = lastVisible + 1;
                    } else {
                        nextPosition = 0;
                    }
                }
                layoutManager.smoothScrollToPosition(recyclerView, new RecyclerView.State(), nextPosition);
                handler.postDelayed(this, 5000);
            }
        };
        handler.postDelayed(runnable,5000);
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    // Tạm dừng trượt tự động khi người dùng bấm xuống
                    handler.removeCallbacks(runnable);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    // Tiếp tục trượt tự động khi người dùng nhả tay
                    handler.postDelayed(runnable, 5000); // Hoặc thời gian chuyển đổi tự động bạn muốn
                }
                return false;
            }
        });

    }

}
