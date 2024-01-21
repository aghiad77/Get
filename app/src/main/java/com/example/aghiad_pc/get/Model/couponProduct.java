package com.example.aghiad_pc.get.Model;

import com.google.gson.annotations.SerializedName;

public class couponProduct {

    @SerializedName("product_id")
    private String product_id;

    @SerializedName("old_price")
    private String old_price;

    @SerializedName("new_price")
    private String new_price;

    public couponProduct(String product_id, String old_price, String new_price) {
        this.product_id = product_id;
        this.old_price = old_price;
        this.new_price = new_price;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public void setOld_price(String old_price) {
        this.old_price = old_price;
    }

    public void setNew_price(String new_price) {
        this.new_price = new_price;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getOld_price() {
        return old_price;
    }

    public String getNew_price() {
        return new_price;
    }

    public String getStringObject(boolean status){
        if(status)
            return this.product_id+";"+this.old_price+";"+this.new_price+"@result@";
        else
            return this.product_id+";"+this.old_price+";"+this.new_price;
    }
}
