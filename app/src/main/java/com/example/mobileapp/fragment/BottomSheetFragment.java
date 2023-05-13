package com.example.mobileapp.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
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
import com.example.mobileapp.adapter.OrderAdapter;
import com.example.mobileapp.constants.Constants;
import com.example.mobileapp.model.Cart;
import com.example.mobileapp.model.Order;
import com.example.mobileapp.utils.Utils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class BottomSheetFragment extends BottomSheetDialogFragment {
    private static final String KEY_ORDER_OBJECT = "object";
    BottomSheetDialog mBottomSheetDialog;
    RecyclerView rvOrder;
    EditText edtName, edtAddress, edtSDT;
    Spinner spnMethodPay;
    TextView tvTotalPr, tvTotalPay, tvTotalPay2;
    OrderAdapter mOrderAdapter;
    private Order mOrder;
    Locale locale = new Locale("vi", "VN"); // Thiết lập địa phương Việt Nam
    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
    LinearLayout layoutCancel, layoutOrder;

    public static BottomSheetFragment newInstance(Order order) {
        BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_ORDER_OBJECT, order);
        bottomSheetFragment.setArguments(bundle);
        return bottomSheetFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundleReceive = getArguments();
        if (bundleReceive != null) {
            mOrder = (Order) bundleReceive.getSerializable(KEY_ORDER_OBJECT);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        mBottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_order, null);
        mBottomSheetDialog.setContentView(view);
        initGUI(view);
        setDataOrder();
        event();
        BottomSheetBehavior behavior = BottomSheetBehavior.from((View) view.getParent());
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        return mBottomSheetDialog;
    }

    private void initGUI(View view) {
        rvOrder = view.findViewById(R.id.recyclerView_cart_order);
        edtName = view.findViewById(R.id.editText_name_order);
        edtAddress = view.findViewById(R.id.editText_address_order);
        edtSDT = view.findViewById(R.id.editText_sdt_order);
        tvTotalPr = view.findViewById(R.id.textView_total_order);
        tvTotalPay = view.findViewById(R.id.textView_total_pay);
        tvTotalPay2 = view.findViewById(R.id.textView_total_pay2);
        spnMethodPay = view.findViewById(R.id.spinner_pay);
        layoutCancel = view.findViewById(R.id.btn_cancel);
        layoutOrder = view.findViewById(R.id.btn_add_order);
    }

    private void setDataOrder() {
        rvOrder.setHasFixedSize(true);
        rvOrder.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        if (mOrder == null) {
            return;
        }
        tvTotalPr.setText(currencyFormatter.format(mOrder.getTotal()));
        tvTotalPay.setText(currencyFormatter.format(mOrder.getTotal() + 35000));
        tvTotalPay2.setText(currencyFormatter.format(mOrder.getTotal() + 35000));
        mOrderAdapter = new OrderAdapter(mOrder.getmList(), getContext());
        rvOrder.setAdapter(mOrderAdapter);
        List<String> list = new ArrayList<>();
        list.add("Thanh toán khi nhận hàng");
        list.add("Chuyển khoản ngân hàng");
        list.add("Thanh toán qua ZaloPay");
        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, list);
        spnMethodPay.setAdapter(adapter);

        int height_item = getResources().getDimensionPixelSize(R.dimen.item_height_cart);
        int num_item = mOrderAdapter.getItemCount();
        int total_height = height_item * num_item;
        ViewGroup.LayoutParams params = rvOrder.getLayoutParams();
        params.height = total_height;
        rvOrder.setLayoutParams(params);


    }

    void event() {
        layoutCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
            }
        });
        List<Cart> mCart = mOrder.getmList();
        String name_quantity = "";
        for (int i = 0; i < mCart.size(); i++) {
            name_quantity += mCart.get(i).getName() + " x " + mCart.get(i).getQuantity() + "\n";
        }
        String finalName_quantity = name_quantity;
        if (mCart != null && !mCart.isEmpty()) {
            layoutOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (edtName.getText().toString().isEmpty() ||
                            edtSDT.getText().toString().isEmpty() ||
                            edtAddress.getText().toString().isEmpty() ||
                            edtSDT.getText().toString().length() < 10) {
                        Toast.makeText(getContext(), "Nhập đầy đủ thông tin đặt hàng!", Toast.LENGTH_SHORT).show();
                    } else {
                        //up len api
                        for (int i = 0; i < mCart.size(); i++) {
                            upApiOrder(mCart, i);
                        }
                        //hien thi bill
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Đặt hàng thành công");
                        builder.setIcon(R.drawable.bill);

                        builder.setMessage("Gồm có các sản phẩm: \n" + finalName_quantity +
                                "\nTên người nhận: " + edtName.getText().toString() +
                                "\n Số điện thoại: " + edtSDT.getText().toString() +
                                "\n Địa chỉ người nhận: " + edtAddress.getText().toString() +
                                "\n Hình thức thanh toán: " + spnMethodPay.getSelectedItem().toString() +
                                "\nTổng thanh toán của bạn là: " + tvTotalPay.getText());
                        builder.setPositiveButton("Đóng", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                mBottomSheetDialog.dismiss();
                                Utils.listCart.clear();
                                getActivity().recreate();
                            }
                        });
                        builder.setCancelable(false);
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (alertDialog.isShowing()) {
                                    alertDialog.dismiss();
                                    mBottomSheetDialog.dismiss();
                                    Utils.listCart.clear();
                                    getActivity().recreate();
                                }

                            }
                        }, 10000);
                        Utils.saveCart(getContext());
                    }

                }
            });
        }
    }

    private void upApiOrder(List<Cart> mCart, int i) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.API_URL_INSERT_ORDER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("img", mCart.get(i).getImg());
                params.put("name", mCart.get(i).getName());
                params.put("price", mCart.get(i).getPrice() * mCart.get(i).getQuantity() + "");
                params.put("quantity", mCart.get(i).getQuantity() + "");
                params.put("trangthai", "Chưa thanh toán");
                params.put("account_id", mCart.get(i).getAccount_id() + "");
                return params;
            }
        };
        Volley.newRequestQueue(getContext()).add(stringRequest);
    }
}
