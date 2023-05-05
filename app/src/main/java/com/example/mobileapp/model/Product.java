package com.example.mobileapp.model;

import java.io.Serializable;

public class Product implements Serializable {
    int id;
    String anh,ten_sp;
    int gia_sp,gia_km;
    String mota,trangthai,quatang;
    int loaisp_id;

    public Product(int id, String anh, String ten_sp, int gia_sp, int gia_km, String quatang,String mota,int loaisp_id) {
        this.id = id;
        this.anh = anh;
        this.ten_sp = ten_sp;
        this.gia_sp = gia_sp;
        this.gia_km = gia_km;
        this.quatang = quatang;
        this.mota = mota;
        this.loaisp_id = loaisp_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAnh() {
        return anh;
    }

    public void setAnh(String anh) {
        this.anh = anh;
    }

    public String getTen_sp() {
        return ten_sp;
    }

    public void setTen_sp(String ten_sp) {
        this.ten_sp = ten_sp;
    }

    public int getGia_sp() {
        return gia_sp;
    }

    public void setGia_sp(int gia_sp) {
        this.gia_sp = gia_sp;
    }

    public int getGia_km() {
        return gia_km;
    }

    public void setGia_km(int gia_km) {
        this.gia_km = gia_km;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }

    public String getTrangthai() {
        return trangthai;
    }

    public void setTrangthai(String trangthai) {
        this.trangthai = trangthai;
    }

    public String getQuatang() {
        return quatang;
    }

    public void setQuatang(String quatang) {
        this.quatang = quatang;
    }

    public int getLoaisp_id() {
        return loaisp_id;
    }

    public void setLoaisp_id(int loaisp_id) {
        this.loaisp_id = loaisp_id;
    }
}
