package com.example.mobileapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.mobileapp.model.Banner;
import com.example.mobileapp.R;

import java.util.List;

public class BannerProductDetailsAdapter extends PagerAdapter {
    List<Banner> imgBannerList;

    public BannerProductDetailsAdapter(List<Banner> imgBannerList) {
        this.imgBannerList = imgBannerList;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.row_details,container,false);
        ImageView imageView = view.findViewById(R.id.imageViewBanner);
        Banner mBanner = imgBannerList.get(position);

//        byte[] decode = Base64.decode(mBanner.getImg(),Base64.DEFAULT);
//        Bitmap mBitmap = BitmapFactory.decodeByteArray(decode,0,decode.length);
//        imageView.setImageBitmap(mBitmap);
        Glide.with(container.getContext()).load(mBanner.getImg()).into(imageView);
        // add view to group
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        if(imgBannerList != null){
            return imgBannerList.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
