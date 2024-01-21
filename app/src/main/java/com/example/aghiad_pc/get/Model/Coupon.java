package com.example.aghiad_pc.get.Model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Coupon {

    @SerializedName("msg")
    private String msg;

    @SerializedName("products")
    private List<couponProduct> products;

    @SerializedName("total_sold")
    private String total_sold;

    public Coupon(String msg, List<couponProduct> products,String total_sold) {
        this.msg = msg;
        this.products = products;
        this.total_sold = total_sold;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setProducts(List<couponProduct> products) {
        this.products = products;
    }

    public void setTotal_sold(String total_sold) {
        this.total_sold = total_sold;
    }

    public String getMsg() {
        return msg;
    }

    public List<couponProduct> getProducts() {
        return products;
    }

    public String getTotal_sold() {
        return total_sold;
    }
}
