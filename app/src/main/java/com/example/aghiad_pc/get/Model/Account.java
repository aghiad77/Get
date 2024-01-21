package com.example.aghiad_pc.get.Model;

import com.google.gson.annotations.SerializedName;

public class Account {

    @SerializedName("user")
    private String user;

    @SerializedName("name")
    private String name;

    @SerializedName("phone")
    private String phone;

    @SerializedName("province")
    private String province;

    @SerializedName("address")
    private String address;

    public Account(String user, String name, String phone, String province, String address) {
        this.user = user;
        this.name = name;
        this.phone = phone;
        this.province = province;
        this.address = address;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUser() {
        return user;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getProvince() {
        return province;
    }

    public String getAddress() {
        return address;
    }
}
