package com.example.mobileapp.model;

public class Cart {
    String img,name;
    int price,saleprice,quantity,account_id;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getSaleprice() {
        return saleprice;
    }

    public void setSaleprice(int saleprice) {
        this.saleprice = saleprice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getAccount_id() {
        return account_id;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }

    public Cart(String img, String name, int price, int saleprice, int quantity, int account_id) {
        this.img = img;
        this.name = name;
        this.price = price;
        this.saleprice = saleprice;
        this.quantity = quantity;
        this.account_id = account_id;
    }
}
