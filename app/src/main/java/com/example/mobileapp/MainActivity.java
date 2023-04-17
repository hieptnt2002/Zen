package com.example.mobileapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mobileapp.constants.Constants;
import com.example.mobileapp.adapter.CategoryAdapter;
import com.example.mobileapp.adapter.FilterAdapter;
import com.example.mobileapp.adapter.MenuAdapter;
import com.example.mobileapp.adapter.ProductAdapter;
import com.example.mobileapp.model.Banner;
import com.example.mobileapp.adapter.BannerAdapter;
import com.example.mobileapp.model.Category;
import com.example.mobileapp.model.Menu;
import com.example.mobileapp.model.Product;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import me.relex.circleindicator.CircleIndicator;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ViewPager viewPager;
    CircleIndicator circleIndicator;
    Spinner spinner;
    BannerAdapter bannerAdapter;
    RecyclerView rcProduct, rcCategory;
    NavigationView navigationView;
    ListView listViewMenu;
    List<Banner> bannerList;
    Timer timer;
    TextView txtCart;
    // menu
    MenuAdapter menuAdapter;
    ArrayList<Menu> menuArrayList;
    ArrayList<Category> danhmucArrayList;
    CategoryAdapter danhmucAdapter;
    ProductAdapter productAdapter, laptopAdapter;
    LinearLayoutManager layoutProduct, layoutLaptop;
    String a = "";
    ProgressBar load_category;
    AutoCompleteTextView autoCompleteText_search;
    TextInputLayout textInputLayout;
    TextView tvLaptop;
    RecyclerView rvLaptop, rvFilter_Smartphone, rvFilter_Laptop;

    private void test() {
        List<String> l = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            l.add("hehe" + i);
        }
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, l);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_multichoice);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                a = l.get(position);
                Toast.makeText(MainActivity.this, a, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//      if(!spinner.hasOnClickListeners()) a = l.get(0);
//        Toast.makeText(this, a, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        anhxa();
        actionBar();
        String t = txtCart.getText().toString();
        if (t.isEmpty()) {
            return;
        } else txtCart.setBackground(this.getResources().getDrawable(R.drawable.num_cart));
        getApiBanner();
        //load
        load_category.setVisibility(View.VISIBLE);


        dataListViewMenu();
        setRcProduct();
        test();
        // danhmuc recyc
        dataRecycleViewDM();

        loadCategory("http://hiepvakhanh21-001-site1.htempurl.com/show_danhmucc.php");
        loadDienThoai("http://hiepvakhanh21-001-site1.htempurl.com/product_iii.php");
        loadDataLaptop("http://hiepvakhanh21-001-site1.htempurl.com/data_laptop.php");
        TextView a111 = (TextView) findViewById(R.id.a111);
        search();
        setLayoutRVLaptop();
        loadDataRvFilterSm();
        loadDataRvFilterLT();


    }

    private void setLayoutRVLaptop() {
        rvLaptop.setHasFixedSize(true);
        layoutLaptop = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvLaptop.setLayoutManager(layoutLaptop);
    }

    private void setRcProduct() {
        rcProduct.setHasFixedSize(true);
        layoutProduct = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rcProduct.setLayoutManager(layoutProduct);

    }

    //add date recycleview danh muc
    public void dataRecycleViewDM() {
        rcCategory.setHasFixedSize(true);
        GridLayoutManager layoutCategory = new GridLayoutManager(this, 6);
        rcCategory.setLayoutManager(layoutCategory);

    }

    // add data listview
    private void dataListViewMenu() {
        menuArrayList = new ArrayList<>();
        menuArrayList.add(new Menu("Điện thoại", R.drawable.baseline_phone_iphone_24));
        menuArrayList.add(new Menu("Laptop", R.drawable.baseline_computer_24));
        menuArrayList.add(new Menu("Tin tức", R.drawable.tintuc));
        menuArrayList.add(new Menu("Liên hệ", R.drawable.lienhe));
        menuArrayList.add(new Menu("Giới thiệu", R.drawable.gioithieu));

        menuAdapter = new MenuAdapter(menuArrayList, MainActivity.this);
        listViewMenu.setAdapter(menuAdapter);


    }

    private void getApiBanner() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.API_URL_BANNER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                bannerList = new ArrayList<>();
                try {

                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject arr = jsonArray.getJSONObject(i);
                        bannerList.add(new Banner(arr.getInt("slider_id"), arr.optString("slider_img")));
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
                Toast.makeText(MainActivity.this, "Xảy ra lỗi", Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(this).add(stringRequest);
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
                handler.postDelayed(this, 5000);
            }
        };

        // Bắt đầu thực hiện slide tự động khi Activity hoặc Fragment được khởi tạo
        handler.postDelayed(runnable, 5000);
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) viewPager.removeCallbacks(runnable);
                else if(event.getAction() == MotionEvent.ACTION_UP) viewPager.postDelayed(runnable,5000);
                return false;
            }
        });

    }

    public void actionBar() {
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    public void anhxa() {
        drawerLayout = findViewById(R.id.drawerLayout_main);
        toolbar = findViewById(R.id.toolbar_main);
        viewPager = findViewById(R.id.viewPager);
        circleIndicator = findViewById(R.id.circleIndicator);
        rcProduct = findViewById(R.id.recyclerView_product);
        navigationView = findViewById(R.id.navigationView_main);
        listViewMenu = findViewById(R.id.list_menu);
        txtCart = findViewById(R.id.sl_cart);
        rcCategory = findViewById(R.id.recyclerView_category);
        spinner = findViewById(R.id.spinner);
        load_category = findViewById(R.id.load_category);
        autoCompleteText_search = findViewById(R.id.autoCompleteText_search);
        textInputLayout = findViewById(R.id.textInputLayout);
        tvLaptop = findViewById(R.id.textView_laptop);
        rvLaptop = findViewById(R.id.recyclerView_laptop);
        rvFilter_Smartphone = findViewById(R.id.rv_filter_smartphone);
        rvFilter_Laptop = findViewById(R.id.rv_filter_laptop);
    }

    //event search
    private void search() {
        autoCompleteText_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textInputLayout.setHintEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void loadDataRvFilterSm() {
        rvFilter_Smartphone.setHasFixedSize(true);
        LinearLayoutManager filterManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        rvFilter_Smartphone.setLayoutManager(filterManager);
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
                Toast.makeText(MainActivity.this, "Xảy ra lỗi", Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void loadDataRvFilterLT() {
        rvFilter_Laptop.setHasFixedSize(true);
        LinearLayoutManager filterManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        rvFilter_Laptop.setLayoutManager(filterManager);
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
                Toast.makeText(MainActivity.this, "Xảy ra lỗi", Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(this).add(stringRequest);
    }
    //doc noi dung url

    public void loadCategory(String url) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                danhmucArrayList = new ArrayList<>();
                try {

                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject arr = jsonArray.getJSONObject(i);
                        danhmucArrayList.add(new Category(arr.getInt("id"), arr.getString("tensp"), arr.optString("anh")));
                    }
                    danhmucAdapter = new CategoryAdapter(danhmucArrayList, MainActivity.this);
                    rcCategory.setAdapter(danhmucAdapter);
                    load_category.setVisibility(View.INVISIBLE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Xảy ra lỗi", Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(this).add(stringRequest);
    }

    public void loadDienThoai(String url_DT) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_DT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayList<Product> listProduct = new ArrayList<>();
                List<String> listTest = new ArrayList<>();
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject arr = jsonArray.getJSONObject(i);
                        listProduct.add(new Product(arr.getInt("id"), arr.optString("anh"), arr.getString("ten_sp"), arr.getInt("gia_sp"), arr.getInt("gia_km"), arr.getString("quatang")));
                        Toast.makeText(MainActivity.this, listProduct.get(i).getTen_sp() + "", Toast.LENGTH_SHORT).show();
                        listTest.add(arr.getString("ten_sp"));

                    }

                    productAdapter = new ProductAdapter(listProduct, MainActivity.this);
                    rcProduct.setAdapter(productAdapter);
                    autoSlide(rcProduct);
                    ArrayAdapter testAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, listTest);
                    autoCompleteText_search.setAdapter(testAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Xảy ra lỗi", Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(this).add(stringRequest);
    }

    public void loadDataLaptop(String url_DT) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_DT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayList<Product> laptopList = new ArrayList<>();

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject arr = jsonArray.getJSONObject(i);
                        laptopList.add(new Product(arr.getInt("id"), arr.optString("anh"), arr.getString("ten_sp"), arr.getInt("gia_sp"), arr.getInt("gia_km"), arr.getString("quatang")));
                    }

                    laptopAdapter = new ProductAdapter(laptopList, MainActivity.this);
                    rvLaptop.setAdapter(laptopAdapter);
                    autoSlide(rvLaptop);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Xảy ra lỗi", Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(this).add(stringRequest);
    }

    //
    public void autoSlide(final RecyclerView recyclerView) {
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            int nextPosition = 0;
            LinearLayoutManager layoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());

            @Override
            public void run() {
                int count = layoutManager.getChildCount();
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