package com.example.aghiad_pc.get.Model;

import com.google.gson.annotations.SerializedName;

public class OfferD {

    @SerializedName("started_at")
    private String started_at;

    @SerializedName("end_at")
    private String end_at;

    public OfferD(String started_at, String end_at) {
        this.started_at = started_at;
        this.end_at = end_at;
    }

    public void setStarted_at(String started_at) {
        this.started_at = started_at;
    }

    public void setEnd_at(String end_at) {
        this.end_at = end_at;
    }

    public String getStarted_at() {
        return started_at;
    }

    public String getEnd_at() {
        return end_at;
    }
}
