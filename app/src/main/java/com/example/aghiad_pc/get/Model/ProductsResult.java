package com.example.aghiad_pc.get.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductsResult {

    @SerializedName("result")
    private List<Products> result;

    public List<Products> getResult() {
        return result;
    }
}
