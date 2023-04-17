package com.example.mobileapp.model;

public class Banner {
   int id;
   String img;

    public int getId() {
        return id;
    }

    public String getImg() {
        return img;
    }

    public Banner(int id, String img) {
        this.id = id;
        this.img = img;
    }
}
