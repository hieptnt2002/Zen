package com.example.mobileapp.tesst;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileapp.R;

import java.util.ArrayList;

public class Nav_MenuAdapter extends RecyclerView.Adapter<Nav_MenuAdapter.ViewHolder> {
    ArrayList<Nav_Menu> mList;


    public Nav_MenuAdapter(ArrayList<Nav_Menu> mList) {
        this.mList = mList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_menu,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Nav_Menu navMenu = mList.get(position);
        holder.img.setImageResource(navMenu.getImg());
        holder.txtTen.setText(navMenu.getName());

    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtTen;
        ImageView img;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTen = itemView.findViewById(R.id.menu_ten);
            img = itemView.findViewById(R.id.menu_img);
        }
    }
}