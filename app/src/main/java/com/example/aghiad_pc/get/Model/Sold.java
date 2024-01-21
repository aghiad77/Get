package com.example.aghiad_pc.get.Model;

import com.google.gson.annotations.SerializedName;

public class Sold {

    @SerializedName("started_at")
    private String started_at;

    @SerializedName("end_at")
    private String end_at;

    @SerializedName("old_price")
    private String old_price;

    @SerializedName("new_price")
    private String new_price;

    @SerializedName("sold_amount_type")
    private String sold_amount_type;

    public String getStarted_at() {
        return started_at;
    }

    public String getEnd_at() {
        return end_at;
    }

    public String getOld_price() {
        return old_price;
    }

    public String getNew_price() {
        return new_price;
    }

    public String getSold_amount_type() {
        return sold_amount_type;
    }

    public void setStarted_at(String started_at) {
        this.started_at = started_at;
    }

    public void setEnd_at(String end_at) {
        this.end_at = end_at;
    }

    public void setOld_price(String old_price) {
        this.old_price = old_price;
    }

    public void setNew_price(String new_price) {
        this.new_price = new_price;
    }

    public void setSold_amount_type(String sold_amount_type) {
        this.sold_amount_type = sold_amount_type;
    }

    public Sold(String started_at, String end_at, String old_price, String price, String sold_amount_type) {
        this.started_at = started_at;
        this.end_at = end_at;
        this.old_price = old_price;
        this.new_price = price;
        this.sold_amount_type = sold_amount_type;
    }
}
