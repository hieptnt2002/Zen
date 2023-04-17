package com.example.mobileapp.tesst;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileapp.R;

import java.util.ArrayList;
import java.util.List;

public class RCADAPTER extends RecyclerView.Adapter<RCADAPTER.ViewHolder> {
    Context context;
    ArrayList<test> list;

    public RCADAPTER(Context context, ArrayList<test> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.test,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        test test = (com.example.mobileapp.tesst.test) list.get(position);
        byte[] decode = Base64.decode(test.getAnh(),Base64.DEFAULT);
        Bitmap imgBitmap = BitmapFactory.decodeByteArray(decode,0,decode.length);
        holder.textView.setImageBitmap(imgBitmap);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView textView;
        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            textView = itemView.findViewById(R.id.textView51);
        }
    }
}
