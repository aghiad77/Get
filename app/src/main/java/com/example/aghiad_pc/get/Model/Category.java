package com.example.aghiad_pc.get.Model;


import com.google.gson.annotations.SerializedName;

public class Category {

    @SerializedName("category")
    String category;

    public Category(String name) {

        this.category = name;
    }

    public String getName() {
        return category;
    }
    public void setName(String name) {
        this.category = name;
    }

    public String getStringObject(boolean status){
        if(status)
            return this.category+";";
        else
            return this.category;
    }

}
