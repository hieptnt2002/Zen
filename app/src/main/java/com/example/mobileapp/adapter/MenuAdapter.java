package com.example.mobileapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mobileapp.model.Menu;
import com.example.mobileapp.R;

import java.util.List;

public class MenuAdapter extends BaseAdapter {
    List<Menu> menuList;
    Context context;

    public MenuAdapter(List<Menu> menuList, Context context) {
        this.menuList = menuList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return menuList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.row_menu,parent,false);
        ImageView menu_img = v.findViewById(R.id.menu_img);
        TextView menu_ten = v.findViewById(R.id.menu_ten);
        Menu menu = menuList.get(position);
        menu_img.setImageResource(menu.getMenu_anh());
        menu_ten.setText(menu.getMenu_ten());
        return v;
    }
}
