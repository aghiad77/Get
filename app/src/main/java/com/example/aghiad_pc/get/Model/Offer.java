package com.example.aghiad_pc.get.Model;

import com.google.gson.annotations.SerializedName;

public class Offer {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("type")
    private String type;

    @SerializedName("category")
    private String category;

    @SerializedName("subcategory")
    private String subcategory;

    @SerializedName("store")
    private String store;

    @SerializedName("logo")
    private String logo;

    @SerializedName("price")
    private String price;

    @SerializedName("comment")
    private String comment;

    @SerializedName("started_at")
    private String started_at;

    @SerializedName("end_at")
    private String end_at;

    @SerializedName("is_favoriate")
    private String is_favoriate;

    public Offer(String id, String name, String type, String category, String subcategory, String store, String logo, String price, String comment, String started_at, String end_at,String is_favoriate) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.category = category;
        this.subcategory = subcategory;
        this.store = store;
        this.logo = logo;
        this.price = price;
        this.comment = comment;
        this.started_at = started_at;
        this.end_at = end_at;
        this.is_favoriate=is_favoriate;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setStarted_at(String started_at) {
        this.started_at = started_at;
    }

    public void setEnd_at(String end_at) {
        this.end_at = end_at;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getCategory() {
        return category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public String getStore() {
        return store;
    }

    public String getLogo() {
        return logo;
    }

    public String getPrice() {
        return price;
    }

    public String getComment() {
        return comment;
    }

    public String getStarted_at() {
        return started_at;
    }

    public String getEnd_at() {
        return end_at;
    }

    public String getStringObject(boolean status){
        if(status)
            return this.id+";"+this.name+";"+this.type+";"+this.category+";"+this.subcategory+";"+this.store+";"+this.logo+";"+this.price+";"+
                    this.comment+";"+this.started_at+";"+this.end_at+";"+this.is_favoriate+"@result@";
        else
            return this.id+";"+this.name+";"+this.type+";"+this.category+";"+this.subcategory+";"+this.store+";"+this.logo+";"+this.price+";"+
                    this.comment+";"+this.started_at+";"+this.end_at+";"+this.is_favoriate;
    }
}
