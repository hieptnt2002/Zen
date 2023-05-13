package com.example.mobileapp.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mobileapp.R;
import com.example.mobileapp.model.Account;
import com.example.mobileapp.model.Order;
import com.example.mobileapp.utils.Utils;
import com.example.mobileapp.view.CartActivity;
import com.google.gson.Gson;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountFragment extends Fragment {
    TextView tvName,tvGmail;
    CircleImageView civImg;
    LinearLayout layoutOrder,layoutVoucher,layoutIntroduce;
    HistoryOrderBottomSheetFragment sheetFragment;
    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_account, container, false);
        initGUI();
        setDataAccount();
        clickToOrder();
        return view;
    }
    private void initGUI(){
        tvName = view.findViewById(R.id.textView_nameProfile);
        tvGmail = view.findViewById(R.id.textView_gmailProfile);
        civImg = view.findViewById(R.id.circleImageView_profile);
        layoutOrder = view.findViewById(R.id.layout_order);
        layoutIntroduce = view.findViewById(R.id.layout_introduce);
        layoutVoucher = view.findViewById(R.id.layout_voucher);
    }
    private void setDataAccount(){
        SharedPreferences sharedPreferences = view.getContext().getSharedPreferences(Utils.login_success, Context.MODE_PRIVATE);
        String object = sharedPreferences.getString("object",null);
        Gson gson = new Gson();
        if(object != null){
            Account account = gson.fromJson(object,Account.class);
            tvName.setText(account.getName());
            tvGmail.setText(account.getEmail());
        }
    }
    private void clickToOrder() {
        layoutOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    sheetFragment = HistoryOrderBottomSheetFragment.newInstance();
                    sheetFragment.show(getFragmentManager(), sheetFragment.getTag());

            }
        });

    }
}
