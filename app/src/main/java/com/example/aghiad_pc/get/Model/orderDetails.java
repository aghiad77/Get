package com.example.aghiad_pc.get.Model;

import com.google.gson.annotations.SerializedName;

public class orderDetails {

    @SerializedName("product_id")
    private  String product_id;

    @SerializedName("product")
    private  String product;

    @SerializedName("logo")
    private  String logo;

    @SerializedName("price")
    private  String price;

    @SerializedName("count")
    private  String count;

    @SerializedName("size")
    private  String size;

    @SerializedName("variant")
    private  String variant;

    @SerializedName("total_price")
    private  String total_price;


    public orderDetails(String product_id, String product, String logo ,String price, String count, String size, String color, String total_price) {
        this.product_id = product_id;
        this.product = product;
        this.logo=logo;
        this.price = price;
        this.count = count;
        this.size = size;
        this.variant = color;
        this.total_price = total_price;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setColor(String color) {
        this.variant = color;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getProduct() {
        return product;
    }

    public String getPrice() {
        return price;
    }

    public String getCount() {
        return count;
    }

    public String getSize() {
        return size;
    }

    public String getColor() {
        return variant;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getLogo() {
        return logo;
    }

    public String getStringObject(boolean status){
        if(status)
            return this.product_id+";"+this.product+";"+this.logo+";"+this.price+";"+this.count+";"+this.size+";"+this.variant+";"+this.total_price+"@result@";
        else
            return this.product_id+";"+this.product+";"+this.logo+";"+this.price+";"+this.count+";"+this.size+";"+this.variant+";"+this.total_price;
    }
}
