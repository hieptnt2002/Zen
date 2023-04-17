package com.example.mobileapp.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileapp.R;
import com.example.mobileapp.adapter.OrderAdapter;
import com.example.mobileapp.model.Order;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class BottomSheetFragment extends BottomSheetDialogFragment {
    private static final String KEY_ORDER_OBJECT = "object";
    RecyclerView rvOrder;
    EditText edtName, edtAddress, edtSDT;
    Spinner spnMethodPay;
    TextView tvTotalPr, tvTotalPay, tvTotalPay2;
    OrderAdapter mOrderAdapter;
    private Order mOrder;

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
        BottomSheetDialog mBottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_order, null);
        mBottomSheetDialog.setContentView(view);
        initGUI(view);
        setDataOrder();
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
    }

    private void setDataOrder() {
        rvOrder.setHasFixedSize(true);
        rvOrder.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        if (mOrder == null) {
            return;
        }
        tvTotalPr.setText(String.valueOf(mOrder.getTotal()));
        tvTotalPay.setText(String.valueOf(mOrder.getTotal() + 35000));
        tvTotalPay2.setText(String.valueOf(mOrder.getTotal() + 35000));
        mOrderAdapter = new OrderAdapter(mOrder.getmList(), getContext());
        rvOrder.setAdapter(mOrderAdapter);
        List<String> list = new ArrayList<>();
        list.add("Thanh toán khi nhận hàng");
        list.add("Chuyển khoản ngân hàng");
        list.add("Thanh toán qua ZaloPay");
        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1,list);
        spnMethodPay.setAdapter(adapter);

    }
}
