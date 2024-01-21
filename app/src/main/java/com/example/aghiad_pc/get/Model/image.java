package com.example.aghiad_pc.get.Model;

import com.google.gson.annotations.SerializedName;

public class image {

    @SerializedName("src")
    private String src;

    @SerializedName("variant")
    private String variant;

    public String getSrc() {
        return src;
    }

    public String getColor() {
        return variant;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public void setColor(String variant) {
        this.variant = variant;
    }

    public image(String src, String color) {
        this.src = src;
        this.variant = color;
    }
}
