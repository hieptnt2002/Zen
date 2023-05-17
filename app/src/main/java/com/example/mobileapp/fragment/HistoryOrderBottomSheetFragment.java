package com.example.mobileapp.fragment;


import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mobileapp.R;
import com.example.mobileapp.adapter.HistoryOrderAdapter;
import com.example.mobileapp.constants.Constants;
import com.example.mobileapp.model.Account;
import com.example.mobileapp.model.HistoryOrder;
import com.example.mobileapp.utils.Utils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoryOrderBottomSheetFragment extends BottomSheetDialogFragment {
    TextView tvIsOrder;
    RecyclerView recyclerView;
    BottomSheetDialog mBottomSheetDialog;


    public static HistoryOrderBottomSheetFragment newInstance() {
        HistoryOrderBottomSheetFragment bottomSheetFragment = new HistoryOrderBottomSheetFragment();
        return bottomSheetFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        mBottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_order_history, null);
        mBottomSheetDialog.setContentView(view);
        recyclerView = view.findViewById(R.id.recycleView_HistoryOrder);
        tvIsOrder = view.findViewById(R.id.textView_isOrder);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
        SharedPreferences pref = getActivity().getSharedPreferences(Utils.login_success, Context.MODE_PRIVATE);
        String object = pref.getString("object", null);
        if (object != null && !object.isEmpty()) {
            Gson gson = new Gson();
            Account account = gson.fromJson(object, Account.class);
            loadData(account);
        }


        BottomSheetBehavior behavior = BottomSheetBehavior.from((View) view.getParent());
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        return mBottomSheetDialog;
    }

    private void loadData(Account account) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.API + "show_order.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                List<HistoryOrder> mList = new ArrayList<>();
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject arr = jsonArray.getJSONObject(i);
                        mList.add(new HistoryOrder(arr.getInt("id"), arr.getString("img"), arr.getString("name"), arr.getInt("price"), arr.getInt("quantity")));
                    }
                    if (mList != null && !mList.isEmpty()) {
                        HistoryOrderAdapter mAdapter = new HistoryOrderAdapter(mList, getContext());
                        recyclerView.setAdapter(mAdapter);
                        tvIsOrder.setVisibility(View.GONE);
                    } else tvIsOrder.setVisibility(View.VISIBLE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Lỗi", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", account.getId() + "");
                return params;
            }
        };
        Volley.newRequestQueue(getContext()).add(stringRequest);
    }

}
