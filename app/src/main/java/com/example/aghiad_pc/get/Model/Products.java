package com.example.aghiad_pc.get.Model;

import com.google.gson.annotations.SerializedName;

public class Products {

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

    @SerializedName("created_at")
    private String created_at;

    @SerializedName("is_sold")
    private String is_sold;

    @SerializedName("is_favorite")
    private String is_favorite;

    public Products(){}


    public Products(String id, String name, String type, String category, String subcategory,String store, String logo, String price,  String comment, String created_at, String is_sold, String is_favorite) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.category = category;
        this.store = store;
        this.logo = logo;
        this.price = price;
        this.subcategory = subcategory;
        this.comment = comment;
        this.created_at = created_at;
        this.is_sold = is_sold;
        this.is_favorite=is_favorite;
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

    public void setStore(String store) {
        this.store = store;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setIs_sold(String is_sold) {
        this.is_sold = is_sold;
    }

    public void setIs_favorite(String is_favorite) {
        this.is_favorite = is_favorite;
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

    public String getStore() {
        return store;
    }

    public String getLogo() {
        return logo;
    }

    public String getPrice() {
        return price;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public String getComment() {
        return comment;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getIs_sold() {
        return is_sold;
    }

    public String getIs_favorite() {
        return is_favorite;
    }

    public String getStringObject(boolean status){
        if(status)
         return this.id+";"+this.name+";"+this.type+";"+this.category+";"+this.subcategory+";"+this.store+";"+this.logo+";"+this.price+";"+
                 this.comment+";"+this.created_at+";"+this.is_sold+";"+this.is_favorite+"@result@";
        else
            return this.id+";"+this.name+";"+this.type+";"+this.category+";"+this.subcategory+";"+this.store+";"+this.logo+";"+this.price+";"+
                    this.comment+";"+this.created_at+";"+this.is_sold+";"+this.is_favorite;
    }
}
