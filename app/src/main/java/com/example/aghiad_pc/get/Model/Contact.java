package com.example.aghiad_pc.get.Model;

import com.google.gson.annotations.SerializedName;

public class Contact {

    @SerializedName("phone")
    private String phone;

    @SerializedName("address")
    private String address;

    @SerializedName("facebook")
    private String facebook;

    public Contact(String phone, String addrees, String facebook) {
        this.phone = phone;
        this.address = addrees;
        this.facebook = facebook;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String addrees) {
        this.address = addrees;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getFacebook() {
        return facebook;
    }
}
