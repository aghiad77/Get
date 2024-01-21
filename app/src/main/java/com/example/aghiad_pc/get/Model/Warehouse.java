package com.example.aghiad_pc.get.Model;

import com.google.gson.annotations.SerializedName;

public class Warehouse {

    @SerializedName("variant")
    private String variant;

    @SerializedName("size")
    private String size;

    @SerializedName("count")
    private String count;

    public Warehouse(String variant, String size, String count) {
        this.variant = variant;
        this.size = size;
        this.count = count;
    }


    public void setColor(String variant) {
        this.variant = variant;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setCount(String count) {
        this.count = count;
    }


    public String getColor() {
        return variant;
    }

    public String getSize() {
        return size;
    }

    public String getCount() {
        return count;
    }
}
