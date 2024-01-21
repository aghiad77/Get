package com.example.aghiad_pc.get.Model;

import com.google.gson.annotations.SerializedName;

public class soldProduct {

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

    @SerializedName("old_price")
    private String old_price;

    @SerializedName("new_price")
    private String new_price;

    @SerializedName("comment")
    private String comment;

    @SerializedName("created_at")
    private String created_at;

    @SerializedName("started_at")
    private String started_at;

    @SerializedName("end_at")
    private String end_at;

    @SerializedName("sold_amount_type")
    private String sold_amount_type;


    public soldProduct(){}

    public soldProduct(String id, String name, String type, String category, String subcategory, String store, String logo, String old_price, String new_price, String comment, String created_at, String started_at, String end_at, String sold_amount_type) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.category = category;
        this.subcategory = subcategory;
        this.store = store;
        this.logo = logo;
        this.old_price = old_price;
        this.new_price = new_price;
        this.comment = comment;
        this.created_at = created_at;
        this.started_at = started_at;
        this.end_at = end_at;
        this.sold_amount_type = sold_amount_type;
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

    public String getOld_price() {
        return old_price;
    }

    public String getNew_price() {
        return new_price;
    }

    public String getComment() {
        return comment;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getStarted_at() {
        return started_at;
    }

    public String getEnd_at() {
        return end_at;
    }

    public String getSold_amount_type() {
        return sold_amount_type;
    }

    public String getStringObject(boolean status){
        if(status)
            return this.id+";"+this.name+";"+this.type+";"+this.category+";"+this.subcategory+";"+this.store+";"+this.logo+";"+this.old_price+";"+
                    this.new_price+";"+this.comment+";"+this.created_at+";"+this.started_at+";"+this.end_at+";"+this.sold_amount_type+"@result@";
        else
            return this.id+";"+this.name+";"+this.type+";"+this.category+";"+this.subcategory+";"+this.store+";"+this.logo+";"+this.old_price+";"+
                    this.new_price+";"+this.comment+";"+this.created_at+";"+this.started_at+";"+this.end_at+";"+this.sold_amount_type;
    }
}
