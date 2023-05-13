package com.example.mobileapp.adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mobileapp.R;
import com.example.mobileapp.model.Cart;
import com.example.mobileapp.model.HistoryOrder;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class HistoryOrderAdapter extends RecyclerView.Adapter<HistoryOrderAdapter.ViewHolder> {
    List<HistoryOrder> mList;
    Context context;

    public HistoryOrderAdapter(List<HistoryOrder> mList, Context context) {
        this.mList = mList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_order_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryOrderAdapter.ViewHolder holder, int position) {
        HistoryOrder object = mList.get(position);
        Glide.with(context).load(object.getImg()).into(holder.img);
        holder.tvName.setText(object.getName());
        Locale locale = new Locale("vi", "VN"); // Thiết lập địa phương Việt Nam
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        holder.tvPrice.setText(currencyFormatter.format(object.getPrice()));
        holder.tvQuantity.setText("Số lượng: " + object.getQuantity());

    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView tvName, tvPrice, tvQuantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.textViewName_row);
            tvPrice = itemView.findViewById(R.id.textView_price_row);
            tvQuantity = itemView.findViewById(R.id.textView_quantity);
            img = itemView.findViewById(R.id.imgorder_row);
        }
    }
}
