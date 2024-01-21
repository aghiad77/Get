package com.example.aghiad_pc.get.Model;

import com.google.gson.annotations.SerializedName;

public class Favorite {

    @SerializedName("product_id")
    private  String product_id;

    @SerializedName("name")
    private  String name;

    @SerializedName("logo")
    private  String logo;

    @SerializedName("price")
    private  String price;

    @SerializedName("added_at")
    private  String added_at;


    public Favorite(String product_id, String name, String logo, String price, String added_at) {
        this.product_id = product_id;
        this.name = name;
        this.logo = logo;
        this.price = price;
        this.added_at = added_at;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setLogo(String logo) {
        this.logo = logo;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setAdded_at(String added_at) {
        this.added_at = added_at;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getName() {
        return name;
    }


    public String getLogo() {
        return logo;
    }

    public String getPrice() {
        return price;
    }

    public String getAdded_at() {
        return added_at;
    }

    public String getStringObject(boolean status){
        if(status)
            return this.product_id+";"+this.name+";"+this.logo+";"+this.price+";"+this.added_at+"@result@";
        else
            return this.product_id+";"+this.name+";"+this.logo+";"+this.price+";"+this.added_at;
    }
}
