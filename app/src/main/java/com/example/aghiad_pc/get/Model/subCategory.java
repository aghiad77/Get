package com.example.aghiad_pc.get.Model;

import com.google.gson.annotations.SerializedName;

public class subCategory {

    @SerializedName("subcategory")
    String subcategory;

    public subCategory(String name) {

        this.subcategory = name;
    }

    public String getName() {
        return subcategory;
    }
    public void setName(String name) {
        this.subcategory = name;
    }

    public String getStringObject(boolean status){
        if(status)
            return this.subcategory+";";
        else
            return this.subcategory;
    }
}
